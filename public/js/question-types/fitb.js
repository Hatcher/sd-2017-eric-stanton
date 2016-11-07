var numBlanks = 0;
// we save the original passage in its entirety to more easily submit it as Content
var passage = null;
var passageSplit = null;
var currentIndex = 0;

/* FITB questions don't have a cancel button, so they need a special fn */
function addFITBTextInputToContentPanel() {
	$("#content-panel").html(
		'<textarea class="textbox" id="fitb-passage" placeholder="Input passage text here"></textarea>'+
		'<div>'+
		'<div style="float:left"><button class="save button">Submit</button></div>'+
		'</div>'
	);
}

/* Replace the content panel with an edit text button when text is submitted */
function addFITBButtonsToContentPanel() {
	$("#content-panel").html(
		'<span class="icon-pencil"></span> Edit Text'
	);
}

/* When FITB question is chosen, inject proper elements into the modal */
$("*[data-type='fitb']").on("click", function() {
	console.log("FITB");
	
	$(".prompt").val("Select the best word for each of the following blanks.");
	
	addFITBTextInputToContentPanel();
});
			
/* When the passage is submitted, rip the text apart into paragraphs and display those paragraphs with word-selectors for the FITB questions. */
$(document).on("click", ".save", function() {
	// save the passage from the textarea and split it into paragraphs
	passage = document.getElementById('fitb-passage').value;
	passageSplit = passage.split("\n");
	
	var html = '<div class="fitb-container">';
	
	// put all the passage text into <p> tags and add it to the .choices html
	for (var i = 0; i < passageSplit.length; i++) {
		if ($.trim(passageSplit[i]).length > 0) {
			html += ('<p class="word_split">' + passageSplit[i] + '</p>');
		}
	}
	
	html += '</div>'
	
	$('.choices').append(html);
	
	// now put every word inside a word span so the hover effects/question creation can apply inside the passage
	$('.word_split').html(function(index, oldHtml) {
		// apparently \s isn't recognized but enumerating whitespace characters seems ok
		return oldHtml.replace(/\b([^\r\n\t\f ]+?)\b/g, '<span class="word">$1</span>');
	});
	// reset order index in case the modal has been closed and opened again
	currentIndex = 0;

	// set the order indices of each word in the passage
	var words = document.getElementsByClassName('word');
	for (var i = 0; i < words.length; i++) {
		words[i].setAttribute("index", currentIndex++);
	}

	// display the question creation view instead of the text entry one now
	addFITBButtonsToContentPanel();
	$('.choices').toggle(true);
	var submit = document.getElementById('submit-fitb');
	submit.style.visibility = "visible";
});

// multiselect for FITB
$(function () {
	var isMouseDown = false, isHighlighted;

	var $start = "";
	var $end = "";
	var top = -1;
	
	$(document)
		// click on a word and start dragging
		.on("mousedown", ".word", function () {
			$start = $(this);
			$end = $start;

			top = $start.position().top;

			isMouseDown = true;
			$(this).toggleClass("highlighted");
			isHighlighted = $(this).hasClass("highlighted");
			return false; // prevent text selection
		})
		// drag to the next word
		.on("mouseover", ".word", function () {
			// only allow dragging horizontally
			if (isMouseDown && $(this).position().top == top) {
				// add highlight when forward dragging,
				// remove highlight when backwards dragging
				if ($(this).position().left > $end.position().left) {
//					$(this).addClass("highlighted");
					$start.andSelf().nextUntil($(this)).andSelf().add($(this)).addClass("highlighted");
				} else {
					$(this).andSelf().nextUntil($end).andSelf().add($end).removeClass("highlighted");
				}
				$end = $(this);
			}
		})

		// don't register if you've clicked on anything other than a word
		.on("mousedown", "*:not(.word)", function () {
			$start = null;
		})

		// if you drag over an existing blank, release the drag
		.on("mouseover", ".blank", function () {
			isMouseDown = false;
		})

		// finish the drag
		.on("mouseup", function() {
			isMouseDown = false;

			$start = $( ".highlighted" ).first();
			$end = $( ".highlighted" ).last();
			var start_index = $start.attr("index");
			var end_index = $end.attr("index");

			// if this is the end of a drag that was started
			if ($start.hasClass("word")) {
				// wrap the selected word(s) in a div
				if ($start.position().left == $end.position().left) {
					// single word
					$start.wrap("<div class='word-selector' id='selected-"+numBlanks+"' start-index='"+start_index+"' end-index='"+end_index+"' />");
				} else {
					// all highlighted words
					$($start).andSelf().nextUntil($end).andSelf().add($end).wrapAll("<div class='word-selector' id='selected-"+numBlanks+"' start-index='"+start_index+"' end-index='"+end_index+"' />");
				}

				var $blank = $('#selected-'+numBlanks);

				// reconstruct the words inside the blank with spaces in-between
				var blankText = "";
				var words = $blank.first().children();

				for (var i = 0; i < words.length - 1; i++) {
					blankText += words[i].innerHTML + " ";
				}

				blankText += words[words.length - 1].innerHTML;

				// replace the div of highlighted words with a dropdown menu
				// whose display text is all the selected words
				$blank.html(
					'<div class="dropdown">'+
					'<a onclick="openDropdown('+numBlanks+')" class="button blank">'+blankText+'</a>'+
					'<div id="blank-'+numBlanks+'" class="dropdown-content"><a onclick="removeDropdown('+numBlanks+')" class="close-dropdown">remove &#215;</div></div>'+
					'</div>'
				);

				// once the dropdown has been created, add a distractor box...
				addFITBChoiceBox(numBlanks);
				// and then open it
				openDropdown(numBlanks);

				numBlanks++;
			}
		});
});

