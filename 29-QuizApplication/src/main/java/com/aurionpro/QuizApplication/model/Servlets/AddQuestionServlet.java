package com.aurionpro.QuizApplication.model.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import com.aurionpro.QuizApplication.model.DBConnection.JDBCConnection;
import com.aurionpro.QuizApplication.model.DBConnection.QuestionService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/addQuestion")
public class AddQuestionServlet extends HttpServlet{

	JDBCConnection jdbcConnection = new JDBCConnection();
	QuestionService questionService=new QuestionService();
	
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
	        if (session == null || session.getAttribute("adminUser") == null) {
	        	pw.println("<p style=color:red;>InValid Session</p>");
				RequestDispatcher rd = req.getRequestDispatcher("Login.html");
				rd.include(req, resp);
				
				return;
	        }

	        String question = req.getParameter("question");
	        String optionA = req.getParameter("optionA");
	        String optionB = req.getParameter("optionB");
	        String optionC = req.getParameter("optionC");
	        String optionD = req.getParameter("optionD");
	        String correctOption = req.getParameter("correctOption");
	        String subject=req.getParameter("subject");
	        String difficultyLevel=req.getParameter("difficultyLevel");
	        
	        try {
	        	
	        	if(questionService.QuestionExists(connection, question, subject)) {
	        		pw.println("<p style=color:red;>Question Already Exits!</p>");
	        		RequestDispatcher rd = req.getRequestDispatcher("AddQuestion.html");
		            rd.include(req, resp);
	        	}
	        	
	        	else {
	        		questionService.addQuestion(connection, question, optionA, optionB, optionC, optionD, correctOption,subject,difficultyLevel);
					
					pw.println("<p style=color:green;>Question Added Successfully!</p>");
					
					 RequestDispatcher rd = req.getRequestDispatcher("AddQuestion.html");
			            rd.include(req, resp);
	        	}
	        	
			
		            
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
	}
}
