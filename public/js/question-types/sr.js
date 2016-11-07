function addSentenceBox() {
	var numSentences = getNumChoicesForQuestion();

    var html =
        '<div class="row align-justify">' +
        '<div class="sentence-num">' + parseInt(numSentences+1) + '</div>' +
        '<div class="columns">' +
        '<input class="choice" type="text" id="sentence-'+numSentences+'" placeholder="Add Sentence">' +
        '</div>' +
        '<div class="columns large-1"> ' +
        '<a><span class="icon-trash"></span></a>' +
        '</div>' +
        '</div>';

	$("#question-parts-div").append(html);

    if (numSentences < 2) {
        $("#sentence-"+numSentences).addClass('required');
    }
}

/***************************************************/
/* When you begin typing in a sentence box,
/* add another sentence box after it (up to 5 total)
/***************************************************/
$(document).on('keydown', '.choice', function(event) {
	// get the total number of sentence boxes
	var numSentences = getNumChoicesForQuestion();
	
	// get the current, next, and last sentence boxes in the list
	var currentSentenceID = $(this).attr('id');
	var nextSentenceID = 'sentence-'+(parseInt(currentSentenceID.split("sentence-")[1])+1).toString();
	var lastSentenceID = 'sentence-'+(parseInt(numSentences)-1).toString();

	// if this is the ENTER or TAB key, jump to the next sentence box
	if (event.keyCode == '13' || event.keyCode == '9') {
		event.preventDefault();
		$("#" + nextSentenceID).focus();
	}

	// if there are less than 6 sentences already...
	if (numSentences < 6){
		// ... and the current box is at the end of the list...
		if (currentSentenceID == lastSentenceID) {
			// ... add a new box
			addSentenceBox();
		}
	}
});

$("*[data-type='sr']").on("click", function() {
    // call each box a "Sentence"
    $("#parts-label").html("Sentences");

    // set prompt text
    $("#prompt").val("Put the following sentences in the correct order.");

	// begin with two sentences by default
	addSentenceBox();
	addSentenceBox();
});

/*
 * Create JSON object for the question and post it to the database
 */
function submitSRQuestionJSON(exerciseId) {
    var question = newQuestion(exerciseId, "sr");
    question.prompt = $("#prompt").val();


    // MC questions have multiple QuestionParts
    var questionParts = [];

    $(".choice").each(function(index) {
        // don't use a matching question with an empty label or empty prompt
        var value = $.trim($(this).val());
        if (value) {
            // Each Matching question has only one choice, the correct answer
            var choices = [];
            choices.push(newChoice((index+1), true));
            questionParts.push(newQuestionPart(value, choices));
        }
    });

    question.questionParts = questionParts;

    //submit question to the database
    submitQuestionToDatabase(question);
}

function addSRQuestionToList(myObject, questionId) {
    var inside = formatSRQuestion(myObject, questionId);

    // increment the counter by 1 and add the new number card
    var num = parseInt($('.number.card').length) + 1;
    var newCount = $('<li class="number card"><div class="v-middle">' + num + '</div></li>');
    $('#not-sortable').append(newCount);
    // reflect this is the total counter at the top of the page
    $("#num-questions").text(num);

    // add a new question card of type 'sr'
    var newQuestion = $('<li class="question type-sr new" id="' + questionId + '">' + inside + '</li>');
    $('#sortable').append(newQuestion);
}

function formatSRQuestion(q) {
    addQuestionAccordion(q);

    $.each(q.questionParts, function (key, part) {
        $.each(part.choices, function (index, choice) {
            var $row = $("<tr></tr>");

            $row.append("" +
                "<td>"+part.prompt+"</td>" +
                "<td>"+choice.choice+"</td>"
            );

            $("#"+q.id).find("table").append($row);
        });

    });
}