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
        
        <script>
            /* Function to show user databases(if they exist)
             * List with databases is included in the page
             */
            function checkUserDB(id,username,target) {
                var ajaxRequest;
                if (window.XMLHttpRequest){
                    ajaxRequest=new XMLHttpRequest(); // IE7+, Firefox, Chrome, Opera, Safari
                } else {
                    ajaxRequest=new ActiveXObject("Microsoft.XMLHTTP"); // IE6, IE5
                }
                ajaxRequest.onreadystatechange = function(){
                    if (ajaxRequest.readyState==4 && ajaxRequest.status==200){
                        document.getElementById(id).innerHTML=ajaxRequest.responseText;
                    }
                }
                   
                ajaxRequest.open("GET", target+"?="+username, true /*async*/);
                ajaxRequest.send();
            }
        </script>
        
    </head>

    <body>
        
        <%
            // Obtaining the user name
            HttpSession mySession = request.getSession();
            String user = (String)mySession.getAttribute("usuario");
            if(user != null){
                out.println("<h1>Welcome to your dashboard, " + user + "!</h1>");
            }
        %>

        <!----------------------- CREATE DB FORM --------------------->
        <form id="formCreateDB" action="CreateDB" method="POST">
            <h2>Create a new database...</h2>
            <label>Database name: </label>
            <input type="text" name="nombre" value="" required="required" />
            <label>User: </label>
            <input type="text" name="usuario" value="" required="required" />
            <label>Password: </label>
            <input type="password" name="contrasenia" value="" required="required" />
            <br>
            <input type="submit" value="Create database" id="button" />
        </form>
        <br>
        
        <!----------------------- INCLUDING USER DATABASES --------------------->
        <%
            /* Check if the user has registered databases
               - YES: show them in a list (Target: userDatabases div)
               - NO: don't show any options*/
            out.println("<script>checkUserDB('userDatabases','"+ user +"','ShowUserDB');</script>");
        %>
        <div id="userDatabases"></div>
        
    </body>
</html>
