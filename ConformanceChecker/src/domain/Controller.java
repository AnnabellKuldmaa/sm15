package domain;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import petri.PetriNet;
import petri.Place;
import petri.Transition;
import data.Event;
import data.Log;
import data.Trace;

public class Controller {

	public List<Double> doLogReplay(String xPathOfPetriNet, String xPathOfLog)
			throws Exception {

		PetriNet petriNet = EntityManager.getPetryNet(xPathOfPetriNet);
		Log log = EntityManager.getLog(xPathOfLog);

		Collection<Trace> traces = log.getTraces();

		for (Trace trace : traces) {

			// Start of the first step

			// Add the first token to start place
			Place startPlace = petriNet.getStartPlace();
			startPlace.produceToken();
			trace.increaseNumberOfProducedTokens();

			// Enable necessary transitions
			Collection<Transition> outTransitions = startPlace
					.getOutgoingTransitions();
			petriNet.enableTransitions(outTransitions);
			int numberOfEnabledTransitions = petriNet
					.getNumberOfEnabledTransitions();
			trace.increaseNumberOfEnabledTransitions(numberOfEnabledTransitions);

			// End of the first step

			// Iterate over events
			List<Event> events = trace.getEvents();
			for (Event event : events) {

				// Find respective transition
				String eventName = event.getName();
				Transition transition = petriNet
						.getTransitionWithName(eventName);

				// Consume tokens or add missing tokens to the incoming places
				Collection<Place> inPlaces = transition.getIncomingPlaces();
				for (Place inPlace : inPlaces) {
					Boolean hasTokens = inPlace.hasTokens();
					if (hasTokens) {
						inPlace.consumeToken();
						trace.increaseNumberOfConsumedTokens();
					} else {
						trace.increaseNumberOfMissingTokens();
					}
				}
				// transition.setEnabled(false);

				// Produce tokens to the outgoing places
				testMethod(trace, transition);

				// Enable/disable transitions
				Collection<Transition> transitions = petriNet.getTransitions();
				petriNet.enableTransitions(transitions);
				numberOfEnabledTransitions = petriNet
						.getNumberOfEnabledTransitions();
				trace.increaseNumberOfEnabledTransitions(numberOfEnabledTransitions);

			}

			// End
			Place endPlace = petriNet.getEndPlace();
			Boolean hasTokens = endPlace.hasTokens();
			if (hasTokens) {
				endPlace.consumeToken();
				trace.increaseNumberOfConsumedTokens();
			} else {
				trace.increaseNumberOfMissingTokens();
			}

			int numberOfTokens = petriNet.getNumberOfTokens();
			trace.setNumberOfRemainingTokens(numberOfTokens);

			numberOfEnabledTransitions = trace.getNumberOfEnabledTransitions();
			trace.computeMeanNumberOfEnabledTransitions(
					numberOfEnabledTransitions, events.size());

			// Clean petrinet: number of tokens to zero and disable transitions
			petriNet.clearPetrinet();
		}

		Collection<Place> places = petriNet.getPlaces();
		Collection<Transition> transitions = petriNet.getTransitions();

		List<Double> metrics = new LinkedList<Double>();
		metrics.add(computeFitness(traces));
		metrics.add(computeBehavAppropr(traces, transitions.size()));
		metrics.add(computeStructAppropr(transitions.size(), places.size()));

		return metrics;

	}

	private void testMethod(Trace trace, Transition transition) {
		Collection<Place> outPlaces = transition.getOutgoingPlaces();
		for (Place outPlace : outPlaces) {
			outPlace.produceToken();
			trace.increaseNumberOfProducedTokens();
		}
	}

	// Compute Fitness
	private double computeFitness(Collection<Trace> traces) {
		int nm = 0;
		int nc = 0;
		int nr = 0;
		int np = 0;
		for (Trace trace : traces) {
			nm += trace.getNumberOfInstances()
					* trace.getNumberOfMissingTokens();
			nc += trace.getNumberOfInstances()
					* trace.getNumberOfConsumedTokens();
			nr += trace.getNumberOfInstances()
					* trace.getNumberOfRemainingTokens();
			np += trace.getNumberOfInstances()
					* trace.getNumberOfProducedTokens();
		}
		double fitness = 0.5 * (1 - nm / nc) + 0.5 * (1 - nr / np);
		return Math.round(fitness * 100.0) / 100.0;
	}

	// Compute Simple Behavioral Appropriateness
	private double computeBehavAppropr(Collection<Trace> traces,
			int numberOfTransitions) {
		double numerator = 0;
		int sumOfInstances = 0;
		for (Trace trace : traces) {
			numerator += trace.getNumberOfInstances()
					* (numberOfTransitions - trace
							.getMeanNumberOfEnabledTransitions());
			sumOfInstances += trace.getNumberOfInstances();
		}
		double behavAppropr = numerator
				/ (double) ((numberOfTransitions - 1) * sumOfInstances);
		return Math.round(behavAppropr * 100.0) / 100.0;
	}

	// Compute Simple Structural Appropriateness
	private double computeStructAppropr(int numberOfTransitions,
			int numberOfPlaces) {
		double structAppropr = (numberOfTransitions + 2)
				/ (double) (numberOfTransitions + numberOfPlaces);
		return Math.round(structAppropr * 100.0) / 100.0;
	}

}
