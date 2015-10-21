package domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;

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
import petri.Arc;
import petri.PetriNet;

public class EntityManager {

	public static PetriNet getPetryNet(String xPathOfPetriNet) {
		PnmlImportUtils ut = new PnmlImportUtils();
		File f = new File(xPathOfPetriNet);
		try {
			InputStream input = new FileInputStream(f);
			Pnml pnml = ut.importPnmlFromStream(input, f.getName(), f.length());
			PetrinetGraph net = PetrinetFactory.newInhibitorNet(pnml.getLabel()
					+ " (imported from " + f.getName() + ")");
			Marking marking = new Marking();
			pnml.convertToNet(net, marking, new GraphLayoutConnection(net));

			Collection<Place> netPlaces = net.getPlaces();
			Collection<Transition> netTransitions = net.getTransitions();
			// Create places and transitions
			Collection<petri.Place> places = new HashSet<petri.Place>();
			Collection<petri.Transition> transitions = new HashSet<petri.Transition>();
			// Add Transitions
			for (Transition netTransition : netTransitions) {
				transitions.add(new petri.Transition(netTransition.getLabel()));
			}
			// Add Places
			for (Place netPlace : netPlaces) {
				petri.Place place = new petri.Place(netPlace.getLabel());
				places.add(place);
				// Loop outgoing edges
				Collection<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> outEdges = net
						.getOutEdges(netPlace);
				for (PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> edge : outEdges) {
					for (petri.Transition transition : transitions) {
						if ((edge.getTarget().getLabel()).equals(transition
								.getEventName())) {
							Arc arc = new Arc(true, transition, place);
							place.addEdge(arc);
							transition.addEdge(arc);

						}
					}
				}
				// Loop incoming edges
				Collection<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> inEdges = net
						.getInEdges(netPlace);
				for (PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> edge : inEdges) {
					for (petri.Transition transition : transitions) {
						if ((edge.getSource().getLabel()).equals(transition
								.getEventName())) {
							Arc arc = new Arc(false, transition, place);
							place.addEdge(arc);
							transition.addEdge(arc);
						}
					}
				}
			}

			return new PetriNet("test", transitions, places);

		} catch (Exception e) {
			e.printStackTrace();
			return new PetriNet();
		}
	}
}
