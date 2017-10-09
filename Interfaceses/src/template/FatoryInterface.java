package template;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FatoryInterface extends Remote {
	public String returnLogin()throws RemoteException;
	public String returnCreate()throws RemoteException;
	public String returnUpload()throws RemoteException;
	public String returnDownload()throws RemoteException;
	public String returnGui()throws RemoteException;
	public String returnLogout()throws RemoteException;
}
