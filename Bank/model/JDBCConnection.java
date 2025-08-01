package com.aurionpro.JDBC.Transaction.Bank.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnection {
	public String URL = "jdbc:mysql://localhost:3306/transactions";
	public String user = "root";
	public String Password = "Pramod@183";

	public Connection getConnection() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection connection = DriverManager.getConnection(URL, user, Password);
		connection.setAutoCommit(false);
		connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
		return connection;
	}
}
