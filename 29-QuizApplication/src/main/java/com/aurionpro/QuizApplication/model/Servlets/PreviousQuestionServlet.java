package com.aurionpro.QuizApplication.model.Servlets;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/prev")
public class PreviousQuestionServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 int index=Integer.parseInt(req.getParameter("index"));
		 
		 resp.sendRedirect("quiz?index=" + (index - 1));
	}
}
