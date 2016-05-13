package sevletsTablesMgmt;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author miguelcasillas
 */
public class ShowAddRegistry extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            HttpSession mySession = request.getSession();
            String username = (String)mySession.getAttribute("username");
            String password = (String)mySession.getAttribute("password");
            String dbName = request.getParameter("dbName");
            String tableName = request.getParameter("tableName");
            StringBuilder builder = new StringBuilder();
            builder.append("<h3>Add a new Registry</h3>");
            builder.append("<form action='AddRegistry' method='POST'>");
            builder.append("<input type='hidden' name='dbName' value='").append(dbName).append("'>");
            builder.append("<input type='hidden' name='tableName' value='").append(tableName).append("'>");
            builder.append("<table border='2'><thead><tr>");
            try {
                Class.forName("org.apache.derby.jdbc.ClientDriver");
                Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/"+dbName,username,password);
                Statement query = con.createStatement();
                
                ResultSet rs = query.executeQuery("SELECT * FROM "+tableName);
                ResultSetMetaData rsmd = rs.getMetaData();
                ArrayList<String> columnNames = new ArrayList();
                
                // Obtaining the table's columns to display
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    columnNames.add(rsmd.getColumnName(i));
                }
                mySession.setAttribute("columnNames",columnNames);
                for(int i=0;i<columnNames.size();i++){
                    builder.append("<th>").append(columnNames.get(i)).append("</th>");
                }
                builder.append("<th>Insert</th>");
                builder.append("</thead>");
                builder.append("<tbody><tr>");
                for(int i=0;i<columnNames.size();i++){
                    builder.append("<td><input type='text' name='").append(columnNames.get(i)).append("' value='' required='required'/></td>");
                }
                builder.append("<td><input type='submit' value='Register' /></td>");
                builder.append("</tr></tbody>");
                con.close();    // Close DB connection
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ShowAddRegistry.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(ShowAddRegistry.class.getName()).log(Level.SEVERE, null, ex);
            }
            builder.append("</table></form>");  // End of AddRegistry form
            out.println(builder.toString());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
