package domain;

import petri.Arc;
import petri.PetriNet;
import petri.Place;
import petri.Transition;
import data.Event;
import data.Log;
import data.Trace;

public class Controller {

	public static void main(String[] args) {
		PetriNet petri = EntityManager.getPetryNet("test.pnml");
		Log log = EntityManager.getLog("test.xes");
		// Testing Places
		// Testing Places
		for (Place place : petri.getPlaces()) {
			// System.out.println("Place id: " + place.getId());
			for (Arc a : place.getIncomingEdges()) {
				// System.out.println("In: " + a.getSourceId());
			}
			for (Arc a : place.getOutgoingEdges()) {
				// System.out.println("Out: " + a.getTargetId());
			}
		}
		// Testing Transitions
		for (Transition transition : petri.getTransitions()) {
			// System.out.println("Transition id: " +
			// transition.getEventName());
			for (Arc a : transition.getIncomingEdges()) {
				// System.out.println("In: " + a.getSourceId());
			}
			for (Arc a : transition.getOutgoingEdges()) {
				// System.out.println("Out: " + a.getTargetId());
			}
		}
		for (Trace trace : log.getTraces()) {
			System.out.println("Trace name" + trace.getName());
			// transition.getEventName());
			for (Event e : trace.getEvents()) {
				System.out.println(e.toString());
			}

		}
	}

}
