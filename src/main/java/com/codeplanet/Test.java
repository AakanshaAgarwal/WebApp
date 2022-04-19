package com.codeplanet;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.SplittableRandom;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class Test {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	

	@GetMapping("/signup")
	public String signup(HttpServletRequest req) {
		String email= req.getParameter("email");
		String password = req.getParameter("psw");
		System.out.print("signupMethod is working" + email + "password is " + password);
		return "First";
	}

	@PostMapping("/signup")
	public String signup1(HttpServletRequest req) throws SQLException, ClassNotFoundException {
		String email = req.getParameter("email");
		String psw = req.getParameter("psw");
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = jdbcTemplate.getDataSource().getConnection();

		String query1 = "select * from signup where email=?,?,?,?";
		PreparedStatement stmt = con.prepareStatement(query1);
		stmt.setString(1, email);

		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			req.setAttribute("test", "you are already signedup");
		} else {
			String otp = "";
			otp = generateOtp(5);
			System.out.print("your otp is" + otp);
			String query2 = "insert into signup (email,password,otp) values(?,?,?)";

			PreparedStatement stmt1 = con.prepareStatement(query2);
			stmt1.setString(1, email);
			stmt1.setString(2, psw);
			stmt1.setString(3, otp);

			// String query2 = "insert into signup (email,password,otp) values('" + email +
			// "','" + psw + "','" + otp
			// + "')";
			// String query = "Select * from city where id=250";
			// ResultSet rs = stmt.executeQuery(query);
			int row = stmt1.executeUpdate();
			if (row >= 1) {
				sendMail(email, "Your OTP for our portal is" + otp, "OTP for verification");
				req.setAttribute("email", email);
			}
		}
		// String query ="select * from city where id=445";
		// String name = "";
		// while (rs.next()) {
		// name = rs.getString("name");
		// }
		// req.setAttribute("test", name);
		return "SignupSuccess";
	}

	private static void sendMail(String emailTo, String body, String subject) {
		Properties p  = new Properties();
		p.put("mail.smtp.host", "smtp.gmail.com");
		p.put("mail.smtp.port", "465");
		p.put("mail.smtp.ssl.enable", "true");
		p.put("mail.smtp.auth", "true");
		MailAuthenticator m = new MailAuthenticator("akshuag02@gmail.com", "9694568946");
		Session session = Session.getInstance(p, m);
		session.setDebug(true);

		MimeMessage msg = new MimeMessage(session);

		try {
			msg.setFrom("aakanshaagarwal25@gmail.com");

			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));

			msg.setSubject(subject);

			msg.setText(body);

			Transport.send(msg);

		} catch (MessagingException e) {

			e.printStackTrace();
		}
	}

	public String generateOtp(int size) {
		StringBuilder sb = new StringBuilder();
		SplittableRandom sp = new SplittableRandom();
		for (int i = 0; i < size; i++) {
			int rn = sp.nextInt(0, 9);
			sb.append(rn);
		}
		return sb.toString();
	}

	@GetMapping("/login")
	public String login(HttpServletRequest req) {
		return "login";
	}

	@PostMapping("/signin")
	public String signin(HttpServletRequest req) throws SQLException, ClassNotFoundException {
		String email = req.getParameter("email");
		System.out.println(email);
		String psw = req.getParameter("psw");
		Connection con = jdbcTemplate.getDataSource().getConnection();
		CallableStatement stmt=con.prepareCall("call signin(?)");
		stmt.setString(1,email);
	   /* Statement stmt = con.createStatement();
		String query1 = "select * from signup where email='" + email + "'";*/
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			if (rs.getInt("is_verify") == 0) {
				req.setAttribute("test", "You are not verified");
				return "First";
			}
			if ((rs.getString("password")).equals(psw)) {
				String query2="select * from links where created_by='"+email+ "'";
				ResultSet rs1 = stmt.executeQuery(query2);
				ArrayList<Map<String, String>> l=new ArrayList<Map<String, String>>();
				while(rs1.next()) {
					Map<String, String> s=new HashMap<String,String>();
					s.put("longurl", rs1.getString("long_link"));
					s.put("shorturl", rs1.getString("short_link"));
					l.add(s);
				}
				req.setAttribute("list", l);
				return "AfterSignin";
			} else {
				req.setAttribute("test", "Your password is not correct");
			}

		} else {
			req.setAttribute("test", "You are not signedup");
		}

		return "First";
	}

	@PostMapping("/otpVerification")
	public String verification(HttpServletRequest req) throws SQLException, ClassNotFoundException {
		String email = req.getParameter("email");
		String otp = req.getParameter("otp");
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/world", "root", "#aakansha25@");
		Statement stmt = con.createStatement();
		String query1 = "select otp from signup where email='" + email + "' ";
		ResultSet rs = stmt.executeQuery(query1);
		if (rs.next()) {
			if (rs.getString("otp").equals(otp)) {
				String query2 = "update signup set is_verify=1 where email='" + email + "' ";
				stmt.executeUpdate(query2);
				req.setAttribute("test", "Your account is verified: ");
			} else {
				req.setAttribute("test", "Your otp is incorrect: ");
			}
		} else {
			req.setAttribute("test", "Your otp is not generated: ");
		}
		return "First";
	}


@GetMapping("/urlshortner")
public String UrlShortner(HttpServletRequest req) throws ClassNotFoundException, SQLException {
	String link = req.getParameter("link");
	String customurl=req.getParameter("customurl");
	if(customurl!=null && !customurl.isEmpty()) {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/world", "root", "#aakansha25@");
		Statement stmt = con.createStatement();
		String query1="select * from links where short_link='"+ customurl +"'";
		ResultSet rs = stmt.executeQuery(query1);
		if (rs.next()) {
			req.setAttribute("error", "custom url already exist");
			
		}else {
			String query2 = "insert into links (long_link,short_link) values(?,?)";
            PreparedStatement stmt1 = con.prepareStatement(query2);
			stmt1.setString(1, link);
			stmt1.setString(2, customurl);
			stmt1.executeUpdate();
			req.setAttribute("url", "Your new url is nano.cc/"+ customurl);
			
			
		}
	}else {
		
	}

	return "home";
  }

@GetMapping("/{url}")
public String handleShortUrl(@PathVariable String url) throws SQLException, ClassNotFoundException{
	Class.forName("com.mysql.jdbc.Driver");
	Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/world", "root", "#aakansha25@");
	PreparedStatement stmt = con.prepareStatement("select * from links where short_link =?");
	stmt.setString(1, url);
	ResultSet rs=stmt.executeQuery();
	if(rs.next()) {
		String longlink= rs.getString("long_link");
		return "redirect:"+longlink;
	} else {
		
	return "error";
	}
}
}
