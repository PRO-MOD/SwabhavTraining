package com.aurionpro.QuizApplication.model.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.aurionpro.QuizApplication.model.DBConnection.JDBCConnection;
import com.aurionpro.QuizApplication.model.DBConnection.QuestionService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/quiz")
public class QuizServlet extends HttpServlet {

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
            pw.println("<p style=color:red;>Invalid Session</p>");
            RequestDispatcher rd = req.getRequestDispatcher("Login.html");
            rd.include(req, resp);
            return;
        }

        Integer totalQuestion = (Integer) session.getAttribute("totalQuestion");
        int questionCount = totalQuestion;
        Integer duration = (Integer) session.getAttribute("timeDuration");
        String difficultyLevel = (String) session.getAttribute("difficultyLevel");

        List<List<String>> questionList = (List<List<String>>) session.getAttribute("questionList");
        if (questionList == null) {
            List<String> subjects = (List<String>) session.getAttribute("subjects");
            try {
                questionList = questionService.getAllQuestionsBySubjects(connection, subjects, difficultyLevel, questionCount);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            session.setAttribute("questionList", questionList);
            session.setAttribute("totalQuestion", questionList.size());
        }

        if (session.getAttribute("quizStartTime") == null) {
            session.setAttribute("quizStartTime", LocalTime.now());
            session.setAttribute("answers", new LinkedHashMap<Integer, String>());
        }

        LocalTime startTime = (LocalTime) session.getAttribute("quizStartTime");
        long secondsPassed = Duration.between(startTime, LocalTime.now()).getSeconds();

        if (secondsPassed > duration) {
            pw.println("<p style=color:red;>Time Up!</p>");
            RequestDispatcher rd = req.getRequestDispatcher("result");
            rd.include(req, resp);
            return;
        }

        int index;
        try {
            index = Integer.parseInt(req.getParameter("index"));
        } catch (Exception e) {
            index = 0;
        }

        List<String> question = questionList.get(index);
        Map<Integer, String> answers = (Map<Integer, String>) session.getAttribute("answers");
        String selectedAnswer = (answers != null) ? answers.get(Integer.parseInt(question.get(0))) : null;

        
        pw.println("<!DOCTYPE html>");
        pw.println("<html lang='en'>");
        pw.println("<head>");
        pw.println("<meta charset='UTF-8'>");
        pw.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        pw.println("<title>Quiz Question</title>");
        pw.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>");
        pw.println("<style>");
        pw.println("body { background-color: #f8f9fa; padding: 20px; }");
        pw.println(".quiz-card { max-width: 700px; margin: auto; background: white; padding: 20px; border-radius: 10px; box-shadow: 0px 0px 10px rgba(0,0,0,0.1); }");
        pw.println(".timer { font-weight: bold; color: #dc3545; font-size: 18px; }");
        pw.println(".question-text { font-size: 20px; font-weight: 500; margin-bottom: 15px; }");
        pw.println(".option { margin-bottom: 10px; }");
        pw.println("</style>");
        pw.println("</head>");
        pw.println("<body>");

        pw.println("<div class='quiz-card'>");
        pw.println("<div class='d-flex justify-content-between mb-3'>");
        pw.println("<div>Question " + (index + 1) + " of " + totalQuestion + "</div>");
        pw.println("<div class='timer'>Time Left: <span id='timer'>" 
                + Math.max(0, duration - secondsPassed) + "</span>s</div>");

        pw.println("</div>");

        pw.println("<form method='post' action='submit'>");
        pw.println("<div class='question-text'>" + question.get(1) + "</div>");

        pw.println("<div class='form-check option'><input class='form-check-input' type='radio' name='answer' value='A' " + ("A".equalsIgnoreCase(selectedAnswer) ? "checked" : "") + ">" +
                "<label class='form-check-label'>" + question.get(2) + "</label></div>");

        pw.println("<div class='form-check option'><input class='form-check-input' type='radio' name='answer' value='B' " + ("B".equalsIgnoreCase(selectedAnswer) ? "checked" : "") + ">" +
                "<label class='form-check-label'>" + question.get(3) + "</label></div>");

        pw.println("<div class='form-check option'><input class='form-check-input' type='radio' name='answer' value='C' " + ("C".equalsIgnoreCase(selectedAnswer) ? "checked" : "") + ">" +
                "<label class='form-check-label'>" + question.get(4) + "</label></div>");

        pw.println("<div class='form-check option'><input class='form-check-input' type='radio' name='answer' value='D' " + ("D".equalsIgnoreCase(selectedAnswer) ? "checked" : "") + ">" +
                "<label class='form-check-label'>" + question.get(5) + "</label></div>");

        pw.println("<input type='hidden' name='questionId' value='" + question.get(0) + "'/>");
        pw.println("<input type='hidden' name='index' value='" + index + "'>");

        pw.println("<div class='mt-3'>");
        pw.println("<button type='submit' class='btn btn-primary me-2'>" + (index + 1 == totalQuestion ? "Finish" : "Next") + "</button>");
        pw.println("</div>");
        pw.println("</form>");

        if (index > 0) {
            pw.println("<form method='post' action='prev' style='display:inline;'>");
            pw.println("<input type='hidden' name='index' value='" + index + "'>");
            pw.println("<button type='submit' class='btn btn-secondary mt-2'>Prev</button>");
            pw.println("</form>");
        }

        pw.println("<div class='mt-3'><a href='logout' class='btn btn-danger'>Logout</a></div>");
        pw.println("</div>");
        
        
        pw.println("<script>");
        pw.println("let timeLeft = " + Math.max(0, duration - secondsPassed) + ";");

        pw.println("function updateTimer() {");
        pw.println("  if(timeLeft <= 0){");
        pw.println("    document.getElementById('timer').innerText = '0';");
        pw.println("    alert('Time Up!');");
        pw.println("    window.location.href = 'result';"); 
        pw.println("  } else {");
        pw.println("    document.getElementById('timer').innerText = timeLeft;");
        pw.println("    timeLeft--;"); 
        pw.println("  }");
        pw.println("}");

        pw.println("setInterval(updateTimer, 1000);");  
        pw.println("</script>");


        pw.println("</body></html>");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}
