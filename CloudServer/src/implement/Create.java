package implement;

import it.unisa.dia.gas.jpbc.Element;

import java.beans.Transient;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;

import data.Databass;
import template.CreateInterface;
import nics.crypto.proxy.afgh.*;
import nics.crypto.*;


public class Create extends UnicastRemoteObject implements CreateInterface {
	
public Create() throws RemoteException
{
	super();
}

@Override
public int createUser(String name, String pass) throws RemoteException {
	// TODO Auto-generated method stub
	DbxClient cli;
	byte[] pbkeyss=null;
	byte[] prkeyss=null;
	ObjectOutputStream out1,out2,out3;
	ByteArrayOutputStream bout1,bout2,bout3;
	ByteArrayInputStream bin1,bin2,bin3;
	String priv_key,publ_key;
	
	int rBits = 256; //160;    // 20 bytes
    int qBits = 1536; //512;    // 64 bytes

    AFGHGlobalParameters global = new AFGHGlobalParameters(rBits, qBits);
	
	try {
		ArrayList<ShareList> list=new ArrayList();
		
		/*String xform = "RSA";  
		  KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA"); 
		  kpg.initialize(2048); 
		    KeyPair kp = kpg.genKeyPair();
		    PublicKey pubk = kp.getPublic();
		    PrivateKey prvk = kp.getPrivate();
		  */
		
		
		
		Element sk = AFGHProxyReEncryption.generateSecretKey(global);
		Element pk = AFGHProxyReEncryption.generatePublicKey(sk, global);
		priv_key=AFGHProxyReEncryption.elementToString(sk);
		publ_key=AFGHProxyReEncryption.elementToString(pk);
		
		//System.out.println(seck+":secret key");
		// Boolean boolean1=sk.isImmutable();
		  
		    //saving as a blob object into database
		   bout1=new ByteArrayOutputStream();
		   bout2=new ByteArrayOutputStream();
		   bout3=new ByteArrayOutputStream();
		   out1=new ObjectOutputStream(bout1);
		   out2=new ObjectOutputStream(bout2);
		   out3=new ObjectOutputStream(bout3);
		   out1.writeObject(publ_key);
		   out2.writeObject(priv_key);
		   out3.writeObject(list);
		   bin1=new ByteArrayInputStream(bout1.toByteArray());
		   bin2=new ByteArrayInputStream(bout2.toByteArray());
		   bin3=new ByteArrayInputStream(bout3.toByteArray());
		
		Connection con=Databass.getConnect();
		PreparedStatement pst=con.prepareStatement("insert into user value(?,?,?,?,?,0)");
		pst.setString(1,name);
		pst.setString(2,pass);
		pst.setBinaryStream(3,bin1,bout1.toByteArray().length);
		pst.setBinaryStream(4,bin2,bout2.toByteArray().length);
		pst.setBinaryStream(5,bin3,bout3.toByteArray().length);
		if(pst.executeUpdate()>0)
		{
			String dir="C:\\Users\\Amaresh\\Documents\\";
			cli=Databass.getDbx();
			cli.createFolder("/"+name);
			return 1;	
		}
		else
			return 0;
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return 2;
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return 2;
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return 2;
	} catch (DbxException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return 0;
}
}