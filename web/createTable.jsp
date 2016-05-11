<%-- 
    Document   : createTable
    Created on : May 8, 2016, 3:26:57 PM
    Author     : CassPratt
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="resources/bootstrap-3.3.6-dist/css/bootstrap.css" rel="stylesheet">
        <script type="text/javascript" src="resources/js/createTable.js"></script>
        <title>Create Table</title>
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
        
        <h2>Create new table on <%=dbName%></h2>
        <form id="formCreate" action="CreateTable" method="POST">
            <!-------------- USERNAME AND PASSWORD ARE REQUIRED -------------->
            <label>Username:</label><input type="text" name="usuario" value="" required="required"/><br>
            <label>Password</label><input type="password" name="password" value="" required="required"/><br><br>
            
            <!-------------- EDIT FIELDS BUTTONS -------------->
            <input type="reset" value="Clear all fields"/><br><br>
            <input type="button" value="Add Field" onclick="addField('formCreate')"/>
            <input type="button" value="Remove Field" onclick="removeField('formCreate')"/>
            <input type="submit" value="Create Table"/><br><br>
            
            <h4>The first field will be the primary key.</h4><br>
            <div id="row1" class="field">
               <!-------------- FIELDS OF THE NEW TABLE -------------->
               <label>Table Name:</label><input type="text" name="tableName" value="" required="required"/><br>
               <label id="labelValue1">Field Name: </label><input id="nameField1" type="text" name="nameField1" value="" required="required"/> 
               <label id="labelType1">Type: </label><select id="typeField1" name="typeField1">
                   <option>VARCHAR(20)</option>
                   <option>INT</option>
                   <option>DOUBLE</option>
                   <option>CHAR</option>
                   <option>BOOLEAN</option>
               </select>
            </div>
        </form>
        
    </body>
</html>
