package sevletsTablesMgmt;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author CassPratt
 */
public class CreateTable extends HttpServlet {

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
            
            // Get the new table data
            String tableName = request.getParameter("tableName");
            String dbName = (String)request.getSession().getAttribute("dbName");
            
            // Get the user data
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            
            // Array of fields' names
            ArrayList<String> names = new ArrayList();
            // Array of types' names
            ArrayList<String> types = new ArrayList();
            // Fill the arrays with the values on createTable.jsp
            int cont = 1;
            String name = request.getParameter("nameField"+cont);
            String type = request.getParameter("typeField"+cont);
            while(name!=null){
                names.add(name); types.add(type);
                cont++;
                name = request.getParameter("nameField"+cont);
                type = request.getParameter("typeField"+cont);
            }
            
            // Connect to the user database and create the table
            try {
                // Stablish connection
                Class.forName("org.apache.derby.jdbc.ClientDriver");
                Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/"+dbName,username,password);
                Statement create = con.createStatement();
                String queryString = "create table "+tableName+" (";
                for(int i=0;i<names.size();i++){
                    queryString += names.get(i)+" "+types.get(i)+", ";
                }
                // First field in createTable.jsp is the primary key
                queryString += "primary key("+names.get(0)+"))";    
                out.println(queryString);
                create.executeUpdate(queryString);
                out.println("Table created successfully!");
                con.close();    // Connection closed
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(CreateTable.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(CreateTable.class.getName()).log(Level.SEVERE, null, ex);
            }
            // Check data of new table
            out.println("Table: "+tableName);
            out.println("Names: "+names.toString());
            out.println("Types: "+types.toString());
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
