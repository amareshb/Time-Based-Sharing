package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Databass {
private Connection con;
private static Databass d=new Databass();
private Databass()
{
	try {
		con=DriverManager.getConnection("jdbc:mysql://localhost:3306/cloud","root","amar");
		if(con!=null)
			System.out.println("null");
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
public static Connection getConnect()
{
	return d.con;
}
}
