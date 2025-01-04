package com.idiot.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@WebServlet("/UserLoginServlet")

public class UserLoginServlet extends HttpServlet {
	
	String url="jdbc:mysql://localhost:3306/railway_database";
	String un="root";
	String pwd="Mano@2001";
	
	Connection con=null;
	PreparedStatement pstmt=null;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String errorMessage=(String) req.getAttribute("errorMessage");
		Boolean isValid=(Boolean) req.getAttribute("isValid");
		
		resp.setContentType("text/html");
		resp.getWriter().println("<!DOCTYPE Html>");
		resp.getWriter().println("<html>");
		resp.getWriter().println("<head>");
		resp.getWriter().println("<title>Login Page</title>");
		resp.getWriter().println("<link rel='style-sheet' href='css/bootstrap.min.css'>");
		resp.getWriter().println("<style>");
		resp.getWriter().println("body{");
		resp.getWriter().println("       font-family:Arial, sans-serif;");
		resp.getWriter().println("       background:linerar-gradient(to right, #ff7e5f, #feb47b);");
		resp.getWriter().println("       height:100vh;");
		resp.getWriter().println("       margin:0;");
		resp.getWriter().println("       display:flex;");
		resp.getWriter().println("       justify-content:center;");
		resp.getWriter().println("       align-items:center;");
		resp.getWriter().println("    background-image:url('images/loginHill.jpg');");
		resp.getWriter().println("    background-size:cover;");
		resp.getWriter().println("    background-repeat:no-repeat;");
		resp.getWriter().println("    background-position:center;");
		resp.getWriter().println("    color:black;");
		resp.getWriter().println("}");
		resp.getWriter().println(".form-container{");
		resp.getWriter().println("width:300px");
		resp.getWriter().println("background-color:rgba(255,255,255,0.8);");
		resp.getWriter().println("border:1px solid #ccc;");
		resp.getWriter().println("border-radius:30px;");
		resp.getWriter().println("padding:30px;");
		resp.getWriter().println("}");
		resp.getWriter().println("h2{");
		resp.getWriter().println("  text-align:center;");
		resp.getWriter().println("  background-color:white;");
		resp.getWriter().println("}");
		resp.getWriter().println("label{");
		resp.getWriter().println(" display:block;");
		resp.getWriter().println(" margin-bottom:5px;");
		resp.getWriter().println("}");
		resp.getWriter().println("label{");
		resp.getWriter().println(" display:block;");
		resp.getWriter().println(" margin-bottom:5px;");
		resp.getWriter().println("color:black");
		resp.getWriter().println("padding:5px 0;");
		resp.getWriter().println("margin-bottom:8px;x");
		resp.getWriter().println("}");
		resp.getWriter().println("input {");
		resp.getWriter().println(" widhth:100%;");
		resp.getWriter().println(" padding:10px;");
		resp.getWriter().println(" margin-top:10px;");
		resp.getWriter().println("}");
		resp.getWriter().println("button {");
		resp.getWriter().println("padding:12px,20px;");
		resp.getWriter().println("font-size:16px;");
		resp.getWriter().println(" width:100%");
		resp.getWriter().println(" padding:10px;");
		resp.getWriter().println(" margin-top:10px;");
		resp.getWriter().println("}");
		resp.getWriter().println(".error, .success {");
        resp.getWriter().println("    font-weight: bold;");
        resp.getWriter().println("    margin-top: 15px;");
        resp.getWriter().println("    padding: 10px;");
        resp.getWriter().println("    border-radius: 5px;");
        resp.getWriter().println("    text-align: center;");
        resp.getWriter().println("    width: 100%;");
        resp.getWriter().println("}");
        resp.getWriter().println(".error {");
        resp.getWriter().println("    color: black;");
        resp.getWriter().println("    background-color: green;");
        resp.getWriter().println("}");
        resp.getWriter().println(".success {");
        resp.getWriter().println("    color: black;");
        resp.getWriter().println("    background-color: black;");
        resp.getWriter().println("}");
		resp.getWriter().println("</style>");
		resp.getWriter().println("</head>");
		resp.getWriter().println("<div class='form-container mt-5'>");
		resp.getWriter().println("<h2 class='form-center'>Login</h2>");
		resp.getWriter().println("<form method='post' action='UserLoginServlet'>");
		resp.getWriter().println("<div class='form-group'>");
		resp.getWriter().println("<label for='username'>UserName</label>");
		resp.getWriter().println("<input type='text' class='form-control' id='username' name='username'placeholder='Enter the username'>");
		resp.getWriter().println("</div>");
		resp.getWriter().println("<div class='form-group'>");
		resp.getWriter().println("<label for='password'>Password</label>");
		resp.getWriter().println("<input type='text' class='form-control' id=password' name='password' placeholder='Enter the password'>");
		resp.getWriter().println("</div>");
		resp.getWriter().println("<button type='submit' class='btn btn-primary' btn-block>Login</button>");
		resp.getWriter().println("<button type='button' class='btn btn-secondary' btn-block' onClick='history.back()'>back</button>");
		resp.getWriter().println("</form>");
		if(errorMessage!=null) {
			String messageClass=isValid?"success":"error";
			resp.getWriter().println("<div id='message'>"+errorMessage+"</div>");
		}
		resp.getWriter().println("</div>");
		resp.getWriter().println("</body>");
		resp.getWriter().println("</html>");
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String username=req.getParameter("username");
		String password=req.getParameter("password");
		String errorMessage=null;
		Boolean isValid=true;
		
		
		resp.setContentType("/text/html");
		
		if(username==null || username.trim().isEmpty()) {	
			errorMessage="username is required";
			isValid=false;
		}
		else if(password==null || password.trim().isEmpty()) {
			errorMessage="password is required";
			isValid=false;
		}
		else {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con=DriverManager.getConnection(url,un,pwd);
			
			String query="SELECT *FROM HillUsers WHERE username=? AND password=?";
			pstmt=con.prepareStatement(query);
			pstmt.setString(1,username);
			pstmt.setString(2, password);
			
			ResultSet rs=pstmt.executeQuery();
			
			if(rs.next()) {
				 errorMessage="Succesfully Logged in";
				 HttpSession session=req.getSession();
				 session.setAttribute("title", rs.getString("title"));
				 session.setAttribute("firstName", rs.getString("firstName"));
				 session.setAttribute("lastName", rs.getString("lastName"));
				 session.setAttribute("emailId",rs.getString("emailId"));
				 session.setAttribute("username",username);
				 session.setAttribute("phoneNumber", rs.getString("phoneNumber"));
				 
				 resp.sendRedirect("MainUserInterfaceServlet");
			}
			else {
				 errorMessage="Username or Password is invalid";
			}
		    con.close();
		}catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			errorMessage="Internal server error, Please try again later";
		}
		}
		req.setAttribute("errorMessage", errorMessage);
		req.setAttribute("isValid", isValid);
		doGet(req,resp);

	}

		}