package petri;

import java.util.Collection;
import java.util.HashSet;

public class Transition {

	private String eventName; 
	private Boolean enabled;
	private Collection<Arc> arcs;

	public Transition(String eventName) {
		super();
		this.eventName = eventName;
		this.enabled = false;
		this.arcs = new HashSet<Arc>();
	}

	public void addArc(Arc arc) {
		this.arcs.add(arc);
	}

	public String getEventName() {
		return eventName;
	}

	public Boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Collection<Arc> getArcs() {
		return arcs;
	}

	public Collection<Place> getIncomingPlaces() {
		Collection<Place> inPlaces = new HashSet<Place>();
		for (Arc arc : this.arcs) {
			// From Place to Transition
			if (arc.isDirection())
				inPlaces.add(arc.getPlace());
		}
		return inPlaces;
	}

	public Collection<Place> getOutgoingPlaces() {
		Collection<Place> outPlaces = new HashSet<Place>();
		for (Arc arc : this.arcs) {
			// Transition to Place
			if (!arc.isDirection())
				outPlaces.add(arc.getPlace());
		}
		return outPlaces;
	}

}
