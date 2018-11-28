package behaviours;

import java.util.Random;

import agents.ConversationConstants;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class FindOtherPlayer extends Behaviour {
	private boolean isEsperandoResposta = false;
	private boolean jogoDecidido = false;
	private Integer myRoll;

	@Override
	public void action() {

		if (!isEsperandoResposta) {
			System.out.println(myAgent.getName() + " CAÇANDO PLAYER 2");
			AID player2ID = searchPlayer2(myAgent);

			if (player2ID != null) {
				ACLMessage aclMessage = new ACLMessage(ACLMessage.CFP);
				aclMessage.setConversationId(ConversationConstants.PLAYER_TO_PLAYER);

				Random generator = new Random();
				myRoll = generator.nextInt();

				System.out.println(myAgent.getName() + " ACHEI O PLAYER 2 e ROLEI");
				System.out.println(myAgent.getName() + myRoll);

				aclMessage.setContent(myRoll.toString());

				aclMessage.addReceiver(player2ID);
				myAgent.send(aclMessage);
				isEsperandoResposta = true;
			}
		} else {
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);

			ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				jogoDecidido = true;
				String text = msg.getContent();
				Integer roll = null;
				try {
					roll = Integer.parseInt(text);
					if (roll > myRoll) {
						System.out.println(myAgent.getName() + "Affs, perdi. Pode começar que to esperando");
						myAgent.addBehaviour(new Wait());
					} else {
						System.out.println(myAgent.getName() + "Rá ganhei, vou jogar");
						myAgent.addBehaviour(new Play());
					}
				} catch (Exception e) {
				}
			}

			mt = MessageTemplate.MatchPerformative(ACLMessage.REFUSE);

			msg = myAgent.receive(mt);
			if (msg != null) {
				System.out.println(myAgent.getName() + "UÉ");
			}
		}
	}

	public static AID searchPlayer2(Agent agent) {

		DFAgentDescription dfAgentDescription = new DFAgentDescription();
		ServiceDescription serviceDescription = new ServiceDescription();

		serviceDescription.setType(ConversationConstants.PLAYER_TYPE);
		serviceDescription.setName(ConversationConstants.PLAYER_2_NAME);

		dfAgentDescription.addServices(serviceDescription);

		DFAgentDescription[] result;
		try {
			result = DFService.search(agent, dfAgentDescription);

			AID agentsAID = result[0].getName();
			if (agentsAID == null) {
				System.out.println("Null AID:" + serviceDescription.getName() + " and " + serviceDescription.getType());
			}

			return result[0].getName();

		} catch (FIPAException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static AID searchPlayer(Agent agent) {
		DFAgentDescription dfAgentDescription = new DFAgentDescription();
		ServiceDescription serviceDescription = new ServiceDescription();

		serviceDescription.setType(ConversationConstants.PLAYER_TYPE);

		dfAgentDescription.addServices(serviceDescription);

		DFAgentDescription[] results;
		try {
			results = DFService.search(agent, dfAgentDescription);

			AID agentsAID = null;
			for(DFAgentDescription desc : results) {
				if(!desc.getName().getName().equals(agent.getAID().getName())) {
					agentsAID = desc.getName();
					break;
				}
			}
			if (agentsAID == null) {
				System.out.println("Null AID:" + serviceDescription.getName() + " and " + serviceDescription.getType());
			}
			return agentsAID;

		} catch (FIPAException e) {
			e.printStackTrace();
			return null;
		}

	}
	@Override
	public boolean done() {
		return jogoDecidido;
	}
}
