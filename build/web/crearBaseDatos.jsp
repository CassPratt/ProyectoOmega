<%-- 
    Document   : crearBaseDatos
    Created on : May 3, 2016, 9:20:15 AM
    Author     : EUCJ
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<html>

    <head>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
        <title>TODO supply a title</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>

    <body>

        <form id="formaBaseDatos" action="creacionBaseDatos" method="POST">
            <h1>Crear base de datos</h1>
            <label>Nombre de la base de datos</label>
            <input type="text" name="nombre" value="" />
            <label>Usuario</label>
            <input type="text" name="usuario" value="" />
            <label>Contraseña</label>
            <input type="text" name="contrasenia" value="" />
            <input type="submit" value="Crear base" id="button" />
        </form>
        
        
        <form action="creacionBaseDatos" method="GET">
    <input type="submit" value="Crear tablas">
</form>
        
     

</html>
