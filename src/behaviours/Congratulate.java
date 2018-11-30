package behaviours;

import agents.JADEHelper;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class Congratulate extends OneShotBehaviour {
	private String idConversation;
	private String playerName;

	public Congratulate(String uniqueID, String adversaryName) {
		idConversation = uniqueID;
		playerName = adversaryName;
	}
	
	public void action() {
		
		AID playerID = JADEHelper.searchPlayer(myAgent, playerName);
		if (playerID != null) {
			ACLMessage aclMessage = new ACLMessage(ACLMessage.FAILURE);
			aclMessage.setConversationId(idConversation);

			aclMessage.setContent("Eu perdi! :C");
			System.out.println(myAgent.getLocalName()  + ": Eu perdi :c \n"+myAgent.getLocalName()+": Foi um bom jogo, valeu!");
			
			aclMessage.addReceiver(playerID);
			myAgent.send(aclMessage);
		}else {
			System.out.println(myAgent.getLocalName()  +": Não achei o outro cara, algo deu errado no jogo :/");
		}
	}

}