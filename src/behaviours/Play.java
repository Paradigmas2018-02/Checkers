package behaviours;

import java.util.Random;

import agents.ConversationConstants;
import agents.Player;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Play extends OneShotBehaviour {
	
	public void action() {
		
		play();
		System.out.println(myAgent.getAID().getLocalName() + ": Joguei!");
		AID player2ID = FindOtherPlayer.searchPlayer(myAgent);
		if (player2ID != null) {
			ACLMessage aclMessage = new ACLMessage(ACLMessage.INFORM);
			aclMessage.setConversationId(ConversationConstants.PLAY);

			aclMessage.setContent("Sua vez!");
			System.out.println(myAgent.getAID().getLocalName()  + ": Joguei, sua vez!");
			
			aclMessage.addReceiver(player2ID);
			myAgent.send(aclMessage);
			myAgent.addBehaviour(new Wait());
		}else {
			System.out.println(myAgent.getAID().getLocalName()  +": Não achei o outro cara");
		}
	}

	public void play() {
		if(myAgent instanceof Player) {
			Player p = (Player) myAgent;
			p.play();
		}
	}

} // End of inner class OfferRequestsServer