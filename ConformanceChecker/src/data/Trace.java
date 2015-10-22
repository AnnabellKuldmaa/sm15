package data;

import java.util.ArrayList;
import java.util.List;

public class Trace {
	private String name;
	private List<Event> events;

	public Trace(String name) {
		super();
		this.name = name;
		this.events = new ArrayList<Event>();
	}

	public Trace() {
		super();
		this.events = new ArrayList<Event>();
	}

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addEvent(Event event) {
		this.events.add(event);
	}

}
