package com.idiot.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.crypto.SecretKey;

import com.idiot.util.CryptoUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/UpdateTicketServlet")

public class UpdateTicketServlet extends HttpServlet {
	
	String encryptedUsername=System.getenv("ENCRYPTED_USERNAME");
	String encryptedPassword=System.getenv("ENCTYPTED_PASSWORD");
	String encodedKey=System.getenv("SECRET_KEY");
	String url="jdbc:mysql://localhost:3306/railway_database";
	
	Connection con=null;
	PreparedStatement pstmt=null;
	PreparedStatement pstmt1=null;
	ResultSet rs=null;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.setContentType("text/html");
		
		String emailId=req.getParameter("emailId");
    	resp.setContentType("text/html");
    	
    	if(emailId==null || emailId.trim().isEmpty()) {
    		req.getRequestDispatcher("/UpdateTicket.html").forward(req,resp);
    		return;
    	}
    	
    	try {
    		SecretKey secretKey=CryptoUtil.getSecretKeyFromEncodedKey(encodedKey);
        	String decryptedUsername=CryptoUtil.decrypt(encryptedUsername, secretKey);
        	String decryptedPassword=CryptoUtil.decrypt(encryptedPassword,secretKey);
             
        	Class.forName("com.mysql.cj.jdbc.Driver");
        	con=DriverManager.getConnection(url,decryptedUsername,decryptedPassword);
        	String query="SELECT *FROM Bookings WHERE emailId=?";
        	pstmt=con.prepareStatement(query);
        	pstmt.setString(1,emailId);
        	rs=pstmt.executeQuery();
        	
        	 if(rs.next()) {
        	    resp.getWriter().println("<form method='post' action='BookingHistoryServlet'>"); 
        		resp.getWriter().println("<div>"); 
        		resp.getWriter().println("<label for='selectTicket'>Select Ticket</label>"); 
        		resp.getWriter().println("<input type='text' id='selectTicket' name='selectTicket' value='" + rs.getString("id") + "' readonly>");
        		resp.getWriter().println("</div>"); 
        		resp.getWriter().println("<div>");
        		resp.getWriter().println("<label for='fromStation'>From Station</label>"); 
        		resp.getWriter().println("<input type='text' id='fromStation' name='fromStation' value='" + rs.getString("fromStation") + "' readonly>"); 
        		resp.getWriter().println("</div>"); 
        		resp.getWriter().println("<div>"); 
        		resp.getWriter().println("<label for='toStation'>To Station</label>");
        		resp.getWriter().println("<input type='text' id='toStation' name='toStation' value='" + rs.getString("toStation") + "' readonly>"); 
        		resp.getWriter().println("</div>");
        		resp.getWriter().println("<div>"); 
        		resp.getWriter().println("<label for='date'>Date</label>"); 
        		resp.getWriter().println("<input type='date' id='date' name='date' value='" + rs.getString("travelDate") + "' required>");
        		resp.getWriter().println("</div>"); 
        		resp.getWriter().println("<div>");
        		resp.getWriter().println("<label for='class'>Class</label>"); 
        		resp.getWriter().println("<select id='class' name='class' required>"); 
        		resp.getWriter().println("<option value='firstClass'" + ("firstClass".equals(rs.getString("class")) ? " selected" : "") + ">First Class</option>"); 
        		resp.getWriter().println("<option value='secondClass'" + ("secondClass".equals(rs.getString("class")) ? " selected" : "") + ">Second Class</option>"); 
        		resp.getWriter().println("<option value='sleeper'" + ("sleeper".equals(rs.getString("class")) ? " selected" : "") + ">Sleeper</option>"); 
        		resp.getWriter().println("</select>"); 
        		resp.getWriter().println("</div>"); 
        		resp.getWriter().println("<button type='submit' class='btn btn-primary'>Update Ticket</button>"); 
        		resp.getWriter().println("</form>");
        		}
        	 else {
        		 resp.getWriter().println("<h2>No booking found for the given email ID.</h2>");
        	}
    	}catch(Exception e) {
    		e.printStackTrace();
    		resp.setContentType("text/html");
    		resp.getWriter().println("<h2>Error occured,Please try again later</h2>");
    	}
    	finally {
    		try {
    			if(con!=null) {
    				con.close();
    			}
    			if(pstmt!=null) {
    				pstmt.close();
    			}
    			if(rs!=null) {
    				rs.close();
    			}
    		}catch(Exception e) {
    			e.printStackTrace();
    		}
    	}
    

	}
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	    String dateStr = req.getParameter("date");
    	    String numberOfPassengerStr = req.getParameter("numberOfPassengers");
    	    String travelClass = req.getParameter("class");
    	    int numberOfPassengers = 0;
    	    int baseFare = 0;
    	    
    	    if (dateStr == null || dateStr.trim().isEmpty() || travelClass == null || travelClass.trim().isEmpty() || numberOfPassengerStr == null) {
    	        resp.getWriter().println("<h2>Please fill all the details</h2>");
    	        return;    
    	    }
    	    
    	    try {
    	        numberOfPassengers = Integer.parseInt(numberOfPassengerStr);
    	    } catch (NumberFormatException e) {
    	        resp.getWriter().println("<h2>Invalid number of passengers.</h2>");
    	        return;
    	    }

    	    Date date = Date.valueOf(dateStr);
    	    double fareMultiplier = 1.0;
    	    
    	    switch (travelClass) {
    	        case "firstClass":
    	            fareMultiplier = 2.0;
    	            break;
    	        case "secondClass":
    	            fareMultiplier = 1.5;
    	            break;
    	        case "sleeper":
    	            fareMultiplier = 1.0;
    	            break;
    	        default:
    	            resp.getWriter().println("<h2>Invalid class selection</h2>");
    	            return;
    	    }

    	    try {
    	        // Decrypt the username and password
    	        SecretKey secretKey = CryptoUtil.getSecretKeyFromEncodedKey(encodedKey);
    	        String decryptedUsername = CryptoUtil.decrypt(encryptedUsername, secretKey);
    	        String decryptedPassword = CryptoUtil.decrypt(encryptedPassword, secretKey);
    	        
    	        // Establish connection to the database
    	        Class.forName("com.mysql.cj.jdbc.Driver");
    	        con = DriverManager.getConnection(url, decryptedUsername, decryptedPassword);
    	        
    	        // Get the emailId from the form submission
    	        String emailId = req.getParameter("emailId");
    	        
    	        // Query to fetch the user's booking data
    	        String bookingQuery = "SELECT * FROM Bookings WHERE emailId = ?";
    	        pstmt = con.prepareStatement(bookingQuery);
    	        pstmt.setString(1, emailId);
    	        rs = pstmt.executeQuery();
    	        
    	        if (rs.next()) {
    	            // Fetch the booking details
    	            String fromStation = rs.getString("fromStation");
    	            String toStation = rs.getString("toStation");
    	            
    	            // Query the fares table to find the base fare using fromStation and toStation
    	            String fareQuery = "SELECT cost FROM fares WHERE from_station = ? AND to_station = ?";
    	            pstmt = con.prepareStatement(fareQuery);
    	            pstmt.setString(1, fromStation);
    	            pstmt.setString(2, toStation);
    	            rs = pstmt.executeQuery();
    	            
    	            if (rs.next()) {
    	                baseFare = rs.getInt("cost");
    	                
    	               
    	                int totalCost = (int) (baseFare * numberOfPassengers * fareMultiplier);
    	                
    	                // Update the booking with the new values
    	                String updateQuery = "UPDATE Bookings SET date = ?, class = ?, numberOfPassengers = ?, total_cost = ? WHERE emailId = ?";
    	                pstmt = con.prepareStatement(updateQuery);
    	                pstmt.setDate(1, date);
    	                pstmt.setString(2, travelClass);
    	                pstmt.setInt(3, numberOfPassengers);
    	                pstmt.setInt(4, totalCost);
    	                pstmt.setString(5, emailId);
    	                
    	                int rowsAffected = pstmt.executeUpdate();
    	                if (rowsAffected > 0) {
    	                    resp.getWriter().println("<h2>Booking updated successfully</h2>");
    	                    resp.getWriter().println("<p>Date: " + date + "</p>");
    	                    resp.getWriter().println("<p>Class: " + travelClass + "</p>");
    	                    resp.getWriter().println("<p>Cost: " + totalCost + "</p>");
    	                    resp.getWriter().println("<p>Number of Passengers: " + numberOfPassengers + "</p>");
    	                    resp.getWriter().println("<hr>");
    	                } else {
    	                    resp.getWriter().println("<h2>Booking update failed. Please check the details and try again.</h2>");
    	                }
    	            } else {
    	                resp.getWriter().println("<h2>No fare found for the given route.</h2>");
    	            }
    	        } else {
    	            resp.getWriter().println("<h2>No booking found for the given email ID.</h2>");
    	        }
    	    } catch (Exception e) {
    	        e.printStackTrace();
    	        resp.getWriter().println("<h2>Error occurred. Please try again later.</h2>");
    	    } finally {
    	        try {
    	            if (con != null) con.close();
    	            if (pstmt != null) pstmt.close();
    	            if (rs != null) rs.close();
    	        } catch (Exception e) {
    	            e.printStackTrace();
    	        }
    	    }
    	}
}