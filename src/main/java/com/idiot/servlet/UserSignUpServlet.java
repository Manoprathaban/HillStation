package com.idiot.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/UserSignUpServlet")

public class UserSignUpServlet extends HttpServlet {
	
	 String url="jdbc:mysql://localhost:3306/railway_database";
	 String un="root";
	 String pwd="Mano@2001";
	 
	 Connection con=null;
	 PreparedStatement pstmt=null;
	

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String errorMessage=(String) req.getAttribute("errorMessage");
}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String title=req.getParameter("title");
		String firstName=req.getParameter("firstName");
		String lastName=req.getParameter("lastName");
		String phoneNumber=req.getParameter("phoneNumber");
		String username=req.getParameter("username");
		String emailId=req.getParameter("emailId");
		String password=req.getParameter("password");
		
		
		
		String errorMessage=null;
		
		if (title == null || title.trim().isEmpty()) {
	        errorMessage="title is required.";
	    }
		if (firstName == null || firstName.trim().isEmpty()) {
	        errorMessage="firstName is required.";
	    }
		if (lastName == null || lastName.trim().isEmpty()) {
	        errorMessage="lastName is required.";
	    }
		if (emailId == null || emailId.trim().isEmpty()) {
	        errorMessage="emailId is required.";
	    }
		if (phoneNumber == null || phoneNumber.length()>35) {
	        errorMessage="phoneNumber is required.";
	    }
		if (username == null || username.trim().isEmpty()) {
	        errorMessage="Username is required.";
	    }
	    if (password == null || password.trim().isEmpty()) {
	        errorMessage="Password is required.";
	    }
	    else {
			try {
			  Class.forName("com.mysql.cj.jdbc.Driver");
			  con=DriverManager.getConnection(url,un,pwd);
			  String query="INSERT INTO HillUsers(title,firstName,lastName,emailId,phoneNumber,username,password) VALUES(?,?,?,?,?,?,?)";
			  PreparedStatement pstmt=con.prepareStatement(query);
			    pstmt.setString(1,title);
			    pstmt.setString(2,firstName);
				pstmt.setString(3,lastName);
				pstmt.setString(4,phoneNumber);
				pstmt.setString(5,username);
				pstmt.setString(6,emailId);
				pstmt.setString(7,password);
				int row=pstmt.executeUpdate();
				resp.setContentType("text/html");
				if(row>0) {
					errorMessage="Registered succesfully";
				}
				else {
					errorMessage="Failed to register users";
				}
		}catch(SQLException | ClassNotFoundException e) {
		    e.printStackTrace();
		    errorMessage="Internal Server error. Please try again later";
	    }
	    }
		req.setAttribute("errorMessage",errorMessage);
		doGet(req,resp);
		}
	}

	
	

