package com.aurionpro.QuizApplication.model.Servlets;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/selectQuiz")
public class SelectQuizServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        session.removeAttribute("answers"); 
        session.removeAttribute("quizStartTime"); 
        session.removeAttribute("durationSeconds"); 
        session.removeAttribute("totalQuestions");
        resp.setContentType("text/html");
        PrintWriter pw = resp.getWriter();

        if (session == null || session.getAttribute("userId") == null) {
            pw.println("<p style='color:red;'>Invalid Session</p>");
            req.getRequestDispatcher("Login.html").include(req, resp);
            return;
        }

        pw.println("<!DOCTYPE html>");
        pw.println("<html>");
        pw.println("<head>");
        pw.println("<meta charset='UTF-8'>");
        pw.println("<title>Select Quiz</title>");
        pw.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css' rel='stylesheet'>");
        pw.println("<style>");
        pw.println("body { background-color: #f8f9fa; }");
        pw.println(".quiz-container { max-width: 500px; margin: 50px auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0px 0px 15px rgba(0,0,0,0.1); }");
        pw.println("h2 { text-align: center; margin-bottom: 20px; }");
        pw.println("</style>");
        pw.println("</head>");
        pw.println("<body>");

        pw.println("<div class='container'>");
        pw.println("<div class='quiz-container'>");
        pw.println("<h2>Build Your Quiz</h2>");
        pw.println("<form action='beginTest' method='post'>");

        // Subjects
        pw.println("<div class='mb-3'>");
        pw.println("<h5>Choose Subjects:</h5>");
        pw.println("<div class='form-check'><input class='form-check-input' type='checkbox' name='subject' value='java' id='java'><label class='form-check-label' for='java'>Java</label></div>");
        pw.println("<div class='form-check'><input class='form-check-input' type='checkbox' name='subject' value='python' id='python'><label class='form-check-label' for='python'>Python</label></div>");
        pw.println("<div class='form-check'><input class='form-check-input' type='checkbox' name='subject' value='servlet' id='servlet'><label class='form-check-label' for='servlet'>Servlet</label></div>");
        pw.println("<div class='form-check'><input class='form-check-input' type='checkbox' name='subject' value='c++' id='cpp'><label class='form-check-label' for='cpp'>C++</label></div>");
        pw.println("</div>");

        // Question Count
        pw.println("<div class='mb-3'>");
        pw.println("<h5>Choose Number of Questions:</h5>");
        pw.println("<select name='questionCount' class='form-select'>");
        pw.println("<option value='10'>10</option>");
        pw.println("<option value='15'>15</option>");
        pw.println("<option value='20'>20</option>");
        pw.println("</select>");
        pw.println("</div>");

        // Difficulty Level
        pw.println("<div class='mb-3'>");
        pw.println("<h5>Choose Difficulty Level:</h5>");
        pw.println("<select name='difficultyLevel' class='form-select'>");
        pw.println("<option value='Easy'>Easy</option>");
        pw.println("<option value='Moderate'>Moderate</option>");
        pw.println("<option value='Difficult'>Difficult</option>");
        pw.println("</select>");
        pw.println("</div>");

        // Submit Button
        pw.println("<div class='d-grid'>");
        pw.println("<input type='submit' value='Proceed' class='btn btn-primary'>");
        pw.println("</div>");

        pw.println("</form><br>");
        
        pw.println("<form action='dashboard' method='post' class='mb-2'>");
      pw.println("<div class='d-grid'><input type='submit' value='View Dashboard' class='btn btn-secondary'></div>");
      pw.println("</form>");

        pw.println("<div class='text-center mt-3'>");
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
