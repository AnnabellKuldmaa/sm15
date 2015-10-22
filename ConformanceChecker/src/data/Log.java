package data;

import java.util.ArrayList;
import java.util.List;

public class Log {
	// TODO:Collection or List
	private List<Trace> traces;

	public Log(List<Trace> traces) {
		super();
		this.traces = traces;
	}

	public Log() {
		super();
		this.traces = new ArrayList<Trace>();
	}

	public void addTrace(Trace trace) {
		this.traces.add(trace);
	}

	public List<Trace> getTraces() {
		return traces;
	}

	public void setTraces(List<Trace> traces) {
		this.traces = traces;
	}
}
