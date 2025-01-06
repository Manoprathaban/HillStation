package com.idiot.servlet;

import com.idiot.util.CryptoUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.crypto.SecretKey;


@WebServlet("/UserSignUpServlet")
public class UserSignUpServlet extends HttpServlet {

    String encryptedUsername = System.getenv("ENCRYPTED_USERNAME");
    String encryptedPassword = System.getenv("ENCRYPTED_PASSWORD");
    String encodedKey = System.getenv("SECRET_KEY");
    String url = "jdbc:mysql://localhost:3306/railway_database";

    Connection con = null;
    PreparedStatement pstmt = null;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String errorMessage = (String) req.getAttribute("errorMessage");
        resp.setContentType("text/html");
        resp.getWriter().println("<h3>" + errorMessage + "</h3>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String title = req.getParameter("title");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String phoneNumber = req.getParameter("phoneNumber");
        String username = req.getParameter("username");
        String emailId = req.getParameter("emailId");
        String password = req.getParameter("password");

        String errorMessage = null;

        if (title == null || title.trim().isEmpty()) {
            errorMessage = "Title is required.";
        }
        if (firstName == null || firstName.trim().isEmpty()) {
            errorMessage = "First name is required.";
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            errorMessage = "Last name is required.";
        }
        if (emailId == null || emailId.trim().isEmpty()) {
            errorMessage = "Email ID is required.";
        }
        if (phoneNumber == null || phoneNumber.length() > 35) {
            errorMessage = "Phone number is required.";
        }
        if (username == null || username.trim().isEmpty()) {
            errorMessage = "Username is required.";
        }
        if (password == null || password.trim().isEmpty()) {
            errorMessage = "Password is required.";
        } else {
            try {
                SecretKey secretKey = CryptoUtil.getSecretKeyFromEncodedKey(encodedKey);
                String decryptedUsername = CryptoUtil.decrypt(encryptedUsername, secretKey);
                String decryptedPassword = CryptoUtil.decrypt(encryptedPassword, secretKey);

                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection(url, decryptedUsername, decryptedPassword);

                String query = "INSERT INTO HillUsers(title, firstName, lastName, emailId, phoneNumber, username, password) VALUES(?, ?, ?, ?, ?, ?, ?)";
                pstmt = con.prepareStatement(query);
                pstmt.setString(1, title);
                pstmt.setString(2, firstName);
                pstmt.setString(3, lastName);
                pstmt.setString(4, emailId);
                pstmt.setString(5, phoneNumber);
                pstmt.setString(6, username);
                pstmt.setString(7, password);
                int row = pstmt.executeUpdate();
                resp.setContentType("text/html");
                if (row > 0) {
                    errorMessage = "Registered successfully";
                } else {
                    errorMessage = "Failed to register user";
                }
            } catch (Exception e) {
                e.printStackTrace();
                errorMessage = "Internal server error. Please try again later.";
            } finally {
                try {
                    if (pstmt != null) pstmt.close();
                    if (con != null) con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        req.setAttribute("errorMessage", errorMessage);
        doGet(req, resp);
    }
}
