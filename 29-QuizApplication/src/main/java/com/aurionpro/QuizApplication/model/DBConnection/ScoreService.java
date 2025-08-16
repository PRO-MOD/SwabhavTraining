package com.aurionpro.QuizApplication.model.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ScoreService {

	public void storeScore(Connection connection,int userId,int score,String username,int TotalMarks) throws SQLException {
		String query="insert into results (user_id,score,username,TotalMarks) values (?,?,?,?)";
		
		try (PreparedStatement psmt = connection.prepareStatement(query)) {
			psmt.setInt(1, userId);
			psmt.setInt(2, score);
			psmt.setString(3, username);
			psmt.setInt(4, TotalMarks);
			
			psmt.execute();
			
		}
	}
	
	

}
