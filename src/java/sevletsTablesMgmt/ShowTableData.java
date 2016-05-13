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
public class ShowTableData extends HttpServlet {

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
                Statement create = con.createStatement();
                Statement query = con.createStatement();
                ResultSet rs = query.executeQuery("SELECT * FROM "+tableName);
                ArrayList<String> lista =new ArrayList<>();
                while(rs.next()){
                    String temp = "";
                    for (int i = 0; i < columnNames.size(); i++) {
                        temp += columnNames.get(i)+": "+rs.getObject(columnNames.get(i)).toString()+" ";
                    }
                    lista.add(temp);
                }
                if(lista.size()>0){
                    Integer index = (Integer) mySession.getAttribute("index");
                    out.println(printCorrectRegistry(lista,index,mySession,dbName,tableName));
                }else{
                    out.println("No hay datos!");
                }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ShowTableData.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                Logger.getLogger(ShowTableData.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
     private String printCorrectRegistry(ArrayList<String> lista, Integer index, HttpSession mySession,String dbName,String tableName){
        String res = "<form action='editTable.jsp' method='POST'>";
        res+="<input type='hidden' name='dbName' value='"+dbName+"' />" +
"            <input type='hidden' name='tableName' value='"+tableName+"' />";
        if(index==null){
            res += lista.get(0);
        }else if(index<lista.size()){
            res += lista.get(index);
        }else{
            index = lista.size()-1;
            res += lista.get(index);
        }
        res += "<br><input type='submit' name='btnPrevious' value='<' /> ";
        res += "<input type='submit' name='btnNext' value='>' /> ";
        res += "</form>";
        mySession.setAttribute("index", index);
        return res;
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
