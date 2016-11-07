package models.common;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.google.gson.annotations.Expose;
import play.data.validation.Constraints.Required;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.List;
import java.util.regex.Pattern;

@Entity
public class Choice extends Model {
    /********************************
     ENUMERATOR: For each Choice type
     ********************************/
    public static enum Type {
        DECIMAL,
        INTEGER,
        WORD,
        TEXT;

        public static Type getType(String typeString) {
            Type type = null;

            switch (typeString) {
                case "decimal":
                    type = DECIMAL;
                    break;
                case "integer":
                    type = INTEGER;
                    break;
                case "word":
                    type = WORD;
                    break;
                case "text":
                    type = TEXT;
                    break;
            }

            return type;
        }
    }



	/********************************
	 FIELDS
	 ********************************/
	/* Universal */
	/*===========*/
	@Id
	public Long id;

	@Required
	public boolean retired = false;

	@CreatedTimestamp
	public Timestamp createdTime;

	@UpdatedTimestamp
	public Timestamp updatedTime;


	/* Specific */
	/*===========*/
	@Required
	@Expose
	public Long entity_id;	// e.g. Word.id

    @Required
    @Expose
    public Type choiceType;

	@Required
	@Expose
	public boolean isCorrect;

	@Required
	public boolean isActive;


	/********************************
	 CONSTRUCTORS
	 ********************************/

	public Choice(Long entity_id, Type choiceType, boolean isCorrect, boolean isActive) {
		this.entity_id = entity_id;
        this.choiceType = choiceType;
		this.isCorrect = isCorrect;
		this.isActive = isActive;
	}



	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, Choice> find = new Finder<Long, Choice>(Choice.class);


	/********************************
	 CREATE / DELETE
	 ********************************/
	public static Choice create(Choice choice) {
		choice.save();
		return choice;
	}


	// Create a new Choice that is a duplicate of the given Choice,
	// and assign it to the given Question
	public static Choice copy(Choice choice, QuestionPart questionPart) {
		Choice newChoice = new Choice(choice.entity_id, choice.choiceType, choice.isCorrect, choice.isActive);
		questionPart.choices.add(newChoice);
		questionPart.save();
		return newChoice;
	}


	public static void delete(Long id) {
		Choice choice = find.ref(id);
		if (choice == null) {
			return;
		}

		choice.retired = true;
		choice.save();
	}



	/********************************
	 GETTERS / SETTERS
	 ********************************/

	//-----------Single-------------//

	//get all Choices in the system
	public static List<Choice> getAll() {
		return find.where()
				.eq("retired", false)
			.findList();
	}


	//get Choice by ID
	public static Choice byId(Long choiceId) {
		return find.where()
				.eq("retired", false)
				.eq("id", choiceId)
				.findUnique();
	}


	//get all Choices for a Question
	public static List<Choice> getAllChoicesForQuestionPart(Long questionPartId) {
		return find.where()
				.eq("retired", false)
				.eq("question_part_id", questionPartId)
				.findList();
	}

	// Get the text of this particular Choice by using the proper parser (integer, text, ...)
	public static Long getEntityIdFromText(String choiceText) {
		Pattern decimalPattern = Pattern.compile("^\\d+\\.\\d+$");
		Pattern integerPattern = Pattern.compile("^\\d+$");
		Pattern wordPattern = Pattern.compile("^[A-Za-z]+$");

		Long entity_id;

		if (decimalPattern.matcher(choiceText).find()) {
			AnswerDecimal ad = AnswerDecimal.byAnswer(Double.parseDouble(choiceText));
			if (ad == null) {
				ad = new AnswerDecimal(Double.parseDouble(choiceText));
				ad.save();
			}
			entity_id = ad.id;
		} else if (integerPattern.matcher(choiceText).find()) {
			AnswerInteger ai = AnswerInteger.byAnswer(Integer.parseInt(choiceText));
			if (ai == null) {
				ai = new AnswerInteger(Integer.parseInt(choiceText));
				ai.save();
			}
			entity_id = ai.id;
		} else if (wordPattern.matcher(choiceText).find()) {
			AnswerWord aw = AnswerWord.byAnswer(choiceText);
			if (aw == null) {
				aw = new AnswerWord(choiceText);
				aw.save();
			}
			entity_id = aw.id;
		} else {
			AnswerText at = AnswerText.byAnswer(choiceText);
			if (at == null) {
				at = new AnswerText(choiceText);
				at.save();
			}
			entity_id = at.id;
		}

		return entity_id;
	}


	public static String getChoiceText(Choice choice) {
//		System.out.println(choice.entity_id + " " + choice.choiceType.toString());
		switch(choice.choiceType) {
			case DECIMAL:
				return Double.toString(AnswerDecimal.byId(choice.entity_id).answer);
			case INTEGER:
				return Integer.toString(AnswerInteger.byId(choice.entity_id).answer);
			case WORD:
				return AnswerWord.byId(choice.entity_id).answer;
			case TEXT:
				return AnswerText.byId(choice.entity_id).answer;
			default:
				return "";
		}
	}

	// Return a comma-delimited list of all the Choices for a given Question
	public static String getAllChoicesTextForQuestionPart(QuestionPart questionPart) {
		String choices = "";
		for (Choice choice : getAllChoicesForQuestionPart(questionPart.id)) {
			if (questionPart.prompt != null) {
				choices += questionPart.prompt.text + " = ";
			}
			choices += getChoiceText(choice) + ", ";
		}
//		System.out.println(choices);
		// remove trailing comma
		if (choices.length() == 0) {
			return "";
		} else {
			return choices.substring(0, choices.length() - 2);
		}
	}

	// Return a comma-delimited list of all the subquestions of this QuestionGroup and their choices
	public static String getAllChoicesTextForQuestion(Question question) {
		String choices = "";
		for (QuestionPart questionPart : QuestionPart.getAllPartsForQuestion(question.id)) {
//			System.out.println(questionPart.id);
//			System.out.println(questionPart.prompt);
//			choices += part.prompt.text + " - ";
			choices += getAllChoicesTextForQuestionPart(questionPart) + "; ";
		}

		// remove trailing comma
		if (choices.length() == 0) {
			return "";
		} else {
			return choices.substring(0, choices.length() - 2);
		}
	}
}
