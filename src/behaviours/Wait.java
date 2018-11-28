package behaviours;

import agents.ConversationConstants;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Wait extends CyclicBehaviour {
	Boolean flag = true;

	public void action() {
		if(flag) {
			System.out.println(myAgent.getAID().getLocalName() + ": esperando");
			flag = false;
		}
		MessageTemplate messageTemplate = MessageTemplate.MatchConversationId(ConversationConstants.PLAY);
		ACLMessage aclMessage = myAgent.receive(messageTemplate);
		if(aclMessage != null) {
			System.out.println(myAgent.getAID().getLocalName() + ": minha vez!");
			myAgent.addBehaviour(new Play());
			myAgent.removeBehaviour(this);
		}		
	}
}
