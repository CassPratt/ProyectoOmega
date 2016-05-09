
// Giving style from the beginning
function init(){
    $(".optionsDB").hide();
    $(".btnOptions").click(function(event){
        var nombre = $(this).attr('id');
        var divID = "#options"+nombre.toString().substring(3);
        $(divID).toggle(700);
    });
}

// AJAX function called for showing the user databases
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
            //Cuando se acaba de ejecutar el AJAX se tienen todos los componentes
            //y se agregan los listeners
            init();
        }
    };
    ajaxRequest.open("GET", target+"?="+username, true /*async*/);
    ajaxRequest.send();
}