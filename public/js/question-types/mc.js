//TODO: validate MC choices to eliminate duplicates and ones identical to correct answer

function addChoiceBox(isAnswer, choiceId) {
    if (isAnswer == undefined) {
        isAnswer = false;
    }

    var numChoices = getNumChoicesForQuestion();

    var html =
        '<div class="input-group columns large-12"> ' +
        '<span class="input-group-label"><input type="checkbox" id="checkbox-'+numChoices+'" class="toggle-correct"></span> ' +
        '<input class="input-group-field choice" type="text" id="choice-' + numChoices +'" placeholder="Input choice text">' +
        '<div class="input-group-button columns large-1"> ' +
        '<a><span class="icon icon-trash"></span></a>' +
        '</div>' +
        '</div>';

    $('#question-parts-div').append(html);

    $("#checkbox-"+numChoices).prop('checked', isAnswer);
    if (isAnswer) {
        $("#choice-" + numChoices).addClass('correct');
    }

    if (numChoices < 2) {
        $("#choice-"+numChoices).addClass('required');
    }
}



/*
 * Adds/removes "correct" class from a choice box when its checkbox is toggled
 */
$(document).on('change', '.toggle-correct', function(){
    var id = $(this).attr('id').split('-')[1];
    $("#choice-"+id).toggleClass('correct');
});

/***************************************************/
/* When you begin typing in a choice box,
 /* add another choice box after it (up to 5 total)
 /***************************************************/
$(document).on('keydown', '.choice', function(event) {
    // get the total number of choice boxes
    var numChoices = getNumChoicesForQuestion();

    // get the current, next, and last choice boxes in the list
    var currentChoiceID = $(this).attr('id');
    var nextChoiceID = 'choice-'+(parseInt(currentChoiceID.split("choice-")[1])+1).toString();
    var lastChoiceID = 'choice-'+(parseInt(numChoices)-1).toString();

    // if this is the ENTER or TAB key, jump to the next choice box
    if (event.keyCode == '13' || event.keyCode == '9') {
        event.preventDefault();
        $("#" + nextChoiceID).focus();
    }

    // if there are less than 6 choices already...
    if (numChoices < 6){
        // ... and the current box is at the end of the list...
        if (currentChoiceID == lastChoiceID) {
            // ... add a new box
            addChoiceBox();
        }
    }
});


$("*[data-type='mc']").on("click", function() {
    console.log("MC OPEN");
    //addButtonsToContentPanel();

    // call each box a "Choice"
    $("#parts-label").html("Choices");

    // clear prompt text
    $("#prompt").val("");

    // begin with two choices by default
    addChoiceBox(true);
    addChoiceBox();
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
function submitMCQuestionJSON(exerciseId) {
    var question = newQuestion(exerciseId, "mc");
    question.prompt = $("#prompt").val();

    // add a new choice for each "choice" box
    var choices = [];

    $(".choice").each(function() {
        var value = $.trim($(this).val());
        if (value) {
            var isCorrect = $(this).hasClass('correct');
            choices.push(newChoice(value, isCorrect));
        }
    });

    // MC questions have only a single QuestionPart
    var questionParts = [];
    questionParts.push(newQuestionPart("", choices));
    question.questionParts = questionParts;


    //submit question to the database
    submitQuestionToDatabase(question);
}





function formatMCQuestion(q) {
    addQuestionAccordion(q);

    $.each(q.questionParts, function (key, part) {
        var $row = $("<tr></tr>");


        $.each(part.choices, function (index, choice) {
            if (choice.correct) {
                $row.append("<td><img src='/assets/images/box-checked.png' style='width: 1.1em;'>"+choice.choice+"</td>");
            } else {
                $row.append("<td><img src='/assets/images/box-unchecked.png' style='width: 1.1em;'>"+choice.choice+"</td>");
            }
        });

        $("#"+ q.id).find("table").append($row);
    });
}



$(document).on("click", ".q-link", function() {
    if ($(this).parent().hasClass('type-mc')) {
        var groupId = $(this).parent().attr('id');
        $(".modal-container").attr('id', 'group-' + groupId);


        $('#createModal').foundation('reveal', 'open');

        addButtonsToContentPanel();
        jsRoutes.controllers.MainQuestionController.getQuestionGroup(groupId).ajax({
            success: function (data) {
                console.log(data);
                $("#createModal .prompt").val(data.questions[0].prompt);
                $("#createModal .prompt").attr('id', 'question-'+data.questions[0].id);
                $.each(data.questions[0].choices, function (index, choice) {
                    addChoiceBox(choice.isCorrect, choice.id);
                    var $closest = $(".choices").find(".choice:last");
                    $closest.html(choice.text);

                    if (choice.correct) {
                        $closest.parent().find("[type=checkbox]").prop('checked', true);
                        $closest.addClass('correct');
                    }
                });
            }
        });

        $(".modal-container").append("<div class='center'> <button class='button' id='edit-mc'><span class='icon-check'></span> Edit Question</button></div>");
    }
});