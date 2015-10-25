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
		//for (Place place : petri.getPlaces()) {

		//}
		// Testing Transitions

		//for (Transition transition : petri.getTransitions()) {
			// System.out.println("Transition id: " +
			// transition.getEventName());

		//}
		for (Trace trace : log.getTraces()) {
			System.out.println("Trace name " + trace.getName());
			System.out.println("Num of instances " + trace.getNumberOfInstances());

		}
	}

}
