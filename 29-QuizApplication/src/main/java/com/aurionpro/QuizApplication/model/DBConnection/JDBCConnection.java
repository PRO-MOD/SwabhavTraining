package com.aurionpro.QuizApplication.model.DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnection {

	public String URL = "jdbc:mysql://localhost:3306/QuizApplication";
	public String user = "root";
	public String Password = "Pramod@183";

	public Connection getConnection() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection connection = DriverManager.getConnection(URL, user, Password);
	
		return connection;
	}
}
