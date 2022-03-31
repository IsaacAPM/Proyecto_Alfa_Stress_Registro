package mx.itam.Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface Registro extends Remote{
    public String registro(String id) throws RemoteException;
}
