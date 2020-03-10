package fr.ensibs.player;

import fr.ensibs.game.Game;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.Scanner;

public class PlayerApp {
    /**
     * Player Who will play BlackJack
     */
    private static Player player;

    /**
     * the server of the game
     */
    private Game game;


    private static ConnectionFactory factory;

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
        }

        HOST = args[0];
        RMIPORT = Integer.valueOf(args[1]);

        System.setProperty("java.naming.factory.initial", "fr.dyade.aaa.jndi2.client.NamingContextFactory");
        System.setProperty("java.naming.factory.host", HOST);
        System.setProperty("java.naming.factory.port", JMSPORT);

        try {

            Registry registry = LocateRegistry.getRegistry(RMIPORT);
            String url = "rmi://" + HOST + ":" + RMIPORT + "/" + obj_name;
            Game game=(Game) registry.lookup(url);

            Context context = new InitialContext();
            factory = (ConnectionFactory) context.lookup("ConnectionFactory");
            destination =(Destination) context.lookup(DESTINATION);
            ctx = factory.createContext();

            player = new PlayerImpl(args[2]);

            JMSConsumer consumer = ctx.createConsumer(destination);
            consumer.setMessageListener(new TextListener(player));

            PlayerApp playerapp=new PlayerApp(args[2],game);
            playerapp.run();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructor to create a player and register it to the game
     * @param playername the name of the player
     * @param game the server of the game
     * @throws RemoteException
     */
    public PlayerApp(String playername,Game game) throws RemoteException {

       //player=new PlayerImpl(playername);
       this.game=game;
       this.game.register(player);

    }

    /**
     * Launch the application process that executes user commands: SHARE, FILTER
     */
    public void run() throws NamingException, JMSException,RemoteException {
        System.out.println("Hello, " + this.player.getName() + ". Enter commands:"
                + "\n CHAT <message>   to chat with the other players");

        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] command = line.split(" +");
        while(true) {
            if (command[0].equals("chat") || command[0].equals("CHAT")) {
                command[0] = "";
                String message = String.join(" ", command);
                sendMessage(message);
            }
            else if (player.getAction()==Action.IS_CHOOSING){
                   if(command[0].equals("continue")||command[0].equals("CONTINUE"))
                       player.setAction(Action.ADDCARD);
                   else if(command[0].equals("stop")||command[0].equals("STOP"))
                       player.setAction(Action.STOP);
                   else
                       System.out.println("Wrong command \n"+player.getName()+" turn: CONTINUE to get an other card or STOP to stop ?");
            }
            line = scanner.nextLine();
            command = line.split(" +");
        }
        //System.err.println("Unknown command: \"" + command[0] + "\"");
    }

    /**
     * Chat with the other players
     */
    public void sendMessage(String msg){
        try(JMSContext jmsContext = ctx.createContext(JMSContext.AUTO_ACKNOWLEDGE);) {
            JMSProducer producer= jmsContext.createProducer();
            String message = player.getName() + " : " + msg;
            producer.send(destination, message);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }








    }
