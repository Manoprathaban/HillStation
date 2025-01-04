package com.idiot.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/TicketBookingServlet")

public class TicketBookingServlet extends HttpServlet {
	
	String un="root";
	String pwd="Mano@2001";
	String url="jdbc:mysql://localhost:3306/railway_database";

	
	Connection con=null;
	PreparedStatement pstmt=null;
	ResultSet rs=null;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.setContentType("text/html");
		
		List<String> fromStation=getStations("from_station");
		List<String>toStation=getStations("to_station");
		String totalCost=req.getParameter("cost");
		
	       
		resp.getWriter().println("<!DOCTYPE Html>");
		resp.getWriter().println("<html>");
		resp.getWriter().println("<head>");
		resp.getWriter().println("<title>Ticket Booking</title>");
		resp.getWriter().println("<link rel='style-sheet' type='text/css' href='css/bootstrap.min.css'>");
		resp.getWriter().println("<style>");
		resp.getWriter().println("body{");
		resp.getWriter().println("background-image:url('images/loginHill.jpg');");
		resp.getWriter().println("}");
		resp.getWriter().println("<body>");
		resp.getWriter().println("</style>");
		resp.getWriter().println("<div class='form-container'>");
		resp.getWriter().println("<h2>Book Your Ticket</h2>");
		resp.getWriter().println("<form method='post' action='TicketBookingServlet'>");
		resp.getWriter().println("<div class='form-group'>");
		resp.getWriter().println("<label for='fromStation'>From Station</label>");
		resp.getWriter().println("<select id='fromStation' name='fromStation' required>");
		
		for(String station:fromStation) {
			resp.getWriter().println("<option value='" + station + "'>" + station +"</option>");
		}
		resp.getWriter().println("</select>");
		resp.getWriter().println("</div>");
		resp.getWriter().println("<div class='form-group'>");
		resp.getWriter().println("<label for='toStation'>To Station</label>");
		resp.getWriter().println("<select id='toStation' name='toStation' required>");
		
		for(String station:toStation) {
			resp.getWriter().println("<option value='" + station + "'>" + station +"</option>");
		}
		resp.getWriter().println("</select>");
		resp.getWriter().println("</div>");
        resp.getWriter().println("<div class='form-group'>");
        resp.getWriter().println("<label for='date'>Date:</label>");
		resp.getWriter().println("<input type='date' id='date' name='date' required>");
		resp.getWriter().println("</div>");
		resp.getWriter().println("<div class='form-group'>");
		resp.getWriter().println("<label for='class'>Class</label>");
		resp.getWriter().println("<select id='class' name='class'>");
		resp.getWriter().println("<option value='First Class'>First Class</option>");
		resp.getWriter().println("<option value='Second Class'>Second CLass</option>");
		resp.getWriter().println("<option value='Sleeper'>Sleeper</option>");
		resp.getWriter().println("</select>");
	    resp.getWriter().println("</div>");
	    resp.getWriter().println("<div class='form-group'>");
	    resp.getWriter().println("<label for='numberOfPassengers'>Number of Passengers:</label>");
	    resp.getWriter().println("<input type='number' id='numberOfPassengers' name='numberOfPassengers' required>");
	    resp.getWriter().println("</div>");
	    resp.getWriter().println("<div class='form-group'>");
	    resp.getWriter().println("<button type='submit'class='btn btn-primary' onClick='history.back()'>Back</button>");
	    resp.getWriter().println("<button type='submit' class='btn btn-primary'>Buy Ticket</button>");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		HttpSession session=req.getSession();
		String username=(String) session.getAttribute("username");
		String title=(String) session.getAttribute("title");
		String firstName=(String) session.getAttribute("firstName");
		String lastName=(String) session.getAttribute("lastName");
		String emailId=(String) session.getAttribute("emailId");
		String phoneNumber=(String) session.getAttribute("phoneNumber");
		
		String fromStation=req.getParameter("fromStation");
		String toStation=req.getParameter("toStation");
		String numberOfPassengerstr=req.getParameter("numberOfPassengers");
		String travelClass=req.getParameter("class");
		String dateStr=req.getParameter("date");
		
		
		int numberOfPassengers=0;
		int baseFare=0;
		double fareMultiplier=1.0;
		java.sql.Date date=null;
		
