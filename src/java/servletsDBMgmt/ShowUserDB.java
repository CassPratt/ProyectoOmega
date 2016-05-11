package servletsDBMgmt;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author EUCJ
 */
public class ShowUserDB extends HttpServlet {

    private final String protocolo = "jdbc:derby://localhost:1527/";
    private final String baseAdmin = "UsersAdmin";
    private final String userAdmin = "dbAdmin";
    private final String passwordAdmin = "dbAdmin";
    
    // Method called for printing the new elements on welcome.jsp page
    // Bootstrap classes are used in order to make administration buttons appear
    private String createOptionsDiv(String dbName){
        String res = "<div id='options"+dbName+"' class='optionsDB'>";
        res +=  "<form action='createTable.jsp' method='POST'>"
                +"<input type='hidden' name='dbName' value='"+dbName+"'>"
                +"<button id='create"+dbName+"' type='submit' class=\"btnCreate btn btn-default btn-sm\">"
                +"<span class=\"glyphicon glyphicon-file\" aria-hidden=\"true\">Create Table</span>"
                +"</button>"
                +"</form>"
                +"<form action='modifyTables.jsp' method='POST'>"
                +"<input type='hidden' name='dbName' value='"+dbName+"'>"
                +"<button id='modify"+dbName+"' type='submit' class=\"btnModify btn btn-default btn-sm\">"
                +"<span class=\"glyphicon glyphicon-pencil\" aria-hidden=\"true\">Modify Tables</span>"
                +"</button>"
                +"</form>"
                +"</div>";
        return res;
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        try (PrintWriter out = response.getWriter()) {

        HttpSession session = request.getSession();
        // Creating connection with the administrator database
        Connection conAdmin = DriverManager.getConnection(protocolo + baseAdmin, userAdmin, passwordAdmin);
        String usuario = (String)session.getAttribute("username");

        // Looking for user ID
        Statement query = conAdmin.createStatement();
        ResultSet rs = query.executeQuery("SELECT ID_USER FROM USERS WHERE USERNAME='" + usuario + "'");

        if (rs.next()) { // Double check for user existence in USERS table
            int id = rs.getInt("ID_USER");
            query = conAdmin.createStatement();
            ResultSet bases = query.executeQuery("SELECT * FROM DATABASES WHERE USERID=" + id+" ORDER BY DBNAME");

            // Check if user has registered databases
            if (bases.next()) { // The user has registered DBs
                StringBuilder builder = new StringBuilder();
                try {
                    builder.append("<div id='databasesList' class='list-group'>");
                    
                    // Creating the drop down list object with the DBs names
                    String dbName = bases.getString("DBNAME");
                    builder.append("<div id='div"+dbName+"' class='list-group-item'>"+
                                "<button id='btn"+dbName+"' type='button' class=\"btnOptions btn btn-default btn-xs\">"
                                +"<span class=\"glyphicon glyphicon-triangle-bottom\" aria-hidden=\"true\"></span>"
                                +"</button><label> "+ dbName +"</label>"
                                +createOptionsDiv(dbName) + "</div>");
                    while(bases.next()){
                        dbName = bases.getString("DBNAME");
                        builder.append("<div id='div"+dbName+"' class='list-group-item'>"+
                                "<button id='btn"+dbName+"' type='button' class=\"btnOptions btn btn-default btn-xs\">"
                                +"<span class=\"glyphicon glyphicon-triangle-bottom\" aria-hidden=\"true\"></span>"
                                +"</button><label> "+ dbName +"</label>"
                                +createOptionsDiv(dbName) + "</div>");
                    }
                    builder.append("</div><br>");

                } catch (SQLException ex) {
                    Logger.getLogger(CreateDB.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
                
                // Printing the user DBs
                String selectBases = builder.toString();
                out.println("<h2>Administrate your Databases</h2>");
                out.println(selectBases);
                
            } else { // The user doesn't have registered DBs
                out.println("<h3>You don't have any registered databases yet.</h3>");
            }
        } else { // In case user doesn't exist in USERS table - FATAL ERROR -
            out.println("Error while loading dashboard...");
        }
        // Closing the connection
        conAdmin.close();
        }
    }

    private void processRequestPOST(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(ShowUserDB.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        processRequestPOST(request, response);
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
