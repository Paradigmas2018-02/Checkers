package agents;

import java.util.ArrayList;

import behaviours.CheckTurn;
import behaviours.OfferTurn;
import checkers.Checkers;
import checkers.Piece;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class Player extends Agent {
	private Checkers game;
	private ArrayList<Piece> pieces;

	public Player(Checkers c, ArrayList<Piece> pieces) {
		game = c;
		this.pieces = pieces;
		
	}
	protected void setup() {
		// Register the book-selling service in the yellow pages
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Checkers-Player");
		sd.setName("JADE-Checker-Playing");
		dfd.addServices(sd);
		
		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		addBehaviour(new CheckTurn());
		addBehaviour(new OfferTurn());
	}
	protected void takeDown() {
		try {
			DFService.deregister(this);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		// Printout a dismissal message
		System.out.println("Player-Agent " + getAID().getName() + " terminating.");
	}
}
