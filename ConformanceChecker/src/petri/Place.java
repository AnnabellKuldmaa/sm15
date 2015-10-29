package petri;

import java.util.Collection;
import java.util.HashSet;

public class Place {

	private String name; // p1
	private int numberOfTokens;
	private Collection<Arc> arcs;

	public String getName() {
		return name;
	}
	
	public void setNumberOfTokens(int numberOfTokens) {
		this.numberOfTokens = numberOfTokens;
	}

	public int getNumberOfTokens() {
		return numberOfTokens;
	}

	public Collection<Arc> getArcs() {
		return arcs;
	}

	public Place(String name) {
		super();
		this.name = name;
		this.numberOfTokens = 0;
		this.arcs = new HashSet<Arc>();
	}

	public void addArc(Arc arc) {
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
		if (this.numberOfTokens > 0)
			return true;
		else
			return false;
	}

	public void produceToken() {
		this.numberOfTokens++;
	}

	public void consumeToken() {
		this.numberOfTokens--;
	}
}