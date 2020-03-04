package fr.ensibs.casino;

import fr.ensibs.card.Card;
import fr.ensibs.card.DefaultCard;
import fr.ensibs.card.Name;
import fr.ensibs.card.Type;
import fr.ensibs.game.Game;
import fr.ensibs.game.State;
import fr.ensibs.player.Action;
import fr.ensibs.player.Player;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import static java.lang.System.exit;

/**
 * Class that take care of starting the game and do the role of the dealer
 */
public class GameImpl implements Game {

    private static String HOST = "localhost";
    private static Integer PORT = 5000;
    private static final String OBJECT = "BLACK_JACK";

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
     * Cards of the dealer
     */
    private static ArrayList<Card> dealerCards;

    public GameImpl() {
        super();
        this.players = new ArrayList<Player>();
        this.cards = new ArrayList<Card>();
        this.dealerCards = new ArrayList<Card>();
        this.state = State.Waiting;
    }

    @Override
    public boolean register(Player player) throws RemoteException {
        System.out.println("New player " + player.getName() + " has joined the table \uD83e\uDD11");
        return players.add(player);
    }

    @Override
    public void startGame() throws RemoteException {

    }

    public static void askPlayersAction() throws RemoteException {
        for(Player p : players) {
            while(p.getAction() == Action.WAIT) {
                try {
                    Thread.sleep(2000);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


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

        for(Player p : players) {
            Card c = cards.remove(random);
            p.addCard(c);
            p.displayCards();
            random = getRandomInt();

            p.chooseAction();
            while(p.getAction() == Action.WAIT) {
                try {
                    Thread.sleep(2000);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
            if(p.getAction() == Action.ADDCARD) {
                c = cards.remove(random);
                p.addCard(c);
                p.displayCards();
                random = getRandomInt();
                p.setAction(Action.WAIT);
            }
        }
    }


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
        System.out.println("java -jar GameApp <host> <port>");
        System.out.println("with :");
        System.out.println("<host> : host of the server");
        System.out.println("<port> : port of the server");
        exit(1);
    }


    private static int getRandomInt() {
        Random rand = new Random();
        return rand.nextInt(cards.size());
    }

    public static void main(String[] args) throws RemoteException {

        if(args.length != 2) {
            usage();
        }
        HOST = args[0];
        PORT = Integer.valueOf(args[1]);

        try {
            Game game = new GameImpl();
            Game stub = (Game) UnicastRemoteObject.exportObject(game, 0);
            Registry registry = LocateRegistry.createRegistry(PORT);
            String url = "rmi://" + HOST + ":" + PORT + "/" + OBJECT;
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
