package behaviours;

import agents.JADEHelper;
import agents.Player;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class Play extends OneShotBehaviour {
	private String idConversation;
	private AID adversary;

	public Play(String uniqueID, AID adversary) {
		idConversation = uniqueID;
		this.adversary = adversary;
	}

	public void action() {

		AID adversaryID = JADEHelper.searchPlayer(myAgent, adversary.getLocalName());
		if (adversaryID != null) {
			ACLMessage aclMessage = new ACLMessage(ACLMessage.INFORM);
			aclMessage.setConversationId(idConversation);

			aclMessage.setContent("Sua vez!");
			Boolean joguei = play();
			if (joguei) {
				System.out.println(myAgent.getAID().getLocalName() + ": Joguei, sua vez!");
				aclMessage.addReceiver(adversaryID);
				myAgent.send(aclMessage);
				myAgent.addBehaviour(new WaitTurn(idConversation, adversary));
			}
		} else {
			System.out.println(myAgent.getLocalName() + ": Não achei o outro cara, algo deu errado no jogo :/");
			myAgent.addBehaviour(new FindOtherPlayer(myAgent));
		}
	}

	public boolean play() {
		if (myAgent instanceof Player) {
			Player p = (Player) myAgent;
			return p.play(adversary.getLocalName());
		} else
			return true;
	}

}
