package com.aurionpro.QuizApplication.model.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.aurionpro.QuizApplication.model.DBConnection.JDBCConnection;
import com.aurionpro.QuizApplication.model.DBConnection.ScoreService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/result")
public class ResultServlet extends HttpServlet {

    JDBCConnection jdbcConnection = new JDBCConnection();
    ScoreService scoreService = new ScoreService();

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

        Integer userId = (Integer) session.getAttribute("userId");
        Map<Integer, String> answers = (Map<Integer, String>) session.getAttribute("answers");
        Integer total = (Integer) session.getAttribute("totalQuestion");

        if (answers == null || total == null) {
            resp.sendRedirect("Login.html");
            return;
        }

        int score = 0;
        String query = "SELECT correct_option FROM questions WHERE question_id=?";

        try (PreparedStatement psmt = connection.prepareStatement(query)) {
            for (Map.Entry<Integer, String> e : answers.entrySet()) {
                psmt.setInt(1, e.getKey());
                ResultSet rs = psmt.executeQuery();
                if (rs.next()) {
                    String correct = rs.getString("correct_option");
                    if (correct != null && correct.equalsIgnoreCase(e.getValue())) {
                        score++;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

       
        pw.println("<!DOCTYPE html>");
        pw.println("<html lang='en'>");
        pw.println("<head>");
        pw.println("<meta charset='UTF-8'>");
        pw.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        pw.println("<title>Quiz Result</title>");
        pw.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>");
        pw.println("<style>");
        pw.println("body { background-color: #f8f9fa; }");
        pw.println(".result-card { max-width: 600px; margin: 50px auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }");
        pw.println("</style>");
        pw.println("</head>");
        pw.println("<body>");

        pw.println("<div class='container'>");
        pw.println("<div class='result-card text-center'>");
        pw.println("<h2 class='mb-4'>Quiz Result</h2>");
        pw.println("<h4 class='text-success'>Score: <b>" + score + "</b> / " + total + "</h4>");
        pw.println("<hr>");
        
        
        pw.println("<form action='viewMyResponse' method='post' class='mb-3'>");
        pw.println("<button type='submit' class='btn btn-info'>View My Responses</button>");
        pw.println("</form>");

        
        pw.println("<a href='selectQuiz' class='btn btn-primary me-2'>Retake Quiz</a>");
        pw.println("<a href='logout' class='btn btn-danger'>Logout</a>");

        pw.println("</div>");
        pw.println("</div>");

        pw.println("<script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js'></script>");
        pw.println("</body>");
        pw.println("</html>");

       
        String username = (String) session.getAttribute("username");
        try {
            scoreService.storeScore(connection, userId, score, username,total);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        
//        session.removeAttribute("answers"); 
//        session.removeAttribute("quizStartTime"); 
//        session.removeAttribute("durationSeconds"); 
//        session.removeAttribute("totalQuestions");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}
