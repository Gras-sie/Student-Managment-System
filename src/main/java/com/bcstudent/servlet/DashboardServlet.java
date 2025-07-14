package com.bcstudent.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class DashboardServlet
 * Handles dashboard access and session validation
 */
@WebServlet("/DashboardServlet")
public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the session
        HttpSession session = request.getSession();

        // Check if user is logged in
        if (session.getAttribute("userId") == null) {
            // Redirect to login page if not logged in
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Set attributes for the JSP
        String name = (String) session.getAttribute("name");
        request.setAttribute("name", name);

        // Forward to dashboard.jsp
        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }
}
