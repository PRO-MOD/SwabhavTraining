package com.aurionpro.QuizApplication.model.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import com.aurionpro.QuizApplication.model.DBConnection.JDBCConnection;
import com.aurionpro.QuizApplication.model.DBConnection.QuestionService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/deleteQuestion")
public class DeleteServlet extends HttpServlet{
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
		
		int id=Integer.parseInt(req.getParameter("questionId"));
		
		try {
			questionService.deleteQuestion(connection, id);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		resp.sendRedirect("viewQuestions");
		
	}
}
