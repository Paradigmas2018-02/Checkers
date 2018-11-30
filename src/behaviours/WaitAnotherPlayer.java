package behaviours;

import java.util.Random;
import java.util.UUID;

import agents.Player;
import checkers.Checkers;
import checkers.CheckersManager;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class WaitAnotherPlayer extends CyclicBehaviour {

	@Override
	public void action() {
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);

		ACLMessage msg = myAgent.receive(mt);
		if (msg != null) {
			answerPlayer(msg);
		} else {
			block();
		}
	}

	private void answerPlayer(ACLMessage msg) {
		System.out.println(myAgent.getLocalName() + ": Opa, finalmente alguém querendo jogar :D");

		Integer roll = parseMessage(msg);

		ACLMessage reply = msg.createReply();

		if (roll != null) {
			prepareMyReply(msg, roll, reply);
		} else {
			reply.setPerformative(ACLMessage.REFUSE);

			System.out
					.println(myAgent.getLocalName() + ": NÃO ROLOU NENHUM NÚMERO, quer decidir quem começa como!??!@#");
			reply.setContent("You didn't roll a number to see who starts");
		}

		myAgent.send(reply);
		myAgent.removeBehaviour(this);
	}

	private void prepareMyReply(ACLMessage msg, Integer roll, ACLMessage reply) {
		reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
		Random generator = new Random();
		int myRoll = generator.nextInt();

		System.out.println(myAgent.getLocalName() + ": hmm, " + msg.getSender().getLocalName() + " rolou " + roll);
		System.out.println(myAgent.getLocalName() + ": vou tentar rolar melhor, e ..." + myRoll);

		String uniqueID = UUID.randomUUID().toString();
		createAGame(uniqueID);
		verifyWhoStarts(msg, roll, myRoll, uniqueID);
		reply.setContent(String.valueOf(myRoll) + "," + uniqueID);
	}

	private Integer parseMessage(ACLMessage msg) {
		String text = msg.getContent();
		Integer roll = null;
		try {
			roll = Integer.parseInt(text);
		} catch (Exception e) {
		}
		return roll;
	}

	private void createAGame(String uniqueID) {
		Checkers c = CheckersManager.createChecker(uniqueID);
		Player p = (Player) myAgent;
		p.startGame(c, true);
	}

	private void verifyWhoStarts(ACLMessage msg, Integer roll, int myRoll, String uniqueID) {
		if (myRoll < roll) {
			System.out.println(myAgent.getLocalName() + ": Droga, perdi.");
			myAgent.addBehaviour(new WaitTurn(uniqueID, msg.getSender().getLocalName()));
		} else {
			System.out.println(myAgent.getLocalName() + ": Rá ganhei, vou começar jogando");
			myAgent.addBehaviour(new Play(uniqueID, msg.getSender().getLocalName()));
		}
	}

}
