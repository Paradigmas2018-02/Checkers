package checkers;

import java.util.ArrayList;

public class CheckersManager {
	public static ArrayList<Checkers> jogos = new ArrayList<Checkers>();

	public static Checkers createChecker(String uniqueID) {
		Checkers c = new Checkers(uniqueID);
		jogos.add(c);
		return c;
	}

	public static Checkers getChecker(String id) {
		for (Checkers c : jogos) {
			if (c.getId().equals(id)) {
				return c;
			}
		}
		return null;
	}

	public static void removeChecker(String id) {
		Checkers target = null;
		for (Checkers c : jogos) {
			if (c.getId().equals(id)) {
				target = c;
			}
		}
		if (target != null) {
			target.endGame(false);
			jogos.remove(target);			
		}
	}
}
