package fr.ensibs.player;

import fr.ensibs.card.Card;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

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

    }
    /*******************Override methods ********************/

    /**
     * Give the player name
     *
     * @return the player name
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Set player name
     *
     * @param name player name
     */
    @Override
    public void setName(String name) {
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
    public List<Card> getCards() {
        return this.cards;
    }

    /**
     * Add a card to the player list of cards
     *
     * @param card card to add to the list of player cards
     */
    @Override
    public void addCard(Card card) {
        this.cards.add(card);
    }
}
