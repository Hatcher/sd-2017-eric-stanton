package controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.*;
import models.Course;
import models.Exercise;
import models.common.*;

import java.util.List;
import java.util.regex.Pattern;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Duplicates the functionality of the common package QuestionController for use in JavaScript routes because seriously how do they even work???
 */
public class MainQuestionController extends Controller {
//	/**
//	 * Creates a Question or QuestionGroup from JSON received from the Exercise page.
//	 *      Expects JSON like this:
//	 *      {"exercise": id, "qgPrompt": text, "type": text, "hasSubquestions": boolean, "questions":[{"prompt": text,
//	 *      "choices":[{"choice": text, "correct": boolean}, {"choice": text, "correct": boolean}, ...]}, {"prompt": text,
//	 *      "choices":[{"choice": text, "correct": boolean}, {"choice": correct, "text": boolean}, ...]}, ...]}
//	 */
	public static Result createQuestionFromJSON(String jsonMsg) {
		User loggedInUser = User.byId(Long.parseLong(session("userId")));

		JsonElement jelement = new JsonParser().parse(jsonMsg);
		JsonObject jobject = jelement.getAsJsonObject();

		Long exerciseId = jobject.get("exercise").getAsLong();

		// getType returns either an existing or a new object representing the given type string
		QuestionType type = QuestionType.getType(jobject.get("type").getAsString());
		String questionPrompt = jobject.get("prompt").getAsString();

		// TODO: implement Content
		Question q = new Question(questionPrompt, type, loggedInUser, Project.getProject("Hand"), false, false);
		Question.create(q);

		// add each QuestionPart to this Question
		JsonArray questionParts = jobject.getAsJsonArray("questionParts");
		for (JsonElement questionPart : questionParts) {
			addQuestionPart(q, exerciseId, questionPart.getAsJsonObject());
		}

		// return the ID of the newly-created question
		return ok(String.valueOf(q.id));
	}
//
//
//
//	/**
//	 * Insert one Question into the database
//	 */
	public static boolean addQuestionPart(Question q, Long exerciseId, JsonObject partObject) {
		QuestionPart questionPart = new QuestionPart();

		// extract the Prompt, or make a new one. if the QuestionPart is not supposed to have a Prompt, keep it null.
		if (partObject.get("prompt") != null) {
			String p = partObject.get("prompt").getAsString();
			Prompt prompt;
			if (Prompt.byText(p) == null) {
				prompt = Prompt.create(new Prompt(partObject.get("prompt").getAsString()));
			} else {
				prompt = Prompt.byText(p);
			}
			questionPart.prompt = prompt;
		}

		JsonArray choices = partObject.getAsJsonArray("choices");

		for (JsonElement choice : choices) {
			JsonObject choicesObject = choice.getAsJsonObject();
			String choiceText = choicesObject.get("choice").getAsString();
			boolean isCorrect = choicesObject.get("correct").getAsBoolean();

			addChoice(questionPart, choiceText, isCorrect);
		}

		// Insert this QuestionPart into the given Question
		q.addQuestionPart(questionPart);

		// Insert the Question into the given Exercise
		Exercise.byId(exerciseId).addQuestion(q);

		return true;
	}
//
//
//
//
//	public static Result editQuestionGroupFromJSON(String jsonMsg) {
//		JsonElement jelement = new JsonParser().parse(jsonMsg);
//		JsonObject groupObject = jelement.getAsJsonObject();
//
//		QuestionGroup qg = QuestionGroup.byId(groupObject.get("group").getAsLong());
//
//		// overwrite QuestionGroup prompt
//		String qgPromptText = groupObject.get("qgPrompt").getAsString();
//		Prompt prompt = Prompt.byText(qgPromptText);
//		if (qgPromptText == null || qgPromptText.trim().length() <= 0) {
//			prompt = null;
//		} else if (prompt == null) {
//			prompt = Prompt.create(new Prompt(qgPromptText));
//		}
//		qg.prompt = prompt;
//		qg.save();
//
//		Long exerciseId = groupObject.get("exercise").getAsLong();
//
//		JsonArray questionsArray = groupObject.getAsJsonArray("questions");
//
//		for (JsonElement question : questionsArray) {
//			JsonObject questionObject = question.getAsJsonObject();
//			// if overwriting a previous question, EDIT
//			if (questionObject.get("id") != null) {
//				editQuestionPart(questionObject);
//			// if adding a new question (e.g. a new pair in a Matching question), ADD
//			} else {
//				System.out.println(questionObject);
//				User loggedInUser = User.byId(Long.parseLong(session("userId")));
//				QuestionType type = QuestionType.getType(groupObject.get("type").getAsString());
//
//				addQuestionPart(qg, exerciseId, loggedInUser, type, questionObject);
//			}
//		}
//
//		return ok(String.valueOf(qg.id));
//	}
//
//
//	public static boolean editQuestionPart(JsonObject editedQuestionPart) {
//		Long questionId = editedQuestionPart.get("id").getAsLong();
//		QuestionPart questionPart = QuestionPart.byId(questionId);
//
//		// delete the question if it's marked, otherwise set the new content
//		if (editedQuestionPart.get("deleted") != null) {
//			QuestionPart.delete(questionId);
//		} else {
//			// extract the Prompt, or make a new one. if the Question is not supposed to have a Prompt, keep it null.
//			String p = editedQuestionPart.get("prompt").getAsString();
//			Prompt prompt = Prompt.byText(editedQuestionPart.get("prompt").getAsString());
//			if (p == null || p.trim().length() <= 0) {
//				prompt = null;
//			} else if (prompt == null) {
//				prompt = Prompt.create(new Prompt(editedQuestionPart.get("prompt").getAsString()));
//			}
//			questionPart.prompt = prompt;
//
//			JsonArray choices = editedQuestionPart.getAsJsonArray("choices");
//
//			for (JsonElement choice : choices) {
//				JsonObject choicesObject = choice.getAsJsonObject();
//				// if overwriting a previous choice, EDIT
//				if (choicesObject.get("id") != null) {
//					System.out.println("EDITING CHOICE : " + choicesObject);
//					editChoice(questionPart, choicesObject);
//					// if adding a new choice, ADD
//				} else {
//					addChoice(questionPart, choicesObject.get("choice").getAsString(), choicesObject.get("correct").getAsBoolean());
//				}
//			}
//		}
//
//		questionPart.save();
//
//		return true;
//	}
//
//
//
//	public static void editChoice(QuestionPart questionPart, JsonObject choicesObject) {
//		JsonElement choiceId = choicesObject.get("id");
//		Choice choice = Choice.byId(choiceId.getAsLong());
//
//		// delete the choice if it's marked, otherwise set the new content
//		if (choicesObject.get("deleted") != null) {
//			Choice.delete(choiceId.getAsLong());
//			questionPart.save();
//		} else {
//			boolean isCorrect = choicesObject.get("correct").getAsBoolean();
//			String choiceText = choicesObject.get("choice").getAsString();
//
//			choice.isCorrect = isCorrect;
//			choice.entity_id = Choice.getEntityIdFromText(choiceText);
//
//			choice.save();
//		}
//		questionPart.save();
//	}
//
//
//
//	/**
//	 * Insert one single Choice into the database and associate it with a given Question
//	 */
	public static void addChoice(QuestionPart questionPart, String choiceText, boolean correct) {
		Pattern decimalPattern = Pattern.compile("^\\d+\\.\\d+$");
		Pattern integerPattern = Pattern.compile("^\\d+$");
		Pattern wordPattern = Pattern.compile("^[A-Za-z]+$");

		String type;

		if (decimalPattern.matcher(choiceText).find()) {
			type = "decimal";
		} else if (integerPattern.matcher(choiceText).find()) {
			type = "integer";
		} else if (wordPattern.matcher(choiceText).find()) {
			type = "word";
		} else {
			type = "text";
		}

		Choice newChoice = new Choice(Choice.getEntityIdFromText(choiceText), Choice.Type.getType(type), correct, true);
		questionPart.choices.add(newChoice);
		questionPart.save();
	}
//
//
	public static Result getAllQuestionsForExercise(Long exerciseId) {
		List<Question> questions = Question.getAllQuestionsForExercise(exerciseId);

		ObjectNode result = Json.newObject();
		ArrayNode questionArray = Json.newArray();

		Exercise exercise = Exercise.byId(exerciseId);
		result.put("course", Course.byId(Course.getCourseIdForExercise(exerciseId)).name);
		result.put("exercise", exercise.name);
		result.put("exerciseType", exercise.exerciseType.toString());

		for (Question question : questions) {
			questionArray.add(getQuestionObject(question));
		}
		result.put("questions", questionArray);

		return ok(result);
	}


