package com.idiot.servlet;

import java.io.IOException;
import java.sql.Connection;
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

@WebServlet("/BookingHistoryServlet")
public class BookingHistoryServlet extends HttpServlet {
	
	Connection con=null;
	PreparedStatement pstmt=null;
	ResultSet rs=null;
	
	String url="jdbc:mysql://localhost:3306/railway_database";
	
	String encryptedUsername=System.getenv("ENCRYPTED_USERNAME");
	String encryptedPassword=System.getenv("ENCRYPTED_PASSWORD");
	String encodedKey=System.getenv("SECRET_KEY");
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.setContentType("text/html"); 
		req.getRequestDispatcher("/UserBookingHistory.html").forward(req, resp);	
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.setContentType("text/html"); 
		
		String emailId=req.getParameter("emailId");
		
		
//		if(emailId==null || !emailId.trim().isEmpty()) {
//			resp.getWriter().println("<h2>Please enter the email id</h2>");
//			return;
//		}
		
		try {
			SecretKey secretKey=CryptoUtil.getSecretKeyFromEncodedKey(encodedKey);
		        String decryptedUsername=null;
		        String decryptedPassword=null;
		  try {
				decryptedUsername=CryptoUtil.decrypt(encryptedUsername, secretKey);
				decryptedPassword=CryptoUtil.decrypt(encryptedPassword,secretKey);
		  }catch(Exception e) {
			  e.printStackTrace();
			  resp.setContentType("text/html");
  	          resp.getWriter().println("<h2>Decryption error, please try again later.</h2>");
  	          return;
		  }
		  Class.forName("com.mysql.cj.jdbc.Driver");
		  con=DriverManager.getConnection(url,decryptedUsername,decryptedPassword);
		  String query="SELECT *FROM Bookings WHERE emailId=?";
		  pstmt=con.prepareStatement(query);
		  pstmt.setString(1,emailId);
		  rs=pstmt.executeQuery();
		  resp.getWriter().println("<h2>Booking for "+ emailId +":</h2>");
		  while(rs.next()) {
			  resp.getWriter().println("<p>Booking ID: " + rs.getInt("id") + "</p>"); 
			  resp.getWriter().println("<p>Booking Date: " + rs.getDate("date") + "</p>"); 
			  resp.getWriter().println("<p>From: " + rs.getString("fromStation") + "</p>"); 
			  resp.getWriter().println("<p>To: " + rs.getString("toStation") + "</p>");
			  resp.getWriter().println("<hr>");
			  }
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
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

}
