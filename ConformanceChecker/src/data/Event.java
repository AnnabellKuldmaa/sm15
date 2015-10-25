package data;

import java.util.Date;

public class Event {
	private String name;
	private Date timestamp;

	public String getName() {
		return name;
	}

	public Date getTimestamp() {
		return timestamp;
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

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Event event = (Event) obj;
		if (!(event.getName().equals(this.getName()) && event.getTimestamp()
				.equals(this.getTimestamp()))) {
			return false;
		}
		return true;
	}

}
