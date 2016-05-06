<%-- 
    Document   : signup
    Created on : May 1, 2016, 1:38:58 PM
    Author     : miguelcasillas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sign Up DWW</title>
    </head>
    <body>
        <%
            /* Flag to check if new user was registered successfully,
               obtained from RegisterUser servlet */
            String fromRU = (String)request.getAttribute("fromRegisterUser");
            if(fromRU!=null&&fromRU.equals("NO")){
                out.println("<script type=\"text/javascript\">");
                out.println("alert('Username already exists');");
                out.println("</script>");
            }
            request.removeAttribute("fromRegisterUser");
        %>
        <h1>Create an account for free!</h1>
        <form action="RegisterUser" method="POST">
            Username: <input type="text" name="usuario" value="" required="required"/><br>
            Password: <input type="password" name="password" value="" required="required"/><br>
            Confirm Password: <input type="password" name="password2" value="" required="required"/><br>
            <input type="reset" value="Clear" />
            <input type="submit" value="Register" />
        </form>
    </body>
</html>
