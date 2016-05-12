<%-- 
    Document   : editTable
    Created on : May 10, 2016, 5:17:16 PM
    Author     : miguelcasillas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="resources/bootstrap-3.3.6-dist/css/bootstrap.css" rel="stylesheet">
        <script type="text/javascript" src="resources/js/editTable.js"></script>
        <title>Edit Table</title>
    </head>
    <body>
        <%
            // Checking if the user is logged in. If no, send to index.jsp
            HttpSession mySession = request.getSession();
            String user = (String)mySession.getAttribute("username");
            if(user==null){
                request.setAttribute("isLogged", "NO");
                String next = "/index.jsp";
                RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher(next);
                dispatcher.forward(request,response);
            }
            
            // Obtaining the database name where the user will add a Table
            String dbName = (String)request.getParameter("dbName");
            mySession.setAttribute("dbName", dbName);
            String tableName = (String)request.getParameter("tableName");
            mySession.setAttribute("tableName", tableName);
        %>
        
        <h1>This is your data on table: <%=tableName%></h1>
        <%
            // Obtaining registries in the table
            out.println("<script>setDivContent('scrollTable','"+user+"','ShowTableData','"+dbName+"','"+tableName+"');</script>");
            // Obtaining fields of table to add a registry
            out.println("<script>setDivContent('addRegistry','"+user+"','ShowAddRegistry','"+dbName+"','"+tableName+"');</script>");
        %>
        
        <!------------------ SCROLL FORM ------------------>
        <br><div id="scrollTable"></div>
        
        <!---------------- ADD REGISTRY FORM -------------->
        <br><br><div id='addRegistry'></div>
        
        <!-------------- DELETE REGISTRY FORM ------------->
        <br><br><h3>Delete a Registry</h3>
        <h4>The ID is the first column on the table</h4>
        <form action='DeleteRegistry' method='POST'>
            <input type='hidden' name='dbName' value='<%=dbName%>'>
            <input type='hidden' name='tableName' value='<%=tableName%>'>
            <table border="2">
                <tbody>
                    <tr>
                        <td>Id:</td>
                        <td><input type="text" name="id" value="" /></td>
                        <td><input type="submit" value="Delete" /></td>
                    </tr>
                </tbody>
            </table>
        </form>
            
    </body>
</html>
