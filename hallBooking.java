import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;

public class hallBooking extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {

        res.setContentType("text/html");
        PrintWriter pw1 = res.getWriter();

        pw1.println("<html><head><title>Hall Booking</title></head><body>");

        try {
            // Get parameters from the form
            int eventId = Integer.parseInt(req.getParameter("eventId"));
            String area = req.getParameter("area");
            String dateOfService = req.getParameter("dateOfService");
            double price = Double.parseDouble(req.getParameter("price"));

            // Escape single quotes in area
            area = area.replace("'", "''");

            // Load Oracle JDBC driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Connect to database
            Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:XE", "system", "Manager");

            Statement stmt = con.createStatement();

            // SQL query (booking_date defaults to SYSDATE)
            String q1 = "INSERT INTO hall (event_id, area, date_of_service, price) " +
                        "VALUES (" + eventId + ", '" + area + "', TO_DATE('" + dateOfService + "', 'YYYY-MM-DD'), " + price + ")";

            int x = stmt.executeUpdate(q1);

            if (x > 0) {
                pw1.println("<h3 style='color:green;'>Hall Booking Created!</h3>");
                pw1.println("<a href='hallBooking.html'>Book Another Hall</a>");
            } else {
                pw1.println("<h3 style='color:red;'>Booking Failed.</h3>");
            }

            con.close();

        } catch (Exception e) {
            pw1.println("<h3 style='color:red;'>Error: " + e + "</h3>");
        }

        pw1.println("</body></html>");
        pw1.close();
    }
}
