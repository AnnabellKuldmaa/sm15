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

}
