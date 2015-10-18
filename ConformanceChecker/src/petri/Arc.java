package petri;

public class Arc {
	
	private String id;
	//if 0 then from Place to Transition, if 1 then from Transition to Place
	private int direction; 
	private Transition transition;
	private Place place;
	
	public Arc(String id, int direction, Transition transition, Place place) {
		super();
		this.id = id;
		this.direction = direction;
		this.transition = transition;
		this.place = place;
	}

	
}
