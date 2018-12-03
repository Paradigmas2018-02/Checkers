package behaviours;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import agents.ConversationConstants;
import agents.JADEHelper;
import agents.Player;
import checkers.Checkers;
import checkers.CheckersManager;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class FindOtherPlayer extends Behaviour {
	private boolean isEsperandoResposta = false;
	private boolean jogoDecidido = false;
	private Integer myRoll;
	private ArrayList<AID> players;
	private Date horario;

	public FindOtherPlayer(Agent agent) {
		setAgent(agent);
		System.out.println(myAgent.getLocalName() + " está procurando outro player :)");
		players = JADEHelper.searchPlayers(myAgent);
	}

	public FindOtherPlayer(Agent agent, AID target) {

		setAgent(agent);
		System.out.println(myAgent.getLocalName() + " vai recomeçar com o " + target.getLocalName());
		players = new ArrayList<AID>();
		players.add(target);
	}

	@Override
	public void action() {
		if (!isEsperandoResposta) {
			tryToCallAPlayer();
		} else {
			waitAnswer();
		}
	}

	private void waitAnswer() {
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);

		ACLMessage msg = myAgent.receive(mt);
		if (msg != null) {
			jogoDecidido = true;
			Integer roll = Integer.parseInt(parseMessage(msg)[0]);
			String uniqueID = parseMessage(msg)[1];
			startGame(uniqueID);
			verifyWhoStarts(msg, roll, uniqueID);
		}
		checkTimeout();
	}

	private void checkTimeout() {
		MessageTemplate mt;
		ACLMessage msg;
		mt = MessageTemplate.MatchPerformative(ACLMessage.REFUSE);

		msg = myAgent.receive(mt);
		if (msg != null) {
			System.out.println(myAgent.getLocalName() + ": UÉ.");
		}
		Date horaAtual = new Date();
		if (horaAtual.getTime() - horario.getTime() > 5000) {
			System.out.println(myAgent.getLocalName() + " cansei de esperar " + players.get(0).getLocalName()
					+ ", vou tentar outra pessoa");
			players.remove(0);
			isEsperandoResposta = false;
		}
	}

	private void verifyWhoStarts(ACLMessage msg, Integer roll, String uniqueID) {
		if (roll > myRoll) {
			System.out.println(myAgent.getLocalName() + ": affs, perdi. " + msg.getSender().getLocalName()
					+ ", pode começar que to esperando.");
			myAgent.addBehaviour(new WaitTurn(uniqueID, msg.getSender()));
		} else {
			System.out.println(myAgent.getLocalName() + ": Rá ganhei, vou começar jogando");
			myAgent.addBehaviour(new Play(uniqueID, msg.getSender()));
		}
	}

	private void startGame(String uniqueID) {
		Checkers c = CheckersManager.getChecker(uniqueID);
		Player p = (Player) myAgent;
		p.startGame(c, false);
	}

	private String[] parseMessage(ACLMessage msg) {
		return msg.getContent().split(",");
	}

	private void tryToCallAPlayer() {
		if (!players.isEmpty()) {
			AID player = players.get(0);

			ACLMessage aclMessage = new ACLMessage(ACLMessage.CFP);
			aclMessage.setConversationId(ConversationConstants.PLAYER_TO_PLAYER);

			Random generator = new Random();
			myRoll = generator.nextInt();

			System.out.println(myAgent.getLocalName() + ": achei " + player.getLocalName()
					+ ", vou rolar um número e ver se ele joga cmg!");
			System.out.println(myAgent.getLocalName() + ": rolei " + myRoll);

			aclMessage.setContent(myRoll.toString());
			aclMessage.addReceiver(player);

			myAgent.send(aclMessage);
			isEsperandoResposta = true;
			horario = new Date();
			
		} else {
			System.out.println(myAgent.getLocalName() + ": Não encontrei ninguém pra jogar, vou esperar.");
			myAgent.addBehaviour(new WaitAnotherPlayer());
			myAgent.removeBehaviour(this);

		}
	}

	@Override
	public boolean done() {
		return jogoDecidido;
	}
}
