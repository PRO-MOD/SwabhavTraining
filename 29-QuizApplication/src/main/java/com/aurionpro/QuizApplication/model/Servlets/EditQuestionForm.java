package com.aurionpro.QuizApplication.model.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.aurionpro.QuizApplication.model.DBConnection.JDBCConnection;
import com.aurionpro.QuizApplication.model.DBConnection.QuestionService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/editQuestionForm")
public class EditQuestionForm extends HttpServlet{

	JDBCConnection jdbcConnection = new JDBCConnection();

	QuestionService questionService = new QuestionService();
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Connection connection = null;

		try {
			connection = jdbcConnection.getConnection();
		}

		catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		if (connection == null) {
			resp.getWriter().println("<h3>Database connection failed. Try again later.</h3>");
			return;
		}

		resp.setContentType("text/html");

		PrintWriter pw = resp.getWriter();

		HttpSession session = req.getSession(false);
		
		
	    int questionId = Integer.parseInt(req.getParameter("questionId"));
	    
	    
	       try (PreparedStatement psmt = connection.prepareStatement("SELECT * FROM questions WHERE question_id=?")) {
	            psmt.setInt(1, questionId);
	            ResultSet rs = psmt.executeQuery();

	            if (rs.next()) {
	                String question = rs.getString("question");
	                String optionA = rs.getString("option_A");
	                String optionB = rs.getString("option_B");
	                String optionC = rs.getString("option_C");
	                String optionD = rs.getString("option_D");
	                String correctOption = rs.getString("correct_option");
	                String subject = rs.getString("subject");
	                String difficulty = rs.getString("DifficultyLevel");

	                pw.println("<!DOCTYPE html>");
	                pw.println("<html><head>");
	                pw.println("<title>Edit Question</title>");
	                pw.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css' rel='stylesheet'>");
	                pw.println("</head><body>");
	                pw.println("<div class='container mt-5'>");
	                pw.println("<div class='card p-4'>");
	                pw.println("<h2>Edit Question</h2>");
	                pw.println("<form action='editQuestion' method='post'>");

	                pw.println("<input type='hidden' name='questionId' value='" + questionId + "'/>");

	                pw.println("<div class='mb-3'><label>Question</label>");
	                pw.println("<input type='text' name='question' class='form-control' value='" + question + "' required></div>");

	                pw.println("<div class='mb-3'><label>Option A</label>");
	                pw.println("<input type='text' name='optionA' class='form-control' value='" + optionA + "' required></div>");

	                pw.println("<div class='mb-3'><label>Option B</label>");
	                pw.println("<input type='text' name='optionB' class='form-control' value='" + optionB + "' required></div>");

	                pw.println("<div class='mb-3'><label>Option C</label>");
	                pw.println("<input type='text' name='optionC' class='form-control' value='" + optionC + "' required></div>");

	                pw.println("<div class='mb-3'><label>Option D</label>");
	                pw.println("<input type='text' name='optionD' class='form-control' value='" + optionD + "' required></div>");

	                pw.println("<div class='mb-3'><label>Correct Option</label>");
	                pw.println("<input type='text' name='correctOption' class='form-control' value='" + correctOption + "' required></div>");

	                pw.println("<div class='mb-3'><label>Subject</label>");
	                pw.println("<select name='subject' class='form-select'>");
	                pw.println("<option " + ("Java".equalsIgnoreCase(subject) ? "selected" : "") + ">Java</option>");
	                pw.println("<option " + ("Python".equalsIgnoreCase(subject) ? "selected" : "") + ">Python</option>");
	                pw.println("<option " + ("Servlet".equalsIgnoreCase(subject) ? "selected" : "") + ">Servlet</option>");
	                pw.println("<option " + ("C++".equalsIgnoreCase(subject) ? "selected" : "") + ">C++</option>");
	                pw.println("</select></div>");

	                pw.println("<div class='mb-3'><label>Difficulty</label>");
	                pw.println("<select name='difficultyLevel' class='form-select'>");
	                pw.println("<option " + ("Easy".equalsIgnoreCase(difficulty) ? "selected" : "") + ">Easy</option>");
	                pw.println("<option " + ("Moderate".equalsIgnoreCase(difficulty) ? "selected" : "") + ">Moderate</option>");
	                pw.println("<option " + ("Difficult".equalsIgnoreCase(difficulty) ? "selected" : "") + ">Difficult</option>");
	                pw.println("</select></div>");

	                pw.println("<button type='submit' class='btn btn-primary'>Update</button>");
	                pw.println("</form>");
	                pw.println("</div></div>");
	                pw.println("</body></html>");
	            } else {
	                pw.println("<p>No question found!</p>");
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
}
