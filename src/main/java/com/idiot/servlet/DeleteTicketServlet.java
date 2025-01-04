package com.idiot.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/DeleteTicketServlet")

public class DeleteTicketServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		 
		 resp.getWriter().println("<!DOCTYPE Html>");
		 resp.getWriter().println("<html>");
		 resp.getWriter().println("<head>");
		 resp.getWriter().println("<title>Delete Ticket</title>");
		 resp.getWriter().println("<body>");
		 resp.getWriter().println("<form>");
		 resp.getWriter().println("<div class='form-group'>");
		 resp.getWriter().println("<label for='selectTicket'>Select Ticket</label>");
		 resp.getWriter().println("<input type='text' id='selectTickt' name='selectTicket' required>");
		 resp.getWriter().println("</div>");
		 resp.getWriter().println("<div class='form-group'>");
		 resp.getWriter().println("<label for='fromStation'>From Station</label>");
		 resp.getWriter().println("<input type='text' id='fromStation' name='fromStation' required>");
		 resp.getWriter().println("</div>");
		 resp.getWriter().println("<div class='form-group'>");
		 resp.getWriter().println("<label for='toStation'>To Station</label>");
		 resp.getWriter().println("<input type='text' id='toStation' name='toStation' required>");
		 resp.getWriter().println("</div");
		 resp.getWriter().println("<div class='form-group'>");
		 resp.getWriter().println("<label for='date'>Date</label>");
		 resp.getWriter().println("<input type='date' id='date' required>");
		 resp.getWriter().println("</div>");
		 resp.getWriter().println("<div class='form-group'>");
		 resp.getWriter().println("<label for='class'>class</label>");
		 resp.getWriter().println("<select id='class' name='class'>");
		 resp.getWriter().println("<option value='firstClass'>First Class</option>");
		 resp.getWriter().println("<option value='secondClass'>Second Class</option>");
		 resp.getWriter().println("<option value='sleeper'>Sleeper</option>");
		 resp.getWriter().println("</select>");
		 resp.getWriter().println("</div>");
		 resp.getWriter().println("<div class='form-group'>");
		 resp.getWriter().println("<label for=cost>Cost</label>");
		 resp.getWriter().println("<input type='number' name='cost' id='cost'>");
		 resp.getWriter().println("</div>");
		 resp.getWriter().println("<button type='submit' class='btn btn-primary' onClick='history.back()'>Back</button>");
		 resp.getWriter().println("<button type='submit' class='btn btn-primary'>Delete Ticketet Ticket</button>");
		 resp.getWriter().println("<button type='submit' class='btm btn-primary' onClick='history.back()'>Clear</button>");
		 resp.getWriter().println("</form>");
		 resp.getWriter().println("</body>");
		 resp.getWriter().println("</head>");
		 resp.getWriter().println("</html>");
	}

}
