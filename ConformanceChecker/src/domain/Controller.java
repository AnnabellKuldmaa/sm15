package domain;

import petri.PetriNet;
import petri.Place;
import petri.Transition;

import java.util.Collection;
import java.util.List;

import data.Event;
import data.Log;
import data.Trace;

public class Controller {

	public void doLogReplay(String xPathOfPetriNet, String xPathOfLog) {

		PetriNet petri = EntityManager.getPetryNet(xPathOfPetriNet);
		Log log = EntityManager.getLog(xPathOfLog);
		
		Collection<Place> places = petri.getPlaces();		
		Collection<Transition> transitions = petri.getTransitions();				
		List<Trace> traces = log.getTraces();
		
		for (Trace trace : traces) {
			
			System.out.print("Trace: ");
			for (Event event : trace.getEvents()) {
				System.out.print(event.getName());
			}
			System.out.println();
			
			PetriNet petrinet = new PetriNet("ID", transitions, places);
			
			// Start of the first step
	
			// Add the first token to start place
			Place startPlace = petrinet.getStartPlace();
			startPlace.produceToken();
			trace.increaseNumberOfProducedTokens();
			System.out.println("Produced token to " + startPlace.getName());

			Collection<Transition> outTransitions = startPlace.getOutgoingTransitions();

			// Enable necessary transitions
			for (Transition outTransition : outTransitions) {
				Collection<Place> inPlaces = outTransition.getIncomingPlaces();
				boolean enabled = true;
				for (Place inPlace : inPlaces) {
					if (inPlace.hasTokens() == false) {
						enabled = false;
					}
				}
				if (enabled) {
					outTransition.setEnabled(enabled);
				}				
			}

			trace.increaseNumberOfEnabledTransitions(petrinet.getNumberOfEnabledTransitions());
			
			// End of the first step

			
			// Iterate over events
			List<Event> events =  trace.getEvents();
			for (int step = 0; step < events.size(); step++) {
				
				Event event = events.get(step);
				// Find respective transition
				String eventName = event.getName();
				Transition transition = petrinet.getTransitionWithName(eventName);
				
				// Consume tokens or add missing tokens to the incoming places
				Collection<Place> inPlaces = transition.getIncomingPlaces();
				for (Place inPlace : inPlaces) {
					if (inPlace.hasTokens()) {
						inPlace.consumeToken();
						trace.increaseNumberOfConsumedTokens();
						System.out.println("Consumed token from " + inPlace.getName());
					} else {
						trace.increaseNumberOfMissingTokens();
						System.out.println("Missing token in " + inPlace.getName());
					}
				}
				transition.setEnabled(false);

				// Produce tokens to the outgoing places
				Collection<Place> outPlaces =  transition.getOutgoingPlaces();
				for (Place outPlace : outPlaces) {
					outPlace.produceToken();
					trace.increaseNumberOfProducedTokens();
					System.out.println("Produced token to " + outPlace.getName());
				}

				// (Dis)enable transitions
				for (Transition transition_2 : petrinet.getTransitions()) {
					boolean enabled = true;
					for (Place inPlace : transition_2.getIncomingPlaces()) {
						if (inPlace.hasTokens() == false) {
							enabled = false;
						}
						transition_2.setEnabled(enabled);
					}
				}

				trace.increaseNumberOfEnabledTransitions(petrinet.getNumberOfEnabledTransitions());
			}
			
			// End
			Place endPlace = petrinet.getEndPlace();
			if (endPlace.hasTokens()) {
				endPlace.consumeToken();
				trace.increaseNumberOfConsumedTokens();
				System.out.println("Consumed token from " + endPlace.getName());
			} else {
				trace.increaseNumberOfMissingTokens();
				System.out.println("Missing token in " + endPlace.getName());
			}
			
			trace.setNumberOfRemainingTokens(petrinet.getNumberOfTokens());
			trace.setMeanNumberOfEnabledTransitions(trace.getnumberOfEnabledTransitions() / (double) events.size());
			
			System.out.println(trace.toString());
			System.out.println();
			
		}
		
		double fitness = computeFitness(traces);
		System.out.println("Fitness: " + fitness); // 1.0
		
		int numberOfTransitions = transitions.size(); // |T_v|, |L|
		int numberOfPlaces = places.size();
		
		double behavAppropr = computeBehavAppropr(traces, numberOfTransitions);
		System.out.println("Simple Behavioral Appropriateness: " + behavAppropr); // 0.9236111
		
		double structAppropr = computeStructAppropr(numberOfTransitions, numberOfTransitions + numberOfPlaces);
		System.out.println("Simple Structural Appropriateness: " + structAppropr); // 0.7
		
			
	}
	
	//public double computeFitness(String xPathOfPetriNet, String xPathOfLog) {
	public double computeFitness(List<Trace> traces) {
		//Log log = EntityManager.getLog(xPathOfLog);
		//List<Trace> traces = log.getTraces();
		
		// Compute Fitness
		int nm = 0;
		int nc = 0;
		int nr = 0;
		int np = 0;
		for (Trace trace : traces) {
			nm += trace.getNumberOfInstances() * trace.getNumberOfMissingTokens();
			nc += trace.getNumberOfInstances() * trace.getNumberOfConsumedTokens();
			nr += trace.getNumberOfInstances() * trace.getNumberOfRemainingTokens();
			np += trace.getNumberOfInstances() * trace.getNumberOfProducedTokens();
		}

		double fitness = 0.5 * (1 - nm / nc) + 0.5 * (1 - nr / np);
		return fitness;
	}
	
	public double computeBehavAppropr(List<Trace> traces, int numberOfTransitions) {
		
		// Compute Simple Behavioral Appropriateness
		double numerator = 0;
		int sumOfInstances = 0;
		for (Trace trace : traces) {
			numerator += trace.getNumberOfInstances() * (numberOfTransitions - trace.getMeanNumberOfEnabledTransitions());
			sumOfInstances += trace.getNumberOfInstances();
		}
		
		double behavAppropr = numerator / (double) ((numberOfTransitions - 1) * sumOfInstances);
		return behavAppropr;
	}
	
	public double computeStructAppropr(int numberOfTransitions, int numberofNodes) {
		
		// Compute Simple Structural Appropriateness
		double structAppropr = (numberOfTransitions + 2) / (double) numberofNodes;
		return structAppropr;
	}

}
