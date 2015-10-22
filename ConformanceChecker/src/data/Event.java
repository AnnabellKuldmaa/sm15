package data;

import java.util.Date;

public class Event {
	private String name;
	private Date timestamp;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Event(String name, Date timestamp) {
		super();
		this.name = name;
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "Event [name=" + name + "]";
	}

}
