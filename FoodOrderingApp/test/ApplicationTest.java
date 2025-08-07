package com.aurionpro.FoodOrderingApp.test;

import com.aurionpro.FoodOrderingApp.model.Application;
import com.aurionpro.FoodOrderingApp.model.JDBCConnection.JDBCConnection;

public class ApplicationTest {

	public static void main (String []args) {
		try {
			JDBCConnection jdbc=new JDBCConnection();
			Application application =new Application();
			application.start(jdbc.getConnection());
		}
		
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
