<%-- 
    Document   : crearBaseDatos
    Created on : May 3, 2016, 9:20:15 AM
    Author     : EUCJ
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

    <head>
        <title>Dashboard DWW</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="resources/bootstrap-3.3.6-dist/css/bootstrap.css" rel="stylesheet">
        <script type="text/javascript" src="resources/js/jquery-1.12.3.min.js"></script>
        <script type="text/javascript" src="resources/js/welcome.js"></script>
    </head>

    <body>
        
        <%
            // Obtaining the user name
            HttpSession mySession = request.getSession();
            String user = (String)mySession.getAttribute("usuario");
            if(user != null){
                out.println("<h1>Welcome to your dashboard, " + user + "!</h1>");
            }else{
                request.setAttribute("isLogged", "NO");
                String next = "/index.jsp";
                RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher(next);
                dispatcher.forward(request,response);
            }
        %>
        
        <!----------------------- INCLUDING USER DATABASES --------------------->
        <%
            /* Check if the user has registered databases
               - YES: show them in a list (Target: userDatabases div)
               - NO: don't show any options*/
            out.println("<script>checkUserDB('userDatabases','"+ user +"','ShowUserDB');</script>");
        %>
        <div id="userDatabases">
        </div>
        
        <h2>Create a new database...</h2>
        <!----------------------- CREATE DB FORM --------------------->
        <form id="formCreateDB" action="CreateDB" method="POST">
            <label>Database name: </label>
            <input type="text" name="nombre" value="" required="required" />
            <label>User: </label>
            <input type="text" name="usuario" value="" required="required" />
            <label>Password: </label>
            <input type="password" name="contrasenia" value="" required="required" />
            <br>
            <input type="submit" value="Create database" id="button" class="btn btn-default"/>
        </form>
        <br>
    </body>
</html>
