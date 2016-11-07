function addAnswerBox(choiceId) {
    var numChoices = getNumChoicesForQuestion();

    var html =
        '<div class="input-group columns large-12"> ' +
        '<input class="input-group-field choice" type="text" id="answer-text-'+numChoices+'" placeholder="Input answer text">' +
        '<div class="input-group-button" style="padding: 0px 20px;"> ' +
        '<a><span class="icon-trash" style="font-size: 1rem;">' +
        '</span>' +
        '</a>' +
        '</div>' +
        '</div>';

    $('#question-parts-div').append(html);

    if (numChoices > 0) {
        $("#answer-text-"+numChoices).attr("placeholder", 'Add another answer?');
    } else {
        $("#answer-text-"+numChoices).addClass('required');
        $("#answer-text-"+numChoices).attr("placeholder", "Input answer text");
    }
}

/***************************************************/
/* When you begin typing in a text box,
 /* add another text box after it (up to 5 total)
 /***************************************************/
$(document).on('keydown', '.choice', function(event) {
    // get the total number of choice boxes
    var numChoices = getNumChoicesForQuestion();

    // get the current, next, and last text boxes in the list
    var currentChoiceID = $(this).attr('id');
    var nextChoiceID = 'answer-text-'+(parseInt(currentChoiceID.split("answer-text-")[1])+1).toString();
    var lastChoiceID = 'answer-text-'+(parseInt(numChoices)-1).toString();

    // if this is the ENTER or TAB key, jump to the next text box
    if (event.keyCode == '13' || event.keyCode == '9') {
        event.preventDefault();
        $("#" + nextChoiceID).focus();
    }

    // if there are less than 6 choices already...
    if (numChoices < 6){
        // ... and the current box is at the end of the list...
        if (currentChoiceID == lastChoiceID) {
            // ... add a new box
            addAnswerBox();
        }
    }
});


$("*[data-type='fr']").on("click", function() {
    console.log("FR OPEN");

    // call each box an "Answer"
    $("#parts-label").html("Answer");

    // clear prompt text
    $("#prompt").val("");

    // begin with two answer boxes by default
    addAnswerBox();
});


/*
 * Create JSON object for the question and post it to the database
 */
function submitFRQuestionJSON(exerciseId) {
    var question = newQuestion(exerciseId, "fr");
    question.prompt = $("#prompt").val();

    // add a new choice for each "choice" box
    var choices = [];

    $(".choice").each(function() {
        var value = $.trim($(this).val());
        if (value) {
            choices.push(newChoice(value, true));
        }
    });

    // FR questions have only a single QuestionPart
    var questionParts = [];
    questionParts.push(newQuestionPart("", choices));
    question.questionParts = questionParts;

    console.log(question);

    //submit question to the database
    submitQuestionToDatabase(question);
}



function formatFRQuestion(q) {
    addQuestionAccordion(q);

    $.each(q.questionParts, function (key, part) {
        var choices = "";
        $.each(part.choices, function (index, choice) {
            choices += choice.choice + ", ";
        });

        $("#"+ q.id).find("table").append("<tr><td>Answers: "+choices+"</td></tr>");
    });
}


$(document).on("click", ".submit", function(e){
    e.stopPropagation();
    e.preventDefault();

    var qtype = $(document.activeElement).attr('id').split("-")[1];

    if (validateForm()) {
        //post question to database
        switch (qtype) {
            case "fr":
                console.log('case is fr');
                submitFRQuestionJSON($("#exercise-id").val());
                break;
            case "mc":
                console.log("case is mc");
                submitMCQuestionJSON($("#exercise-id").val());
                break;
            case "matching":
                submitMatchingQuestionJSON($("#exercise-id").val());
                break;
            case "fitb":
                submitFITBQuestionJSON($("#exercise-id").val());
                break;
            case "sr":
                submitSRQuestionJSON($("#exercise-id").val());
                break;
        }
    }
});