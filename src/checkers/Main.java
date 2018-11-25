package checkers;

import agents.Player;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;

public class Main {
	public static void main(String[] args) {
		new Checkers();
		Properties pp = new Properties();
		pp.setProperty(Profile.GUI, Boolean.TRUE.toString());
		Profile p = new ProfileImpl(pp);
		AgentContainer ac = jade.core.Runtime.instance().createMainContainer(p);
		try {
			ac.acceptNewAgent("Pablo", new Player()).start();
		}catch(StaleProxyException e) {
			throw new Error(e);
		}
	}
}
