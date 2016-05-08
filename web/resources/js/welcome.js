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

function addCreateTableOptions(divID){
    var createOptions = "<h4>Create new table</h4>";
    document.getElementById(divID).innerHTML = createOptions;
}

function addModifyTableOptions(divID){
    var createOptions = "<h4>Modify Table</h4>";
    document.getElementById(divID).innerHTML = createOptions;
}

function clickAdministrate(divID){
    //alert('click en '+divID);
    var options = "<button type='button'class=\"btn btn-default btn-sm\" onclick='addCreateTableOptions(\"options"+divID+"\")'>"
                   +"<span class=\"glyphicon glyphicon-file\" aria-hidden=\"true\">Create Table</span>"
                   +"</button><button type='button'class=\"btn btn-default btn-sm\" onclick='addModifyTableOptions(\"options"+divID+"\")'>"
                   +"<span class=\"glyphicon glyphicon-pencil\" aria-hidden=\"true\">Modify Tables</span>"
                   +"</button><div id='options"+divID+"'></div>";
    document.getElementById(divID).innerHTML = options;
}