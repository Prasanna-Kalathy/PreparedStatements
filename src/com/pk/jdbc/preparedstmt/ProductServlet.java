package com.pk.jdbc.preparedstmt;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet("/ProductServlet")
public class ProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Connection con;
	PreparedStatement stmt;

	public void init(ServletConfig config) throws ServletException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			ServletContext context = config.getServletContext();
			con = DriverManager.getConnection(context.getInitParameter("dbUrl"), 
											 context.getInitParameter("dbUser"),
											 context.getInitParameter("dbPass"));
			
			stmt = con.prepareStatement("insert into product values(?,?,?,?)");
		}

		catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		String description = request.getParameter("description");
		int price = Integer.parseInt(request.getParameter("price"));

		try {
			stmt.setInt(1, id);
			stmt.setString(2, name);
			stmt.setString(3, description);
			stmt.setInt(4, price);

			int result = stmt.executeUpdate();

			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println(result + " Products Created");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void destroy() {
		try {
			stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
