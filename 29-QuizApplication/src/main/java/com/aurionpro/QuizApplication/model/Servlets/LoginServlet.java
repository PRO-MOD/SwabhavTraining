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
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	JDBCConnection jdbcConnection = new JDBCConnection();
	ApplicationService service = new ApplicationService();

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

		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String role = req.getParameter("role");

		int userId=0;

		try {
			userId = service.getUserId(connection, username);
		} catch (SQLException e) {

			e.printStackTrace();
		}

		try {
			if (service.userAuthentication(connection, username, password,role)) {

				HttpSession session = req.getSession();
				session.setMaxInactiveInterval(60);
			
				
				if ("user".equals(role)) {
					session.setAttribute("username", username);
					session.setAttribute("userId", userId);
					RequestDispatcher rd = req.getRequestDispatcher("selectQuiz");
					rd.forward(req, resp);
				}

				else {
					 session.setAttribute("adminUser", username);
					 session.setAttribute("userId", userId);

					RequestDispatcher rd = req.getRequestDispatcher("admin");
					rd.forward(req, resp);
				}

				
			}

			else {
				pw.println("<p style='color:red;'>Invalid Credentails!</p>");

				RequestDispatcher rd = req.getRequestDispatcher("Login.html");
				rd.include(req, resp);

			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		PrintWriter pw = resp.getWriter();

		pw.println("<p style='color:red;'>Do Login!</p>");

		RequestDispatcher rd = req.getRequestDispatcher("Login.html");
		rd.include(req, resp);

	}
}
