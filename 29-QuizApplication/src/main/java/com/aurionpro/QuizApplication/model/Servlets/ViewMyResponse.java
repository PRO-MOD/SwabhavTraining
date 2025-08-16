package com.aurionpro.QuizApplication.model.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.aurionpro.QuizApplication.model.DBConnection.JDBCConnection;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/viewMyResponse")
public class ViewMyResponse extends HttpServlet {
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
            RequestDispatcher rd = req.getRequestDispatcher("Login.html");
            rd.include(req, resp);
            return;
        }

        Map<Integer, String> answers = (Map<Integer, String>) session.getAttribute("answers");
        if (answers == null || answers.isEmpty()) {
            pw.println("<p>No quiz responses found for the current session.</p>");
            pw.println("<p><a href='result'>Go Back</a></p>");
            return;
        }

        // HTML + Bootstrap Layout
        pw.println("<!DOCTYPE html>");
        pw.println("<html lang='en'>");
        pw.println("<head>");
        pw.println("<meta charset='UTF-8'>");
        pw.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        pw.println("<title>My Quiz Responses</title>");
        pw.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>");
        pw.println("<style>");
        pw.println("body { background-color: #f8f9fa; }");
        pw.println(".response-card { border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1); margin-bottom: 20px; }");
        pw.println("</style>");
        pw.println("</head>");
        pw.println("<body>");

        pw.println("<div class='container mt-4'>");
        pw.println("<div class='text-center mb-4'>");
        pw.println("<h2>My Quiz Responses</h2>");
        pw.println("<a href='result' class='btn btn-secondary mt-2'>Back to Results</a>");
        pw.println("</div>");

        String query = "SELECT question, option_A, option_B, option_C, option_D, correct_option FROM questions WHERE question_id = ?";

        try (PreparedStatement psmt = connection.prepareStatement(query)) {
            for (Map.Entry<Integer, String> entry : answers.entrySet()) {
                int questionId = entry.getKey();
                String userAnswer = entry.getValue();

                psmt.setInt(1, questionId);
                ResultSet rs = psmt.executeQuery();

                if (rs.next()) {
                    String correct = rs.getString("correct_option");

                    pw.println("<div class='card response-card p-3'>");
                    pw.println("<p><b>Q:</b> " + rs.getString("question") + "</p>");
                    
                    // User Answer
                    if (userAnswer != null && userAnswer.equalsIgnoreCase(correct)) {
                        pw.println("<p class='text-success'><b>Your Answer:</b> " + decorateAnswer(userAnswer, rs) + " </p>");
                    } else {
                        pw.println("<p class='text-danger'><b>Your Answer:</b> " + decorateAnswer(userAnswer, rs) + " </p>");
                    }

                    // Correct Answer
                    pw.println("<p><b>Correct Answer:</b> " + decorateAnswer(correct, rs) + "</p>");

                    // Status
                    if (userAnswer != null && userAnswer.equalsIgnoreCase(correct)) {
                        pw.println("<span class='badge bg-success'>Correct</span>");
                    } else {
                        pw.println("<span class='badge bg-danger'>Incorrect</span>");
                    }

                    pw.println("</div>");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        pw.println("</div>");
        pw.println("<script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js'></script>");
        
    
        pw.println("</body>");
        pw.println("</html>");
    }

    private String decorateAnswer(String option, ResultSet rs) throws SQLException {
        if (option == null)
            return "Not Answered";
        switch (option.toUpperCase()) {
            case "A":
                return "A. " + rs.getString("option_A");
            case "B":
                return "B. " + rs.getString("option_B");
            case "C":
                return "C. " + rs.getString("option_C");
            case "D":
                return "D. " + rs.getString("option_D");
            default:
                return option;
        }
    }
}