/* delete a dropdown (identified by selected-numBlanks) from the HTML */
function removeDropdown(numBlanks) {
	// do not update the numBlanks variable; just leave a gap--we'd need to update the IDs of every blank after this one if we changed numBlanks
	var blank = document.getElementById('selected-' + numBlanks);
	var words = (blank.children[0].children[0].innerHTML).split(" ");
	var startingIndex = blank.getAttribute("start-index");

	// replace the dropdown with the first word in a regular span
	var newSpan = document.createElement("span");
	newSpan.innerHTML = words[0];
	newSpan.className = "word";
	newSpan.setAttribute("index", startingIndex++);

	// replace the word and add a space if there are multiple words
	blank.parentNode.replaceChild(newSpan, blank);
	if (words.length > 1) {
		newSpan.parentNode.insertBefore(document.createTextNode("\x20"), newSpan.nextSibling);
		newSpan = newSpan.nextSibling;
	}

	// now, if there are more words to insert, iteratively insert them after the first one
	// keep newSpan around to make insertBefore() easier
	for (var i = 1; i < words.length; i++) {
		var newSpan2 = document.createElement("span");
		newSpan2.innerHTML = words[i];
		newSpan2.className = "word";
		newSpan2.setAttribute("index", startingIndex++);
		// append (if newSpan.nextSibling is null, insertBefore() just appends the new child to the end of the list
		newSpan.parentNode.insertBefore(newSpan2, newSpan.nextSibling);

		// if this is the last iteration of the loop, don't add a new space
		if (i < words.length - 1) {
			console.log("still need a space " + i);
			newSpan.parentNode.insertBefore(document.createTextNode("\x20"), newSpan2.nextSibling);
			// nextSibling can't be null here because we just inserted a new text node
			newSpan = newSpan2.nextSibling;
		}
	}
}

function getNumChoicesInDropdown(blankID) {
	return $("#"+blankID).find(".choice-fitb").length;
}

function addFITBChoiceBox(numBlanks) {
	var numChoices = $("#blank-"+numBlanks+" .choice-fitb").length;

	var html = '<li><div class="fill"><div id="choice-'+ numChoices + '" class="choice-fitb" contenteditable="true"></div></div></li>';
	$("#blank-"+numBlanks).append(html);
}

function openDropdown(numBlanks) {
	var isOpen = $("#blank-"+numBlanks).hasClass('show');
	$('[id^="blank-"]').removeClass("show"); //close all other dropdowns (if open)
	if (isOpen) {
		$("#blank-"+numBlanks).removeClass('show');
	} else {
		$("#blank-"+numBlanks).addClass('show');
	}
}

/***********************************************************/
/* Close the dropdown menu if the user clicks outside of it
 /**********************************************************/
$(document).click(function(e) {
	var container = $(".word-selector");
	if (!container.is(e.target) && container.has(e.target).length === 0) {
		$('[id^="blank-"]').removeClass("show"); //close all other dropdowns (if open)
	}
});

/***************************************************/
/* When you begin typing in a FITB choice box,
/* add another choice box after it (up to 5 total)
/***************************************************/
// note that the function for MC choice boxes listens for all .choice elements inside the .choices div, so to avoid that listener we give these choice boxes a special class .choice-fitb. we could probably do this in a less hacky way, but this works.
$(document).on('keydown', '.choice-fitb', function(event) {
	var $blank = $(this).closest('[id^="blank-"]');
	var blankID = $blank.attr('id');

	// get the total number of choice boxes currently in this dropdown
	var numChoices = getNumChoicesInDropdown(blankID);

	// get the current, next, and last choice boxes in the dropdown
	var currentChoiceID = $(this).attr('id');
	var nextChoiceID = 'choice-'+(parseInt(currentChoiceID.split("choice-")[1])+1).toString();
	var lastChoiceID = 'choice-'+(parseInt(numChoices)-1).toString();

	// if this is the ENTER or TAB key, jump to the next choice box
	if (event.keyCode == '13' || event.keyCode == '9') {
		event.preventDefault();
		$("#"+blankID + " #" + nextChoiceID).focus();
	}

	// if there are less than 5 choices already...
		if (numChoices < 5){
		// ... and the current box is at the end of the dropdown...
		if (currentChoiceID == lastChoiceID) {
			// ... add a new box
			addFITBChoiceBox(blankID.split("-")[1]);
		}
	}
});

