/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

// AJAX function called for showing the databases from a table
function setDivContent(divID,username,servlet,dbName,tableName) {
    var ajaxRequest;
    if (window.XMLHttpRequest){
        ajaxRequest=new XMLHttpRequest(); // IE7+, Firefox, Chrome, Opera, Safari
    } else {
        ajaxRequest=new ActiveXObject("Microsoft.XMLHTTP"); // IE6, IE5
    }
    ajaxRequest.onreadystatechange = function(){
        if (ajaxRequest.readyState===4 && ajaxRequest.status===200){
            document.getElementById(divID).innerHTML=ajaxRequest.responseText;
            //Cuando se acaba de ejecutar el AJAX se tienen todos los componentes
            //y se agregan los listeners
            //init();
        }
    };
    ajaxRequest.open("GET", servlet+"?username="+username+"&dbName="+dbName+"&tableName="+tableName, true /*async*/);
    ajaxRequest.send();
}
