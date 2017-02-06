function initAll(){
  console.log(chart_config_all);
  
  initItem(chart_config_all.nodeStructure);
  
  console.log("complete!");
}
function initItem(item){
  console.log(item);
  if (item.children != null){
    for (child of item.children){
// need way to identify which one was clicked
      $("#"+item.HTMLid).click(function(){
        $("#clicked-skill-group-dialog").dialog("open");
      });
      
      
      initItem(child);
    }
  }
  else{
    $("#"+item.HTMLid).click(function(){
      $("#create-question-dialog").dialog("open");
    });
  }
  
$(function() {
  $("#clicked-skill-group-dialog").dialog({
    autoOpen: false,
    modal: true,
    width: "550px",
    buttons : {
         "Create Skill Group" : function() {
           $(this).dialog("close");
           $("#create-skill-group-dialog").dialog("open");
         },
         "Create Skill" : function() {
           $(this).dialog("close");
           $("#create-skill-dialog").dialog("open");
         }
       }
     });
  
    $("#create-skill-group-dialog").dialog({
       autoOpen: false,
       modal: true,
       width: "550px",
       buttons : {
            "Confirm" : function() {
                alert("You have confirmed!");            
            },
            "Cancel" : function() {
              $(this).dialog("close");
            }
          }
        });

    $("#create-skill-dialog").dialog({
      autoOpen: false,
      modal: true,
      width: "550px",
      buttons : {
           "Confirm" : function() {
               alert("You have confirmed!");            
           },
           "Cancel" : function() {
             $(this).dialog("close");
           }
         }
       });
    
    $("#create-question-dialog").dialog({
      autoOpen: false,
      modal: true,
      width: "550px",
      buttons : {
           "Confirm" : function() {
               alert("You have confirmed!");            
           },
           "Cancel" : function() {
             $(this).dialog("close");
           }
         }
       });
  });
  
}