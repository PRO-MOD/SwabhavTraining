package com.aurionpro.FoodOrderingApp.model.JDBCConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnection {

	public String URL="jdbc:mysql://localhost:3306/FoodOrderApp";
	public String user="root";
	public String password="Pramod@183";
	
	public Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection connection=DriverManager.getConnection(URL,user,password);
		
		return connection;
	}
}
