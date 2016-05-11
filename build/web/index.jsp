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
        <link href="resources/bootstrap-3.3.6-dist/css/bootstrap.css" rel="stylesheet">
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
            String isLogged = (String)request.getAttribute("isLogged");
            if(isLogged!=null&&isLogged.equals("NO")){
                out.println("<script type=\"text/javascript\">");
                out.println("alert('You need to login');");
                out.println("</script>");
            }
            request.removeAttribute("isLogged");
        %>
        
        <h1>DataWeb Wizard</h1>
        <h3>Login into your account to experience the magic!</h3>
        
        <!---------------- LOGIN FORM ---------------->
        <form action="Login" method="POST">
            Username: <input type="text" name="username" value="" required="required" /> <br>
            Password: <input type="password" name="password" value="" required="required"/><br>
            <input type="submit" value="Login" class="btn btn-default"/>
        </form>
        
        <!---------------- SIGN UP FORM ---------------->
        <form action="signup.jsp" method="POST">
            <br>Not registered yet?
            <input type="submit" value="Sign Up" class="btn btn-default"/>
        </form>
    </body>
</html>
