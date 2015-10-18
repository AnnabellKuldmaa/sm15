package petri;

public class Transition {
	
	private String id;
	private String eventName; //TODO: need both?
	private Boolean isEnabled;
	
	public Transition(String id, String eventName) {
		super();
		this.id = id;
		this.eventName = eventName;
		this.isEnabled = false;
	}
	

}