	public static ObjectNode getQuestionObject(Question question) {
		ObjectNode questionObject = Json.newObject();
		questionObject.put("id", question.id);
		questionObject.put("prompt", question.prompt.text);
		questionObject.put("type", QuestionType.getTypeAbbrev(question.questionType));

		ArrayNode questionParts = Json.newArray();
		for (QuestionPart questionPart : QuestionPart.getAllPartsForQuestion(question.id)) {
			ObjectNode questionPartsObject = Json.newObject();

			if (questionPart.prompt != null) {
				questionPartsObject.put("prompt", questionPart.prompt.text);
			}

			ArrayNode choiceArray = Json.newArray();
			for (Choice choice : Choice.getAllChoicesForQuestionPart(questionPart.id)) {
				ObjectNode choiceObject = Json.newObject();

				choiceObject.put("choice", Choice.getChoiceText(choice));
				choiceObject.put("correct", choice.isCorrect);

				choiceArray.add(choiceObject);
			}
			questionPartsObject.put("choices", choiceArray);

			questionParts.add(questionPartsObject);
		}
		questionObject.put("questionParts", questionParts);
		return questionObject;
	}
//
//
//	public static Result getQuestionGroup(Long groupId) {
//		QuestionGroup group = QuestionGroup.byId(groupId);
//
//		ObjectNode groupObject = Json.newObject();
//
//		groupObject.put("id", group.id);
//
//		if (group.prompt != null) {
//			groupObject.put("prompt", group.prompt.text);
//			groupObject.put("choices", Choice.getAllChoicesTextForQuestionGroup(group));
//
//		} else {
//			groupObject.put("prompt", "");
//		}
//
//		ArrayNode questions = Json.newArray();
//
//		for (QuestionPart questionPart : QuestionPart.getAllPartsForGroup(group.id)) {
//			ObjectNode questionObject = Json.newObject();
//
//			questionObject.put("id", questionPart.id);
//			questionObject.put("prompt", questionPart.prompt.text);
//			ArrayNode choices = Json.newArray();
//			for (Choice choice : Choice.getAllChoicesForQuestionPart(questionPart.id)) {
//				String choiceText = Choice.getChoiceText(choice);
//
//				ObjectNode choicesObject = Json.newObject();
//
//				choicesObject.put("id", choice.id);
//				choicesObject.put("text", choiceText);
//				choicesObject.put("correct", choice.isCorrect);
//
//				choices.add(choicesObject);
//			}
//			questionObject.put("choices", choices);
//			questions.add(questionObject);
//		}
//		groupObject.put("questions", questions);
//		return ok(groupObject);
//	}
//
//
//	public static Result reorderQuestionGroups(String jsonMsg) {
//		JsonElement jelement = new JsonParser().parse(jsonMsg);
//		JsonObject jobject = jelement.getAsJsonObject();
//
//		JsonArray groups = jobject.getAsJsonArray("groups");
//
//		for (JsonElement group : groups) {
//			System.out.println(group);
//			QuestionGroup qg = QuestionGroup.byId(group.getAsJsonObject().get("id").getAsLong());
//
//			// set the new order index of this group
//			int index = group.getAsJsonObject().get("orderIndex").getAsInt();
//			qg.setOrderIndex(index);
//		}
//
//		return ok("yay!");
//	}
//
//
//
//	public static Result importQuestionGroups(String groupIds, Long exerciseId) {
//		System.out.println(groupIds);
//		Exercise exercise = Exercise.byId(exerciseId);
//		String[] ids = groupIds.substring(1, groupIds.length()-1).split(",");
//
//		for (String groupId : ids) {
//			QuestionGroup newQG = QuestionGroup.copy(QuestionGroup.byId(Long.parseLong(groupId)));
//
//			int index = QuestionGroup.getAllQuestionGroupsForExercise(exercise.id).size();
//			newQG.setOrderIndex(index);
//
//			exercise.groups.add(newQG);
//			exercise.save();
//		}
//		return ok("yay!");
//	}
}