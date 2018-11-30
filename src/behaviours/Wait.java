package behaviours;

import agents.Player;
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
		MessageTemplate isMyGame = MessageTemplate.MatchConversationId(idConversation);
		MessageTemplate isMyTurn = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
		MessageTemplate stillMyGame = MessageTemplate.MatchConversationId(idConversation);
		MessageTemplate messageIWon = MessageTemplate.MatchPerformative(ACLMessage.FAILURE);
		
		ACLMessage iWon = myAgent.receive(MessageTemplate.and(stillMyGame, messageIWon));
		ACLMessage myTurn = myAgent.receive(MessageTemplate.and(isMyGame, isMyTurn));
		
		if(myTurn != null) {	
			System.out.println(myAgent.getAID().getLocalName() + ": minha vez!");
			myAgent.addBehaviour(new Play(idConversation, playerName));
			myAgent.removeBehaviour(this);
			
		}
		
		if(iWon != null) {
			System.out.println(myAgent.getLocalName()+": EITCHA, Ganhei!");
			Player p = (Player) myAgent;
			myAgent.removeBehaviour(this);
		}
	
	}}
