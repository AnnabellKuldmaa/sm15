package domain;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Boundary {

	public static void main(String[] args) {
		BufferedReader bufferReader = new BufferedReader(new InputStreamReader(
				System.in));
		try {
			System.out
					.println("Insert path of the Petri net file (.pnml file)");
			String xPathOfPetriNet = bufferReader.readLine();
			System.out.println("Insert path of the log (.xes file)");
			String xPathOfLog = bufferReader.readLine();
			Controller controller = new Controller();
			controller.doLogReplay(xPathOfPetriNet, xPathOfLog);
		} catch (Exception e) {
			System.out.println("Invalid input. Something went wrong...");
		}

	}

}
