var numRules = 1;
var numVariables = 0;
var questionJson = {};

function initQuestionUi(){
	initAddRuleButton();
	initAddVariableButton();
	initUploadButton();
	initLabelingDialog();
	initQuestionJson();
}

function initAddRuleButton() {
	$("#add-rule").click(function() {
		numRules = numRules + 1;
		var rule1 = $("#rule-item-no1").clone();
		var rules = $("#rules-row");
		rule1.prop("id", "rule-item-no"+numRules);
		rules.append(rule1);
	});
}

function initAddVariableButton(){
	$("#add-variable").click(function() {
		var variable = "${"+String.fromCharCode(97 + numVariables)+"}";
		numVariables = numVariables + 1;
		$('#equation').val($('#equation').val() + ""+variable);
	});
}

function initLabelingDialog(){
	$("#create-labels-dialog").dialog({
	    autoOpen: false,
	    modal: true,
	    width: "550px",
	    buttons : {
	         "Confirm" : function() {
	             $(this).dialog("close");
	         },
	         "Cancel" : function() {
	           $(this).dialog("close");
	         }
	       }
	     });
	
	$("#image-labels-button").click(function(){
		$("#label-me-image").attr("src", $('img#upload-image').prop("src"));
		
		// get labels from equation and append as draggables
		var equationString = $("#equation").val();
		// break up into variables
		console.log(equationString);
		
		var matchString;
		matchString = equationString.match(/\${([a-zA-Z]+)}/g);
		console.log(matchString);	
		
		
		for (var label of matchString){
			var labelPrefix = "draggable-label-";
			var labelSuffix = getVariableFromHolder(label);
			var labelId = labelPrefix + labelSuffix;
			
			$("#labels-starting-point").append("<br/><div id =\""+labelId+"\">"+label+"</div>")
			$("#"+labelId).draggable(getDraggableOptionsForLabeling());
		}
		
    
		$("#create-labels-dialog").dialog("open");
  });
		  
}

function getVariableFromHolder(holder){
	return holder.replace("$", "").replace("{", "").replace("}", "");
}

function initUploadButton(){
	
	var thumb = $('img#upload-image');        

		  new AjaxUpload('image-upload-input', {
		    action: $('form#upload-image-form').attr('action'),
		    name: 'image',
		    onComplete: function(file, response) {
		    	sourceString = $.parseHTML( response ),
		    	  nodeNames = [];
		    	thumb.attr('src', sourceString[0].textContent);
		    }
		  });
}

function initQuestionJson(){
	console.log('INIT');
	questionJson = {};
	questionJson.labels = []
}

function submitQuestion(){
	// ajax 1: post tree
	saveTree();
	// ajax 2: post question
	// get values from fields
	
	// create json object

	questionJson.questionName = $("#question-name").val();
	questionJson.questionText = $("#question-text").val();
	questionJson.equation = $("#equation").val();
	questionJson.skillId = lastClickedSkill;
	questionJson.rules = [];

	questionJson.image = ""; // image
	
	console.log(questionJson);
	
	$("[id^=rule-item-no]").each(function(index, element){
		var tmpRule = {"rule": $(element).val()}
		questionJson.rules.push(tmpRule);
	});
	
//TODO images, labels not yet implemented
//	questionJson.imageUrl = $("#image-url").val();
	console.log("about to request: ");
	console.log(questionJson);
	var questionRequestBody = {"question": questionJson };
	$.ajax({
		 headers: { 
		        'Accept': 'application/json',
		        'Content-Type': 'application/json' 
		},
	    url : "/math/demo/question",
	    type: "post",
	    dataType: "json",
	    data : JSON.stringify(questionRequestBody),
	    success: function(data, textStatus, jqXHR)
	    {
	    	if (data.response.success==true){
	    		alert("Question added to skill");
	    		$("#create-question-dialog").dialog("close");
	    	}
	    	else{
	    		alert("Question Not Added.  Validation Errors: "+data.response.errors);
	    	}
	    },
	    error: function (jqXHR, textStatus, errorThrown)
	    {
	    	alert("Error.  Tree not saved: "+errorThrown);
	    	// raise error dialog
	    }
	});
}
function saveTree(){
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
	        // user does not need to be notified about successes
	    },
	    error: function (jqXHR, textStatus, errorThrown)
	    {
	    	alert("Error.  Tree not saved: "+errorThrown);
	    	// raise error dialog
	    }
	});
}

function getDraggableOptionsForLabeling(){
	  var draggableOptions = {};
	  draggableOptions.revert = false;
	  draggableOptions.stop = function(){
		  var offset = $(this).offset();
//		  console.log("from left: "+offset.left);
//		  console.log("from top: "+offset.top);
		  var imageOffset = $("#label-me-image").offset();
//		  console.log("image from left: "+imageOffset.left);
//		  console.log("image from top: "+imageOffset.top);
		  
		  var xOffset = offset.left - imageOffset.left;
		  var yOffset = offset.top - imageOffset.top;
//		  console.log("labels relative x,y: "+xOffset+","+yOffset);
		  
		  // how do I get the label name?
//		  console.log($(this));
//		  console.log($(this)[0].innerHTML);
		  // if lands on
		  addOrUpdateLabelToQuestion(getVariableFromHolder($(this)[0].innerHTML),xOffset,yOffset);
		  console.log("added.  question json:");
		  console.log(JSON.stringify(questionJson));
	  }
	  return draggableOptions;
	}
function addOrUpdateLabelToQuestion(name, x, y){
	var label = newLabel(name, x, y);	
	var found = false;
	for (var i = 0; i < questionJson.labels.length; i++){
		if (questionJson.labels[i] == name){
			found = true;
		}
	}
	if (!found){
		questionJson.labels.push(label);
	}
	console.log("added label: ");
	console.log(questionJson);
}
function removeOrIgnoreLabelFromQuestion(name, x, y){
	console.log("removing label");
	var label = newLabel(name, x, y);
	for (var i = 0; i < questionJson.labels.length; i++){
		if (questionJson.labels[i] = name){
			questionJson.labels = questionJson.labels.splice(i);
		}
	}
}
function newLabel(name, x, y){
	var label = {};
	label.name=name;
	label.x = x;
	label.y = y;
	return label;
}

	