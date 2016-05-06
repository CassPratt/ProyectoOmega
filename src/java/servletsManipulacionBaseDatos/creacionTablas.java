/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servletsManipulacionBaseDatos;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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
 * @author EUCJ
 */
public class creacionTablas extends HttpServlet {

    private final String protocolo = "jdbc:derby://localhost:1527/";
    private final String baseAdmin = "AdminUsuarios";
    private final String usuarioAdmin = "administrador";
    private final String passwordAdmin = "administrador";

    protected void processRequestGET(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        HttpSession session = request.getSession();
        Connection conAdmin = DriverManager.getConnection(protocolo + baseAdmin, usuarioAdmin, passwordAdmin);
        String usuario = (String) session.getAttribute("usuario");

        Statement query = conAdmin.createStatement();
        ResultSet rs = query.executeQuery("SELECT ID_USER FROM USUARIOS WHERE USERNAME='" + usuario + "'");

        rs.next();

        int id = rs.getInt("ID_USER");

        query = conAdmin.createStatement();
        ResultSet bases = query.executeQuery("SELECT * FROM BASES WHERE USERID=" + id);

        StringBuilder builder = new StringBuilder();
        try {
            builder.append("<select>");

            //Solo esta hecho para resultsets de 1 columna
            while (bases.next()) {

                builder.append("<option value=\"" + bases.getString(3) + "\">" + bases.getString(3) + "</option>");

            }

            builder.append("</select>");

        } catch (SQLException ex) {
            Logger.getLogger(creacionBaseDatos.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        String selectBases = builder.toString();

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>CreacionTablas</title>");
            out.println("</head>");
            out.println("<body>");
            out.println(selectBases);
            out.println("</body>");
            out.println("</html>");
        }

        conAdmin.close();
    }

    private void processRequestPOST(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequestGET(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(creacionTablas.class.getName()).log(Level.SEVERE, null, ex);
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
