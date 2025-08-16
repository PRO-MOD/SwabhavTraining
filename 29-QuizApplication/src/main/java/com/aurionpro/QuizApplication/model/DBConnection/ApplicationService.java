package com.aurionpro.QuizApplication.model.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ApplicationService {

	public boolean userAuthentication(Connection connection, String username, String password,String role) throws SQLException {

		String query = "select * from user Where username=? And password=? and role=?";

		try (PreparedStatement psmt = connection.prepareStatement(query)) {
			psmt.setString(1, username);
			psmt.setString(2, password);
			psmt.setString(3, role);

			ResultSet rs = psmt.executeQuery();

			if (rs.next()) {
				String pass=rs.getString("password");
				
				if(pass.equals(password)) {
					return true;
				}
				
				
			}
		}

		return false;

	}
	
	
	public boolean userExits(Connection connection,String userName,String email,String role) throws SQLException {
		String query = "select * from user where username=? and email=? and role=?";
		
		
		try (PreparedStatement psmt = connection.prepareStatement(query)) {
			psmt.setString(1, userName);
			psmt.setString(2, email);
			psmt.setString(3, role);

			ResultSet rs = psmt.executeQuery();

			if (rs.next()) {
				return true;
			}
		}

		return false;
	}
	
	
	public void addUser(Connection connection,String userName,String password,String email,String role) throws SQLException {
		String query = "insert into user (username,password,email,role) values(?,?,?,?)";

		try (PreparedStatement psmt = connection.prepareStatement(query)) {
			psmt.setString(1, userName);
			psmt.setString(2, password);
			psmt.setString(3, email);
			psmt.setString(4, role);
			

			psmt.execute();

			

			System.out.println("User Added Succesfully!");
		}
	}
	
	
	public int getUserId (Connection connection,String username) throws SQLException {
		String query="select id from user where username=? ";
		
		try(PreparedStatement psmt=connection.prepareStatement(query)){
			psmt.setString(1, username);
			
			
			ResultSet rs = psmt.executeQuery();
			
			int userId=0;
			
			if(rs.next()) {
				 userId=rs.getInt("id");
			}
			
			return userId;
		}
	}
	
	
	
}
