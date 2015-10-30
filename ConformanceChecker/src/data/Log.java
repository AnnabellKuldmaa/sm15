package data;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class Log {
	private Collection<Trace> traces;

	public Log(List<Trace> traces) {
		super();
		this.traces = traces;
	}

	public Log() {
		super();
		this.traces = new HashSet<Trace>();
	}

	public void addTrace(Trace trace) {
		this.traces.add(trace);
	}

	public Collection<Trace> getTraces() {
		return traces;
	}

}
