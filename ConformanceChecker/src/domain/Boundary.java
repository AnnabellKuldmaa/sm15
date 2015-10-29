package domain;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Boundary {

	public static void main(String[] args) {
		BufferedReader bufferReader = new BufferedReader(new InputStreamReader(
				System.in));
		String xPathOfLog;
		try {
			System.out.println("Insert xPath of Petri Net (.pnml file)");
			String xPathOfPetriNet = bufferReader.readLine();
			System.out.println("Insert xPath of Log (.xes file)");
			xPathOfLog = bufferReader.readLine();
			Controller controller = new Controller();
			controller.doLogReplay(xPathOfPetriNet, xPathOfLog);
		} catch (Exception e) {
			System.out.println("Invalid input");
			// e.printStackTrace();
		}

	}

}
