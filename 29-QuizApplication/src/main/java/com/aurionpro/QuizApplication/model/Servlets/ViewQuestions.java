package com.aurionpro.QuizApplication.model.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.aurionpro.QuizApplication.model.DBConnection.JDBCConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/viewQuestions")
public class ViewQuestions extends HttpServlet {

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

        
        String selectedSubject = req.getParameter("subject");

        pw.println("<!DOCTYPE html>");
        pw.println("<html lang='en'>");
        pw.println("<head>");
        pw.println("<meta charset='UTF-8'>");
        pw.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        pw.println("<title>View Questions</title>");
        pw.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>");
        pw.println("<style>");
        pw.println("body { background-color: #f8f9fa; }");
        pw.println(".question-card { border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1); margin-bottom: 20px; }");
        pw.println("</style>");
        pw.println("</head>");
        pw.println("<body>");

        pw.println("<div class='container mt-4'>");
        pw.println("<div class='text-center mb-4'>");
        pw.println("<h2> All Questions</h2>");
        pw.println("</div>");

        
        pw.println("<form method='post' action='viewQuestions' class='mb-4'>");
        pw.println("<label for='subject' class='form-label'>Choose Subject:</label>");
        pw.println("<select name='subject' id='subject' class='form-select w-50 d-inline'>");
        pw.println("<option value='' " + (selectedSubject == null || selectedSubject.isEmpty() ? "selected" : "") + ">All</option>");
        pw.println("<option value='Java' " + ("Java".equalsIgnoreCase(selectedSubject) ? "selected" : "") + ">Java</option>");
        pw.println("<option value='Python' " + ("Python".equalsIgnoreCase(selectedSubject) ? "selected" : "") + ">Python</option>");
        pw.println("<option value='C++' " + ("C++".equalsIgnoreCase(selectedSubject) ? "selected" : "") + ">C++</option>");
        pw.println("<option value='Servlet' " + ("Servlet".equalsIgnoreCase(selectedSubject) ? "selected" : "") + ">Servlet</option>");
        pw.println("</select>");
        pw.println("<button type='submit' class='btn btn-primary ms-2'>Filter</button>");
        pw.println("</form>");

        String query = "SELECT * FROM questions";
        if (selectedSubject != null && !selectedSubject.isEmpty()) {
            query += " WHERE subject = ?";
        }

        try (PreparedStatement psmt = connection.prepareStatement(query)) {
            if (selectedSubject != null && !selectedSubject.isEmpty()) {
                psmt.setString(1, selectedSubject);
            }

            ResultSet rs = psmt.executeQuery();

            boolean hasQuestions = false;
            while (rs.next()) {
                hasQuestions = true;
                int id = rs.getInt("question_id");
                String q = rs.getString("question");
                String optionA = rs.getString("option_A");
                String optionB = rs.getString("option_B");
                String optionC = rs.getString("option_C");
                String optionD = rs.getString("option_D");
                String correct_option = rs.getString("correct_option");
                String subject = rs.getString("subject");

                pw.println("<div class='card question-card p-3'>");
                pw.println("<h5><b>Q" + id + ":</b> " + q + "</h5>");
                pw.println("<p><b>Subject:</b> " + subject + "</p>");
                pw.println("<ul class='list-group list-group-flush'>");
                pw.println("<li class='list-group-item'>A. " + optionA + "</li>");
                pw.println("<li class='list-group-item'>B. " + optionB + "</li>");
                pw.println("<li class='list-group-item'>C. " + optionC + "</li>");
                pw.println("<li class='list-group-item'>D. " + optionD + "</li>");
                pw.println("</ul>");
                pw.println("<p class='mt-2'><b> Correct Answer:</b> " + correct_option + "</p>");

                pw.println("<form action='deleteQuestion' method='post' class='mt-2'>");
                pw.println("<input type='hidden' name='questionId' value='" + id + "'/>");
                pw.println("<button type='submit' class='btn btn-danger btn-sm'> Delete Question</button>");
                pw.println("</form>");
                
                pw.println("<form action='editQuestionForm' method='post' class='mt-2'>");
                pw.println("<input type='hidden' name='questionId' value='" + id + "'/>");
                pw.println("<button type='submit' class='btn btn-success btn-sm'> Edit Question</button>");
                pw.println("</form>");

                pw.println("</div>");
            }

            if (!hasQuestions) {
                pw.println("<p class='text-center text-muted'>No questions found for the selected subject.</p>");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        pw.println("<div class='text-center mt-4'>");
        pw.println("<form action='admin' method='post'>");
        pw.println("<button type='submit' class='btn btn-secondary'> Back to Dashboard</button>");
        pw.println("</form>");
        pw.println("</div>");

        pw.println("</div>");
        pw.println("<script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js'></script>");
        pw.println("</body>");
        pw.println("</html>");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}
