package sevletsTablesMgmt;

import com.sun.xml.ws.util.StringUtils;
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
public class AddRegistry extends HttpServlet {

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
            ArrayList<String> columnNames = (ArrayList<String>)mySession.getAttribute("columnNames");
            try {
                Class.forName("org.apache.derby.jdbc.ClientDriver");
                Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/"+dbName,username,password);
                Statement query = con.createStatement();
                StringBuilder builder = new StringBuilder();
                // Insert values obtained from request (editTable.jsp)
                builder.append("INSERT INTO ").append(tableName).append("(");
                for(int i=0;i<columnNames.size()-1;i++) {
                    builder.append(columnNames.get(i));
                    builder.append(",");
                }
                builder.append(columnNames.get(columnNames.size()-1)).append(") VALUES(");
                ArrayList<String> params = new ArrayList<>();
                for(int i=0;i<columnNames.size();i++){
                    params.add(request.getParameter(columnNames.get(i)));
                }
                for(int i=0;i<params.size()-1;i++){
                    if(!isNumeric(params.get(i))){
                        builder.append("'").append(params.get(i)).append("',");
                    }else{
                        builder.append(params.get(i)).append(",");
                    }
                }
                if(!isNumeric(params.get(params.size()-1))){
                    builder.append("'").append(params.get(params.size()-1)).append("')");
                }else{
                    builder.append(params.get(params.size()-1)).append(")");
                }
                out.println(builder.toString());
                query.executeUpdate(builder.toString());
                ArrayList<String> lista = setArrayList(con, tableName, columnNames);
                if(lista!=null){
                    out.println("<br>Success!");    // Successfully added registry
                    out.println("<br>"+lista.toString());
                }else{
                    out.println("Fallo la lista!");
                }
                mySession.setAttribute("lista", lista);
                con.close();    // Closing DB connection
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(AddRegistry.class.getName()).log(Level.SEVERE, null, ex);
                out.println("Fail!");   // Error in values or DB
            } catch (SQLException ex) {
                Logger.getLogger(AddRegistry.class.getName()).log(Level.SEVERE, null, ex);
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
