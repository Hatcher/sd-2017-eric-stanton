var numRules = 1;
var numVariables = 0;
function initQuestionUi(){
	initAddRuleButton();
	initAddVariableButton();
	initUploadButton();
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



function submitQuestion(){
	// ajax 1: post tree
	saveTree();
	// ajax 2: post question
	// get values from fields
	
	// create json object
	var questionJson = {};
	questionJson.questionName = $("#question-name").val();
	questionJson.questionText = $("#question-text").val();
	questionJson.equation = $("#equation").val();
	questionJson.skillId = lastClickedSkill;
	questionJson.rules = [];
	
	$("[id^=rule-item-no]").each(function(index, element){
		var tmpRule = {"rule": $(element).val()}
		questionJson.rules.push(tmpRule);
	});
	
//TODO images, labels not yet implemented
//	questionJson.imageUrl = $("#image-url").val();
//	questionJson.labels = [];
	
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
	