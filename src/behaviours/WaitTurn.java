package behaviours;

import java.util.Random;

import agents.Player;
import checkers.CheckersManager;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class WaitTurn extends CyclicBehaviour {
	private Boolean flag = true;
	private AID adversary;
	private String idConversation;

	public WaitTurn(String uniqueID, AID adversary) {
		idConversation = uniqueID;
		this.adversary = adversary;
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

		MessageTemplate playAgain = MessageTemplate.MatchPerformative(ACLMessage.CFP);

		ACLMessage iWon = myAgent.receive(MessageTemplate.and(stillMyGame, messageIWon));
		ACLMessage myTurn = myAgent.receive(MessageTemplate.and(isMyGame, isMyTurn));
		ACLMessage startNewGame = myAgent.receive(MessageTemplate.and(isMyGame, playAgain));

		if (myTurn != null) {
			myAgent.addBehaviour(new Play(idConversation, adversary));
			myAgent.removeBehaviour(this);

		} else if (iWon != null) {
			System.out.println(myAgent.getLocalName() + ": EITCHA, Ganhei!");
			Player p = (Player) myAgent;
			if(wantsANewGame()) {
				System.out.println(myAgent.getLocalName()+": Quero jogar de novo, vou procurar alguém.");
				myAgent.addBehaviour(new FindOtherPlayer(myAgent));
			}else {
				System.out.println(myAgent.getLocalName()+": Cansei, vou embora.");
				((Player)myAgent).desistir();
			}
			
			CheckersManager.removeChecker(idConversation);
			
			myAgent.removeBehaviour(this);
		} else if (startNewGame != null) {
			if (wantsANewGame()) {
				System.out.println(myAgent.getLocalName() + ": Vamos!");
				myAgent.addBehaviour(new FindOtherPlayer(myAgent, adversary));
				
				myAgent.removeBehaviour(this);
			} else {
				System.out.println(myAgent.getLocalName() + ": Cansei, vou embora");
				((Player)myAgent).desistir();

			}
		}
	}

	public boolean wantsANewGame() {
		Random generator = new Random();
		if (generator.nextInt(11) >= 5)
			return true;
		else
			return false;
	}
}
