package agents;

import java.util.ArrayList;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class JADEHelper {

	public static AID searchPlayer(Agent agent, String agentName) {
		DFAgentDescription description = createDescription(agentName);

		DFAgentDescription[] results;
		try {
			results = DFService.search(agent, description);
			
			AID agentsAID = results[0].getName();
			if (agentsAID == null) {
				System.out.println("Agente não encontrado");
			}
			return agentsAID;

		} catch (FIPAException e) {
			e.printStackTrace();
			return null;
		}

	}

	private static DFAgentDescription createDescription(String agentName) {
		DFAgentDescription dfAgentDescription = new DFAgentDescription();
		ServiceDescription serviceDescription = new ServiceDescription();

		if (agentName != null) {
			serviceDescription.setName(agentName);
		}
		serviceDescription.setType(ConversationConstants.PLAYER_TYPE);

		dfAgentDescription.addServices(serviceDescription);
		return dfAgentDescription;
	}
	
	public static void esperar(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}
	public static ArrayList<AID> searchPlayers(Agent myAgent) {
		DFAgentDescription description = createDescription(null);

		DFAgentDescription[] allResults;
		ArrayList<AID> results = new ArrayList<AID>();
		try {
			allResults = DFService.search(myAgent, description);

			for (DFAgentDescription desc : allResults) {
				AID agentsAID = null;
				if (!desc.getName().getName().equals(myAgent.getAID().getName())) {
					agentsAID = desc.getName();
					results.add(agentsAID);
				}
			}
			return results;
		} catch (FIPAException e) {
			e.printStackTrace();
			return null;
		}
	}
}
