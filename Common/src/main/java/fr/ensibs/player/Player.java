package fr.ensibs.player;

import fr.ensibs.card.Card;
import fr.ensibs.game.Result;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Player extends Remote {
    /**
     * Give the player name
     * @return the player name
     */
    String getName() throws RemoteException;

    /**
     * Set player name
     * @param name player name
     */
    void setName(String name) throws RemoteException;

    /**
     * Get the player score
     * @return player score
     */
    Integer getScore() throws RemoteException;

    /**
     * Set the player score
     * @param score player score
     */
    void setScore(Integer score) throws RemoteException;

    /**
     * Get the player list of cards
     * @return list of player cards
     */
    List<Card> getCards() throws RemoteException;

    /**
     * Add a card to the player list of cards
     * @param card card to add to the list of player cards
     */
    void addCard(Card card) throws RemoteException;
    /**
     * Give the player action
     *
     * @return the player action
     */
    Action getAction() throws RemoteException;

    void setAction(Action action) throws RemoteException;

    void chooseAction()throws RemoteException;

    void displayCards() throws RemoteException;

    void displayDealerCards(List<Card> cards) throws RemoteException;

    void setResult(Result result) throws RemoteException;
}
