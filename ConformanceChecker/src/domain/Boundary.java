package domain;

import domain.Controller;

public class Boundary {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Controller controller = new Controller();
		controller.doLogReplay("test.pnml", "test.xes");
		//controller.computeFitness("test.pnml", "test.xes");
			
	}

}
