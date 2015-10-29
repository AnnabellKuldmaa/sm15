package data;

import java.util.ArrayList;
import java.util.List;

public class Trace {
	private String name;
	private List<Event> events;
	private int numberOfInstances;
	private int numberOfMissingTokens;
	private int numberOfProducedTokens;
	private int numberOfConsumedTokens;
	private int numberOfRemainingTokens;
	private int numberOfEnabledTransitions;
	private double meanNumberOfEnabledTransitions;

	public Trace(String name) {
		super();
		this.name = name;
		this.events = new ArrayList<Event>();
		this.numberOfInstances = 1;
		this.numberOfMissingTokens = 0;
		this.numberOfProducedTokens = 0;
		this.numberOfConsumedTokens = 0;
		this.numberOfRemainingTokens = 0;
		this.numberOfEnabledTransitions = 0;
		this.meanNumberOfEnabledTransitions = 0;
	}

	public List<Event> getEvents() {
		return events;
	}

	public int getNumberOfInstances() {
		return numberOfInstances;
	}
	
	public void addEvent(Event event) {
		this.events.add(event);
	}

	public void addInstance() {
		this.numberOfInstances++;
	}
	
	public void increaseNumberOfMissingTokens() {
		this.numberOfMissingTokens++;
	}
	
	public int getNumberOfMissingTokens() {
		return numberOfMissingTokens;
	}
	
	public void increaseNumberOfProducedTokens() {
		this.numberOfProducedTokens++;
	}
	
	public int getNumberOfProducedTokens() {
		return numberOfProducedTokens;
	}
	
	public void increaseNumberOfConsumedTokens() {
		this.numberOfConsumedTokens++;
	}
	
	public int getNumberOfConsumedTokens() {
		return numberOfConsumedTokens;
	}
	
	public void setNumberOfRemainingTokens(int numberOfRemainingTokens) {
		this.numberOfRemainingTokens = numberOfRemainingTokens;
	}
	
	public int getNumberOfRemainingTokens() {
		return numberOfRemainingTokens;
	}
	
	public void increaseNumberOfEnabledTransitions(int numberOfEnabledTransitions) {
		this.numberOfEnabledTransitions += numberOfEnabledTransitions;
	}
	
	public int getNumberOfEnabledTransitions() {
		return numberOfEnabledTransitions;
	}
	
	public void computeMeanNumberOfEnabledTransitions(int numberOfEnabledTransitions, int numberOfEvents) {
		this.meanNumberOfEnabledTransitions = numberOfEnabledTransitions / (double) numberOfEvents;
	}
	
	public double getMeanNumberOfEnabledTransitions() {
		return meanNumberOfEnabledTransitions;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Trace trace = (Trace) obj;
		if (trace.getEvents().size() != this.getEvents().size())
			return false;

		for (int i = 0; i < trace.getEvents().size(); i++) {
			if (!this.getEvents().get(i).equals(trace.getEvents().get(i))) {
				return false;
			}

		}

		return true;
	}
	
	@Override
	public String toString() {
		String info = "Trace " +
				"\nNumber of produced tokens: " + numberOfProducedTokens +
				"\nNumber of consumed tokens: " + numberOfConsumedTokens +
				"\nNumber of missing tokens: " + numberOfMissingTokens +
				"\nNumber of remaining tokens: " + numberOfRemainingTokens +
				"\nNumber of enabled transitions: " + numberOfEnabledTransitions +
				"\nMean number of enabled transitions: " + meanNumberOfEnabledTransitions;
		return info;
	}

}
