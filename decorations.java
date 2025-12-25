import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;

public class decorations extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {

        res.setContentType("text/html");
        PrintWriter pw = res.getWriter();

        pw.println("<html><body>");

        try {
            int eventId = Integer.parseInt(req.getParameter("event_id"));
            String decorationType = req.getParameter("decoration_type");
            double price = Double.parseDouble(req.getParameter("price"));
            String serviceDate = req.getParameter("service_date");

            if (serviceDate == null || serviceDate.trim().isEmpty()) {
                pw.println("<h3 style='color:red;'>Service Date is required!</h3>");
                return;
            }

            decorationType = decorationType.replace("'", "''");

            Class.forName("oracle.jdbc.driver.OracleDriver");

            Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:XE", "system", "Manager");

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(
                "SELECT COUNT(*) cnt FROM event WHERE event_id = '" + eventId + "'");

            rs.next();
            if (rs.getInt("cnt") == 0) {
                pw.println("<h3 style='color:red;'>Event ID does not exist!</h3>");
                return;
            }

            String q1 =
                "INSERT INTO decorations (event_id, decoration_type, price, service_date, booking_date) " +
                "VALUES (" + eventId + ", '" + decorationType + "', " + price +
                ", TO_DATE('" + serviceDate + "','YYYY-MM-DD'), SYSDATE)";

            stmt.executeUpdate(q1);

            pw.println("<h3 style='color:green;'>Decoration Booked Successfully!</h3>");

            rs.close();
            stmt.close();
            con.close();

        } catch (Exception e) {
            pw.println("<h3 style='color:red;'>Error: " + e + "</h3>");
        }

        pw.println("</body></html>");
    }
}