// http://stackoverflow.com/questions/5898656/test-if-an-element-contains-a-class
// the .hasClass() function doesn't work on a regular var apparently so here is a workaround
function hasClass(element, classNameToTestFor) {
    var classNames = element.className.split(' ');
    for (var i = 0; i < classNames.length; i++) {
        if (classNames[i].toLowerCase() == classNameToTestFor.toLowerCase()) {
            return true;
        }
    }
    return false;
}

/*
 * Create JSON object for the question and post it to the database
 */
function submitFITBQuestionJSON(exerciseId) {
	var warn = document.getElementById("alert-box");
	var myObject = new Object();
	var hasCorrect = false;

    //add exercise id
    myObject.exercise = exerciseId;

    // FITB questions are all contained in one QuestionGroup
    myObject.qgPrompt = $(".prompt").val();
    myObject.type = "Fill in the Blank";
	myObject.hasSubquestions = true;

	// don't submit FITB with no prompt
	if (myObject.qgPrompt.length <= 0) {
		warn.innerHTML = "Please provide a prompt!";
		warn.style.display = "block";
		return false;
	}


	// ** if some blank has less than two choices

    // FITB has many subquestions inside one array
    var questions = [];
	var blanks = [];
	var dropdowns = document.getElementsByClassName('word-selector');
	
	// every dropdown becomes one Question in this QuestionGroup
	for (var i = 0; i < dropdowns.length; i++) {
		var questionObject = new Object();
		// it is not possible for this prompt to be empty because only nonempty words are counted as word selectors in the initial processing
		questionObject.prompt = dropdowns[i].children[0].children[0].innerHTML;
		
		var blankObject = new Object();
		blankObject.first = dropdowns[i].getAttribute("start-index");
		blankObject.last = dropdowns[i].getAttribute("end-index");

		// collect all the choices for this particular dropdown
		var choices = [];
		// we should NOT be hardcoding the traversal of the document like this
		var wordChoices = dropdowns[i].children[0].children[1].children;
		
		// put the prompt in as the first, correct, answer choice
		var choiceObject = new Object();
		choiceObject.choice = questionObject.prompt;
		choiceObject.correct = true;
		choices.push(choiceObject);
		
		// make each choice in the dropdown into a new choiceObject for this particular Question
		// ignore the close button at the top (start at index 1)
		for (var j = 1; j < wordChoices.length; j++) {
			// accomodate old IE versions
			var str = $.trim(wordChoices[j].children[0].children[0].innerText || wordChoices[j].children[0].children[0].textContent);
			
			// do not take an empty box as a valid Choice
			if (str.length > 0) {
				var choicesObject = new Object();
				
				// a FITB question has only one correct answer
				choicesObject.choice = str;
				choicesObject.correct = false;
				
				choices.push(choicesObject);
			}
		}
		
		// don't submit FITB if some blank has fewer than two choices (total, counting the correct one)
		if (choices.length <= 1) {
			warn.innerHTML = "Every blank needs at least one choice! Please check blank '" + questionObject.prompt + "'.";
			warn.style.display = "block";
			return false;
		}

		questionObject.choices = choices;
		questions.push(questionObject);
		blanks.push(blankObject);
	}

    myObject.questions = questions;
	myObject.blanks = blanks;

	// don't submit a matching question with fewer than two nonempty matches
	if (questions.length <= 0) {
		warn.innerHTML = "Please select at least one blank!";
		warn.style.display = "block";
		return false;
	}

	warn.style.display = "none";

    //submit question to the database
    var jsonMsg = JSON.stringify(myObject);
	
	console.log(jsonMsg);

    jsRoutes.controllers.MainQuestionController.createQuestionGroupFromJSON(jsonMsg).ajax({
        type : 'POST',
        dataType : 'json',
        contentType : 'application/json; charset=utf-8',
        data : jsonMsg,
        success : function(response) {
            parent.closeModal();

            addFITBQuestionToList(myObject);
        }
    });
    
}

/* Dynamically add a newly-created FITB question to the Exercise page's Question list */
function addFITBQuestionToList(myObject) {
	var str = "";

    var questions =  myObject.questions;
    var prompt = myObject.qgPrompt;

    for (var i in questions) {
        var question = questions[i];
        str += question.prompt + " - ";
        for (var j in question.choices) {
            str += question.choices[j].choice + ", ";
        }
    }

    str = str.substring(0, str.length - 2);

    // increment the counter by 1 and add the new number card
    var num = parseInt($('.number.card').length)+1;
    var newCount = $('<li class="number card"><div class="v-middle">'+ num +'</div></li>');
    $('#not-sortable').append(newCount);
    // reflect this in the total counter at the top of the page
    $("#num-questions").text(num);

    // add a new question card of type 'fr'
    var newQuestion = $('<li class="question type-fitb new"><a class="v-middle" href="#">'+prompt +' | <span class="small">'+str+'</span></a></li>');
    $('#sortable').append(newQuestion);

    // focus on this newly-added element
    $("#sortable").focus();
    newQuestion.removeClass('new');
}


$(document).on("click", "#submit-fitb", function(){
    //post question to database
    submitFITBQuestionJSON($(this).data('exerciseId'));
});