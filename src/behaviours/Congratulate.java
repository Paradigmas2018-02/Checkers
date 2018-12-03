package behaviours;

import java.util.Random;

import agents.JADEHelper;
import agents.Player;
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
			Random generator = new Random();
			if(generator.nextInt(11)>=5) {
				offerNewGame(playerID);
			}else {
				sayGoodBye(playerID);				
			}
		}else {
			System.out.println(myAgent.getLocalName()  +": Não achei o outro cara, algo deu errado no jogo :/");
		}
	}

	private void sayGoodBye(AID playerID) {

		ACLMessage aclMessage = new ACLMessage(ACLMessage.FAILURE);
		aclMessage.setConversationId(idConversation);
		
		aclMessage.setContent("Eu perdi! :C");
		System.out.println(myAgent.getLocalName()  + ": Eu perdi :c \n"+myAgent.getLocalName()+": Foi um bom jogo, valeu!");
		
		aclMessage.addReceiver(playerID);
		myAgent.send(aclMessage);
		((Player)myAgent).desistir();
	}

	private void offerNewGame(AID playerID) {
		ACLMessage aclMessage = new ACLMessage(ACLMessage.CFP);
		aclMessage.setConversationId(idConversation);
		
		System.out.println(myAgent.getLocalName()  + ": Eu perdi :c \n");
		System.out.println(myAgent.getLocalName()  + ": Quer jogar de novo?");

		Random generator = new Random();
		Integer myRoll = generator.nextInt();
		
		aclMessage.setContent(myRoll.toString());

		aclMessage.addReceiver(playerID);
		myAgent.send(aclMessage);
		
		myAgent.addBehaviour(new WaitAnotherPlayer(playerID));
	}

}