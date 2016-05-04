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
import java.util.ArrayList;
import javax.json.Json;

import java.util.Properties;
import javax.json.JsonObject;
import javax.servlet.http.HttpSession;

/**
 *
 * @author EUCJ LA VIDA DE PABLO
 */
public class creacionBaseDatos extends HttpServlet {

    private final String framework = "embedded";
    private final String protocol = "jdbc:derby://localhost:1527/";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
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
                Connection conexion = DriverManager.getConnection(protocol + nombreBD + ";create=true", propiedades);
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
        response.setContentType("application/json");
        String bases = obtenerBasesUsuario((String) session.getAttribute("usuario"));

        JsonObject jsonRespuesta = Json.createObjectBuilder().add("resultado", resultado).add("bases", bases).build();

        //Enviar respuesta JSON
        try (PrintWriter out = response.getWriter()) {
            out.println(jsonRespuesta);
            out.close();

        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(creacionBaseDatos.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(creacionBaseDatos.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    public String rsAArregloJson(ResultSet resultSet) {
        StringBuilder builder = new StringBuilder();
        try {
            builder.append("[");
            int cuentaColumnas = resultSet.getMetaData().getColumnCount();
            
            //Solo esta hecho para resultsets de 1
            while (resultSet.next()) {
                for (int i = 0; i < cuentaColumnas; i++) {
                    builder.append("\"" + resultSet.getString(i + 1) + "\",");

                }

            }

            int ultimo = builder.length() - 1;
            builder.deleteCharAt(ultimo);

        } catch (SQLException ex) {
            Logger.getLogger(creacionBaseDatos.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        builder.append("]");
        return builder.toString();
    }

    public String registrarBD(String usuario, String nombreBD) throws ClassNotFoundException {

        try {

            int id = obtenerIdUsuario(usuario);

            if (id != -1) {
                Connection con = DriverManager.getConnection(protocol + "AdminUsuarios", "administrador", "administrador");

                Statement query = con.createStatement();

                query.executeUpdate("INSERT INTO BASES (USERID,DBNAME) VALUES (" + id + ",'" + nombreBD + "')");
                con.close();
                return "exito";
            } else {
                throw new SQLException("No existe el usuario" + usuario);
            }

        } catch (SQLException ex) {
            Logger.getLogger(creacionBaseDatos.class
                    .getName()).log(Level.SEVERE, null, ex);
            return ex.toString();
        }

    }

    public int obtenerIdUsuario(String usuario) {

        try {
            Connection con = DriverManager.getConnection(protocol + "AdminUsuarios", "administrador", "administrador");

            Statement query = con.createStatement();
            ResultSet rs = query.executeQuery("SELECT ID_USER FROM USUARIOS WHERE USERNAME='" + usuario + "'");
            rs.next();
            int id = rs.getInt("ID_USER");
            con.close();
            return id;

        } catch (SQLException ex) {
            Logger.getLogger(creacionBaseDatos.class
                    .getName()).log(Level.SEVERE, null, ex);
            return -1;
        }

    }

    public String obtenerBasesUsuario(String usuario) {

        try {

            int id = obtenerIdUsuario(usuario);

            Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/AdminUsuarios", "administrador", "administrador");

            Statement query = con.createStatement();
            ResultSet rs = query.executeQuery("SELECT DBNAME FROM BASES WHERE USERID=" + id);
            String r = rsAArregloJson(rs);
            con.close();

        
            return r;

        } catch (SQLException ex) {
            Logger.getLogger(creacionBaseDatos.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }
}
