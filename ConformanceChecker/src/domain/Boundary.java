package domain;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

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
			List<Double> metrics = controller.doLogReplay(xPathOfPetriNet, xPathOfLog);
			
			System.out.println("Fitness: " +  metrics.get(0));
			System.out.println("Simple Behavioral Appropriateness: "
					+ metrics.get(1));
			System.out.println("Simple Structural Appropriateness: "
					+ metrics.get(2));

			
		} catch (Exception e) {
			System.out.println("Invalid input. Something went wrong...");
		}

	}

}
