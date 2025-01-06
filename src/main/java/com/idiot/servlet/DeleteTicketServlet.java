package com.idiot.servlet;

import java.io.BufferedReader;
import java.io.FileReader;
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

import javax.crypto.SecretKey;

import com.idiot.util.CryptoUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/DeleteTicketServlet")
public class DeleteTicketServlet extends HttpServlet {

    String encrypted_username=System.getenv("ENCRYPTED_USERNAME");
    String encrypted_password=System.getenv("ENCRYPTED_PASSWORD");
    String encodedKey=System.getenv("SECRET_KEY");
    String url = "jdbc:mysql://localhost:3306/railway_database";

    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");

        List<String> fromStation = getStations("from_station");
        List<String> toStation = getStations("to_station");

        String filePath = getServletContext().getRealPath("/DeleteTicket.html");
        StringBuilder htmlContent = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                htmlContent.append(line).append("\n");
            }
        }

        String htmlString = htmlContent.toString();
        htmlString = htmlString.replace("<!-- From Station dynamic content will be added here -->", generateOptions(fromStation))
                               .replace("<!-- To Station dynamic content will be added here -->", generateOptions(toStation));

        // Write the updated HTML content to the response
        resp.getWriter().println(htmlString);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	HttpSession session = req.getSession();

    	String fromStation = req.getParameter("fromStation");
    	String toStation = req.getParameter("toStation");
    	String emailId = req.getParameter("emailId");
    	String dateStr = req.getParameter("date");
    	String travelClass = req.getParameter("class");

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

    	    session.setAttribute("fromStation", fromStation);
    	    session.setAttribute("toStation", toStation);
    	    session.setAttribute("emailId", emailId);
    	    session.setAttribute("date", date);
    	    session.setAttribute("class", travelClass);

    	    SecretKey secretKey = CryptoUtil.getSecretKeyFromEncodedKey(encodedKey);
    	    String decryptedUsername;
    	    String decryptedPassword;

    	    try {
    	        decryptedUsername = CryptoUtil.decrypt(encrypted_username, secretKey);
    	        decryptedPassword = CryptoUtil.decrypt(encrypted_password, secretKey);
    	    } catch (Exception e) {
    	        e.printStackTrace();
    	        resp.setContentType("text/html");
    	        resp.getWriter().println("<h2>Decryption error, please try again later.</h2>");
    	        return; 
    	    }

    	    Class.forName("com.mysql.cj.jdbc.Driver");
    	    con = DriverManager.getConnection(url, decryptedUsername, decryptedPassword);

    
    	    String query = "DELETE FROM bookings WHERE emailId=? AND fromStation=? AND toStation=? AND date=?";
    	    pstmt = con.prepareStatement(query);
    	    pstmt.setString(1, emailId);
    	    pstmt.setString(2, fromStation);
    	    pstmt.setString(3, toStation);
    	    pstmt.setDate(4, date);

    	    int rowsAffected = pstmt.executeUpdate();

    	    resp.setContentType("text/html");
    	    resp.getWriter().println("<!DOCTYPE html>");
    	    resp.getWriter().println("<html>");
    	    resp.getWriter().println("<head>");
    	    resp.getWriter().println("<title>Delete Confirmation</title>");
    	    resp.getWriter().println("</head>");
    	    resp.getWriter().println("<body>");
    	    if (rowsAffected > 0) {
    	        resp.getWriter().println("<h2>Ticket deleted successfully.</h2>");
    	    } else {
    	        resp.getWriter().println("<h2>No matching details found.</h2>");
    	    }
    	    resp.getWriter().println("</body>");
    	    resp.getWriter().println("</html>");

    	} catch (Exception e) {
    	    e.printStackTrace();
    	    resp.getWriter().println("text/html");
    	    resp.getWriter().println("<h2>Internal Error, Please try again later.</h2>");
    	} finally {
    	    try {
    	        if (con != null) {
    	            con.close();
    	        }
    	        if (pstmt != null) {
    	            pstmt.close();
    	        }
    	        if (rs != null) {
    	            rs.close();
    	        }
    	    } catch (Exception e) {
    	        e.printStackTrace();
    	    }
    	}
    }

    private String generateOptions(List<String> stations) {
        StringBuilder options = new StringBuilder();
        for (String station : stations) {
            options.append("<option value='").append(station).append("'>").append(station).append("</option>\n");
        }
        return options.toString();
    }

    private List<String> getStations(String column) {
    	List<String> stations = new ArrayList<>();

    	try {
    	    SecretKey secretKey = CryptoUtil.getSecretKeyFromEncodedKey(encodedKey);
    	    String decryptedUsername;
    	    String decryptedPassword;

    	    try {
    	        decryptedUsername = CryptoUtil.decrypt(encrypted_username, secretKey);
    	        decryptedPassword = CryptoUtil.decrypt(encrypted_password, secretKey);
    	    } catch (Exception e) {
    	        e.printStackTrace();
    	        return stations; 
    	    }

    	    Class.forName("com.mysql.cj.jdbc.Driver");
    	    con = DriverManager.getConnection(url, decryptedUsername, decryptedPassword);
    	    String query = "SELECT DISTINCT " + column + " FROM fares";
    	    pstmt = con.prepareStatement(query);
    	    rs = pstmt.executeQuery();

    	    while (rs.next()) {
    	        stations.add(rs.getString(column));
    	    }
    	} catch (SQLException | ClassNotFoundException e) {
    	    e.printStackTrace();
    	} finally {
    	    try {
    	        if (con != null) {
    	            con.close();
    	        }
    	        if (pstmt != null) {
    	            pstmt.close();
    	        }
    	        if (rs != null) {
    	            rs.close();
    	        }
    	    } catch (SQLException e) {
    	        e.printStackTrace();
    	    }
    	}
    	return stations;
}
}
