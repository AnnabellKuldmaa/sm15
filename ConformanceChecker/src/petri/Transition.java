package petri;

import java.util.Collection;
import java.util.HashSet;

public class Transition {

	private String id;
	private String eventName; // TODO: need both?
	private Boolean isEnabled;
	private Collection<Arc> arcs;

	public Transition(String eventName) {
		super();
		this.eventName = eventName;
		this.isEnabled = false;
		this.arcs = new HashSet<Arc>();
	}

	public void addEdge(Arc arc) {
		this.arcs.add(arc);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Collection<Arc> getArcs() {
		return arcs;
	}

	public void setArcs(Collection<Arc> arcs) {
		this.arcs = arcs;
	}

	public Collection<Arc> getIncomingEdges() {
		Collection<Arc> inArcs = new HashSet<Arc>();
		for (Arc arc : this.arcs) {
			// From Place to Transition
			if (arc.isDirection())
				inArcs.add(arc);
		}
		return inArcs;
	}

	public Collection<Arc> getOutgoingEdges() {
		Collection<Arc> outArcs = new HashSet<Arc>();
		for (Arc arc : this.arcs) {
			// Transition to Place
			if (!arc.isDirection())
				outArcs.add(arc);
		}
		return outArcs;
	}

}
