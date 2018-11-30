package behaviours;

import java.util.Random;
import java.util.UUID;

import agents.Player;
import checkers.Checkers;
import checkers.CheckersManager;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class WaitFirstPlayer extends CyclicBehaviour {
	
	@Override
	public void action() {
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
		
		ACLMessage msg = myAgent.receive(mt);
		if (msg != null) {

			System.out.println(myAgent.getLocalName()+ ": Opa, finalmente alguém querendo jogar :D");
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
				
				
				System.out.println(myAgent.getLocalName()+": hmm, a pessoa rolou " + roll);
				System.out.println(myAgent.getLocalName()+": vou tentar rolar melhor, e ..." + myRoll);
				
				String uniqueID = UUID.randomUUID().toString();
				
				Checkers c = CheckersManager.createChecker(uniqueID);				
				Player p = (Player) myAgent;
				p.startGame(c, true);
				
				if(myRoll < roll) {
					System.out.println(myAgent.getLocalName() + ": Droga, perdi.");
					myAgent.addBehaviour(new Wait(uniqueID, msg.getSender().getLocalName()));
				}else {
					System.out.println(myAgent.getLocalName() + ": Rá ganhei, vou começar jogando");
					myAgent.addBehaviour(new Play(uniqueID, msg.getSender().getLocalName()));
				}

				reply.setContent(String.valueOf(myRoll)+","+uniqueID);
				
			} else {
				reply.setPerformative(ACLMessage.REFUSE);

				System.out.println(myAgent.getLocalName()+": NÃO ROLOU NENHUM NÚMERO, quer decidir quem começa como!??!@#");
				reply.setContent("You didn't roll a number to see who starts");
			}
			
			myAgent.send(reply);
			System.out.println(myAgent.getLocalName()+": Enviei a resposta");
			myAgent.removeBehaviour(this);
		} else {
			block();
		}
	}

}
