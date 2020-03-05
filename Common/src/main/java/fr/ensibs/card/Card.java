package fr.ensibs.card;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Card extends Remote {

    Integer getValue() throws RemoteException;
    void setValue(Integer value) throws RemoteException;
    Type getType() throws RemoteException;
    Name getName() throws RemoteException;
}
