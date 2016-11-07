function getNumMatchesInBox() {
    return $(".lbl").length;
}

function addMatchingBox(questionId) {
    var numMatches = getNumMatchesInBox();

    var html =
        '<div class="row align-justify">' +
        '<div class="columns large-5">' +
        '<input class="matching lbl" type="text" id="label-'+numMatches+'" placeholder="Add Label text">' +
        '</div>' +
        '<div class="columns large-5">' +
        '<input class="matching answer" type="text" id="answer-'+numMatches+'" placeholder="Add Answer text">' +
        '</div>' +
        '<div class="columns large-1"> ' +
        '<a><span class="icon-trash"></span></a>' +
        '</div>' +
        '</div>';

    $("#question-parts-div").append(html);

    if (numMatches < 2) {
        $("#label-"+numMatches).addClass('required');
        $("#answer-"+numMatches).addClass('required');
    }
}

/***************************************************/
/* When you begin typing in a label or answer box,
 /* add another choice box after it (up to 5 total)
 /***************************************************/
$(document).on('keydown', '.lbl, .answer', function(event) {
    // get the total number of choice boxes
    var numMatches = getNumMatchesInBox();

    // get the current, next, and last choice boxes in the list
    var currentMatchNumber = $(this).attr('id').split("-")[1];
    var nextMatchNumber = (parseInt(currentMatchNumber)+1).toString();
    var lastMatchNumber = (parseInt(numMatches)-1).toString();

    // if this is the ENTER or TAB key, jump to the next choice box
    if (event.keyCode == '13' || event.keyCode == '9') {
        event.preventDefault();

        if ($(this).hasClass("lbl")) {
            $("#answer-" + currentMatchNumber).focus();
        } else {
            console.log(nextMatchNumber);
            $("#label-" + nextMatchNumber).focus();
        }
    }

    // if there are less than 6 choices already...
    if (numMatches < 6){
        // ... and the current box is at the end of the list...
        if (currentMatchNumber == lastMatchNumber) {
            // ... and you're inside an answer box...
            if ($(this).hasClass("answer")) {
                // ... add a new box
                addMatchingBox();
            }
        }
    }
});




$("*[data-type='matching']").on("click", function() {
    //addButtonsToContentPanel();

    // call each box a "Match"
    $("#parts-label").html("Matches");

    // clear prompt text
    $("#prompt").val("");

    // begin with two choices by default
    addMatchingBox();
    addMatchingBox();
});


/*
 * Create JSON object for the question and post it to the database
 */
function submitMatchingQuestionJSON(exerciseId) {
    var question = newQuestion(exerciseId, "matching");
    question.prompt = $("#prompt").val();


    // MC questions have multiple QuestionParts
    var questionParts = [];

    $(".lbl").each(function() {
        // don't use a matching question with an empty label or empty prompt
        var labelValue = $.trim($(this).val());
        var lblNumber = $(this).attr('id').split("-")[1];

        var answerValue = $.trim($("#answer-"+lblNumber).val());

        if (labelValue) {
            // Each Matching question has only one choice, the correct answer
            var choices = [];
            choices.push(newChoice(answerValue, true));
            questionParts.push(newQuestionPart(labelValue, choices));
        }

    });

    question.questionParts = questionParts;

    //submit question to the database
    submitQuestionToDatabase(question);
}



/*
 * Edit a question from a JSON object and post it to the database
 */
function submitMatchingQuestionJSON_edit(exerciseId, groupId) {
    var myObject = {};

    //add exercise id
    myObject.exercise = exerciseId;
    myObject.group = groupId.split("group-")[1];

    // Matching Questions are all contained in one QuestionGroup
    myObject.qgPrompt = $(".prompt").val();
    myObject.type = "Matching";

    console.log(myObject);

    // Matching has many subquestions inside one array
    var questions = [];


    $(".lbl").each(function() {
        var $fill = $(this).parent().parent();
        var $lbl = $(this);
        var $prompt = $.trim($lbl.val());

        var questionObject = {};

        var questionId = $fill.attr("id");
        if (questionId) {
            questionObject.id = questionId.split("question-")[1];
        }

        if ($prompt.length > 0) {
            questionObject.prompt = $prompt;

            // Each Matching question has only one choice, the correct answer
            var choices = [];
            var choicesObject = {};

            $fill.find(".answer").each(function() {
                var $answer = $(this);
                var $choice = $.trim($answer.val());
                var choiceId = $answer.parent().attr("id");
                if (choiceId) {
                    choicesObject.id = choiceId.split("choice-")[1];
                }

                if ($answer.length > 0) {
                    choicesObject.choice = $choice;
                    choicesObject.correct = true;
                    choices.push(choicesObject);
                }
            });

            questionObject.choices = choices;
            questions.push(questionObject);
        }  else if (questionId) {
            questionObject.deleted = true;
            questions.push(questionObject);
        }
    });

    myObject.questions = questions;

    //submit question to the database
    var jsonMsg = JSON.stringify(myObject);

    console.log(jsonMsg);
    jsRoutes.controllers.MainQuestionController.editQuestionGroupFromJSON(jsonMsg).ajax({
        type : 'POST',
        dataType : 'json',
        contentType : 'application/json; charset=utf-8',
        data : jsonMsg,
        success : function(questionId) {
            parent.closeModal();

            rewriteMatchingQuestionInList(myObject, questionId);
        }
    });
}


function formatMatchingQuestion(q) {
    addQuestionAccordion(q);

    $.each(q.questionParts, function (key, part) {
        $.each(part.choices, function (index, choice) {
            var $row = $("<tr></tr>");

            $row.append("" +
                "<td>"+part.prompt+"</td>" +
                "<td>"+choice.choice+"</td>"
            );

            $("#"+ q.id).find("table").append($row);
        });

    });
}