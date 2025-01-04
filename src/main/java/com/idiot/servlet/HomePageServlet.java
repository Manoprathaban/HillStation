package com.idiot.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/HomePageServlet")

public class HomePageServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 resp.setContentType("text/html");	
		 resp.getWriter().println("<!DOCTYPE html>");
		 resp.getWriter().println("<html>");
		 resp.getWriter().println("<head>");
		 resp.getWriter().println("<title>Hills Railways Reservation</title>");
		 resp.getWriter().println("<link rel='stylesheet' type='text/css' href='css/bootstrap.min.css'>");
		 resp.getWriter().println("<style>");
		 resp.getWriter().println("body{");
		 resp.getWriter().println("   background-image:url('images/HillRail.jpg');");
		 resp.getWriter().println("   background-size:cover;");
		 resp.getWriter().println("   background-repeat:no-repeat;");
		 resp.getWriter().println("   background-position-center");
		 resp.getWriter().println("}");
		 resp.getWriter().println("button{");
		 resp.getWriter().println("padding:12px 20px;");
		 resp.getWriter().println("background-color: #04AA6D;");
		 resp.getWriter().println("font-weight:bold;");
		 resp.getWriter().println("background-radius:5px;");
		 resp.getWriter().println("}");
		 resp.getWriter().println("a{");
		 resp.getWriter().println("text-decoration:none:");
		 resp.getWriter().println("}");
		 resp.getWriter().println("</style");
		 resp.getWriter().println("</head>");
		 resp.getWriter().println("<body class='bg-light'>");
		 resp.getWriter().println("<div class='container mt-5'>");
		 resp.getWriter().println("<h1 class='text-center'>Welcome to Hilly Railways</h1>");
		 resp.getWriter().println("<div class='row justify-content-center mt-4'>");
		 resp.getWriter().println("<div class='col-12 text-center mb-2'>");
		 resp.getWriter().println("<a href='UserSignUpServlet'  class='btn-primary'>Sign Up</a>");
		 resp.getWriter().println("</div>");
		 resp.getWriter().println("<div class='col-12 text-center mb-2'>");
		 resp.getWriter().println("<a href='UserLoginServlet' class='btn-primary'>Sign in</a>");
		 resp.getWriter().println("</div>");
		 resp.getWriter().println("</div>");	
		 resp.getWriter().println("</div>");
		 resp.getWriter().println("</body>");
		 resp.getWriter().println("</html>");
	}

}
