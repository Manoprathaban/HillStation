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
				
		resp.setContentType("text/html");
		resp.getWriter().println("<!DOCTYPE html>");
		resp.getWriter().println("<html>");
		resp.getWriter().println("<head>");
		resp.getWriter().println("<title>Sign Up Page</title>");
		resp.getWriter().println("<link rel='style-sheet' href='css/bootstrap.min.css'>");
		resp.getWriter().println("<style>");
		resp.getWriter().println("body{");
		resp.getWriter().println("    display:grid;");
		resp.getWriter().println("    place-items:center;");
		resp.getWriter().println("    min-height:100vh;");
		resp.getWriter().println("    background-image:url('images/HillSign.jpg');");
		resp.getWriter().println("    background-size:cover;");
		resp.getWriter().println("    background-repeat:no-repeat;");
		resp.getWriter().println("    background-position:center;");
		resp.getWriter().println("    color:white;");
		resp.getWriter().println("}");
		resp.getWriter().println(".form-container{");
		resp.getWriter().println("  background:rgba(0,0,0,0.8);");
		resp.getWriter().println("  padding:50px;");
		resp.getWriter().println("  border-radius:10px");
		resp.getWriter().println("  max-width:500px");
		resp.getWriter().println("  margin-auto;");
		resp.getWriter().println("  width:400px;");
		resp.getWriter().println("}");
		resp.getWriter().println("h2{");
		resp.getWriter().println("align:center;");
		resp.getWriter().println("display:block");
		resp.getWriter().println("text-align:center");
		resp.getWriter().println("font-size:1.5rem;");
		resp.getWriter().println("text-align:justify;");
		resp.getWriter().println("margin-bottom:1.5rem;");
		resp.getWriter().println("}");
		resp.getWriter().println("label{");
		resp.getWriter().println("display:block;");
		resp.getWriter().println("width:100%;");
		resp.getWriter().println("margin-bottom:10px;");
		resp.getWriter().println("}");
		resp.getWriter().println("input{");
		resp.getWriter().println("display:block;");
		resp.getWriter().println("width:100%;");
		resp.getWriter().println("padding:10px;");
		resp.getWriter().println("}");
		resp.getWriter().println("button{");
		resp.getWriter().println("padding:12px 20px");
		resp.getWriter().println("font-size:16px;");
		resp.getWriter().println("border-radius:5px;");
		resp.getWriter().println("}");
		resp.getWriter().println("</style>");
		resp.getWriter().println("</head>");
		resp.getWriter().println("<body>");
		resp.getWriter().println("<div class='form-container mt-5'>");
		resp.getWriter().println("<h2 class='form-center'>SignUp</h2>");
		resp.getWriter().println("<form method='post' action='UserSignUpServlet'>");
		resp.getWriter().println("<div class='form-group'>");
		resp.getWriter().println("<label for='title'>Title</label>");
		resp.getWriter().println("<select class='form-control' id='title' name='title'>");
		resp.getWriter().println("<option>Mr.</option>");
		resp.getWriter().println("<option>Dr.</option>");
		resp.getWriter().println("<option>Mrs.</option>");
		resp.getWriter().println("</select>");
		resp.getWriter().println("</div>");
		resp.getWriter().println("<div class='form-group'>");
		resp.getWriter().println("<label for='firstName'>FirstName</label>");
		resp.getWriter().println("<input type='text' class='form-control' id='firstName' name='firstName'placeholder='Enter first name'>");
		resp.getWriter().println("</div>");
		resp.getWriter().println("<div class='form-group'>");
		resp.getWriter().println("<label for='lastname'>LastName</label>");
		resp.getWriter().println("<input type='text' class='form-control' id='lastName' name='lastName' placeholder='Enter last name'>");
		resp.getWriter().println("</div>");
		resp.getWriter().println("<div class='form-group'>");
		resp.getWriter().println("<label for='phoneNumber'>Phone number</label>");
		resp.getWriter().println("<input type='tel' class='form-control' id='phoneNumber' name='phoneNumber' placeholder='Enter the phone number'>");
		resp.getWriter().println("</div>");
		resp.getWriter().println("<div class='form-group'>");
		resp.getWriter().println("<label for='username'>UserName</label>");
		resp.getWriter().println("<input type='text' class='form-control' id='username' name='username' placeholder='Enter the username'>");
		resp.getWriter().println("</div>");
		resp.getWriter().println("<div class='form-group'>");
		resp.getWriter().println("<label for='emailId'>Email Id</label>");
		resp.getWriter().println("<input type='email' class='form-control' id='emailId' name='emailId' placeholder='Enter the emailID'>");
		resp.getWriter().println("</div>");
		resp.getWriter().println("<div class='form-group'>");
		resp.getWriter().println("<label for='password'>Password</label>");
		resp.getWriter().println("<input type='password' class='form-control' id='password' name='password' placeholder='Enter the password'>");
		resp.getWriter().println("</div>");
		resp.getWriter().println("<button type='submit' class='btn btn-primary' btn-block>Sign Up</button>");
		resp.getWriter().println("<button type='button' class='btn btn-secondary' btn-block' onClick='history.back()'>back</button>");
		resp.getWriter().println("</form>");
		if(errorMessage!=null) {
			resp.getWriter().println("<div id='message'>" +errorMessage+"</div>");
		}
		resp.getWriter().println("</div>");
		resp.getWriter().println("</body>");
		resp.getWriter().println("</html>");
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

	
	

