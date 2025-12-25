import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;

public class catering extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {

        res.setContentType("text/html");
        PrintWriter pw = res.getWriter();

        pw.println("<html><head><title>Catering Booking</title></head><body>");

        try {
            // Get parameters from form
            String eventId = req.getParameter("event_id");
            String plateType = req.getParameter("plate_type");
            double price = Double.parseDouble(req.getParameter("price"));
            String serviceDate = req.getParameter("service_date"); // YYYY-MM-DD

            if (eventId == null || eventId.trim().isEmpty() || 
                serviceDate == null || serviceDate.trim().isEmpty()) {
                pw.println("<h3 style='color:red;'>Event ID and Service Date are required!</h3>");
                return;
            }

            // Escape single quotes
            plateType = plateType.replace("'", "''");

            // Load Oracle JDBC driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Connect to database
            Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:XE", "system", "Manager");

            Statement stmt = con.createStatement();

            // Optional: check if event_id exists (user-friendly message)
            ResultSet rs = stmt.executeQuery(
                    "SELECT COUNT(*) cnt FROM event WHERE event_id = '" + eventId + "'");
            rs.next();
            if (rs.getInt("cnt") == 0) {
                pw.println("<h3 style='color:red;'>Event ID does not exist!</h3>");
                return;
            }

            // Insert booking
            String sql = "INSERT INTO catering " +
                    "(event_id, plate_type, price, service_date, booking_date) " +
                    "VALUES ('" + eventId + "', '" + plateType + "', " + price +
                    ", TO_DATE('" + serviceDate + "','YYYY-MM-DD'), SYSDATE)";

            stmt.executeUpdate(sql);

            pw.println("<h3 style='color:green;'>Catering Booking Successful!</h3>");
            pw.println("<p>Event ID: " + eventId + "</p>");
            pw.println("<p>Plate Type: " + plateType + "</p>");
            pw.println("<p>Service Date: " + serviceDate + "</p>");
            pw.println("<p>Price: ₹" + price + "</p>");
            pw.println("<a href='catering_booking.html'>Book Another Catering</a>");

            rs.close();
            stmt.close();
            con.close();

        } catch (Exception e) {
            pw.println("<h3 style='color:red;'>Error: " + e + "</h3>");
        }

        pw.println("</body></html>");
    }
}
