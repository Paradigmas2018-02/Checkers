package checkers;

import agents.ConversationConstants;
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
		
		Player p1 = new Player(ConversationConstants.PLAYER_1_NAME, c, c.getBoard().getP1Pieces());
		Player p2 = new Player(ConversationConstants.PLAYER_2_NAME, c, c.getBoard().getP2Pieces());
		Player p3 = new Player(ConversationConstants.PLAYER_2_NAME, c, c.getBoard().getP2Pieces());

		try {
			ac.acceptNewAgent("Pablo", p1).start();
			Thread.sleep(100);
			ac.acceptNewAgent("Rodrigo", p2).start();
			Thread.sleep(5000);
			ac.acceptNewAgent("Intruso", p3).start();

		}catch(StaleProxyException | InterruptedException e) {
			throw new Error(e);
		}
		
	}
}
