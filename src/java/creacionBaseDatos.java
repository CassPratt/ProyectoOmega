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
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String resultado;
        String nombreBD = request.getParameter("nombre");
        String usuario=request.getParameter("usuario");
        String password=request.getParameter("contrasenia");
        
        String[] bases=(String[])session.getAttribute("bases");
        String[] basesAux=new String[bases.length+1];
        int i=0;
        while(i<bases.length){
            basesAux[i]=bases[i];
           i++;
        }
        basesAux[i]=nombreBD;
        session.setAttribute("bases", basesAux);
        
        
        
        
        
        
        
        

        Properties propiedades = new Properties();
        //Poniendo parametros como propiedades para la base de datos
        propiedades.put("user", usuario);
        propiedades.put("password",password);
        //Estableciendo nombre de base de datos
       
        
        


        try {
            //El parametro create=true crea la base de datos con las propiedaddes establecidas
            Connection conexion = DriverManager.getConnection(protocol + nombreBD + ";create=true", propiedades);
            resultado = "Se creo la base de datos " + nombreBD + " con exito";
            
            
            
            
          
            
            //Cerrando base de datos
            conexion.close();
        } catch (SQLException ex) {
            Logger.getLogger(creacionBaseDatos.class.getName()).log(Level.SEVERE, null, ex);
            resultado = "No fue posible crear la base de datos " + nombreBD + " porque " + ex.toString();
        }

        //Crear respuesta en JSON
        response.setContentType("application/json");
        
        JsonObject jsonRespuesta = Json.createObjectBuilder().add("resultado", resultado).build();

        //Enviar respuesta JSON
        try (PrintWriter out = response.getWriter()) {
            out.println(jsonRespuesta);
            out.close();

        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    public String rsAString(ResultSet resultSet) {
        StringBuilder builder = new StringBuilder();
        try {

            int columnCount = resultSet.getMetaData().getColumnCount();
            while (resultSet.next()) {
                for (int i = 0; i < columnCount;) {
                    builder.append(resultSet.getString(i + 1));
                    if (++i < columnCount) {
                        builder.append(",");
                    }
                }
                builder.append("\r\n");
            }

        } catch (SQLException ex) {
            Logger.getLogger(creacionBaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return builder.toString();
    }
}
