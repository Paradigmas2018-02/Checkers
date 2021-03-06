package agents;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import behaviours.Congratulate;
import behaviours.FindOtherPlayer;
import checkers.Checkers;
import checkers.Move;
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

	private boolean isPlaying = false;

	public Player(String name) {
		this.nome = name;
	}

	protected void setup() {
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

	public boolean play(String adversaryName) {
		checkGameEnd(adversaryName);

		if (isPlaying) {
			Vector<Move> allPossibleMoves = getAllPossibleMoves();
			Random generator = new Random();
			int index = generator.nextInt(allPossibleMoves.size());

			Square move = allPossibleMoves.elementAt(index).getSquare();
			Piece p = allPossibleMoves.elementAt(index).getPiece();

			game.selectSquare(game.getBoard().getSquare(p));

			JADEHelper.esperarVariando(100);

			game.selectSquare(move);
			return true;
		} else {
			return false;
		}
	}

	private Vector<Move> getAllPossibleMoves() {
		Vector<Move> allPossibleMoves = new Vector<Move>();

		for (Piece p : pieces) {
			Vector<Square> pieceMoves = getMoves(p);
			for (Square s : pieceMoves) {
				allPossibleMoves.add(new Move(p, s));
			}
		}
		return allPossibleMoves;
	}

	private Vector<Square> getMoves(Piece p) {
		Vector<Square> pieceMoves = new Vector<Square>();
		pieceMoves = game.getBoard().getPossibleMoves(p);
		return pieceMoves;
	}

	private void checkGameEnd(String adversaryName) {
		if (!hasActivePieces() || getAllPossibleMoves().size() == 0) {
			isPlaying = false;
			addBehaviour(new Congratulate(game.getId(), adversaryName));
		}
	}

	private boolean hasActivePieces() {
		for (Piece p : pieces) {
			if (p.isActive()) {
				return true;
			}
		}
		return false;
	}
	public void desistir() {
		takeDown();
	}
	protected void takeDown() {
		try {
			DFService.deregister(this);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		System.out.println("Player-Agent " + getAID().getName() + " terminating.");
	}

	public void startGame(Checkers c, boolean flag) {
		isPlaying = true;
		game = c;
		if (flag) {
			pieces = c.getBoard().getP1Pieces();
		} else {
			pieces = c.getBoard().getP2Pieces();
		}
	}

	public String getNome() {
		return nome;
	}
}
