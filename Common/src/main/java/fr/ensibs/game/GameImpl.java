package fr.ensibs.game;

import fr.ensibs.player.Player;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Locale;

public class GameImpl implements Game {

    private static String HOST = "localhost";
    private static Integer PORT = 5000;
    private static final String OBJECT = "BLACK_JACK";

    /**
     * List of the Player register to this game
     */
    private static ArrayList<Player> players;

    /**
     * Current state of the game
     */
    private static State state;

    public GameImpl() {
        super();
        this.players = new ArrayList<Player>();
        this.state = State.Waiting;
    }

    @Override
    public boolean register(Player player) throws RemoteException {
        return false;
    }

    @Override
    public void startGame() throws RemoteException {

    }


    public static void play() {

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
    }

    public static void main(String[] args) {

        if(args.length != 2) {
            usage();
        }
        HOST = args[0];
        PORT = Integer.valueOf(args[1]);

        try {
            Game game = new GameImpl();
            Game stub = (Game) UnicastRemoteObject.exportObject(game);
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
        }
    }

}
