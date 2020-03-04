package fr.ensibs.player;

import fr.ensibs.card.Card;

import java.rmi.Remote;
import java.util.List;

public interface Player extends Remote {
    /**
     * Give the player name
     * @return the player name
     */
    String getName();

    /**
     * Set player name
     * @param name player name
     */
    void setName(String name);

    /**
     * Get the player score
     * @return player score
     */
    Integer getScore();

    /**
     * Set the player score
     * @param score player score
     */
    void setScore(Integer score);

    /**
     * Get the player list of cards
     * @return list of player cards
     */
    List<Card> getCards();

    /**
     * Add a card to the player list of cards
     * @param card card to add to the list of player cards
     */
    void addCard(Card card);

}
