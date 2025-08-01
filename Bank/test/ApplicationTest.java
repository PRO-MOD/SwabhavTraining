package com.aurionpro.JDBC.Transaction.Bank.test;

import com.aurionpro.JDBC.Transaction.Bank.model.Application;
import com.aurionpro.JDBC.Transaction.Bank.model.JDBCConnection;

public class ApplicationTest {

	public static void main(String[] args) {
		
		try {
			JDBCConnection jdbcConnection=new JDBCConnection();
			
			Application application=new Application();
			application.start(jdbcConnection.getConnection());
		}
		
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
