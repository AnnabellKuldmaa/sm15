package domain;

import petri.Arc;
import petri.PetriNet;
import petri.Place;
import petri.Transition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import data.Event;
import data.Log;
import data.Trace;

public class Controller {

	public static void main(String[] args) {
		String xPathOfPetriNet = "test.pnml";
		String xPathOfLog = "test.xes";
		
		// TODO: funktsioonina
		// DoLogRepaly(String xPathOfPetriNet, String xPathOfLog) {
		PetriNet petri = EntityManager.getPetryNet(xPathOfPetriNet);
		Log log = EntityManager.getLog(xPathOfLog);
		
		Collection<Place> places = petri.getPlaces();
		
		// Testing Places
		for (Place place : places) {
			System.out.println("Place id: " + place.getName());
		}
		
		Collection<Transition> transitions = petri.getTransitions();
		// Testing Transitions
		for (Transition transition : transitions) {
			System.out.println("Transition id: " + transition.getEventName());
		}
		
		int T_v = transitions.size();
		System.out.println("T_v=" + T_v);
		
		int L = transitions.size();
		System.out.println("L=" + L);
		
		int N = places.size() + transitions.size();
		System.out.println("N=" + N);		
				
		List<Trace> traces = log.getTraces();
				
		// Testing traces
		for (Trace trace : traces) {
			System.out.print("Trace name: " + trace.getName() + ". ");
			System.out.print("Number of instances: " + trace.getNumberOfInstances() + ". ");
			System.out.print("Events: ");
			List<Event> events = trace.getEvents();
			for (Event event : events) {
				System.out.print(event.getName());
			}
			System.out.println();
		}
		
		int k = traces.size();
		
		// TODO: trace'i objektiks
		List<Integer> n = new ArrayList<Integer>(); //number of process instances combined into current trace
		List<Integer> m = new ArrayList<Integer>(); // number of missing tokens
		List<Integer> r = new ArrayList<Integer>(); // number of remaining tokens
		List<Integer> c = new ArrayList<Integer>(); // number of consumed tokens
		List<Integer> p = new ArrayList<Integer>(); // number of produced tokens
		List<Double> x = new ArrayList<Double>(); // mean number of enabled transitions
		
		System.out.println();
		System.out.println();
		
		for (Trace trace : traces) {
			
			System.out.print("Trace: ");
			for (Event event : trace.getEvents()) {
				System.out.print(event.getName());
			}
			System.out.println();
			
			PetriNet petrinet = new PetriNet("ID", transitions, places);
			
			int p_i = 0;
			int c_i = 0;
			int m_i = 0;
			List<Integer> numberOfEnabledTransitions = new ArrayList<Integer>();
			int numberOfEnabledTransitions_i = 0;
			
			Place currentPlace = null;
			
			// Start of the first step
	
			// Add the first token to start place
			currentPlace = petrinet.getStartPlace();
			currentPlace.produceToken();
			p_i++;
			System.out.println("Produced token to " + currentPlace.getName());

			Collection<Transition> outTransitions = currentPlace.getOutgoingTransitions();

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

			numberOfEnabledTransitions_i = petrinet.getNumberOfEnabledTransitions();
			numberOfEnabledTransitions.add(numberOfEnabledTransitions_i);

			System.out.println("Step 1: p_i=" + p_i + "; c_i=" + c_i + "; m_i=" + m_i + "; numberOfEnabledTransitions_i=" + numberOfEnabledTransitions_i);
			
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
						c_i++;
						System.out.println("Consumed token from " + inPlace.getName());
					} else {
						m_i++;
						System.out.println("Missing token in " + inPlace.getName());
					}
				}
				transition.setEnabled(false);

				// Produce tokens to the outgoing places
				Collection<Place> outPlaces =  transition.getOutgoingPlaces();
				for (Place outPlace : outPlaces) {
					outPlace.produceToken();
					p_i++;
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

				numberOfEnabledTransitions_i = petrinet.getNumberOfEnabledTransitions();
				//System.out.println("numberOfEnabledTransitions_i " + numberOfEnabledTransitions_i);
				numberOfEnabledTransitions.add(numberOfEnabledTransitions_i);

				// End of step
				System.out.println("Step " + step + ": p_i=" + p_i + "; c_i=" + c_i + "; m_i=" + m_i + "; numberOfEnabledTransitions_i=" + numberOfEnabledTransitions_i);


			}
			
			// End
			Place endPlace = petrinet.getEndPlace();
			if (endPlace.hasTokens()) {
				endPlace.consumeToken();
				c_i++;
				System.out.println("Consumed token from " + endPlace.getName());
			} else {
				m_i++;
				System.out.println("Missing token in " + endPlace.getName());
			}
			
			int n_i = trace.getNumberOfInstances();
			int r_i = petrinet.getNumberOfTokens();

			// Add to lists
			n.add(n_i);
			m.add(m_i);
			r.add(r_i);
			p.add(p_i);
			c.add(c_i);

			int numberOfEnabledTransitions_Sum = 0;
			for (int i = 0; i < numberOfEnabledTransitions.size(); i++) {
				//System.out.println("numberOfEnabledTransitions.get(i) " + numberOfEnabledTransitions.get(i));
				numberOfEnabledTransitions_Sum += numberOfEnabledTransitions.get(i);
			}
			
			numberOfEnabledTransitions.add(numberOfEnabledTransitions_Sum);

			double x_i = numberOfEnabledTransitions_Sum / (double) events.size();
			x.add(x_i);

			System.out.println("p_i=" + p_i + "; c_i=" + c_i + "; m_i=" + m_i + "; numberOfEnabledTransitions_Sum=" + 
					numberOfEnabledTransitions_Sum + "; x_i=" + x_i + "; r_i=" + r_i + "; n_i=" + n_i);

			System.out.println();
			
		}
		
		
		// TODO: funktsioonideks
		
		// Fitness
		int nm = 0;
		int nc = 0;
		int nr = 0;
		int np = 0;
		for (int i = 0; i < k; i++) {
			nm += n.get(i) * m.get(i);
			nc += n.get(i) * c.get(i);
			nr += n.get(i) * r.get(i);
			np += n.get(i) * p.get(i);
		}
		double f = 0.5 * (1 - nm / nc) + 0.5 * (1 - nr / np);
		System.out.println("f " + f); // 1.0
		
		// Simple Behavioral Appropriateness
		double numerator = 0;
		int nSum = 0;
		for (int i = 0; i < k; i++) {
			numerator += n.get(i) * (T_v - x.get(i));
			nSum += n.get(i);
		}
		double a_B = numerator / (double) ((T_v - 1) * nSum);
		System.out.println("a_B " + a_B); // 0.9236111
		
		// Simple Structural Appropriateness
		double a_S = (L + 2) / (double) N;
		System.out.println("a_S " + a_S); // 0.7
		
	}

}
