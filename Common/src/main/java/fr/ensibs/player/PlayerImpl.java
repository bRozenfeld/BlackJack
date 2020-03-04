package fr.ensibs.player;

import fr.ensibs.card.Card;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlayerImpl extends UnicastRemoteObject implements Player {
    /**
     * player name
     */
    private String name;
    /**
     * Player score
     */
    private Integer score;
    /**
     * Player list of cards
     */
    private List<Card> cards;
    /**
     * Action of the player
     */
    private Action action;

    /*******************Constructor********************/

    /**
     *Constructor:initialize the name,score and list of cards of the player
     *
     * @throws RemoteException if failed to export object
     *
     */
    public PlayerImpl(String name) throws RemoteException {
        this.name=name;
        this.score=0;
        this.cards=new ArrayList<Card>();
        this.action=Action.WAIT;

    }
    /*******************Override methods ********************/

    /**
     * Give the player name
     *
     * @return the player name
     */
    @Override
    public String getName() throws RemoteException {
        return this.name;
    }

    /**
     * Set player name
     *
     * @param name player name
     */
    @Override
    public void setName(String name) throws RemoteException {
        this.name=name;
    }

    /**
     * Get the player score
     *
     * @return player score
     */
    @Override
    public Integer getScore() {
        return this.score;
    }

    /**
     * Set the player score
     *
     * @param score player score
     */
    @Override
    public void setScore(Integer score) {
        this.score=score;
    }

    /**
     * Get the player list of cards
     *
     * @return list of player cards
     */
    @Override
    public List<Card> getCards() throws RemoteException {
        return this.cards;
    }

    /**
     * Add a card to the player list of cards
     *
     * @param card card to add to the list of player cards
     */
    @Override
    public void addCard(Card card) throws RemoteException {
        this.cards.add(card);
    }

    /**
     * Give the player action
     *
     * @return the player action
     */
    @Override
    public Action getAction() throws RemoteException {
        return this.action;
    }

    @Override
    public void setAction(Action action) throws RemoteException{
        this.action=action;
    }

    /*******************Private methods*********************/
    public void chooseAction()throws RemoteException
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
        Action action = this.getAction();
        if (action == null) {
            System.out.println("player " + this.getName() + " not found");
        } else {
            System.out.println("player " + this.getName() + ": " + action);
        }
    }

    /**
     * Stop the device
     */
    public void stop() throws RemoteException
    {
        this.setAction(Action.WAIT);
        try { Thread.sleep(2000); } catch (Exception e) { }

    }

    /**
     *
     */
    public void addCard() throws RemoteException
    {
        this.setAction(Action.ADDCARD);
        try { Thread.sleep(2000); } catch (Exception e) { }
    }
    /**
     *
     */
    public void waiT() throws RemoteException
    {
        this.setAction(Action.WAIT);
        try { Thread.sleep(2000); } catch (Exception e) { }
    }

}
