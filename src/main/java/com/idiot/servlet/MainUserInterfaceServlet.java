package com.idiot.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/MainUserInterfaceServlet")

public class MainUserInterfaceServlet extends HttpServlet {
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.setContentType("text/html");
		
		resp.getWriter().println("<!DOCTYPE Html>");
		resp.getWriter().println("<html>");
		resp.getWriter().println("<head>");
		resp.getWriter().println("<title>Main User Interface</title>");
		resp.getWriter().println("<link rel='stylesheet' type='text/css' href='css/bootstrap.min.css'>");
		resp.getWriter().println("<style>");
		resp.getWriter().println("body{");
		resp.getWriter().println("     background-color:#f0f0f0;");
		resp.getWriter().println("     background-image:url('images/mainframeuser.jpg');");
		resp.getWriter().println("     font-size:16px;");
		resp.getWriter().println("     margin:0;");
		resp.getWriter().println("     padding:20px;");
		resp.getWriter().println("     min-height:100vh;");
		resp.getWriter().println("     display:flex;");
		resp.getWriter().println("     align-items:center;");
		resp.getWriter().println("     justify-content:center;");
		resp.getWriter().println("}");
		resp.getWriter().println("h2{");
		resp.getWriter().println("font-family:'arial';");
		resp.getWriter().println("font-size:50px;");
		resp.getWriter().println("font-weight:bold;");
		resp.getWriter().println("margin:20px 0;");
		resp.getWriter().println("}");
		resp.getWriter().println("a{");
		resp.getWriter().println("color:#007bff");
		resp.getWriter().println("text-decoration:none;");
		resp.getWriter().println("font-weight:bold;");
		resp.getWriter().println("font-size:16px;");
		resp.getWriter().println("padding:10px 20px;");
		resp.getWriter().println("border-radius:4px;");
		resp.getWriter().println("margin-right:15px;");
		resp.getWriter().println("}");
		resp.getWriter().println("</style");
		resp.getWriter().println("<body class='bg-light'>");
		resp.getWriter().println("<div class='container mt-5'>");
		resp.getWriter().println("<h1 class='text-center'>Main User Interface</h1>");
		resp.getWriter().println("<div class='row justify-content-center mt-4'>");
		resp.getWriter().println("<div class='col-12 text-center mb-2'>");
		resp.getWriter().println("<a href='TicketBookingServlet' class='btn-primary'>Ticket Booking</a>");
		resp.getWriter().println("</div");
		resp.getWriter().println("<div class='col-12 text-center mb-2'>");
		resp.getWriter().println("<a href='UpdateTicketServlet' class='btn-primary'>Update Ticket</a>");
		resp.getWriter().println("</div>");
		resp.getWriter().println("<div class='col-12 text-center mb-2'>");
		resp.getWriter().println("<a href='DeleteTicketServlet' class='btn-primary'>Delete Ticket</a>");
		resp.getWriter().println("</div>");
		resp.getWriter().println("<div class='col-12 text-center mb-2'>");
		resp.getWriter().println("<a href='BookingHistoryServlet' class='btn-primary'>Booking History</a>");
		resp.getWriter().println("</div>");
		resp.getWriter().println("<button type='submit' class='btn-primary' onClick='history.back()'>Back</button>");
		resp.getWriter().println("</body>");
		resp.getWriter().println("</head>");
		resp.getWriter().println("</html>");
	
	}

}
