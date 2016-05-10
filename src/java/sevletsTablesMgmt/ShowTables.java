/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sevletsTablesMgmt;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class ShowTables extends HttpServlet {

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
            String username = (String)mySession.getAttribute("usuario");
            String password = (String)mySession.getAttribute("password");
            String dbName = request.getParameter("dbName");
            StringBuilder builder = new StringBuilder();
            try {
                Class.forName("org.apache.derby.jdbc.ClientDriver");
                Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/"+dbName+";create=true;",username,password);
                DatabaseMetaData meta = con.getMetaData();
                ResultSet res = meta.getTables(null, null, null, new String[] {"TABLE"});
                if(res.next()){
                    String tableName = res.getString("TABLE_NAME");
                    builder.append("<div id='tablesList' class='list-group'>");
                    builder.append("<div id='div"+tableName+"' class='list-group-item'>"+
                                "<button id='btn"+tableName+"' type='button' class=\"btnOptions btn btn-default btn-xs\">"
                                +"<span class=\"glyphicon glyphicon-triangle-bottom\" aria-hidden=\"true\"></span>"
                                +"</button><label> "+ tableName +"</label>"
                                +"</div>");
                    while(res.next()){
                        tableName = res.getString("TABLE_NAME");
                        builder.append("<div id='div"+tableName+"' class='list-group-item'>"+
                                    "<button id='btn"+tableName+"' type='button' class=\"btnOptions btn btn-default btn-xs\">"
                                    +"<span class=\"glyphicon glyphicon-triangle-bottom\" aria-hidden=\"true\"></span>"
                                    +"</button><label> "+ tableName +"</label>"
                                    +"</div>");
                    }
                    builder.append("</div><br>");
                    res.close();
                }else{
                    builder.append("<h3>This database has no tables</h3>");
                }
                con.close();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ShowTables.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(ShowTables.class.getName()).log(Level.SEVERE, null, ex);
            }
            String tables = builder.toString();
            out.println(tables);
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
