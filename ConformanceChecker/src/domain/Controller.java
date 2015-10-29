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
		
		PetriNet petriNet = EntityManager.getPetryNet(xPathOfPetriNet);
		Log log = EntityManager.getLog(xPathOfLog);
				
		List<Trace> traces = log.getTraces();
		
		for (Trace trace : traces) {
			
			// Start of the first step
	
			// Add the first token to start place
			Place startPlace = petriNet.getStartPlace();
			startPlace.produceToken();
			trace.increaseNumberOfProducedTokens();
			
			// Enable necessary transitions
			Collection<Transition> outTransitions = startPlace.getOutgoingTransitions();
			petriNet.enableTransitions(outTransitions);
			int numberOfEnabledTransitions = petriNet.getNumberOfEnabledTransitions();
			trace.increaseNumberOfEnabledTransitions(numberOfEnabledTransitions);
			
			// End of the first step

			// Iterate over events
			List<Event> events =  trace.getEvents();
			for (Event event : events) {

				// Find respective transition
				String eventName = event.getName();
				Transition transition = petriNet.getTransitionWithName(eventName);
				
				// Consume tokens or add missing tokens to the incoming places
				Collection<Place> inPlaces = transition.getIncomingPlaces();
				for (Place inPlace : inPlaces) {
					boolean hasTokens = inPlace.hasTokens();
					if (hasTokens) {
						inPlace.consumeToken();
						trace.increaseNumberOfConsumedTokens();
					} else {
						trace.increaseNumberOfMissingTokens();
					}
				}
				//transition.setEnabled(false);

				// Produce tokens to the outgoing places
				Collection<Place> outPlaces =  transition.getOutgoingPlaces();
				for (Place outPlace : outPlaces) {
					outPlace.produceToken();
					trace.increaseNumberOfProducedTokens();
				}

				// Enable/disable transitions
				Collection<Transition> transitions = petriNet.getTransitions();
				petriNet.enableTransitions(transitions);
				numberOfEnabledTransitions = petriNet.getNumberOfEnabledTransitions();
				trace.increaseNumberOfEnabledTransitions(numberOfEnabledTransitions);
				
			}
			
			// End
			Place endPlace = petriNet.getEndPlace();
			boolean hasTokens = endPlace.hasTokens();
			if (hasTokens) {
				endPlace.consumeToken();
				trace.increaseNumberOfConsumedTokens();
			} else {
				trace.increaseNumberOfMissingTokens();
			}
			
			int numberOfTokens = petriNet.getNumberOfTokens();
			trace.setNumberOfRemainingTokens(numberOfTokens);
			
			numberOfEnabledTransitions = trace.getNumberOfEnabledTransitions();
			trace.computeMeanNumberOfEnabledTransitions(numberOfEnabledTransitions, events.size());
			
			// Clean petrinet: number of tokens to zero and disable transitions
			petriNet.clearPetrinet();			
		}
		
		Collection<Place> places = petriNet.getPlaces();	
		Collection<Transition> transitions = petriNet.getTransitions();		
		
		double fitness = computeFitness(traces);
		System.out.println("Fitness: " + fitness); // 1.0
				
		double behavAppropr = computeBehavAppropr(traces, transitions.size());
		System.out.println("Simple Behavioral Appropriateness: " + behavAppropr); // 0.9236111
		
		double structAppropr = computeStructAppropr(transitions.size(), places.size());
		System.out.println("Simple Structural Appropriateness: " + structAppropr); // 0.7
			
	}
	
	// Compute Fitness
	private double computeFitness(List<Trace> traces) {
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
	
	// Compute Simple Behavioral Appropriateness
	private double computeBehavAppropr(List<Trace> traces, int numberOfTransitions) {
		double numerator = 0;
		int sumOfInstances = 0;
		for (Trace trace : traces) {
			numerator += trace.getNumberOfInstances() * (numberOfTransitions - trace.getMeanNumberOfEnabledTransitions());
			sumOfInstances += trace.getNumberOfInstances();
		}
		double behavAppropr = numerator / (double) ((numberOfTransitions - 1) * sumOfInstances);
		return behavAppropr;
	}
	
	// Compute Simple Structural Appropriateness
	private double computeStructAppropr(int numberOfTransitions, int numberOfPlaces) {
		double structAppropr = (numberOfTransitions + 2) / (double) (numberOfTransitions + numberOfPlaces);
		return structAppropr;
	}

}
