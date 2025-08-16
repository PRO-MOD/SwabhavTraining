package com.aurionpro.QuizApplication.model.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import com.aurionpro.QuizApplication.model.DBConnection.ApplicationService;
import com.aurionpro.QuizApplication.model.DBConnection.JDBCConnection;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

	JDBCConnection jdbcConnection = new JDBCConnection();
	ApplicationService service = new ApplicationService();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Connection connection = null;

		try {
			connection = jdbcConnection.getConnection();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		if (connection == null) {
			resp.getWriter().println("<h3>Database connection failed. Try again later.</h3>");
			return;
		}

		resp.setContentType("text/html");
		PrintWriter pw = resp.getWriter();

		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String email = req.getParameter("email");
		String role = req.getParameter("role");
		
		
		

		try {
			if (service.userExits(connection, username, email,role)) {
				pw.println("<p style='color:red;'>User Already Exits!</p>");

				RequestDispatcher rd = req.getRequestDispatcher("Register.html");
				rd.include(req, resp);
			}

			else {
				pw.println("<p style='color:green;'>Registration Successfull! Do Login.</p>");
				
				service.addUser(connection, username, password, email,role);

				RequestDispatcher rd = req.getRequestDispatcher("Login.html");
				rd.include(req, resp);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

}
