package petri;

import java.util.Collection;

public class PetriNet {

	private Collection<Transition> transitions;
	private Collection<Place> places;

	public PetriNet() {
		super();
	}

	public PetriNet(Collection<Transition> transitions,
			Collection<Place> places) {
		super();
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
		Place startPlace = null;
		for (Place place : this.places) {
			Collection<Transition> inTransitions = place.getIncomingTransitions();
			if (inTransitions.size() == 0)
				startPlace = place;
		}
		return startPlace;
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
				if (inPlace.hasTokens() == false)
					enabled = false;
			}
			transition.setEnabled(enabled);
		}
	}
	
	public int getNumberOfEnabledTransitions() {
		int numberOfEnabledTransitions = 0;
		for (Transition transition : this.transitions) {
			if (transition.isEnabled())
				numberOfEnabledTransitions++;
		}
		return numberOfEnabledTransitions;
	}
	
	public void clearPetrinet() {
		for (Place place : this.places) {
			place.setNumberOfTokens(0);
		}
		for (Transition transition : this.transitions) {
			transition.setEnabled(false);
		}
	}
}
