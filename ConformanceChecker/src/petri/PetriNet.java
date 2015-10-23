package petri;

import java.util.Collection;

public class PetriNet {

	private String id;
	private Collection<Transition> transitions;
	private Collection<Place> places;

	// TODO: marking, initialMarking
	// TODO: use list or collection?

	public String getId() {
		return id;
	}

	public PetriNet() {
		super();
	}

	public PetriNet(String id, Collection<Transition> transitions,
			Collection<Place> places) {
		super();
		this.id = id;
		this.transitions = transitions;
		this.places = places;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Collection<Transition> getTransitions() {
		return transitions;
	}

	public void setTransitions(Collection<Transition> transitions) {
		this.transitions = transitions;
	}

	public Collection<Place> getPlaces() {
		return places;
	}

	public void setPlaces(Collection<Place> places) {
		this.places = places;
	}

	public int getNumberOfEnabledTransitions() {
		int numberOfEnabledTransitions = 0;
		for (Transition transition : this.transitions) {
			if (transition.isEnabled())
				numberOfEnabledTransitions++;
		}
		return numberOfEnabledTransitions;
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
}
