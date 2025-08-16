package com.aurionpro.QuizApplication.model.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import com.aurionpro.QuizApplication.model.DBConnection.JDBCConnection;
import com.aurionpro.QuizApplication.model.DBConnection.QuestionService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/beginTest")
public class QuizBeginServlet extends HttpServlet {

    JDBCConnection jdbcConnection = new JDBCConnection();
    QuestionService questionService = new QuestionService();

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

        String[] subjects = req.getParameterValues("subject");
        String countStr = req.getParameter("questionCount");
        String difficultyLevel = req.getParameter("difficultyLevel");
        session.setAttribute("difficultyLevel", difficultyLevel);

        if (subjects == null || subjects.length == 0 || countStr == null) {
            pw.println("<p style='color:red;'>Please select at least one subject and question count.</p>");
            req.getRequestDispatcher("selectQuiz").include(req, resp);
            return;
        }

        int questionCount = Integer.parseInt(countStr);
        int timeDuration;
        switch (questionCount) {
            case 10: timeDuration = 180; break;
            case 15: timeDuration = 300; break;
            case 20: timeDuration = 480; break;
            default: timeDuration = 180;
        }

        session.setAttribute("subjects", Arrays.asList(subjects));
        session.setAttribute("totalQuestion", questionCount);
        session.setAttribute("timeDuration", timeDuration);

        List<String> subjectsList = (List<String>) session.getAttribute("subjects");
        Integer totalQuestion = (Integer) session.getAttribute("totalQuestion");
        Integer time = (Integer) session.getAttribute("timeDuration");

       
        pw.println("<!DOCTYPE html>");
        pw.println("<html>");
        pw.println("<head>");
        pw.println("<meta charset='UTF-8'>");
        pw.println("<title>Begin Quiz</title>");
        pw.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css' rel='stylesheet'>");
        pw.println("<style>");
        pw.println("body { background-color: #f8f9fa; }");
        pw.println(".begin-quiz-container { max-width: 500px; margin: 50px auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0px 0px 15px rgba(0,0,0,0.1); }");
        pw.println("h2 { text-align: center; margin-bottom: 20px; }");
        pw.println("</style>");
        pw.println("</head>");
        pw.println("<body>");

        pw.println("<div class='container'>");
        pw.println("<div class='begin-quiz-container'>");
        pw.println("<h2>Ready to Begin Quiz?</h2>");

        pw.println("<ul class='list-group mb-3'>");
        pw.println("<li class='list-group-item'><strong>Subjects:</strong> " + String.join(", ", subjectsList) + "</li>");
        pw.println("<li class='list-group-item'><strong>Total Questions:</strong> " + totalQuestion + "</li>");
        pw.println("<li class='list-group-item'><strong>Difficulty Level:</strong> " + difficultyLevel + "</li>");
        pw.println("<li class='list-group-item'><strong>Time Duration:</strong> " + (time / 60) + " minutes</li>");
        pw.println("</ul>");

        pw.println("<form action='quiz' method='post' class='mb-2'>");
        pw.println("<input type='hidden' name='index' value='0'>");
        pw.println("<div class='d-grid'><input type='submit' value='Start Quiz' class='btn btn-primary'></div>");
        pw.println("</form>");

//        pw.println("<form action='dashboard' method='post' class='mb-2'>");
//        pw.println("<div class='d-grid'><input type='submit' value='View Dashboard' class='btn btn-secondary'></div>");
//        pw.println("</form>");

        pw.println("<div class='text-center'>");
        pw.println("<a href='logout' class='btn btn-danger btn-sm'>Logout</a>");
        pw.println("</div>");

        pw.println("</div>");
        pw.println("</div>");

        pw.println("</body>");
        pw.println("</html>");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}
