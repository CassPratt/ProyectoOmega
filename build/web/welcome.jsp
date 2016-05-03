<%-- 
    Document   : welcome
    Created on : May 1, 2016, 5:50:41 PM
    Author     : miguelcasillas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dashboard DWW</title>
    </head>
    <body>
        <h1>Welcome to your Dashboard!</h1>
        
        <%
            /* Check if user is logged in, in order to access the dashboard */
            String username = (String)request.getSession().getAttribute("username");
            if(username==null)
                response.sendRedirect("index.jsp");
        %>
        
    </body>
</html>
