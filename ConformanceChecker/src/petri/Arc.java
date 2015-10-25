package petri;

public class Arc {
	// if true then from Place to Transition, if false then from Transition to
	// Place
	// Place
	private boolean direction;
	private Transition transition;
	private Place place;

	public Arc(boolean direction, Transition transition, Place place) {
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

	public String getSourceName() {
		// from place to transition
		if (this.direction)
			return this.place.getName();
		// from transition to place
		else
			return this.transition.getEventName();
	}

	public String getTargetName() {
		// from place to transition
		if (this.direction)
			return this.transition.getEventName();
		// from transition to place
		else
			return this.place.getName();
	}

	public boolean isDirection() {
		return direction;
	}

}
