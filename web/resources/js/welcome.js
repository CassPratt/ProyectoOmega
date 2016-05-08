/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function checkUserDB(id,username,target) {
    var ajaxRequest;
    if (window.XMLHttpRequest){
        ajaxRequest=new XMLHttpRequest(); // IE7+, Firefox, Chrome, Opera, Safari
    } else {
        ajaxRequest=new ActiveXObject("Microsoft.XMLHTTP"); // IE6, IE5
    }
    ajaxRequest.onreadystatechange = function(){
        if (ajaxRequest.readyState===4 && ajaxRequest.status===200){
            document.getElementById(id).innerHTML=ajaxRequest.responseText;
        }
    };
    ajaxRequest.open("GET", target+"?="+username, true /*async*/);
    ajaxRequest.send();
}

function clickAdministrate(divID){
    //alert('click en '+divID);
    var opciones = "<button type='button'class=\"btn btn-default btn-sm\">"
                   +"<span class=\"glyphicon glyphicon-file\" aria-hidden=\"true\">Create Table</span>"
                   +"</button><button type='button'class=\"btn btn-default btn-sm\">"
                   +"<span class=\"glyphicon glyphicon-pencil\" aria-hidden=\"true\">Modify Tables</span>"
                   +"</button>"+ "</div>";
    document.getElementById(divID).innerHTML = opciones;
}