<%-- 
    Document   : index
    Created on : May 1, 2016, 1:13:30 PM
    Author     : miguelcasillas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>DataWeb Wizard</title>
    </head>
    <body>
        <%
            /* Flag to check if new user was registered successfully,
               obtained from RegisterUser servlet */
            String fromRU = (String)request.getAttribute("fromRegisterUser");
            if(fromRU!=null&&fromRU.equals("YES")){
                out.println("<script type=\"text/javascript\">");
                out.println("alert('User registered');");
                out.println("</script>");
            }
            request.removeAttribute("fromRegisterUser");
            
            /* Flag to check if login data was correct,
               obtained from Login servlet.
               - Username exists
               - Username and password match registers in DB */
            String loginSuccess = (String)request.getAttribute("loginSuccess");
            if(loginSuccess!=null&&loginSuccess.equals("NO")){
                out.println("<script type=\"text/javascript\">");
                out.println("alert('Wrong username or password');");
                out.println("</script>");
            }
            request.removeAttribute("loginSuccess");
        %>
        <h1>DataWeb Wizard</h1>
        <h3>Login into your account to experience the magic!</h3>
        <form action="Login" method="POST">
            Username: <input type="text" name="usuario" value="" required="required" /> <br>
            Password: <input type="password" name="password" value="" required="required"/><br>
            <input type="submit" value="Login" />
        </form>
        <form action="signup.jsp" method="POST">
            <br>Not registered yet?
            <input type="submit" value="Sign Up" />
        </form>
    </body>
</html>
