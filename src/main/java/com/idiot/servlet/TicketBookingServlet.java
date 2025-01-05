package com.idiot.servlet;

import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/TicketBookingServlet")
public class TicketBookingServlet extends HttpServlet {

    String un = "root";
    String pwd = "Mano@2001";
    String url = "jdbc:mysql://localhost:3306/railway_database";

    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");

        // Fetch station lists
        List<String> fromStation = getStations("from_station");
        List<String> toStation = getStations("to_station");

        // Load the HTML file
        String filePath = getServletContext().getRealPath("/UserTicketBooking.html");
        StringBuilder htmlContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                htmlContent.append(line).append("\n");
            }
        }

        // Inject dynamic content (station lists)
        String htmlString = htmlContent.toString();
        htmlString = htmlString.replace("<!-- Dynamic content will be added here -->", generateOptions(fromStation))
                               .replace("<!-- Dynamic content will be added here -->", generateOptions(toStation));

        // Write the updated HTML content to the response
        resp.getWriter().println(htmlString);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	    HttpSession session = req.getSession();

    	    // Fetching user details from session
    	    String username = (String) session.getAttribute("username");
    	    String title = (String) session.getAttribute("title");
    	    String firstName = (String) session.getAttribute("firstName");
    	    String lastName = (String) session.getAttribute("lastName");
    	    String emailId = (String) session.getAttribute("emailId");
    	    String phoneNumber = (String) session.getAttribute("phoneNumber");

    	    // Fetching booking details from the request
    	    String fromStation = req.getParameter("fromStation");
    	    String toStation = req.getParameter("toStation");
    	    String numberOfPassengersStr = req.getParameter("numberOfPassengers");
    	    String travelClass = req.getParameter("class");
    	    String dateStr = req.getParameter("date");

    	    int numberOfPassengers = Integer.parseInt(numberOfPassengersStr);
    	    int baseFare = 0;
    	    double fareMultiplier = 1.0;
    	    java.sql.Date date = null;

    	    try {
    	        if (dateStr != null && !dateStr.trim().isEmpty()) {
    	            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	            dateFormat.setLenient(false);
    	            try {
    	                java.util.Date parsedDate = dateFormat.parse(dateStr);
    	                date = new java.sql.Date(parsedDate.getTime());
    	            } catch (ParseException e) {
    	                throw new ServletException("Invalid date format, please use YYYY-MM-DD.");
    	            }
    	        }

    	        // Setting session attributes for the booking details
    	        session.setAttribute("fromStation", fromStation);
    	        session.setAttribute("toStation", toStation);
    	        session.setAttribute("numberOfPassengers", numberOfPassengersStr);
    	        session.setAttribute("class", travelClass);
    	        session.setAttribute("date", dateStr);

    	        Class.forName("com.mysql.cj.jdbc.Driver");
    	        con = DriverManager.getConnection(url, un, pwd);

    	        // Fetching base fare from the database
    	        pstmt = con.prepareStatement("SELECT cost FROM Fares WHERE from_station=? AND to_station=?");
    	        pstmt.setString(1, fromStation);
    	        pstmt.setString(2, toStation);
    	        rs = pstmt.executeQuery();
    	        
    	        if (rs.next()) {
    	            baseFare = rs.getInt("cost");
    	            System.out.println("Base Fare: " + baseFare); // Debug log
    	        } else {
    	            System.out.println("No matching fare found for the stations.");
    	            resp.getWriter().println("<h3>Fare not found for the selected route. Please try again.</h3>");
    	            return;
    	        }

    	        // Applying multiplier based on class
    	        switch (travelClass) {
    	            case "First Class":
    	                fareMultiplier = 2.0;
    	                break;
    	            case "Second Class":
    	                fareMultiplier = 1.5;
    	                break;
    	            case "Sleeper":
    	                fareMultiplier = 1.0;
    	                break;
    	        }
    	        System.out.println("Travel Class: " + travelClass);
    	        System.out.println("Fare Multiplier: " + fareMultiplier); // Debug log

    	        // Calculating total cost
    	        int totalCost = (int) (baseFare * numberOfPassengers * fareMultiplier);
    	        System.out.println("Total Cost (before rounding): " + totalCost); // Debug log

    	        // Storing the calculated total cost in the session
    	        session.setAttribute("totalCost", totalCost);

    	        // Inserting ticket booking details into the database
    	        String insertQuery = "INSERT INTO Bookings(title, firstName, lastName, emailId, phoneNumber, fromStation, toStation, class, numberOfPassengers, total_cost, date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    	        pstmt = con.prepareStatement(insertQuery);
    	        pstmt.setString(1, title);
    	        pstmt.setString(2, firstName);
    	        pstmt.setString(3, lastName);
    	        pstmt.setString(4, emailId);
    	        pstmt.setString(5, phoneNumber);
    	        pstmt.setString(6, fromStation);
    	        pstmt.setString(7, toStation);
    	        pstmt.setString(8, travelClass);
    	        pstmt.setInt(9, numberOfPassengers);
    	        pstmt.setInt(10, totalCost);
    	        pstmt.setDate(11, date);
    	        pstmt.executeUpdate();

    	        // Displaying confirmation page
    	        resp.setContentType("text/html");
    	        resp.getWriter().println("<!DOCTYPE html>");
    	        resp.getWriter().println("<html>");
    	        resp.getWriter().println("<head>");
    	        resp.getWriter().println("<title>Booking Confirmation</title>");
    	        resp.getWriter().println("</head>");
    	        resp.getWriter().println("<body>");
    	        resp.getWriter().println("<h2>Booking Confirmation</h2>");
    	        resp.getWriter().println("<p>From Station: " + fromStation + "</p>");
    	        resp.getWriter().println("<p>To Station: " + toStation + "</p>");
    	        resp.getWriter().println("<p>Travel Date: " + dateStr + "</p>");
    	        resp.getWriter().println("<p>Class: " + travelClass + "</p>");
    	        resp.getWriter().println("<p>Number of Passengers: " + numberOfPassengers + "</p>");
    	        resp.getWriter().println("<p>Total Cost: Rs. " + totalCost + "</p>");
    	        resp.getWriter().println("</body>");
    	        resp.getWriter().println("</html>");
    	        
    	    } catch (SQLException | ClassNotFoundException e) {
    	        e.printStackTrace();
    	        resp.setContentType("text/html");
    	        resp.getWriter().println("<h2>Internal error, Please Try again later</h2>");
    	    } finally {
    	        try {
    	            if (rs != null) {
    	                rs.close();
    	            }
    	            if (pstmt != null) {
    	                pstmt.close();
    	            }
    	            if (con != null) {
    	                con.close();
    	            }
    	        } catch (SQLException e) {
    	            e.printStackTrace();
    	        }
    	    }
    	}

    private List<String> getStations(String column) {
        List<String> stations = new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, un, pwd);
            pstmt = con.prepareStatement("SELECT DISTINCT " + column + " FROM fares");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                stations.add(rs.getString(column));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return stations;
    }

    private String generateOptions(List<String> stations) {
        StringBuilder options = new StringBuilder();
        for (String station : stations) {
            options.append("<option value='").append(station).append("'>").append(station).append("</option>\n");
        }
        return options.toString();
    }
}
