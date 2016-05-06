package servletsManipulacionBaseDatos;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * @author EUCJ LA VIDA DE PABLO
 */
public class creacionBaseDatos extends HttpServlet {

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
        //Poniendo parametros como propiedades para la base de datos
        propiedades.put("user", usuario);
        propiedades.put("password", password);
        //Estableciendo nombre de base de datos

        try {

            String resultadoRegistro = registrarBD((String) session.getAttribute("usuario"), nombreBD);
            if (resultadoRegistro.equals("exito")) {
                //El parametro create=true crea la base de datos con las propiedaddes establecidas
                Connection conexion = DriverManager.getConnection(protocolo + nombreBD + ";create=true", propiedades);
                resultado = "Se creo la base de datos " + nombreBD + " con exito.";
                conexion.close();

            } else {
                resultado = resultadoRegistro;
            }

        } catch (SQLException ex) {
            Logger.getLogger(creacionBaseDatos.class.getName()).log(Level.SEVERE, null, ex);
            //Si error, enviar excepcion como respuesta
            resultado = "No fue posible crear la base de datos " + nombreBD + ", error: " + ex.toString();
        }

        //Crear respuesta en JSON, resultado de la operación y arreglo de bases creadas por el usuario transformada en arreglo JSON para parse de JQUERY
        response.setContentType("text/html");

        //Enviar respuesta JSON
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Resultado</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>" + resultado + "</h1>");

            if (resultado.equals("Se creo la base de datos " + nombreBD + " con exito.")) {

                out.println("     <form action=\"crearTablas\">\n"
                        + "    <input type=\"submit\" value=\"Crear tablas\">\n"
                        + "</form>\n"
                        + "        \n"
                        + "               <form action=\"crearBaseDatos\">\n"
                        + "    <input type=\"submit\" value=\"Crear otra base\">\n"
                        + "</form>");
            } else {
                out.println("    <form action=\"crearBaseDatos\">\n"
                        + "    <input type=\"submit\" value=\"Intentar de nuevo\">\n"
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
            Logger.getLogger(creacionBaseDatos.class
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

            Statement query = con.createStatement();
            ResultSet rs = query.executeQuery("SELECT ID_USER FROM USUARIOS WHERE USERNAME='" + usuario + "'");
            rs.next();
            int id = rs.getInt("ID_USER");

            if (id != -1) {

                query = con.createStatement();

                query.executeUpdate("INSERT INTO BASES (USERID,DBNAME) VALUES (" + id + ",'" + nombreBD + "')");
                con.close();
                return "exito";
            } else {
                throw new SQLException("No existe el usuario o hubo un error al obtener el id de " + usuario);
            }

        } catch (SQLException ex) {
            Logger.getLogger(creacionBaseDatos.class
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
