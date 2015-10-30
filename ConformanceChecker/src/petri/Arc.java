package petri;

public class Arc {
	// if true then from Place to Transition, if false then from Transition to
	private Boolean direction;
	private Transition transition;
	private Place place;

	public Arc(Boolean direction, Transition transition, Place place) {
		super();
		this.direction = direction;
		this.transition = transition;
		this.place = place;
	}

	public Transition getTransition() {
		return transition;
	}

	public Place getPlace() {
		return place;
	}

	public Boolean isDirection() {
		return direction;
	}

}
