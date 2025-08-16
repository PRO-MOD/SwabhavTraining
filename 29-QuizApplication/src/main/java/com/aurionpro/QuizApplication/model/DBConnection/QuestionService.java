package com.aurionpro.QuizApplication.model.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionService {

	public int getQuestionCount(Connection connection) throws SQLException {

		String query = "select count(*)  from questions";

		try (PreparedStatement psmt = connection.prepareStatement(query)) {
			ResultSet rs = psmt.executeQuery();

			int totalQuestion = 0;
			if (rs.next()) {
				totalQuestion = rs.getInt(1);
			}

			return totalQuestion;
		}

	}

	public List<List<String>> getAllQuestionsBySubjects(Connection connection, List<String> subjects,
			String difficultyLevel, int questionLimit) throws SQLException {
		String inClause = String.join(",", Collections.nCopies(subjects.size(), "?"));
		String query = "SELECT * FROM questions WHERE subject IN (" + inClause
				+ ")  and DifficultyLevel=? ORDER BY RAND() LIMIT ?";

		List<List<String>> questionList = new ArrayList<>();

		try (PreparedStatement psmt = connection.prepareStatement(query)) {
			int i = 1;
			for (String sub : subjects) {
				psmt.setString(i++, sub);

			}

			psmt.setString(i++, difficultyLevel);
			psmt.setInt(i, questionLimit);

			ResultSet rs = psmt.executeQuery();
			while (rs.next()) {
				List<String> q = new ArrayList<>();
				q.add(String.valueOf(rs.getInt("question_id")));
				q.add(rs.getString("question"));
				q.add(rs.getString("option_A"));
				q.add(rs.getString("option_B"));
				q.add(rs.getString("option_C"));
				q.add(rs.getString("option_D"));
				questionList.add(q);
			}
		}
		return questionList;
	}

	public void addQuestion(Connection connection, String question, String optionA, String optionB, String optionC,
			String optionD, String correctOption, String subject, String difficultyLevel) throws SQLException {
		String query = "insert into questions (question,option_A,option_B,option_C,option_D,correct_option,subject,DifficultyLevel) values(?,?,?,?,?,?,?,?)";

		try (PreparedStatement psmt = connection.prepareStatement(query)) {
			psmt.setString(1, question);
			psmt.setString(2, optionA);
			psmt.setString(3, optionB);
			psmt.setString(4, optionC);
			psmt.setString(5, optionD);
			psmt.setString(6, correctOption);
			psmt.setString(7, subject);
			psmt.setString(8, difficultyLevel);

			psmt.execute();

		}

	}

	public void deleteQuestion(Connection connection, int question_id) throws SQLException {
		String query = "delete from questions where question_id=?";

		try (PreparedStatement psmt = connection.prepareStatement(query)) {
			psmt.setInt(1, question_id);

			psmt.execute();

		}

	}

	public void updateQuestion(Connection connection, int question_id, String question, String optionA, String optionB,
			String optionC, String optionD, String correctOption, String subject, String difficultyLevel)
			throws SQLException {
		String query = "update questions set question =?, option_A=?,option_B=?,option_C=?,option_D=?,correct_option = ? ,subject= ? ,DifficultyLevel=? where question_id=?";

		try (PreparedStatement psmt = connection.prepareStatement(query)) {
			psmt.setString(1, question);
			psmt.setString(2, optionA);
			psmt.setString(3, optionB);
			psmt.setString(4, optionC);
			psmt.setString(5, optionD);
			psmt.setString(6, correctOption);
			psmt.setString(7, subject);
			psmt.setString(8, difficultyLevel);
			psmt.setInt(9, question_id);

			psmt.execute();

		}

	}

	public boolean QuestionExists(Connection connection, String question, String subject) throws SQLException {
		String query = "select * from questions where question=? and subject=?";

		try (PreparedStatement psmt = connection.prepareStatement(query)) {
			psmt.setString(1, question);
			psmt.setString(2, subject);
			
			ResultSet rs=psmt.executeQuery();

			if(rs.next()) {
				return true;
			}
		}
		return false;
	}
}
