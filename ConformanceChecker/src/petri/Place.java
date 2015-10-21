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

	public Collection<Arc> getIncomingEdges() {
		Collection<Arc> inArcs = new HashSet<Arc>();
		for (Arc arc : this.arcs) {
			// From Transition to Place
			if (!arc.isDirection())
				inArcs.add(arc);
		}
		return inArcs;
	}

	public Collection<Arc> getOutgoingEdges() {
		Collection<Arc> outArcs = new HashSet<Arc>();
		for (Arc arc : this.arcs) {
			// From Place to Transition
			if (arc.isDirection())
				outArcs.add(arc);
		}
		return outArcs;
	}

}