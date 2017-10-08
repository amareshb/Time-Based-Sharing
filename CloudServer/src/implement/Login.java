package implement;

import it.unisa.dia.gas.jpbc.Element;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import nics.crypto.proxy.afgh.AFGHProxyReEncryption;
import data.Databass;
import template.LoginInterface;

public class Login extends UnicastRemoteObject implements LoginInterface {

	public Login() throws RemoteException
	{
		super();
	}
	@Override
	public int loginCheck(String name, String pass) throws RemoteException {
		// TODO Auto-generated method stub
		try {
			Connection con=Databass.getConnect();
			PreparedStatement stm=con.prepareStatement("select * from user where name=? and password=?");
			stm.setString(1, name);
			stm.setString(2, pass);
			ResultSet rs=stm.executeQuery();
			if(rs.next())
			{
				stm=con.prepareStatement("update user set stat=1 where name=?");
				stm.setString(1, name);
				stm.executeUpdate();
				return 1;
			}
			else
				return 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	@Override
	public String returnPublic(String name) {
		// TODO Auto-generated method stub
		Connection con=Databass.getConnect();
		ResultSet rs;
		String puk=null;
		String pub_key;
		ObjectInputStream out;
		try {
			PreparedStatement stm=con.prepareStatement("select publickey from user where name=?");
			stm.setString(1,name);
			rs=stm.executeQuery();
			if(rs.next())
			{
				out=new ObjectInputStream(rs.getBinaryStream(1));
				puk=(String) out.readObject();
				//pub_key=(String) out.readObject();
				//puk=AFGHProxyReEncryption.stringToElement(pub_key,puk);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return puk;
	}
	@Override
	public String returnPrivate(String name) {
		// TODO Auto-generated method stub
		Connection con=Databass.getConnect();
		ResultSet rs;
		String pri=null;
		ObjectInputStream out;
		try {
			PreparedStatement stm=con.prepareStatement("select privatekey from user where name=?");
			stm.setString(1,name);
			rs=stm.executeQuery();
			if(rs.next())
			{
				out=new ObjectInputStream(rs.getBinaryStream(1));
				pri=(String) out.readObject();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pri;
	}

}