		try {
			if(dateStr!=null && !dateStr.trim().isEmpty()) {
				 SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
				 dateFormat.setLenient(false);
				 try {
					 java.util.Date parsedDate=dateFormat.parse(dateStr);
					 date=new java.sql.Date(parsedDate.getTime());
				 }catch(ParseException e) {
					 throw new ServletException("Invalid date format, please use YYYY-MM-DD.");
				 }
			}
			
			if(numberOfPassengerstr!=null && !numberOfPassengerstr.isEmpty()) {
				numberOfPassengers=Integer.parseInt(numberOfPassengerstr);
			}
			Class.forName("com.mysql.cj.jdbc.Driver");
			con=DriverManager.getConnection(url,un,pwd);
			pstmt=con.prepareStatement("SELECT cost FROM Fares WHERE from_station=? AND to_station=?");
			pstmt.setString(1, fromStation);
			pstmt.setString(2, toStation);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				baseFare=rs.getInt("cost");
			}
		    rs.close();
		    pstmt.close();
		
			switch(travelClass) {
			case "First Class":
				fareMultiplier=2.0;
				break;
			case "Second Class":
				fareMultiplier=1.5;
			    break;
			case "Sleeper":
				fareMultiplier=1.0;
				break;
		}
			int totalCost=(int) (baseFare*numberOfPassengers*fareMultiplier);
			
			pstmt=con.prepareStatement("INSERT INTO BOOKINGS(title,firstName,lastName,emailId,phoneNumber,fromStation,toStation,class,numberOfPassengers,date,total_cost) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
			pstmt.setString(1, title); 
			pstmt.setString(2, firstName); 
			pstmt.setString(3, lastName); 
			pstmt.setString(4, emailId); 
			pstmt.setString(5, phoneNumber); 
			pstmt.setString(6, fromStation); 
			pstmt.setString(7, toStation);
			pstmt.setString(8, travelClass);
			pstmt.setInt(9, numberOfPassengers); 
			pstmt.setDate(10,date);
			pstmt.setInt(11, totalCost);
			pstmt.executeUpdate();
				
			
			resp.setContentType("text/html");
		    resp.getWriter().println("<!DOCTYPE Html>");
		    resp.getWriter().println("<html>");
		    resp.getWriter().println("<head>");
		    resp.getWriter().println("<title>Confirm Ticket</title>");
		    resp.getWriter().println("<link rel='style-sheet' href=''css/bootstrap.min.css>");
		    resp.getWriter().println("</head>");
		    resp.getWriter().println("<body>");
		    resp.getWriter().println("h2{");
		    resp.getWriter().println("text-align:center;");
		    resp.getWriter().println("}");
		    resp.getWriter().println("<h2>Confirm Your Ticket</h2>");
		    resp.getWriter().println("<form method='post' action='TicketBookingServlet'>");
		    resp.getWriter().println("<input type='hidden' name='fromStation' value='" + fromStation + "'>");
		    resp.getWriter().println("<input type='hidden' name='toStation' value='" + toStation + "'>");
		    resp.getWriter().println("<input type='hidden' name='class' value='" + travelClass + "'>");
		    resp.getWriter().println("<input type='hidden' name='numberOfPassengers' value='" + numberOfPassengers + "'>");
		    resp.getWriter().println("<div>");
		    resp.getWriter().println("<p>From Station: " + fromStation + "</p>");
		    resp.getWriter().println("<p>To Station: " + toStation + "</p>");
		    resp.getWriter().println("<p>Class: " + travelClass + "</p>");
		    resp.getWriter().println("<p>Number of Passengers: " + numberOfPassengers + "</p>");
		    resp.getWriter().println("<p>Total Cost: " + totalCost + "</p>");
		    resp.getWriter().println("</div>");
		    resp.getWriter().println("<button type='submit'>Buy Ticket</button>");
		    resp.getWriter().println("</form>");
		    resp.getWriter().println("</body>");
		    resp.getWriter().println("</html>");
		}catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			resp.setContentType("text/html");
			resp.getWriter().println("<!DOCTYPE Html>");
			resp.getWriter().println("<html>");
			resp.getWriter().println("<head>");
			resp.getWriter().println("<title>Error</title>");
			resp.getWriter().println("</head>");
			resp.getWriter().println("<body>");
			resp.getWriter().println("<h2>Internal error, Please Try again later</h2>");
			resp.getWriter().println("</body>"); 
			resp.getWriter().println("</html>");
		}finally {
			try {
				if(rs!=null) {
					rs.close();
				}
				if(pstmt!=null) {
					rs.close();
				}
				if(con!=null) {
					rs.close();
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	private List<String> getStations(String column){
		List<String> stations=new ArrayList<>();
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con=DriverManager.getConnection(url,un,pwd);
			pstmt=con.prepareStatement("SELECT DISTINCT "+column+ " FROM fares");
			rs=pstmt.executeQuery();
			while(rs.next()) {
				stations.add(rs.getString(column));
			}
		}catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return stations;

}
}
