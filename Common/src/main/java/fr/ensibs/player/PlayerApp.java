package fr.ensibs.player;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;

public class PlayerApp {
    /**
     * Player Who will play BlackJack
     */
    private Player player;


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
                String url = obj_name;
                Remote r = Naming.lookup(url);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public PlayerApp(String playername) throws RemoteException {
       player=new PlayerImpl(playername);
    }

}
