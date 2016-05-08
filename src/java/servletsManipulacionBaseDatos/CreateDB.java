package servletsManipulacionBaseDatos;

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
    private final String baseAdmin = "AdminUsuarios";
    private final String usuarioAdmin = "administrador";
    private final String passwordAdmin = "administrador";

    protected void processRequestPOST(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException {
        HttpSession session = request.getSession();
        String resultado;
        String nombreBD = request.getParameter("nombre");
        String usuario = request.getParameter("usuario");

        String password = request.getParameter("contrasenia");

        Properties propiedades = new Properties();
        //Creating parameters as properties for the DB
        propiedades.put("user", usuario);
        propiedades.put("password", password);
        //Stablishing the DB name

        try {
            // Connecting and creating the DB
            String resultadoRegistro = registrarBD((String) session.getAttribute("usuario"), nombreBD);
            if (resultadoRegistro.equals("exito")) {
                //The paremeter "create=true" creates the DB with the indicated properties
                Connection conexion = DriverManager.getConnection(protocolo + nombreBD + ";create=true", propiedades);
                resultado = "The database with name " + nombreBD + " was created successfully.";
                conexion.close();

            } else {
                resultado = resultadoRegistro;
            }

        } catch (SQLException ex) {
            Logger.getLogger(CreateDB.class.getName()).log(Level.SEVERE, null, ex);
            //Sending exception error
            resultado = "The database with name " + nombreBD + " couldn't be created, error: " + ex.toString();
        }

        //  Page for results of creating DB
        // THIS CAN BE A .JSP FILE, NOT A MANUALLY PRINTED ONE <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>DB created</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>" + resultado + "</h1>");

            if (resultado.equals("The database with name " + nombreBD + " was created successfully.")) {

                out.println("<form action=\"CreateTables\">\n"
                        + "<input type=\"submit\" value=\"Create tables\">\n"
                        + "</form>\n"
                        + "<form action=\"welcome.jsp\">\n"
                        + "<input type=\"submit\" value=\"Go to dashboard\">\n"
                        + "</form>\n");
            } else {
                out.println("<form action=\"welcome.jsp\">\n"
                        + "<input type=\"submit\" value=\"Go to dashboard\">\n"
                        + "</form>");

            }
            out.println("</body>");
            out.println("</html>");
            out.close();
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequestGET(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequestPOST(request, response);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CreateDB.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    public String registrarBD(String usuario, String nombreBD) throws ClassNotFoundException {

        try {
            Connection con = DriverManager.getConnection(protocolo + baseAdmin, usuarioAdmin, passwordAdmin);

            // Obtaining user ID
            Statement query = con.createStatement();
            ResultSet rs = query.executeQuery("SELECT ID_USER FROM USERS WHERE USERNAME='" + usuario + "'");
            
            if(rs.next()){
                int id = rs.getInt("ID_USER");
                
                if (id != -1) { // Valid user, registered user
                    
                    // Checking that the new DB name doesn't already exists for the user
                    query = con.createStatement();
                    rs = query.executeQuery("SELECT * FROM DATABASES WHERE USERID =" + id +
                                            " AND DBNAME = '"+ nombreBD +"'");
                    
                    if (!rs.next()) {   // DB name is not registered yet
                        query = con.createStatement();
                        query.executeUpdate("INSERT INTO DATABASES(USERID,DBNAME) VALUES (" + id + ",'" + nombreBD + "')");
                        con.close();
                        return "exito";
                    } else {    // DB name already registered
                        return "Database name already registered. Choose another one.";
                    }
                } else {    // User error, invalid user
                    throw new SQLException("invalid user or user error with ID " + usuario);
                }
            }
            return "Failure.";

        } catch (SQLException ex) {
            Logger.getLogger(CreateDB.class
                    .getName()).log(Level.SEVERE, null, ex);
            return ex.toString();
        }
    }

    private void processRequestGET(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        ServletContext context = getServletContext();
        RequestDispatcher rd = context.getRequestDispatcher("/creacionTablas");
        rd.forward(request, response);
    }

}
