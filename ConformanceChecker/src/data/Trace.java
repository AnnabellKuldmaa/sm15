package data;

import java.util.ArrayList;
import java.util.List;

public class Trace {
	private String name;
	private List<Event> events;
	private int numberOfInstances;

	public Trace(String name) {
		super();
		this.name = name;
		this.events = new ArrayList<Event>();
		this.numberOfInstances = 1;
	}

	public List<Event> getEvents() {
		return events;
	}

	public String getName() {
		return name;
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

}
