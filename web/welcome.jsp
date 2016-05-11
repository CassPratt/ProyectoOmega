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
        <script type="text/javascript" src="resources/js/welcome.js"></script>
    </head>

    <body>
        
        <%
            // Obtaining the user name
            HttpSession mySession = request.getSession();
            String user = (String)mySession.getAttribute("username");
            if(user != null){
                out.println("<h1>Welcome to your dashboard, " + user + "!</h1>");
            }else{
                request.setAttribute("isLogged", "NO");
                String next = "/index.jsp";
                RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher(next);
                dispatcher.forward(request,response);
            }
            
            /* Flag to check if the database was created successfully,
               obtained from CreateDB servlet */
            String fromCDB = (String)request.getAttribute("fromCreateDB");
            if(fromCDB!=null&&fromCDB.equals("YES")){
                out.println("<script type=\"text/javascript\">");
                out.println("alert('Database created successfully!');");
                out.println("</script>");
            } else if (fromCDB != null && fromCDB.equals("NO")){
                out.println("<script type=\"text/javascript\">");
                out.println("alert('Database name already exists...');");
                out.println("</script>");
            }
            request.removeAttribute("fromCreateDB");
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
            <input type="text" name="dbName" value="" required="required" />
            <label>User: </label>
            <input type="text" name="username" value="" required="required" />
            <label>Password: </label>
            <input type="password" name="password" value="" required="required" />
            <br>
            <input type="submit" value="Create database" id="button" class="btn btn-default"/>
        </form>
        <br>
    </body>
</html>
