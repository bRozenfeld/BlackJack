package fr.ensibs.casino;

import fr.ensibs.card.Card;
import fr.ensibs.card.DefaultCard;
import fr.ensibs.card.Name;
import fr.ensibs.card.Type;
import fr.ensibs.game.Game;
import fr.ensibs.game.Result;
import fr.ensibs.game.State;
import fr.ensibs.joram.Joram;
import fr.ensibs.joram.JoramAdmin;
import fr.ensibs.player.Action;
import fr.ensibs.player.Player;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import static fr.ensibs.player.Action.IS_CHOOSING;
import static fr.ensibs.player.Action.STOP;
import static java.lang.System.exit;

/**
 * Class that take care of starting the game and do the role of the dealer
 */
public class GameImpl implements Game {

    private static String HOST_RMI = "localhost";
    private static String HOST_JMS = "localhost";
    private static Integer RMI_PORT = 5005;
    private static Integer JMS_PORT = 5000;
    private static final String OBJECT = "BLACK_JACK";
    private static final String TOPIC = "CHAT";

    /**
     * List of the Player register to this game
     */
    private static ArrayList<Player> players;

    /**
     * Cards left of the dealer
     */
    private static ArrayList<Card> cards;

    /**
     * Current state of the game
     */
    private static State state;

    /**
     * Instance that manage the chat
     */
    private static JoramAdmin joramAdmin;

    /**
     * Instance of joram used to jms
     */
    private static Joram joramServer;

    /**
     * Cards of the dealer
     */
    private static ArrayList<Card> dealerCards;

