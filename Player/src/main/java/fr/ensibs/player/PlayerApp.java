package fr.ensibs.player;

import fr.ensibs.game.Game;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class PlayerApp {
    /**
     * Player Who will play BlackJack
     */
    private Player player;

    /**
     * the server of the game
     */
    private Game game;

    private static String HOST = "localhost";
    private static Integer PORT = 5000;


    private static final String obj_name = "BLACK_JACK";

    /**
     * Print a usage message and exit. Used when the standalone
     * application is started with wrong command line arguments
     */
    private static void usage()
    {
        System.out.println("Usage: java PlayerApp <Host> <Port> <player_name>");
        System.out.println("An application client who will connect to the casino server to play BlackJack");
        System.exit(-1);
    }

    public static void main(String[] args) {
        if (args.length != 3 || args[0].equals("-h")) {
            usage();
        }else {
            try {
                HOST = args[0];
                PORT = Integer.valueOf(args[1]);

                Registry registry = LocateRegistry.getRegistry(PORT);
                String url = "rmi://" + HOST + ":" + PORT + "/" + obj_name;
                Game game=(Game) registry.lookup(url);
                PlayerApp player=new PlayerApp(args[2],game);
                //player.run();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Constructor to create a player and register it to the game
     * @param playername the name of the player
     * @param game the server of the game
     * @throws RemoteException
     */
    public PlayerApp(String playername,Game game) throws RemoteException {
       player=new PlayerImpl(playername);
       this.game=game;
       this.game.register(player);
    }





}
