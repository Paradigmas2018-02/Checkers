package agents;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import behaviours.FindOtherPlayer;
import behaviours.WaitFirstPlayer;
import checkers.Board;
import checkers.Checkers;
import checkers.Piece;
import checkers.Square;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class Player extends Agent {
	private Checkers game;
	private ArrayList<Piece> pieces;
	private String name;

	public Player(String name, Checkers c, ArrayList<Piece> pieces) {
		game = c;
		this.pieces = pieces;
		this.name = name;
	}

	protected void setup() {
		// Register the book-selling service in the yellow pages
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType(ConversationConstants.PLAYER_TYPE);
		sd.setName(name);
		dfd.addServices(sd);

		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

		if (name == ConversationConstants.PLAYER_1_NAME) {
			addBehaviour(new FindOtherPlayer());
		} else {
			addBehaviour(new WaitFirstPlayer());
		}

	}

	public void play() {
		Board board = game.getBoard();
		
		Vector<Square> allPossibleMoves = new Vector<Square>();
		Vector<Piece> correspondingPiece = new Vector<Piece>();
		
		for(Piece p : pieces) {
			Vector<Square> pieceMoves = new Vector<Square>();
			pieceMoves = board.getPossibleMoves(p);
			
			for(Square s: pieceMoves) {
				correspondingPiece.add(p);
			}
			
			allPossibleMoves.addAll(pieceMoves);
		}
		
		Random generator = new Random();
		int index = generator.nextInt(allPossibleMoves.size());
		
		Square move = allPossibleMoves.elementAt(index);
		Piece p = correspondingPiece.elementAt(index);
		
		game.selectSquare(game.getBoard().getSquare(p));
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		game.selectSquare(move);
	
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
