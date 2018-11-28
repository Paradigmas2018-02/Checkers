package behaviours;

import java.util.Random;

import agents.ConversationConstants;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class WaitFirstPlayer extends CyclicBehaviour {
	
	@Override
	public void action() {
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
		
		ACLMessage msg = myAgent.receive(mt);
		if (msg != null) {

			System.out.println(myAgent.getName()+ " RECEBI MSG PRA JOGAR");
			String text = msg.getContent();
			Integer roll = null;
			try {
				roll = Integer.parseInt(text);
			}catch(Exception e) {
			}

			ACLMessage reply = msg.createReply();
			if (roll != null) {
				reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);

				Random generator = new Random();
				int myRoll = generator.nextInt();
				

				System.out.println(myAgent.getName()+" O CARA ROLOU " + roll);
				System.out.println(myAgent.getName()+" ROLEI " + myRoll);
				
				if(myRoll < roll) {
					myAgent.addBehaviour(new Wait());
				}else {
					myAgent.addBehaviour(new Play());
				}
				
				reply.setContent(String.valueOf(myRoll));
			} else {
				reply.setPerformative(ACLMessage.REFUSE);

				System.out.println(myAgent.getName()+" O CARA NÃO ROLOU");
				reply.setContent("You didn't roll a number to see who starts");
			}
			
			myAgent.send(reply);
		} else {
			block();
		}
	}

}
