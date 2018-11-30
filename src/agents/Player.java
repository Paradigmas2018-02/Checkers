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
	private String nome;
	
	public String getNome() {
		return nome;
	}

	private boolean isPlaying = false;
	public Player(String name) {
		this.nome = name;
	}

	protected void setup() {
		// Register the book-selling service in the yellow pages
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType(ConversationConstants.PLAYER_TYPE);
		sd.setName(nome);
		dfd.addServices(sd);

		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		addBehaviour(new FindOtherPlayer(this));
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
			Thread.sleep(1000);
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

	public void startGame(Checkers c, boolean flag) {
		isPlaying = true;
		game = c;
		if(flag) {
			pieces = c.getBoard().getP1Pieces();			
		}else {
			pieces = c.getBoard().getP2Pieces();
		}
	}
}
