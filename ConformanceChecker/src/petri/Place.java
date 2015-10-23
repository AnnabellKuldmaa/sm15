package petri;

import java.util.Collection;
import java.util.HashSet;

public class Place {

	private String id; // p1
	private String name; // TODO: need both?
	private int numOfTokens;
	private Collection<Arc> arcs;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getNumOfTokens() {
		return numOfTokens;
	}

	public void setNumOfTokens(int numOfTokens) {
		this.numOfTokens = numOfTokens;
	}

	public Collection<Arc> getArcs() {
		return arcs;
	}

	public void setArcs(Collection<Arc> arcs) {
		this.arcs = arcs;
	}

	public Place(String id) {
		super();
		this.id = id;
		this.numOfTokens = 0;
		this.arcs = new HashSet<Arc>();
	}

	public void addEdge(Arc arc) {
		this.arcs.add(arc);
	}

	public Collection<Transition> getIncomingTransitions() {
		Collection<Transition> inTransitions = new HashSet<Transition>();
		for (Arc arc : this.arcs) {
			// From Transition to Place
			if (!arc.isDirection())
				inTransitions.add(arc.getTransition());
		}
		return inTransitions;
	}

	public Collection<Transition> getOutgoingTransitions() {
		Collection<Transition> outTransitions = new HashSet<Transition>();
		for (Arc arc : this.arcs) {
			// From Transition to Place
			if (arc.isDirection())
				outTransitions.add(arc.getTransition());
		}
		return outTransitions;
	}

	public boolean hasTokens() {
		if (this.numOfTokens > 0)
			return true;
		else
			return false;
	}

	public void produceToken() {
		this.numOfTokens++;
	}

	public void consumeToken() {
		this.numOfTokens--;
	}
}