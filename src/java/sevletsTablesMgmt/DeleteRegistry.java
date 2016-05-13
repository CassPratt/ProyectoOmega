package sevletsTablesMgmt;

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
 * @author miguelcasillas
 */
public class DeleteRegistry extends HttpServlet {

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
            String id = request.getParameter("id");
            ArrayList<String> columnNames = (ArrayList<String>)mySession.getAttribute("columnNames");
            try {
                Class.forName("org.apache.derby.jdbc.ClientDriver");
                Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/"+dbName,username,password);
                Statement query = con.createStatement();
              
                String delete = "DELETE FROM "+tableName+" WHERE "+columnNames.get(0)+"=";
                if(!isNumeric(id)){ // NOT GENERAL, COULD BE NON-NUMERIC
                    delete += "'"+id+"'";
                }else{
                    delete += id;
                }
                out.println(delete+"<br>");
                // Deleting registry that corresponds to the id
                query.executeUpdate(delete);
                ArrayList<String> lista = setArrayList(con, tableName, columnNames);
                if(lista!=null){
                    out.println("<br>Success!");    // Successfully added registry
                    out.println("<br>"+lista.toString());
                }else{
                    out.println("Fallo la lista!");
                }
                mySession.setAttribute("lista", lista);
                con.close();
                out.println("Success!");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DeleteRegistry.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(DeleteRegistry.class.getName()).log(Level.SEVERE, null, ex);
                out.println("Fail!");
            }
        }
    }
    
    private static boolean isNumeric(String str){
        try{
            double d = Double.parseDouble(str);
        }catch(NumberFormatException e){
            return false;
        }
        return true;
    }
    
    private ArrayList<String> setArrayList(Connection con, String tableName, ArrayList<String> columnNames){
        try {
            Statement query = con.createStatement();
            ResultSet rs = query.executeQuery("SELECT * FROM "+tableName);
            ArrayList<String> lista =new ArrayList<>();
            while(rs.next()){
                String temp = "";
                for (int i = 0; i < columnNames.size(); i++) {
                    temp += columnNames.get(i)+" "+rs.getObject(columnNames.get(i)).toString()+" ";
                }
                lista.add(temp);
            }
            return lista;
        } catch (SQLException ex) {
            Logger.getLogger(AddRegistry.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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
