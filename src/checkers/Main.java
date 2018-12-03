package checkers;

import agents.Player;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;

public class Main {
	public static void main(String[] args) {
		Properties pp = new Properties();
		pp.setProperty(Profile.GUI, Boolean.TRUE.toString());
		Profile p = new ProfileImpl(pp);
		AgentContainer ac = jade.core.Runtime.instance().createMainContainer(p);

		Player p1 = new Player("Lino");
		Player p2 = new Player("ED");
		
		Player p3 = new Player("Intruso");
		Player p4 = new Player("Amigo do intruso");

		try {
			ac.acceptNewAgent(p1.getNome(), p1).start();
			Thread.sleep(1000);
			ac.acceptNewAgent(p2.getNome(), p2).start();
//			Thread.sleep(5000);
//			ac.acceptNewAgent(p3.getNome(), p3).start();
//			Thread.sleep(15000);
//			ac.acceptNewAgent(p4.getNome(), p4).start();

		} catch (StaleProxyException | InterruptedException e) {
			throw new Error(e);
		}

	}
}
