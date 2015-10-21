package domain;

import petri.PetriNet;
import petri.Place;
import petri.Arc;
import petri.Transition;

public class Controller {

	public static void main(String[] args) {
		PetriNet petri = EntityManager.getPetryNet("test.pnml");
		// Testing Places
		for (Place place : petri.getPlaces()) {
			System.out.println("Place id: " + place.getId());
			for (Arc a : place.getIncomingEdges()) {
				System.out.println("In: " + a.getSourceId());
			}
			for (Arc a : place.getOutgoingEdges()) {
				System.out.println("Out: " + a.getTargetId());
			}
		}
		// Testing Transitions
		for (Transition transition : petri.getTransitions()) {
			System.out.println("Transition id: " + transition.getEventName());
			for (Arc a : transition.getIncomingEdges()) {
				System.out.println("In: " + a.getSourceId());
			}
			for (Arc a : transition.getOutgoingEdges()) {
				System.out.println("Out: " + a.getTargetId());
			}
		}

	}

}
