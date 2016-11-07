package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.*;
import models.common.*;
import models.common.Announcement;
import play.Play;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MobileController extends Controller {

	/***********
		Check a student's mobile login and return a code:
			200 (ok): student login is valid
			400 (badRequest): bad request data sent (expected: username and password)
			404 (notFound): not a valid student login (may not exist, or may be another user role)
	 ***********/
	public static Result mobileLogin() {
		JsonNode json = request().body().asJson();

		if (json == null) {
    		return badRequest("Expected: Json data");
  		} else {
  			String username = json.findPath("username").asText();
  			String password = json.findPath("password").asText();

  			// Return a badRequest error if empty data got through the form
  			if ((username == null || password == null) || (username == "" || password == "")) {
  				return badRequest("Incomplete form");
  			}

  			User user = User.byLogin(username, password);

  			// Retun a notFound error if the user does not exist or is not a student
  			if (user == null) {
  				return notFound("Incorrect username and/or password");
  			} else if (user.role != User.Role.STUDENT) {
  				return notFound("Incorrect username and/or password");
  			}

  			// TODO: maybe want to send student's name back in the response??
  			ObjectNode userInfoNode = Json.newObject();
  			userInfoNode.put("id", user.id);
  			userInfoNode.put("name", user.firstName + " " + user.lastName);

  			return ok(userInfoNode);
		}
	}

	/***********
		Collect all questions in a certain exercise identified by exerciseId
		and wrap their relevant information in JSON to be sent to the app.
	 ***********/
	public static Result getQuestionsForExercise(long exerciseId) {
		JsonNode result;
		ObjectMapper mapper = new ObjectMapper();
		Exercise exercise = Exercise.byId(exerciseId);

		// if the exercise does not exist, return 404.
		// (if the questions list is null, we will just return empty JSON)
		if (exercise == null) {
			return notFound("Exercise not found.");
		}

//		List<Question> questionList = Question.getAllQuestionsForExercise(exercise);
//		ArrayNode questionsJSON = JsonNodeFactory.instance.arrayNode();
//
//		// Add every question to the list of questions to be sent
//		for (Question question : questionList) {
//			ObjectNode questionNode = Json.newObject();
//			//TODO: content other than text
//			if (question.group != null) {
//				System.out.println("NOT NULL! " + question.id);
//				System.out.println(ContentText.byId(QuestionGroup.byId(question.group.id).content.entityId).text);
//				questionNode.put("content", ContentText.byId(QuestionGroup.byId(question.group.id).content.entityId).text);
//			} else {
//				questionNode.put("content", "");
//			}
//			questionNode.put("prompt", question.message.prompt.text);
//			questionNode.put("id", question.id);
//
//			// Collect all of the active choices for this question
//			ArrayNode choicesNode = JsonNodeFactory.instance.arrayNode();
//
//			// Randomize order of choices
//			List<Choice> choices = new ArrayList<Choice>();
//			choices.addAll(question.choices);
//			Collections.shuffle(choices);
//
//			// Add each active choice to the JSON to be sent
//			for (Choice choice : choices) {
//				if (choice.isActive) {
//					ObjectNode choiceNode = Json.newObject();
//
//					choiceNode.put("id", choice.id);
//
//					if (choice.choiceType == Choice.Type.getType("decimal")) {
//						choiceNode.put("text", String.valueOf(AnswerDecimal.byId(choice.entity_id).answer));
//					} else if (choice.choiceType == Choice.Type.getType("word")) {
//						choiceNode.put("text", AnswerWord.byId(choice.entity_id).answer);
//					} else if (choice.choiceType == Choice.Type.getType("integer")) {
//						choiceNode.put("text", String.valueOf(AnswerInteger.byId(choice.entity_id).answer));
//					} else if (choice.choiceType == Choice.Type.getType("text")) {
//						choiceNode.put("text", AnswerText.byId(choice.entity_id).answer);
//					}
//
//					choiceNode.put("correct", choice.isCorrect);
//
//					choicesNode.add(choiceNode);
//				}
//			}
//
//			questionNode.put("choices", choicesNode);
//
//			questionsJSON.add(questionNode);
//		}

		// send the result
//		return ok(questionsJSON);
		return ok();
	}

	/***********
		Collect all exercises in a certain course identified by courseId
		of a certain type identified by type, and wrap their relevant
		information in JSON to be sent to the app.
		String type - does not need to be capitalized any particular way
	 ***********/
	public static Result getExercisesOfTypeForCourse(Long courseId, Long studentId, String type) {
		JsonNode result;
		ObjectMapper mapper = new ObjectMapper();

		Course course = Course.byId(courseId);

		// if the course does not exist, return 404.
		// (if the exercises list is null, we will just return empty JSON)
		if (course == null) {
			return notFound("Course not found.");
		}

		// do not send data on a course that is not in session; send 404.
		if (!Course.isInSession(course)) {
			return notFound("Course not found.");
		}

		// Make the function robust to capitalization of the type string
		type = type.toLowerCase().trim();
		type = Character.toUpperCase(type.charAt(0)) + type.substring(1);

		// if the exercise type is invalid, return 400.
		if (!type.equals("Exercise") && !type.equals("Survey") && !type.equals("Evaluation")) {
			return badRequest("Invalid exercise type.");
		}

		// get AllExercisesForCourseByType will get all exercises which are NOT retired
		List<Exercise> exerciseList = Exercise.getAllExercisesForCourseByType(courseId, Exercise.Type.getType(type));
		List<Long> finishedExerciseIds = new ArrayList<Long>();

		//get the list of all Evaluation/Survey exercises this student has already completed
		if (!type.equals("Exercise")) {
			finishedExerciseIds = Exercise.getAllFinishedExercisesForCourseForStudentByType(courseId, studentId, Exercise.Type
					.getType(type));
		}
		ArrayNode exercisesJSON = JsonNodeFactory.instance.arrayNode();
		Date today = new Date();
		SimpleDateFormat simpleFormat = new SimpleDateFormat("MMMM dd, yyyy");

		// Add every active revealed exercise to the list of exercises to be sent
		for (Exercise exercise : exerciseList) {
			//skip any exercises that have already been completed and cannot be retaken
			if (finishedExerciseIds.contains(exercise.id)) {
				continue;
			}

			// Do not send any exercises that have not been released yet or are hidden
			if (today.after(exercise.releaseDate) && !exercise.isHidden) {
				ObjectNode exerciseNode = Json.newObject();

				exerciseNode.put("id", exercise.id);
				exerciseNode.put("name", exercise.name);
				exerciseNode.put("orderIndex", exercise.orderIndex);
				exerciseNode.put("releaseDate", simpleFormat.format(exercise.releaseDate));

				ExerciseRecord mostRecentRecord = ExerciseRecord.getMostRecentExerciseRecordForStudent(exercise.id, studentId);

				//if the exercise hasn't ever been taken before, or if the most recent attempt is
				// still in progress, display no score and no attempt number
				int score = -1;
				int attemptNumber = -1;
				int isCleared = 0;

				if (mostRecentRecord != null) {
					attemptNumber = mostRecentRecord.attemptNumber;

					//if the most recent attempt was finished, display the most recent score
					if (mostRecentRecord.isCleared) {
						score = (int)(((double) mostRecentRecord.numberCorrect / (double) mostRecentRecord
								.numberAnswered) * 100);
						isCleared = 1;
					}
				}

				exerciseNode.put("isCleared", isCleared);
				exerciseNode.put("score", score);
				exerciseNode.put("attemptNumber", attemptNumber);

				exercisesJSON.add(exerciseNode);
			}
		}

		// send the result
		return ok(exercisesJSON);
	}

	/***********
		Receive a response about all questions in a single exercise and
		create new response objects in the database for each question.
	 ***********/
	public static Result submitQuestionRecord() {
		JsonNode json = request().body().asJson();

		if(json == null) {
    		return badRequest("expected: Json data");
  		} else {
  			int questionId = json.findPath("questionId").asInt();
  			int studentId = json.findPath("studentId").asInt();
  			int choiceId = json.findPath("choiceId").asInt();

  			// not implementing timeToComplete and attemptNumber at this time
  			// no questionRecord will be submitted without being completed
  			QuestionRecord questionRecord = new QuestionRecord((long)studentId, (long)questionId, null, 0, 0, true);
			QuestionRecord.create(questionRecord);

			return ok("received response");
		}
	}

	/***********
	 Receive a response about all questions in a single exercise and
	 create new response objects in the database for each question.
	 ***********/
	public static Result createExerciseRecord() {
		JsonNode json = request().body().asJson();

		if(json == null) {
			return badRequest("expected: Json data");
		} else {
			int exerciseId = json.findPath("exerciseId").asInt();
			int studentId = json.findPath("studentId").asInt();
			boolean isCleared = json.findPath("isCleared").asInt() == 1;

			ExerciseRecord mostRecentRecord = ExerciseRecord.getMostRecentExerciseRecordForStudent((long)exerciseId, (long)studentId);
			int attemptNum = 1;

			if (mostRecentRecord != null) {
				attemptNum = mostRecentRecord.attemptNumber;

				if (mostRecentRecord.isCleared) {
					attemptNum += 1;
				}

				//ensures that we don't create duplicate exercise records every time a student enters
				//the exercise, doesn't answer the first question, exits, and returns
				if (ExerciseRecord.recordExists((long)studentId, (long)exerciseId, attemptNum)) {
					return ok();
				}
			}

			ExerciseRecord exerciseRecord = new ExerciseRecord(0, attemptNum, isCleared, 0, 0, (long)studentId, (long)exerciseId);
			ExerciseRecord.create(exerciseRecord);

			return ok("received response");
		}
	}

	/***********
	 Receive a response about all questions in a single exercise and
	 create new response objects in the database for each question.
	 ***********/
	public static Result updateExerciseRecord() {
		JsonNode json = request().body().asJson();

		if(json == null) {
			return badRequest("expected: Json data");
		} else {
			int exerciseId = json.findPath("exerciseId").asInt();
			int studentId = json.findPath("studentId").asInt();
			boolean isCleared = json.findPath("isCleared").asInt() == 1;
			int numCorrect = json.findPath("numCorrect").asInt();
			int numAnswered = json.findPath("numAnswered").asInt();


			System.out.println("updating! exercise id=" + exerciseId + " studentid=" + studentId + " isCleared=" +
					isCleared + " " +
					"" + numCorrect + "/" +
					numAnswered);

			ExerciseRecord mostRecentRecord = ExerciseRecord.getMostRecentExerciseRecordForStudent((long)exerciseId, (long)studentId);
			mostRecentRecord.isCleared = true;
			mostRecentRecord.numberCorrect = numCorrect;
			mostRecentRecord.numberAnswered = numAnswered;
			mostRecentRecord.save();

			return ok("received response");
		}
	}

	/**
	 * Encode image to string
	 * @param image The image to encode
	 * @param type jpeg, bmp, ...
	 * @return encoded string
	 */
	public static String encodeToString(BufferedImage image, String type) {
		String imageString = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {
			ImageIO.write(image, type, bos);
			byte[] imageBytes = bos.toByteArray();

			BASE64Encoder encoder = new BASE64Encoder();
			imageString = encoder.encode(imageBytes);

			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imageString;
	}

	/***********
		Collect all courses for a student identified by studentId
		and wrap their relevant information in JSON to be sent to the app.
	 ***********/
	public static Result getCoursesforStudent(Long studentId) throws IOException {
		JsonNode result;
		ObjectMapper mapper = new ObjectMapper();
		User user = User.byId(studentId);
		if (user == null) {
			return notFound("Invalid user.");
		} else {
			List<Course> courses = Course.getAllCoursesForStudent(studentId);
			ArrayNode courseJSON = JsonNodeFactory.instance.arrayNode();

			// Add every course to the list of courses to be sent
			for (Course course : courses) {
				ObjectNode courseNode = Json.newObject();
				courseNode.put("description", course.description);
				courseNode.put("id", course.id);
				courseNode.put("name", course.name);

				File file = Play.application().getFile("public/images/users/"+ Course.byId(course.id).instructor.id + ".png");
				BufferedImage img = ImageIO.read(file);
				String imgstr = encodeToString(img, "png");

				courseNode.put("image", imgstr);

				courseJSON.add(courseNode);
			}

			return ok(courseJSON);
		}
	}

	/***********
		Select the image for a given Course identified by courseId.
	 ***********/
	public static Result getCourseImage(Long courseId) {
		return ok(new File("/public/images/users/"+ Course.byId(courseId).instructor.id));
	}

	// TODO: This method should perhaps actually be looking for all announcements for a specific course /for a specific user/
	// (since rivalries can happen between just two individuals in a certaincourse)
	public static Result getAnnouncementsForCourse(Long courseId) {
		JsonNode result;
		ObjectMapper mapper = new ObjectMapper();

		Course course = Course.byId(courseId);

		// if the course does not exist, return 404.
		// (if the exercises list is null, we will just return empty JSON)
		if (course == null) {
			return notFound("Course not found.");
		}

		// do not send data on a course that is not in session; send 404.
		if (!Course.isInSession(course)) {
			return notFound("Course not found.");
		}

		// getAllAnnouncementsForCourseByType will get all announcements which are NOT retired
		List<Announcement> announcementList = Announcement.getAllAnnouncementsForCourse(courseId);
		ArrayNode announcementsJSON = JsonNodeFactory.instance.arrayNode();

		// Add every announcement to the list of announcements to be sent
		for (Announcement announcement : announcementList) {
			ObjectNode announcementNode = Json.newObject();

			announcementNode.put("id", announcement.id);
			announcementNode.put("text", announcement.prompt.text);
			String shortText = announcement.prompt.text;

			// TODO: We can take the maximum string length as a parameter maybe, and then it can be decided by the device the student is using
			// We can also try to do this dynamically on the app one day (for now, this is easy, ok?)
			if (shortText.length() > 22) {
				shortText = shortText.substring(0, 22) + "...";
			}

			announcementNode.put("shortText", shortText);
			announcementNode.put("issuer", announcement.creator.firstName + " " + announcement.creator.lastName);

			announcementsJSON.add(announcementNode);
		}

		// send the result
		return ok(announcementsJSON);
	}

	/***********
		Return the ID of the Course this Exercise is in
	 ***********/
	public static Result getCourseIdForExercise(Long exerciseId) {
		Long courseId = Course.getCourseIdForExercise(exerciseId);

		ObjectNode announcementNode = Json.newObject();
		announcementNode.put("courseId", courseId);

		// send the result
		return ok(announcementNode);
	}
}