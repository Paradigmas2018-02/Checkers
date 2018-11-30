package checkers;

import java.util.ArrayList;
import java.util.UUID;

public class CheckersManager {
	public static ArrayList<Checkers> jogos = new ArrayList<Checkers>();
	
	public static Checkers createChecker(String uniqueID) {
		Checkers c = new Checkers(uniqueID);
		jogos.add(c);
		return c;
	}
	
	public static Checkers getChecker(String id) {
		for(Checkers c : jogos) {
			if(c.getId().equals(id)) {
				return c;
			}
		}
		return null;
	}
	
	private static void updateGames() {
		for(Checkers c : jogos) {
			if(c.isEnded()) {
				jogos.remove(c);
			}
		}
	}
}
