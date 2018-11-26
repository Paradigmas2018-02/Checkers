package checkers;

import agents.Player;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;

public class Main {
	public static void main(String[] args) {
		Checkers c = new Checkers();
		
		Properties pp = new Properties();
		pp.setProperty(Profile.GUI, Boolean.TRUE.toString());
		Profile p = new ProfileImpl(pp);
		AgentContainer ac = jade.core.Runtime.instance().createMainContainer(p);
		
		Player p1 = new Player(c, c.getBoard().getP1Pieces());
		Player p2 = new Player(c, c.getBoard().getP2Pieces());
		try {
			ac.acceptNewAgent("Pablo", p1).start();
			ac.acceptNewAgent("Rodrigo", p2).start();
		}catch(StaleProxyException e) {
			throw new Error(e);
		}
		p2.play();
	}
}
