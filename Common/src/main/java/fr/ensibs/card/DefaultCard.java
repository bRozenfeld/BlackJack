package fr.ensibs.card;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DefaultCard extends UnicastRemoteObject implements Card {

    /**
     * Name of the card (2, 3, King ...)
     */
    private Name name;

    /**
     * Type of the card
     */
    private Type type;

    /**
     * Value of the card
     */
    private Integer value;

    public DefaultCard(Type type, Name name) throws RemoteException {
        this.type = type;
        this.name = name;
        switch(name) {
            case Ace:
                this.value  = 11;
                break;
            case Two:
                this.value = 2;
                break;
            case Three:
                this.value = 3;
                break;
            case Four:
                this.value = 4;
                break;
            case Five:
                this.value = 5;
                break;
            case Six:
                this.value = 6;
                break;
            case Seven:
                this.value = 7;
                break;
            case Eight:
                this.value = 8;
                break;
            case Nine:
                this.value = 9;
                break;
            case Ten:
                this.value = 10;
                break;
            case Jack:
                this.value = 10;
                break;
            case Queen:
                this.value = 10;
                break;
            case King:
                this.value = 10;
                break;
        }
    }

    @Override
    public Name getName() throws RemoteException {
        return name;
    }

    @Override
    public Integer getValue() throws RemoteException {
        return value;
    }

    @Override
    public Type getType() throws RemoteException {
        return type;
    }
}
