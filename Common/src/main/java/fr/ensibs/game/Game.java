package fr.ensibs.game;

import fr.ensibs.player.Player;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * A game that register player and start playing
 */
public interface Game extends Remote {

    /**
     * Register a new player to the game
     * @param player
     * @return
     * @throws RemoteException
     */
    public boolean register(Player player) throws RemoteException;

    /**
     * Unregister a player
     * @param player
     * @throws RemoteException
     */
    public void unregister(Player player) throws RemoteException;
}
