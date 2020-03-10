package fr.ensibs.player;

import fr.ensibs.game.Game;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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
    /**
     * the JNDI context
     */
    private static Context context;
    /**
     * the JMS context
     */
    private static JMSContext ctx;
    /**
     * the topic destination
     */
    private static Destination destination;
    /**
     *the destination of the topic
     */
    private static  String DESTINATION="CHAT";

    private static String HOST = "localhost";
    private static String JMSPORT = "5000";
    private static Integer RMIPORT = 5005;


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

    public static void main(String[] args)  {
        if (args.length != 3 || args[0].equals("-h")) {
            usage();
        }else {
            try {
                System.setProperty("java.naming.factory.initial", "fr.dyade.aaa.jndi2.client.NamingContextFactory");
                System.setProperty("java.naming.factory.host", HOST);
                System.setProperty("java.naming.factory.port", JMSPORT);
                HOST = args[0];
                RMIPORT = Integer.valueOf(args[1]);

                Registry registry = LocateRegistry.getRegistry(RMIPORT);
                String url = "rmi://" + HOST + ":" + RMIPORT + "/" + obj_name;
                Game game=(Game) registry.lookup(url);

                context=new InitialContext();
                ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
                destination =(Destination) context.lookup(DESTINATION);
                ctx=factory.createContext();
                PlayerApp playerapp=new PlayerApp(args[2],game);
                playerapp.run();


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
        JMSConsumer consumer = ctx.createConsumer(destination);
        consumer.setMessageListener(new TextListener(this.player));

    }

    /**
     * Launch the application process that executes user commands: SHARE, FILTER
     */
    public void run() throws NamingException, JMSException,RemoteException {
        System.out.println("Hello, " + this.player.getName() + ". Enter commands:"
                + "\n QUIT                     to quit the application"
                + "\n CHAT <messgae>   to chat with the other players");

        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] command = line.split(" +");
        while (command[0].equals("chat") || command[0].equals("CHAT")) {
            String message= command[1];
            sendMessage(message);
            line = scanner.nextLine();
            command = line.split(" +");
        }
        //System.err.println("Unknown command: \"" + command[0] + "\"");
    }

    /**
     * Chat with the other players
     */
    public void sendMessage(String msg){

        TextMessage message=ctx.createTextMessage(msg);
        JMSProducer producer= ctx.createProducer();
        producer.send(destination,message);
        System.out.println("message bien envoye");
    }








    }
