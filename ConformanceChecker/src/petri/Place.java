package petri;

public class Place {

	private String id;
	private String name; // TODO: need both?
	private int numOfTokens;

	public Place(String id, String name, int numOfTokens) {
		super();
		this.id = id;
		this.name = name;
		this.numOfTokens = numOfTokens;
	}

}
