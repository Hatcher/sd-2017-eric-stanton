var numRules = 1;
function initAddRuleButton() {
	$("#add-rule").click(function() {
		numRules = numRules + 1;
		var rule1 = $("#rule1").clone();
		console.log(rule1);
		
		var rules = $("#rules-row");
		
		rule1.prop("id", "rule"+numRules);
		
		rules.append(rule1);
		
		
		
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
	
	console.log("questionName: "+questionJson.questionName);
	
	//TODO images, labels and rules not yet implemented
//	questionJson.imageUrl = $("#image-url").val();
//	questionJson.rules = [];
//	questionJson.labels = [];
	
	// post to backend
	
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
	        alert("Question added to skill");
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
	