    public GameImpl() {
        super();
        this.players = new ArrayList<Player>();
        this.cards = new ArrayList<Card>();
        this.dealerCards = new ArrayList<Card>();
        this.state = State.Waiting;
        try {
            this.joramServer = new Joram(JMS_PORT);
            this.joramServer.run();
            this.joramAdmin = new JoramAdmin(HOST_JMS, JMS_PORT);
            this.joramAdmin.createTopic(TOPIC);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean register(Player player) throws RemoteException {
        System.out.println("New player " + player.getName() + " has joined the table \uD83e\uDD11");
        return players.add(player);
    }

    @Override
    public void unregister(Player player) throws RemoteException {
        System.out.println("Player " + player.getName() + " has left the table ");
        players.remove(player);
    }


    /**
     * Start a game of black jack
     * @throws RemoteException
     */
    public static void play() throws RemoteException {
        System.out.println("Game started !");

        state = State.Running;
        initCards();
        int random = getRandomInt();

        for(Player p : players) {
            Card c = cards.remove(random);
            p.addCard(c);
            p.displayCards();
            random = getRandomInt();
        }

        Card dealerCard = cards.remove(random);
        dealerCards.add(dealerCard);

        for (Player p : players) {
            // Getting the 2nd card
            Card c = cards.remove(random);
            p.addCard(c);
            p.displayCards();
            random = getRandomInt();

            // while player not satisfy, continue to serve him
            while(p.getAction() != STOP) {
                p.chooseAction();
                while (p.getAction() == IS_CHOOSING) {
                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (p.getAction() == Action.ADDCARD) {
                    c = cards.remove(random);
                    p.addCard(c);
                    p.displayCards();
                    random = getRandomInt();
                    if(p.getScore() >= 21) p.setAction(STOP);
                    else p.setAction(IS_CHOOSING);
                }
            }
            p.setAction(Action.WAIT);
        }

        // after all the players, the dealer get a 2nd card and continue to add if he has less than 17 points
        random = getRandomInt();
        dealerCard = cards.remove(random);
        dealerCards.add(dealerCard);
        int dealerScore = getDealerScore();
        while(dealerScore < 17) {
            random = getRandomInt();
            dealerCard = cards.remove(random);
            dealerCards.add(dealerCard);
            dealerScore = getDealerScore();
        }

        for(Player p: players) {
            p.displayDealerCards(dealerCards);
            int playerScore = p.getScore();

            // Player has score > 21 -> lose
            if(playerScore > 21) {
                p.setResult(Result.Lose);
            } // player under 22 and dealer > 21 -> win
            else if(dealerScore > 21) {
                p.setResult(Result.Win);
            } // player under 22, dealer under 22 and player > dealer -> win
            else if((playerScore > dealerScore)) {
                p.setResult(Result.Win);
            } // player under 22, dealer under 22 and player == dealer -> nul
            else if(playerScore == dealerScore) {
                p.setResult(Result.Draw);
            } // player under 22, dealer under 22, player < dealer -> lose
            else {
                p.setResult(Result.Lose);
            }
        }
    }

    /**
     * Initialise the cards for the game
     * @throws RemoteException
     */
    private static void initCards() throws RemoteException {
        for(int i = 0; i < 4; i++) {
            Type type = null;
            switch(i) {
                case 0:
                    type = Type.Club;
                    break;
                case 1:
                    type = Type.Diamond;
                    break;
                case 2:
                    type = Type.Heart;
                    break;
                case 3:
                    type = Type.Spade;
                    break;
            }
            for(int j = 0; j < 13; j++) {
                Card c = null;
                switch(j) {
                    case 0:
                        c = new DefaultCard(type, Name.Ace);
                        break;
                    case 1:
                        c = new DefaultCard(type, Name.Two);
                        break;
                    case 2:
                        c = new DefaultCard(type, Name.Three);
                        break;
                    case 3:
                        c = new DefaultCard(type, Name.Four);
                        break;
                    case 4:
                        c = new DefaultCard(type, Name.Five);
                        break;
                    case 5:
                        c = new DefaultCard(type, Name.Six);
                        break;
                    case 6:
                        c = new DefaultCard(type, Name.Seven);
                        break;
                    case 7:
                        c = new DefaultCard(type, Name.Eight);
                        break;
                    case 8:
                        c = new DefaultCard(type, Name.Nine);
                        break;
                    case 9:
                        c = new DefaultCard(type, Name.King);
                    case 10:
                        c = new DefaultCard(type, Name.Ten);
                        break;
                    case 11:
                        c = new DefaultCard(type, Name.Jack);
                        break;
                    case 12:
                        c = new DefaultCard(type, Name.Queen);
                        break;
                }
                cards.add(c);
            }
        }
        Collections.shuffle(cards);
    }

    /**
     * Print a message and exit
     */
    private static void usage() {
        System.out.println("Launch the app :");
        System.out.println("java -jar GameApp <host_rmi> <port_rmi> <host_jms> <port_jms>");
        exit(1);
    }

    /**
     * Generate a random int between 0 and the size of the deck cards
     * @return int
     */
    private static int getRandomInt() {
        Random rand = new Random();
        return rand.nextInt(cards.size());
    }

    /**
     * Calculate the score of the dealer
     * @return int containing the score of the dealer
     * @throws RemoteException
     */
    private static int getDealerScore() throws RemoteException {
        int score = 0;
        int numberOfAce = 0;
        for(Card c : dealerCards) {
            if(c.getName() == Name.Ace) {
                numberOfAce++;
            }
            score += c.getValue();
        }

        if(score > 21) {
            while(numberOfAce > 0) {
                score -= 10;
                numberOfAce--;
            }
        }
        return score;
    }


    public static void main(String[] args) throws RemoteException {

        if(args.length != 4) {
            usage();
        }
        HOST_RMI = args[0];
        RMI_PORT = Integer.valueOf(args[1]);
        HOST_JMS = args[2];
        JMS_PORT = Integer.valueOf(args[3]);

        try {
            Game game = new GameImpl();
            Game stub = (Game) UnicastRemoteObject.exportObject(game, 0);
            Registry registry = LocateRegistry.createRegistry(RMI_PORT);
            String url = "rmi://" + HOST_RMI + ":" + RMI_PORT + "/" + OBJECT;
            registry.bind(url, stub);

        } catch(Exception e) {
            e.printStackTrace();
        }

        while(state.equals(State.Waiting)) {
            if(players.size() > 0) {
                play();
            }
            System.out.println("Waiting for players...");
            try {
                Thread.sleep(30 * 1000);
            } catch(Exception e) {
                e.printStackTrace();
            }

        }
    }
}
