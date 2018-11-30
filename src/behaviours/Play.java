package behaviours;

import agents.ConversationConstants;
import agents.JADEHelper;
import agents.Player;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class Play extends OneShotBehaviour {
	private String idConversation;
	private String playerName;

	public Play(String uniqueID, String adversaryName) {
		idConversation = uniqueID;
		playerName = adversaryName;
	}

	public void action() {

		AID player2ID = JADEHelper.searchPlayer(myAgent, playerName);
		if (player2ID != null) {
			ACLMessage aclMessage = new ACLMessage(ACLMessage.INFORM);
			aclMessage.setConversationId(idConversation);

			aclMessage.setContent("Sua vez!");
			Boolean joguei = play();
			if (joguei) {
				System.out.println(myAgent.getAID().getLocalName() + ": Joguei, sua vez!");
				aclMessage.addReceiver(player2ID);
				myAgent.send(aclMessage);
				myAgent.addBehaviour(new Wait(idConversation, playerName));
			}
		} else {
			System.out
					.println(myAgent.getAID().getLocalName() + ": Não achei o outro cara, algo deu errado no jogo :/");
			myAgent.addBehaviour(new FindOtherPlayer(myAgent));
		}
	}

	public boolean play() {
		if (myAgent instanceof Player) {
			Player p = (Player) myAgent;
			return p.play(playerName);
		} else
			return true;
	}

} // End of inner class OfferRequestsServer