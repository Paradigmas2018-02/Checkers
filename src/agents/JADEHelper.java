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
		DFAgentDescription dfAgentDescription = new DFAgentDescription();
		ServiceDescription serviceDescription = new ServiceDescription();

		serviceDescription.setType(ConversationConstants.PLAYER_TYPE);

		dfAgentDescription.addServices(serviceDescription);

		DFAgentDescription[] results;
		try {
			results = DFService.search(agent, dfAgentDescription);

			AID agentsAID = null;
			for(DFAgentDescription desc : results) {
				if(desc.getName().getLocalName().equals(agentName)) {
					agentsAID = desc.getName();
					break;
				}
			}
			if (agentsAID == null) {
				System.out.println("Agente não encontrado");
				System.out.println("Null AID:" + serviceDescription.getName() + " and " + serviceDescription.getType());
			}
			return agentsAID;

		} catch (FIPAException e) {
			e.printStackTrace();
			return null;
		}

	}
	public static ArrayList<AID> searchPlayers(Agent myAgent) {
		DFAgentDescription dfAgentDescription = new DFAgentDescription();
		ServiceDescription serviceDescription = new ServiceDescription();

		serviceDescription.setType(ConversationConstants.PLAYER_TYPE);

		dfAgentDescription.addServices(serviceDescription);

		DFAgentDescription[] allResults;
		ArrayList<AID> results = new ArrayList<AID>();
		try {
			allResults = DFService.search(myAgent, dfAgentDescription);

			for(DFAgentDescription desc : allResults) {
				AID agentsAID = null;
				if(!desc.getName().getName().equals(myAgent.getAID().getName())) {
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
