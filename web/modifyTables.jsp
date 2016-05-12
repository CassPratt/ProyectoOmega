<%-- 
    Document   : modifyTables
    Created on : May 8, 2016, 3:27:29 PM
    Author     : CassPratt
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="resources/bootstrap-3.3.6-dist/css/bootstrap.css" rel="stylesheet">
        <script type="text/javascript" src="resources/js/modifyTables.js"></script>
        <title>JSP Page</title>
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
        %>
        <h2>Modify table on <%=dbName%></h2>
        <%
            // Obtaining tables list of DB
            out.println("<script>showDBTables('dbTables','"+user+"','ShowTables','"+dbName+"');</script>");
        %>
        
        <div id="dbTables">
            <!---------------- TABLES LIST ----------------> <!-- TO FILL -->
            <!---------------- EDIT TABLE FORM ----------------> <!-- TO FILL -->
        </div>
    </body>
</html>
