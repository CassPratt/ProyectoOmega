/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
// Giving style from the beginning
function init(){
    var opDB = document.querySelectorAll(".optionsDB");
    for(var i=0;i<opDB.length;i++){
        opDB[i].style.display = 'none';
    }
    var btnOp = document.querySelectorAll(".btnOptions");
    for(var i=0;i<btnOp.length;i++){
        btnOp[i].onclick = function(){
            var nombre = this.id;
            var divID = "options"+nombre.toString().substring(3);
            var divOp = document.getElementById(divID);
            if(divOp.style.display==="none"){
                divOp.style.display = '';
            }else{
                divOp.style.display = 'none';
            }
            
        };
    }
}

// AJAX function called for showing the databases from a table
function showDBTables(id,username,servlet,dbName) {
    var ajaxRequest;
    if (window.XMLHttpRequest){
        ajaxRequest=new XMLHttpRequest(); // IE7+, Firefox, Chrome, Opera, Safari
    } else {
        ajaxRequest=new ActiveXObject("Microsoft.XMLHTTP"); // IE6, IE5
    }
    ajaxRequest.onreadystatechange = function(){
        if (ajaxRequest.readyState===4 && ajaxRequest.status===200){
            document.getElementById(id).innerHTML=ajaxRequest.responseText;
            //Cuando se acaba de ejecutar el AJAX se tienen todos los componentes
            //y se agregan los listeners
            //init();
        }
    };
    ajaxRequest.open("GET", servlet+"?username="+username+"&dbName="+dbName, true /*async*/);
    ajaxRequest.send();
}