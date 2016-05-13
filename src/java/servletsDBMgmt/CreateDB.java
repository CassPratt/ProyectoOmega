package servletsDBMgmt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author EUCJ
 */
public class CreateDB extends HttpServlet {

    private final String protocolo = "jdbc:derby://localhost:1527/";
    private final String baseAdmin = "UsersAdmin";
    private final String usuarioAdmin = "dbAdmin";
    private final String passwordAdmin = "dbAdmin";
    
    public String registerBD(String _user, String _pass, String _dbName) throws ClassNotFoundException {

        try {
            Connection con = DriverManager.getConnection(protocolo + baseAdmin, usuarioAdmin, passwordAdmin);

            // Obtaining user ID
            Statement query = con.createStatement();
            ResultSet rs = query.executeQuery("SELECT ID_USER FROM USERS WHERE USERNAME='" + _user + "'"
                                    + " AND PASSWORD='" + _pass + "'");
            
            if(rs.next()){  // Valid user, registered user
                int id = rs.getInt("ID_USER");
                
                if (id != -1) { 
                    // Checking that the new DB name doesn't already exists for the user
                    query = con.createStatement();
                    rs = query.executeQuery("SELECT * FROM DATABASES WHERE USERID =" + id +
                                            " AND DBNAME = '"+ _dbName +"'");
                    
                    if (!rs.next()) {   // DB name is not registered yet
                        query = con.createStatement();
                        query.executeUpdate("INSERT INTO DATABASES(USERID,DBNAME) VALUES (" + id + ",'" + _dbName + "')");
                        con.close();    // Close DB connection
                        return "Success";
                    } else {    // DB name already registered
                        return "Database name already registered. Choose another one.";
                    }
                } else {    // User error, invalid user
                    throw new SQLException("invalid user or user error with ID " + _user);
                }
            } else {
                return "Wrong username or password.";
            }

        } catch (SQLException ex) {
            Logger.getLogger(CreateDB.class
                    .getName()).log(Level.SEVERE, null, ex);
            return ex.toString();
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException {
        HttpSession session = request.getSession();
        String result = "";
        
        // User parameters for DB creation
        String dbName = request.getParameter("dbName");
        String user = request.getParameter("username");
        String password = request.getParameter("password");
        
        try {
            // Check the user parameter from welcome.jsp and from session are the same
            String resRegister = "";
            if (user.equals((String) session.getAttribute("username")) ) {
                resRegister = registerBD(user, password, dbName);
                if (resRegister.equals("Success")) {
                    //The paremeter "create=true" creates the DB with the indicated properties
                    Connection con = DriverManager.getConnection(protocolo + dbName + ";create=true;",user,password);
                    result = "The database with name " + dbName + " was created successfully.";
                    con.close();    // Closing UsersAdmin DB connection

                } else {
                    result = resRegister;
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(CreateDB.class.getName()).log(Level.SEVERE, null, ex);
            //Sending exception error
            result = "The database with name " + dbName + " couldn't be created, error: " + ex.toString();
        }

        //  Redirect with DB creation result
        try (PrintWriter out = response.getWriter()) {
            System.out.println(result);
            if (result.equals("The database with name " + dbName + " was created successfully.")) {
                request.setAttribute("fromCreateDB", (Object)"YES");
            } else {
                request.setAttribute("fromCreateDB", (Object)"NO");
            }
            // Redirect to welcome.jsp
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/welcome.jsp");
            dispatcher.forward(request,response);
            
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CreateDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CreateDB.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
