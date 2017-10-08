package template;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.PrivateKey;
import java.security.PublicKey;
import it.unisa.dia.gas.jpbc.*;

public interface LoginInterface extends Remote {
	public int loginCheck(String name,String pass)throws RemoteException;
	public String returnPublic(String name)throws RemoteException;
	public String returnPrivate(String name)throws RemoteException;
}