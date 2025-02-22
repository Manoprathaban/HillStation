package com.idiot.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.crypto.SecretKey;

import com.idiot.util.CryptoUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@WebServlet("/UserLoginServlet")

public class UserLoginServlet extends HttpServlet {
	
	String url="jdbc:mysql://localhost:3306/railway_database";
	String encryptedUsername=System.getenv("ENCRYPTED_USERNAME");
	String encryptedPassword=System.getenv("ENCRYPTED_PASSWORD");
	String encodedKey=System.getenv("SECRET_KEY");
	
	Connection con=null;
	PreparedStatement pstmt=null;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String errorMessage=(String) req.getAttribute("errorMessage");
		Boolean isValid=(Boolean) req.getAttribute("isValid");
		
		resp.setContentType("text/html");
		req.getRequestDispatcher("/UserLogin.html").forward(req, resp);		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String username=req.getParameter("username");
		String password=req.getParameter("password");
		String errorMessage=null;
		Boolean isValid=true;
		
		
		resp.setContentType("text/html");
		
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
		        SecretKey secretKey = CryptoUtil.getSecretKeyFromEncodedKey(encodedKey);
		        String decryptedUsername;
		        String decryptedPassword;

		        try {
		            decryptedUsername = CryptoUtil.decrypt(encryptedUsername, secretKey);
		            decryptedPassword = CryptoUtil.decrypt(encryptedPassword, secretKey);
		        } catch (Exception e) {
		            e.printStackTrace();
		            errorMessage = "Decryption error, please try again later.";
		            isValid = false;
		            req.setAttribute("errorMessage", errorMessage);
		            req.getRequestDispatcher("/UserLogin.html").forward(req, resp);
		            return;  
		        }

		        Class.forName("com.mysql.cj.jdbc.Driver");
		        con = DriverManager.getConnection(url, decryptedUsername, decryptedPassword);

		        String query = "SELECT * FROM HillUsers WHERE username=? AND password=?";
		        pstmt = con.prepareStatement(query);
		        pstmt.setString(1, username);
		        pstmt.setString(2, password);

		        ResultSet rs = pstmt.executeQuery();

		        if (rs.next()) {
		            HttpSession session = req.getSession();
		            session.setAttribute("title", rs.getString("title"));
		            session.setAttribute("firstName", rs.getString("firstName"));
		            session.setAttribute("lastName", rs.getString("lastName"));
		            session.setAttribute("emailId", rs.getString("emailId"));
		            session.setAttribute("username", username);
		            session.setAttribute("phoneNumber", rs.getString("phoneNumber"));

		            resp.sendRedirect("MainUserInterfaceServlet");
		        } else {
		            errorMessage = "Username or Password is invalid";
		            isValid = false;
		        }
		        con.close();
		    } catch (ClassNotFoundException | SQLException e) {
		        e.printStackTrace();
		        errorMessage = "Internal server error, please try again later.";
		    }

		    if (!isValid) {
		        resp.sendRedirect("UserLogin.html?errorMessage=" + java.net.URLEncoder.encode(errorMessage, "UTF-8"));
		        return;
		    }

		}
	}
}
