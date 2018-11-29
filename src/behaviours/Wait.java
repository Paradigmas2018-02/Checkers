package behaviours;

import agents.ConversationConstants;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Wait extends CyclicBehaviour {
	private String playerName;
	private Boolean flag = true;
	private String idConversation;
	
	public Wait(String uniqueID, String adversaryName) {
		idConversation = uniqueID;
		playerName = adversaryName;
	}
	public void action() {
		if(flag) {
			System.out.println(myAgent.getAID().getLocalName() + ": esperando");
			flag = false;
		}
		MessageTemplate messageTemplate = MessageTemplate.MatchConversationId(idConversation);
		ACLMessage aclMessage = myAgent.receive(messageTemplate);
		if(aclMessage != null) {
			System.out.println(myAgent.getAID().getLocalName() + ": minha vez!");
			myAgent.addBehaviour(new Play(idConversation, playerName));
			myAgent.removeBehaviour(this);
		}		
	}
}
