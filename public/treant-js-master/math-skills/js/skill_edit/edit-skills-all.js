
function getDraggableOptions(){
  var draggableOptions = {};
  draggableOptions.revert = true;
  draggableOptions.stop = function(){
    $('[id^="group_"]').css({"background-color":"#FFFFFF"})
  };
  draggableOptions.start = function(){
    console.log($('[id^="group_"]'));
    $('[id^="group_"]').css({"background-color":"#F0E68C"})
  };
  return draggableOptions;
}


// init methods
function initAll(){
  $.getJSON("https://s3.amazonaws.com/remind/Math/Trees/CaringTree.json").success(function(data){
	  chart_config_all["nodeStructure"] = data["nodeStructure"];
	  // redraw the tree
	  tree_all = new Treant(chart_config_all);
	  initCreateButtons();
	  initTree(chart_config_all.nodeStructure);
	  initSaveButtons();
	  initQuestionUi();
  });

}

function initSaveButtons(){
	$("#saveTree").click(function(){
		nodeStructure = {"nodeStructure": chart_config_all.nodeStructure };
		$.ajax({
			 headers: { 
			        'Accept': 'application/json',
			        'Content-Type': 'application/json' 
			},
		    url : "/math/demo/tree",
		    type: "post",
		    dataType: "json",
		    data : JSON.stringify(nodeStructure),
		    success: function(data, textStatus, jqXHR)
		    {
		    	alert("SUCCESS! tree saved to s3: "+data);
		        // raise success dialog
		    },
		    error: function (jqXHR, textStatus, errorThrown)
		    {
		    	alert("FAILURE! tree not saved anywhere: "+errorThrown);
		    	// raise error dialog
		    }
		});
	});
	
}

function initCreateButtons(){
  $("#create-skill-group").click(function(){
    $("#create-skill-group-dialog").dialog("open");
  });
  $("#create-skill").click(function(){
    $("#create-skill-dialog").dialog("open");
  });
  
}

function initUnmappedGroups(){
  $("#unmappedGroups").html('');
  
  for (skillGroup of unmappedSkillGroups){
    $("#unmappedGroups").append('<div id="'+skillGroup.HTMLid+'" class="unmapped">'+skillGroup.text.name+'</div>');
    $("#"+skillGroup.HTMLid).draggable(getDraggableOptions());
  }
}

function initUnmappedSkills(){
  $("#unmappedSkills").html('');
  
  for (skill of unmappedSkills){
    $("#unmappedSkills").append('<div id="'+skill.HTMLid+'" class="unmapped">'+skill.text.name+'</div>');
    $("#"+skill.HTMLid).draggable(getDraggableOptions());
  }
}

function initTree(item){
  if (typeof item.children != 'undefined'){
    for (child of item.children){
// need way to identify which one was clicked so I can auto move skill or group
// here
      $("#"+item.HTMLid).click(function(){
        $("#clicked-skill-group-dialog").dialog("open");
      });
      initTree(child);
    }
    $("#"+item.HTMLid).droppable({
      drop: dropped,
      hoverClass: 'hovered'
    });
  }
  else{
    $("#"+item.HTMLid).click(function(){
    	lastClickedSkill=item.HTMLid;
      $("#create-question-dialog").dialog("open");
    });
  }
  
// create methods
function createSkillGroup(id, name){
  var skillGroup = {};
  skillGroup.HTMLid = "group_"+id;
  skillGroup.text = {};
  skillGroup.text.name=name;
  
  $("#unmappedGroups").append('<div id="'+skillGroup.HTMLid+'" class="unmapped">'+skillGroup.text.name+'</div>');
 
  $("#"+skillGroup.HTMLid).draggable(getDraggableOptions());
  unmappedSkillGroups.push(skillGroup);
}

function createSkill(id, name){
  var skill = {};
  skill.HTMLid = "skill_"+id;
  skill.text = {};
  skill.text.name=name;
  
  $("#unmappedSkills").append('<div id="'+skill.HTMLid+'" class="unmapped">'+skill.text.name+'</div>');
 
  $("#"+skill.HTMLid).draggable(getDraggableOptions());
  unmappedSkills.push(skill);
}


function delByIdUnmappedGroups(id){
  for (var i=0; i < unmappedSkillGroups.length; i++){
    if (unmappedSkillGroups[i].HTMLid == id){
      unmappedSkillGroups.splice(i, 1);
      break;
    }
  }
}
function delByIdUnmappedSkills(id){
  for (var i=0; i < unmappedSkills.length; i++){
    if (unmappedSkills[i].HTMLid == id){
      unmappedSkills.splice(i, 1);
      break;
    }
  }
}


function dropped(event, ui){
  alert("Successfully Added to Skill Tree");
  // add to json
  var needle = $(this).context.id; 
  var skillNode = {};
  // add id
  skillNode.HTMLid = ui.draggable.context.id;
  skillNode.text = {};
  // add name
  skillNode.text.name=ui.draggable.context.innerText;
  
  if (skillNode.HTMLid.startsWith("group_")){
    skillNode.children = [];
    
    var skipLevelNode={};
    skipLevelNode.HTMLid="skipTo_"+skillNode.HTMLid;
    skipLevelNode.pseudo = true;
    skipLevelNode.children = [];
    skipLevelNode.children.push(skillNode);

    addToSkillMap(needle, skipLevelNode, chart_config_all.nodeStructure);
  }
  else{
    addToSkillMap(needle, skillNode, chart_config_all.nodeStructure);  
  }
  
  
  // remove from unmapped skill groups
  if (skillNode.HTMLid.startsWith("group_")){
    delByIdUnmappedGroups(skillNode.HTMLid);
    initUnmappedGroups();
  }
  else{
    delByIdUnmappedSkills(skillNode.HTMLid);
    initUnmappedSkills();
  }
    
// redraw tree
  tree_all = new Treant(chart_config_all);
  initTree(chart_config_all.nodeStructure);
  
}




function addToSkillMap(parentId, addNode, currentNode){
  if (parentId == currentNode.HTMLid){
    if (currentNode.children == null){
      currentNode.children = [];
    }
    currentNode.children.push(addNode);
  }
  else{
    if (currentNode.children != null){
      for (child of currentNode.children){
        addToSkillMap(parentId, addNode, child);
      }  
    }
  }
}





  
// dialog actions
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
                $(this).dialog("close");
                // upload image to s3 webservice call
                // add to global unmapped skill groups
                var skillGroupName=$("#skill-group-name").val().replace(" ", "_");
                createSkillGroup(skillGroupName,skillGroupName);
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
             $(this).dialog("close");
             // upload image to s3 webservice call
             // add to global unmapped skill groups
             var skillName=$("#skill-name").val().replace(" ", "_");
             createSkill(skillName,skillName);            
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
        	   submitQuestion();            
           },
           "Cancel" : function() {
             $(this).dialog("close");
           }
         }
       });
  });
  
}