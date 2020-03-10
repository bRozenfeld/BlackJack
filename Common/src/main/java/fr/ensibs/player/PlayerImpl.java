package fr.ensibs.player;

import fr.ensibs.card.Card;
import fr.ensibs.card.Name;
import fr.ensibs.game.Result;

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
        this.score = 0;
        int numberOfAce = 0;
        try {
            for (Card c : cards) {
                if(c.getName() == Name.Ace) numberOfAce++;
                this.score += c.getValue();
            }
        } catch(RemoteException e) {
            e.printStackTrace();
        }

        if(this.score > 21) {
            while(numberOfAce > 0) {
                this.score -= 10;
                numberOfAce--;
            }
        }
        return this.score;
    }

    @Override
    public void setResult(Result result) throws RemoteException {
        switch(result) {
            case Win:
                System.out.println("You win !");
                break;
            case Lose:
                System.out.println("You lose !");
                break;
            case Draw:
                System.out.println("Draw game !");
                break;
        }
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

    @Override
    public void chooseAction()throws RemoteException
    {
        setAction(Action.IS_CHOOSING);
        System.out.println(getName()+" turn: CONTINUE to get an other card or STOP to stop ?");
    }

    @Override
    public void displayCards() throws RemoteException {
        String s = "YOUR CARDS :\n";
        for(Card c : cards) {
            s += "+-------+\t";
        }
        s += "\n";
        for(Card c : cards) {
            switch(c.getName()) {
                case Ace:
                    s += "| 1     |";
                    break;
                case Two:
                    s += "| 2     |";
                    break;
                case Three:
                    s += "| 3     |";
                    break;
                case Four:
                    s += "| 4     |";
                    break;
                case Five:
                    s += "| 5     |";
                    break;
                case Six:
                    s += "| 6     |";
                    break;
                case Seven:
                    s += "| 7     |";
                    break;
                case Eight:
                    s += "| 8     |";
                    break;
                case Nine:
                    s += "| 9     |";
                    break;
                case Ten:
                    s += "| 10    |";
                    break;
                case Jack:
                    s += "| J     |";
                    break;
                case Queen:
                    s += "| Q     |";
                    break;
                case King:
                    s += "| K     |";
                    break;
            }
            s += "\t";
        }
        s += "\n";
        for(Card c : cards) {
            s += "|       |\t";
        }
        s += "\n";
        for(Card c : cards) {
            switch (c.getType()) {
                case Diamond:
                    s += "|   \u2666   |\t";
                    break;
                case Spade:
                    s += "|   \u2660   |\t";
                    break;
                case Heart:
                    s += "|   \u2665   |\t";
                    break;
                case Club:
                    s += "|   \u2663   |\t";
                    break;
            }
        }
        s += "\n";
        for(Card c : cards) {
            s += "|       |\t";
        }
        s += "\n";
        for(Card c : cards) {
            s += "|       |\t";
        }
        s += "\n";
        for(Card c : cards) {
            s += "+-------+\t";
        }
        s += "\n";

        System.out.println(s);
    }


    @Override
    public void displayDealerCards(List<Card> dCards) throws RemoteException {
        String s = "DEALERS CARDS :\n";
        for(Card c : dCards) {
            s += "+-------+\t";
        }
        s += "\n";
        for(Card c : dCards) {
            switch(c.getName()) {
                case Ace:
                    s += "| 1     |";
                    break;
                case Two:
                    s += "| 2     |";
                    break;
                case Three:
                    s += "| 3     |";
                    break;
                case Four:
                    s += "| 4     |";
                    break;
                case Five:
                    s += "| 5     |";
                    break;
                case Six:
                    s += "| 6     |";
                    break;
                case Seven:
                    s += "| 7     |";
                    break;
                case Eight:
                    s += "| 8     |";
                    break;
                case Nine:
                    s += "| 9     |";
                    break;
                case Ten:
                    s += "| 10    |";
                    break;
                case Jack:
                    s += "| J     |";
                    break;
                case Queen:
                    s += "| Q     |";
                    break;
                case King:
                    s += "| K     |";
                    break;
            }
            s += "\t";
        }
        s += "\n";
        for(Card c : dCards) {
            s += "|       |\t";
        }
        s += "\n";
        for(Card c : dCards) {
            switch (c.getType()) {
                case Diamond:
                    s += "|   \u2666   |\t";
                    break;
                case Spade:
                    s += "|   \u2660   |\t";
                    break;
                case Heart:
                    s += "|   \u2665   |\t";
                    break;
                case Club:
                    s += "|   \u2663   |\t";
                    break;
            }
        }
        s += "\n";
        for(Card c : dCards) {
            s += "|       |\t";
        }
        s += "\n";
        for(Card c : dCards) {
            s += "|       |\t";
        }
        s += "\n";
        for(Card c : dCards) {
            s += "+-------+\t";
        }
        s += "\n";

        System.out.println(s);
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
        this.setAction(Action.STOP);


    }

    /**
     *
     */
    public void addCard() throws RemoteException
    {
        this.setAction(Action.ADDCARD);

    }
    /**
     *
     */
    public void waiT() throws RemoteException
    {
        this.setAction(Action.WAIT);

    }

}
