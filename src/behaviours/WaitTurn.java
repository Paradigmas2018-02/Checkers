package behaviours;

import agents.Player;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class WaitTurn extends CyclicBehaviour {
	private Boolean flag = true;
	private String playerName;
	private String idConversation;

	public WaitTurn(String uniqueID, String adversaryName) {
		idConversation = uniqueID;
		playerName = adversaryName;
	}

	public void action() {
		if (flag) {
			System.out.println(myAgent.getAID().getLocalName() + ": esperando");
			flag = false;
		}
		MessageTemplate isMyGame = MessageTemplate.MatchConversationId(idConversation);
		MessageTemplate isMyTurn = MessageTemplate.MatchPerformative(ACLMessage.INFORM);

		MessageTemplate stillMyGame = MessageTemplate.MatchConversationId(idConversation);
		MessageTemplate messageIWon = MessageTemplate.MatchPerformative(ACLMessage.FAILURE);

		ACLMessage iWon = myAgent.receive(MessageTemplate.and(stillMyGame, messageIWon));
		ACLMessage myTurn = myAgent.receive(MessageTemplate.and(isMyGame, isMyTurn));

		if (myTurn != null) {
			myAgent.addBehaviour(new Play(idConversation, playerName));
			myAgent.removeBehaviour(this);

		}

		if (iWon != null) {
			System.out.println(myAgent.getLocalName() + ": EITCHA, Ganhei!");
			Player p = (Player) myAgent;
			myAgent.removeBehaviour(this);
		}

	}
}
