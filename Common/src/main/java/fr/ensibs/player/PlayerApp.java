package fr.ensibs.player;

import fr.ensibs.game.Game;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
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
                Game game=(Game) r;
                PlayerApp player=new PlayerApp(args[2],game);
                player.run();

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

    public void run()throws RemoteException
    {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        if (line.startsWith("action") || line.startsWith("ACTION")) {
            displayAction();
        }else {
            switch (line) {
                case "wait":
                case "WAIT":
                    waiT();
                    break;
                case "addcard":
                case "ADDCARD":
                    addCard();
                    break;
                case "stop":
                case "STOP":
                    stop();
                    break;
                case "quit":
                case "QUIT":
                    System.exit(0);
            }
        }
    }

    /**
     * Display the status of the device having the given id, if it exists
     *
     */
    public void displayAction() throws RemoteException
    {
        Action action = this.player.getAction();
        if (action == null) {
            System.out.println("player " + this.player.getName() + " not found");
        } else {
            System.out.println("player " + this.player.getName() + ": " + action);
        }
    }

    /**
     * Stop the device
     */
    public void stop() throws RemoteException
    {
        this.player.setAction(Action.WAIT);
        try { Thread.sleep(2000); } catch (Exception e) { }

    }

    /**
     *
     */
    public void addCard() throws RemoteException
    {
        this.player.setAction(Action.ADDCARD);
        try { Thread.sleep(2000); } catch (Exception e) { }
    }
    /**
     *
     */
    public void waiT() throws RemoteException
    {
        this.player.setAction(Action.WAIT);
        try { Thread.sleep(2000); } catch (Exception e) { }
    }




}