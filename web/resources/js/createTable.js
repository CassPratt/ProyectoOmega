
// Function called to add an extra field for the table
function addField(formID){
    var id = "#"+formID+" >.field";
    var num = document.querySelectorAll(id).length+1;   // Number of childs
    var newField = "<br id=\"br"+num+"\"><label class='field' id=\"labelValue"+num+"\">Field Name: </label><input id=\"nameField"+num+"\" type='text' name=\"nameField"+num+"\" value='' required=\"required\"/> "
            +"<label id=\"labelType"+num+"\">Type: </label><select id=\"typeField"+num+"\" name=\"typeField"+num+"\">"
            +"<option>VARCHAR(20)</option><option>INT</option><option>DOUBLE</option>"
            +"<option>CHAR</option><option>BOOLEAN</option></select>";
   // Add at the end of the form "formCreate"
   document.getElementById(formID).insertAdjacentHTML('beforeend',newField);
}

// Function called to remove a field (the last one)
function removeField(formID){
    var id = "#"+formID+" >.field";
    var num = document.querySelectorAll(id).length; // Number of childs
    // USING JQUERY, FIND ANOTHER WAY TO DO IT WITHOUT IT <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    if(num>1){
        $("#formCreate").children("label").last().remove();
        $("#formCreate").children("label").last().remove();
        $("#formCreate").children("input").last().remove();
        $("#formCreate").children("select").last().remove();
        $("#formCreate").children("br").last().remove();
    }
}