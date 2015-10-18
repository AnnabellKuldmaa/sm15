package parsers;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;

import org.deckfour.xes.model.XEvent;
import org.processmining.models.connections.GraphLayoutConnection;
import org.processmining.models.graphbased.directed.petrinet.PetrinetEdge;
import org.processmining.models.graphbased.directed.petrinet.PetrinetGraph;
import org.processmining.models.graphbased.directed.petrinet.PetrinetNode;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.graphbased.directed.petrinet.impl.PetrinetFactory;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.plugins.pnml.Pnml;

public class PNMLTest {

	public static void main(String[] args) {
		PnmlImportUtils ut = new PnmlImportUtils();
		File f = new File ("test.pnml");
		try {
			InputStream input = new FileInputStream(f);
			Pnml pnml = ut.importPnmlFromStream(input, f.getName(), f.length());
			PetrinetGraph net = PetrinetFactory.newInhibitorNet(pnml.getLabel() + " (imported from " + f.getName() + ")");
			Marking marking = new Marking();
			pnml.convertToNet(net,marking ,new GraphLayoutConnection(net));
			Collection <Place> places = net.getPlaces(); // List of Places :[p2, p3, p5, p4, p1]
			Collection <Transition> transitions = net.getTransitions(); //List of transitions: [D, A, B, E, C]
			//System.out.println(net.getNodes().toString());
			//Just example of getting transitions/places
			Place aPlace = places.iterator().next();
			Transition aTransition = transitions.iterator().next();
			
			for (Place place : places){
				System.out.println(net.getOutEdges(place));
				System.out.println(net.getInEdges(place));
			}
			
			// to get outgoing edges from a place
			Collection<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> edgesOutP = net.getOutEdges(aPlace);
			//System.out.println(edgesOutP.toString()); 
			
			//to get ingoing edges to a place
			Collection<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> edgesInP = net.getInEdges(aPlace);
			//System.out.println(edgesInP.toString());
			//to get outgoing edges from a transition
			Collection<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> edgesOutT = net.getOutEdges(aTransition);
			//System.out.println(edgesOutT.toString()); 
			//to get ingoing edges to a transition
			Collection<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> edgesInT = net.getInEdges(aTransition);
		
			//System.out.println(places.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
