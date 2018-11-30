package checkers;

public class Move {
	private Piece p;
	private Square s;
	
	public Move(Piece p, Square s) {
		this.p = p;
		this.s = s;
	}

	public Piece getPiece() {
		return p;
	}

	public Square getSquare() {
		return s;
	}
}
