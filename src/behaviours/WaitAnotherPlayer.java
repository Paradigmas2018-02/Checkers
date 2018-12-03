package behaviours;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

import agents.Player;
import checkers.Checkers;
import checkers.CheckersManager;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class WaitAnotherPlayer extends CyclicBehaviour {
	
	private AID specificPlayerName;
	private boolean hasAdversary;
	private Date horario;
	public WaitAnotherPlayer(AID playerName) {
		specificPlayerName = playerName;
		hasAdversary = true;
		horario = new Date();
		System.out.println("Esperando:" + playerName.getLocalName());
	}
	
	public WaitAnotherPlayer() {
		hasAdversary = false;
	}
	
	@Override
	public void action() {
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);

		ACLMessage msg = myAgent.receive(mt);
		if (msg != null) {
			if(hasAdversary) {
				timeOutSpecificPlayer();
				if(msg.getSender().equals(specificPlayerName)) {
					System.out.println(myAgent.getLocalName() + ": Vamos!");
					answerPlayer(msg);
				}
			}else {				
				System.out.println(myAgent.getLocalName() + ": Opa, finalmente alguém querendo jogar :D");
				answerPlayer(msg);
			}
		} else {
			block();
		}
	}

	private void timeOutSpecificPlayer() {
		System.out.println("tic");
		Date horaAtual = new Date();
		if (horaAtual.getTime() - horario.getTime() > 20000) {
			System.out.println(myAgent.getLocalName() + " cansei de esperar " +", vou esperar outra pessoa :c");
			hasAdversary = false;
			specificPlayerName = null;
		}
	}

	private void answerPlayer(ACLMessage msg) {

		Integer roll = parseMessage(msg);

		ACLMessage reply = msg.createReply();
		if (roll != null) {
			System.out.println(myAgent.getLocalName() + "PREPARANDO RESPOSTA");
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
			myAgent.addBehaviour(new WaitTurn(uniqueID, msg.getSender()));
		} else {
			System.out.println(myAgent.getLocalName() + ": Rá ganhei, vou começar jogando");
			myAgent.addBehaviour(new Play(uniqueID, msg.getSender()));
		}
	}

}
