package com.aurionpro.QuizApplication.model.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/submit")
public class SubmitAnswerServlet extends HttpServlet{

	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.setContentType("text/html");

		PrintWriter pw = resp.getWriter();
		
		 HttpSession session = req.getSession(false);
		 
		 if (session == null || session.getAttribute("userId") == null) {
			 pw.println("<p style=color:red;>InValid Session</p>");

				RequestDispatcher rd = req.getRequestDispatcher("Login.html");
				rd.include(req, resp);
	            return;
	        }
		 
		 int index=Integer.parseInt(req.getParameter("index"));
		 String answer=req.getParameter("answer");
		 int questionId = Integer.parseInt(req.getParameter("questionId"));
		 
		 Map<Integer,String>answers=(Map<Integer, String>) session.getAttribute("answers");
		 
		 if(answers==null) {
			  answers = new LinkedHashMap<>();
			  session.setAttribute("answers", answers);
		 }
		 
		 answers.put(questionId, answer);
		 
		 int total=(int) session.getAttribute("totalQuestion");
		 if(index+1>=total) {
			 resp.sendRedirect("result");
		 }
		 
		 else {
			 resp.sendRedirect("quiz?index=" + (index + 1));
		 }
				 
		 
		 
	}
}
