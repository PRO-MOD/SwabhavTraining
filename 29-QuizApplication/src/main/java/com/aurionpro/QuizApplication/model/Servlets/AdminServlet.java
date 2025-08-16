package com.aurionpro.QuizApplication.model.Servlets;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter pw = resp.getWriter();

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("adminUser") == null) {
            pw.println("<p style='color:red;'>Invalid Session</p>");
            RequestDispatcher rd = req.getRequestDispatcher("Login.html");
            rd.include(req, resp);
            return;
        }

        pw.println("<!DOCTYPE html>");
        pw.println("<html lang='en'>");
        pw.println("<head>");
        pw.println("<meta charset='UTF-8'>");
        pw.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        pw.println("<title>Admin Dashboard</title>");
        pw.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>");
        pw.println("<style>");
        pw.println("body { background-color: #f8f9fa; }");
        pw.println(".dashboard { max-width: 600px; margin: 50px auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }");
        pw.println(".btn-custom { width: 100%; margin-bottom: 10px; }");
        pw.println("</style>");
        pw.println("</head>");
        pw.println("<body>");

        pw.println("<div class='container'>");
        pw.println("<div class='dashboard text-center'>");
        pw.println("<h2 class='mb-4'>Admin Dashboard</h2>");
        pw.println("<p class='lead'>Welcome, <strong>" + session.getAttribute("adminUser") + "</strong></p>");

        pw.println("<form action='AddQuestion.html' method='post'>");
        pw.println("<button type='submit' class='btn btn-primary btn-custom'>Add Question</button>");
        pw.println("</form>");

        pw.println("<form action='viewQuestions' method='post'>");
        pw.println("<button type='submit' class='btn btn-success btn-custom'>View Questions</button>");
        pw.println("</form>");

        pw.println("<form action='viewResponse' method='post'>");
        pw.println("<button type='submit' class='btn btn-warning btn-custom'>View Responses</button>");
        pw.println("</form>");

        pw.println("<a href='logout' class='btn btn-danger btn-custom'>Logout</a>");

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
