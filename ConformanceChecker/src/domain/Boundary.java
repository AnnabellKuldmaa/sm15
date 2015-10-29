package domain;

import domain.Controller;

public class Boundary {

	public static void main(String[] args) {
		
		Controller controller = new Controller();
		controller.doLogReplay("test.pnml", "test.xes");
			
	}

}
