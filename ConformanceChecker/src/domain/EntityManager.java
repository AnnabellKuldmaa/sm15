package domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.extension.std.XLifecycleExtension;
import org.deckfour.xes.extension.std.XTimeExtension;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.models.connections.GraphLayoutConnection;
import org.processmining.models.graphbased.directed.petrinet.PetrinetEdge;
import org.processmining.models.graphbased.directed.petrinet.PetrinetGraph;
import org.processmining.models.graphbased.directed.petrinet.PetrinetNode;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.graphbased.directed.petrinet.impl.PetrinetFactory;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.plugins.pnml.Pnml;

import parsers.PnmlImportUtils;
import parsers.XLogReader;
import petri.Arc;
import petri.PetriNet;
import data.Event;
import data.Log;
import data.Trace;

public class EntityManager {

	public static PetriNet getPetryNet(String xPathOfPetriNet) {
		PnmlImportUtils utils = new PnmlImportUtils();
		File file = new File(xPathOfPetriNet);
		try {
			PetrinetGraph net = getPetrinetGraph(utils, file);
			Collection<petri.Place> places = new HashSet<petri.Place>();
			Collection<petri.Transition> transitions = new HashSet<petri.Transition>();
			populatePlacesAndTransitions(net, places, transitions);
			return new PetriNet(net.getLabel(), transitions, places);

		} catch (Exception e) {
			e.printStackTrace();
			return new PetriNet();
		}
	}

	private static PetrinetGraph getPetrinetGraph(PnmlImportUtils ut, File f)
			throws FileNotFoundException, Exception {
		InputStream input = new FileInputStream(f);
		Pnml pnml = ut.importPnmlFromStream(input, f.getName(), f.length());
		PetrinetGraph net = PetrinetFactory.newInhibitorNet(pnml.getLabel()
				+ " (imported from " + f.getName() + ")");
		Marking marking = new Marking();
		pnml.convertToNet(net, marking, new GraphLayoutConnection(net));
		return net;
	}

	private static void populatePlacesAndTransitions(PetrinetGraph net,
			Collection<petri.Place> places,
			Collection<petri.Transition> transitions) {
		Collection<Place> netPlaces = net.getPlaces();
		Collection<Transition> netTransitions = net.getTransitions();
		// Add Transitions
		for (Transition netTransition : netTransitions) {
			transitions.add(new petri.Transition(netTransition.getLabel()));
		}
		// Add Places
		for (Place netPlace : netPlaces) {
			petri.Place place = new petri.Place(netPlace.getLabel());
			places.add(place);
			populateEdgesFromPlaces(net, transitions, netPlace, place);
			populatEdgesToPlaces(net, transitions, netPlace, place);
		}
	}

	private static void populateEdgesFromPlaces(PetrinetGraph net,
			Collection<petri.Transition> transitions, Place netPlace,
			petri.Place place) {
		Collection<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> outEdges = net
				.getOutEdges(netPlace);
		for (PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> edge : outEdges) {
			for (petri.Transition transition : transitions) {
				if ((edge.getTarget().getLabel()).equals(transition
						.getEventName())) {
					Arc arc = new Arc(true, transition, place);
					place.addArc(arc);
					transition.addArc(arc);

				}
			}
		}
	}

	private static void populatEdgesToPlaces(PetrinetGraph net,
			Collection<petri.Transition> transitions, Place netPlace,
			petri.Place place) {
		Collection<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> inEdges = net
				.getInEdges(netPlace);
		for (PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> edge : inEdges) {
			for (petri.Transition transition : transitions) {
				if ((edge.getSource().getLabel()).equals(transition
						.getEventName())) {
					Arc arc = new Arc(false, transition, place);
					place.addArc(arc);
					transition.addArc(arc);
				}
			}
		}
	}

	public static Log getLog(String xPathOfLog) {
		try {
			XLog xlog = XLogReader.openLog("test.xes");
			// Loop traces in a log
			Log log = getLog(xlog);
			return log;
		} catch (Exception e) {
			e.printStackTrace();
			return new Log();
		}
	}

	private static Log getLog(XLog xlog) {
		Log log = new Log();
		if (xlog.size() > 0) {
			for (XTrace xtrace : xlog) {
				Trace trace = new Trace(XConceptExtension.instance()
						.extractName(xtrace));
				populateEvents(xtrace, trace);

				addTraceToLog(log, trace);

			}

		}
		return log;
	}

	private static void addTraceToLog(Log log, Trace trace) {
		for (Trace existingTrace : log.getTraces()) {
			if (existingTrace.equals(trace)) {
				existingTrace.addInstance();
				return;
			}
		}
		log.addTrace(trace);

	}

	private static void populateEvents(XTrace xtrace, Trace trace) {
		for (XEvent event : xtrace) {
			String activityName = XConceptExtension.instance().extractName(
					event);
			Date timestamp = XTimeExtension.instance().extractTimestamp(event);
			String eventType = XLifecycleExtension.instance()
					.extractTransition(event); // EventType
			trace.addEvent(new Event(activityName, timestamp));
		}
	}
}
