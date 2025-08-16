package com.aurionpro.QuizApplication.model.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.aurionpro.QuizApplication.model.DBConnection.JDBCConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    JDBCConnection jdbcConnection = new JDBCConnection();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Connection connection = null;

        try {
            connection = jdbcConnection.getConnection();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (connection == null) {
            resp.getWriter().println("<h3>Database connection failed. Try again later.</h3>");
            return;
        }

        resp.setContentType("text/html");
        PrintWriter pw = resp.getWriter();

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            pw.println("<p style='color:red;'>Invalid Session</p>");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        String username = (String) session.getAttribute("username");

        String query = "SELECT * FROM results WHERE user_id=?";

        pw.println("<!DOCTYPE html>");
        pw.println("<html lang='en'>");
        pw.println("<head>");
        pw.println("<meta charset='UTF-8'>");
        pw.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        pw.println("<title>Dashboard</title>");
        pw.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>");
        pw.println("<style>");
        pw.println("body { background-color: #f8f9fa; }");
        pw.println(".dashboard { max-width: 900px; margin: 50px auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }");
        pw.println("</style>");
        pw.println("</head>");
        pw.println("<body>");

        pw.println("<div class='container'>");
        pw.println("<div class='dashboard'>");
        pw.println("<h2 class='mb-4'>Welcome, " + username + "</h2>");
        pw.println("<h4 class='mb-3'>Quiz History</h4>");

        pw.println("<div class='table-responsive'>");
        pw.println("<table class='table table-striped table-bordered'>");
        pw.println("<thead class='table-dark'>");
        pw.println("<tr>");
        pw.println("<th>User ID</th>");
        pw.println("<th>Username</th>");
        pw.println("<th>Score</th>");
        pw.println("<th>Time Taken</th>");
        pw.println("</tr>");
        pw.println("</thead>");
        pw.println("<tbody>");

        try (PreparedStatement psmt = connection.prepareStatement(query)) {
            psmt.setInt(1, userId);
            ResultSet rs = psmt.executeQuery();

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                int userid = rs.getInt("user_id");
                int score = rs.getInt("score");
                Timestamp time = rs.getTimestamp("taken_at");
                String name = rs.getString("username");
                int TotalMarks=rs.getInt("TotalMarks");

                pw.println("<tr>");
                pw.println("<td>" + userid + "</td>");
                pw.println("<td>" + name + "</td>");
               
                pw.println("<td>" + score + "/"+TotalMarks+"</td>");
                pw.println("<td>" + time + "</td>");
                pw.println("</tr>");
            }

            if (!hasData) {
                pw.println("<tr><td colspan='4' class='text-center text-muted'>No quiz history found</td></tr>");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        pw.println("</tbody>");
        pw.println("</table>");
        pw.println("</div>");

        pw.println("<div class='mt-3'>");
        pw.println("<a href='selectQuiz' class='btn btn-primary me-2'>Go Back</a>");
        pw.println("<a href='logout' class='btn btn-danger'>Logout</a>");
        pw.println("</div>");

        pw.println("</div>");
        pw.println("</div>");

        pw.println("<script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js'></script>");
        pw.println("</body>");
        pw.println("</html>");
    }
}
