package petri;

import java.util.Collection;

public class PetriNet {

	private String id;
	private Collection<Transition> transitions;
	private Collection<Place> places;

	public PetriNet() {
		super();
	}

	public PetriNet(Collection<Transition> transitions,
			Collection<Place> places) {
		super();
		this.id = id;
		this.transitions = transitions;
		this.places = places;
	}

	public Collection<Transition> getTransitions() {
		return transitions;
	}

	public Collection<Place> getPlaces() {
		return places;
	}

	public int getNumberOfTokens() {
		int numberOfTokens = 0;
		for (Place place : this.places)
			numberOfTokens = numberOfTokens + place.getNumberOfTokens();

		return numberOfTokens;
	}

	public Transition getTransitionWithName(String name) {
		for (Transition transition : this.transitions) {
			if (transition.getEventName().equals(name))
				return transition;
		}
		return null;

	}

	public Place getStartPlace() {
		for (Place place : this.places) {
			if (place.getIncomingTransitions().size() == 0)
				return place;
		}
		return null;
	}

	public Place getEndPlace() {
		for (Place place : this.places) {
			if (place.getOutgoingTransitions().size() == 0)
				return place;
		}
		return null;
	}
	
	public void enableTransitions(Collection<Transition> transitions) {
		for (Transition transition : transitions) {
			boolean enabled = true;
			for (Place inPlace : transition.getIncomingPlaces()) {
				if (inPlace.hasTokens() == false) {
					enabled = false;
				}
			}
			transition.setEnabled(enabled);
		}
	}
	
	public int getNumberOfEnabledTransitions() {
		int numberOfEnabledTransitions = 0;
		for (Transition transition : this.transitions) {
			if (transition.isEnabled()) {
				System.out.println("Enabled transition:" + transition.getEventName());
				numberOfEnabledTransitions++;
			}
		}
		return numberOfEnabledTransitions;
	}
}
