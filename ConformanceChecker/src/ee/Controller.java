package ee;

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

			// Initial marking - one token in start
			Place startPlace = petriNet.getStartPlace();
			startPlace.produceToken();
			trace.increaseNumberOfProducedTokens();

			// Iterate over events
			List<Event> events = trace.getEvents();
			for (Event event : events) {

				petriNet.enableTransitions();

				int numberOfEnabledTransitions = petriNet
						.getNumberOfEnabledTransitions();
				trace.increaseNumberOfEnabledTransitions(numberOfEnabledTransitions);

				// Find respective transition
				String eventName = event.getName();
				Transition transition = petriNet
						.getTransitionWithName(eventName);

				// Consume tokens or add missing tokens to the incoming places
				Collection<Place> inPlaces = transition.getIncomingPlaces();
				for (Place inPlace : inPlaces) {
					Boolean hasTokens = inPlace.hasTokens();
					if (hasTokens)
						inPlace.consumeToken();
					else
						trace.increaseNumberOfMissingTokens();

					trace.increaseNumberOfConsumedTokens();
				}

				// Produce tokens to the outgoing places
				Collection<Place> outPlaces = transition.getOutgoingPlaces();
				for (Place outPlace : outPlaces) {
					outPlace.produceToken();
					trace.increaseNumberOfProducedTokens();
				}

			}

			// Check final marking
			Place endPlace = petriNet.getEndPlace();
			Boolean hasTokens = endPlace.hasTokens();
			if (hasTokens)
				endPlace.consumeToken();
			else
				trace.increaseNumberOfMissingTokens();

			trace.increaseNumberOfConsumedTokens();

			int numberOfTokens = petriNet.getNumberOfTokens();
			trace.setNumberOfRemainingTokens(numberOfTokens);

			int numberOfEnabledTransitions = trace
					.getNumberOfEnabledTransitions();
			trace.computeMeanNumberOfEnabledTransitions(
					numberOfEnabledTransitions, events.size());

			// Clean petrinet: number of tokens to zero and disable transitions
			petriNet.clearPetrinet();
		}

		Collection<Transition> transitions = petriNet.getTransitions();
		Collection<Place> places = petriNet.getPlaces();

		List<Double> metrics = new LinkedList<Double>();
		metrics.add(computeFitness(traces));
		metrics.add(computeBehavAppropr(traces, transitions.size()));
		metrics.add(computeStructAppropr(transitions.size(), places.size()));

		return metrics;

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
