package com.aurionpro.QuizApplication.model.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.aurionpro.QuizApplication.model.DBConnection.JDBCConnection;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/viewResponse")
public class ViewResponses extends HttpServlet {
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

        if (session == null || session.getAttribute("adminUser") == null) {
            pw.println("<p style=color:red;>Invalid Session</p>");
            RequestDispatcher rd = req.getRequestDispatcher("Login.html");
            rd.include(req, resp);
            return;
        }

        String name = (String) session.getAttribute("adminUser");

        // Bootstrap & CSS
        pw.println("<!DOCTYPE html>");
        pw.println("<html lang='en'>");
        pw.println("<head>");
        pw.println("<meta charset='UTF-8'>");
        pw.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        pw.println("<title>Quiz Responses</title>");
        pw.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>");
        pw.println("<style>");
        pw.println("body { background-color: #f8f9fa; padding: 20px; }");
        pw.println(".container { background: white; padding: 20px; border-radius: 10px; box-shadow: 0px 0px 10px rgba(0,0,0,0.1); }");
        pw.println("h2 { color: #0d6efd; }");
        pw.println("</style>");
        pw.println("</head>");
        pw.println("<body>");

        pw.println("<div class='container'>");
        pw.println("<h2 class='mb-4'>Welcome, " + name + "</h2>");
        pw.println("<h4 class='mb-3'>Leaderboard</h4>");
        pw.println("<table class='table table-striped table-bordered'>");
        pw.println("<thead class='table-dark'><tr>");
        pw.println("<th>User ID</th>");
        pw.println("<th>Username</th>");
        pw.println("<th>Score</th>");
        pw.println("<th>Time</th>");
        pw.println("</tr></thead>");
        pw.println("<tbody>");

        String query = "SELECT * FROM results ORDER BY score DESC"; 
        int highest = Integer.MIN_VALUE;
        int lowest = Integer.MAX_VALUE;
        int total = 0;
        int count = 0;

        try (PreparedStatement psmt = connection.prepareStatement(query)) {
            ResultSet rs = psmt.executeQuery();
            while (rs.next()) {
                int userid = rs.getInt("user_id");
                int score = rs.getInt("score");
                Timestamp time = rs.getTimestamp("taken_at");
                String username = rs.getString("username");
                int TotalMarks = rs.getInt("TotalMarks");

                
                pw.println("<tr>");
                pw.println("<td>" + userid + "</td>");
                pw.println("<td>" + username + "</td>");
                pw.println("<td>" + score + "/" + TotalMarks + "</td>");
                pw.println("<td>" + time + "</td>");
                pw.println("</tr>");

                
                if (score > highest) highest = score;
                if (score < lowest) lowest = score;
                total += score;
                count++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        double average = (count > 0) ? (double) total / count : 0;

       
        pw.println("</tbody>");
        pw.println("</table>");

        
        pw.println("<div class='mt-4 p-3 bg-light border rounded'>");
        pw.println("<h4> Analytics</h4>");
        pw.println("<p><strong>Highest Score:</strong> " + (highest == Integer.MIN_VALUE ? "N/A" : highest) + "</p>");
        pw.println("<p><strong>Lowest Score:</strong> " + (lowest == Integer.MAX_VALUE ? "N/A" : lowest) + "</p>");
        pw.println("<p><strong>Average Score:</strong> " + String.format("%.2f", average) + "</p>");
        pw.println("</div>");

        pw.println("<div class='mt-3'>");
        pw.println("<a href='admin' class='btn btn-primary me-2'>Go Back</a>");
        pw.println("<a href='logout' class='btn btn-danger'>Logout</a>");
        pw.println("</div>");

        pw.println("</div>");
        pw.println("</body>");
        pw.println("</html>");
    }
}
