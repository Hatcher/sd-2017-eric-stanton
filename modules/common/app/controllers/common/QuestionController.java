package controllers.common;


import actors.*;
import akka.actor.ActorRef;
import com.google.gson.*;
import models.common.*;
import models.common.QuestionType;


import play.mvc.Controller;
import play.mvc.Result;

import java.util.regex.Pattern;

public class QuestionController extends Controller {
//
////	public static Result generateReport()
////	{
////		//create akka job
////
////		//we should really create the actor with UUID name so that someone can't guess
////		//and use the API to view the status of other peoples jobs, it be fairly easy
////		//to guess as it goes $a,$b,$c etc...
////		ActorRef myActor = Akka.system().actorOf(new Props(MyGeneratorMaster.class));
////
////		System.out.println( myActor.path());
////		myActor.tell(new Configprompt("blarg prompt"));
////
////		return ok(generating.render("blarg","title",myActor.path().name()));
////	}
////
////	public static Result status(String uuid)
////	{
////		uuid =  "akka://application/user/"+uuid;
////		ActorRef myActor = Akka.system().actorFor(uuid);
////
////		if(myActor.isTerminated())
////		{
////			return ok("Report Generated - All Actors Terminated") ;
////		}
////		else
////		{
////
////			return async(
////					Akka.asPromise(ask(myActor,new Statusprompt(), 3000)).map(
////							new F.Function<Object,Result>() {
////								public Result apply(Object response) {
////
////									if(response instanceof Resultprompt)
////									{
////										return ok(((Resultprompt) response).getResult());
////									}
////									return ok(response.toString());
////								}
////							}
////					)
////			);
////
////		}
////	}
//
//
//
//
//
//    /**********************
//     * Create a new question and add it to the database
//     * @permission A, I
////     **********************/
////    public static Result createQuestion(String type, Long creatorId) throws ParseException {
////        prompt prompt = prompt.createAprompt(prompt.Type.QUESTION);
////        prompt.save();
////
////        Question question = new Question(prompt, Question.Type.getType(type), User.byId(creatorId), false, false, null);
////        Question.create(question);
////
////        question.choices.add(new Choice(ad.id, correct, true));
////    }
//
//
//    public static Result createQuestionFromJSON(String jsonMsg) {
//      System.out.println(jsonMsg);
//
//      return ok("well?");
//// 	    User loggedInUser = User.byId(Long.parseLong(session("userId")));
//
////         JsonElement jelement = new JsonParser().parse(jsonMsg);
////         JsonObject  jobject = jelement.getAsJsonObject();
//
////         Long exerciseId = jobject.get("exercise").getAsLong();
//// //        Exercise exercise = Exercise.byId(exerciseId);
//
////         JsonObject question = jobject.getAsJsonObject("question");
//
//// 	    QuestionType type = QuestionType.getType(question.get("type").getAsString());
////         Prompt prompt = new Prompt(question.get("prompt").getAsString());
////         prompt.save();
//
//// 	    Question q;
//// 	    if (type == QuestionType.getType("Multiple Choice") || type == QuestionType.getType("Free Response")) {
//// 		    q = new Question(prompt, type, loggedInUser, Project.getProject("Hand"), null, false, false);
//// 	    } else {
//// 		    //TODO: include QuestionGroup's prompt and content
//// 		    q = new Question(prompt, type, loggedInUser, Project.getProject("Hand"), new QuestionGroup("derp", null),
//// 				    false, false);
//// 	    }
//
////         //TODO: global questions
////         Question.create(q);
//
//// 	    JsonArray choices = question.getAsJsonArray("choices");
//
////         for (JsonElement choice : choices) {
////             JsonObject choicesObject = choice.getAsJsonObject();
////             String choiceText = choicesObject.get("choice").getAsString();
////             boolean isCorrect = choicesObject.get("correct").getAsBoolean();
//
////             addChoice(q, choiceText, isCorrect);
////         }
//
//// //	    exercise.questions.add(q);
//
//// 	    System.out.println(q);
//
//// //	    exercise.save();
//
//// 	    //return the ID of the newly-created question
//// 	    return ok(String.valueOf(q.id));
//    }
//
//
//
//	public static Result status(String uuid) {
//		ActorRef myActor = ActorSysContainer.getInstance().getSystem().actorFor(ActorSysContainer.getInstance()
//				.getSystem() + "/user/" + uuid);
//
////		System.out.println("-->" + myActor.path());
//
//		//TODO: return progress value
//		if(myActor.isTerminated()) {
//			//reset the system container
//			ActorSysContainer.setInstance();
//			return ok("");
//		} else {
//			return ok("LOADING");
//		}
//	}
//
//
//    public static void addChoice(QuestionPart part, String choice, boolean correct) {
//        Pattern decimalPattern = Pattern.compile("^\\d+\\.\\d+$");
//        Pattern integerPattern = Pattern.compile("^\\d+$");
//        Pattern wordPattern = Pattern.compile("^[A-Za-z]+$");
//
//       if (decimalPattern.matcher(choice).find()) {
//           AnswerDecimal ad = new AnswerDecimal(Double.parseDouble(choice));
//           ad.save();
//	       part.choices.add(new Choice(ad.id, Choice.Type.getType("decimal"), correct, true));
//       }
//
//       else if (integerPattern.matcher(choice).find()) {
//           AnswerInteger ai = new AnswerInteger(Integer.parseInt(choice));
//           ai.save();
//	       part.choices.add(new Choice(ai.id, Choice.Type.getType("integer"), correct, true));
//        }
//
//       else if (wordPattern.matcher(choice).find()) {
//           AnswerWord aw = new AnswerWord(choice);
//           aw.save();
//	       part.choices.add(new Choice(aw.id, Choice.Type.getType("word"), correct, true));
//       }
//
//       else {
//           AnswerText at = new AnswerText(choice);
//           at.save();
//	       part.choices.add(new Choice(at.id, Choice.Type.getType("text"), correct, true));
//       }
//
//	    part.save();
//    }
//
//    public static Result delete(Long questionGroupId) {
//      QuestionGroup questionGroup = QuestionGroup.byId(questionGroupId);
//      if (questionGroup == null) {
//        return ok("false");
//      }
//
//      QuestionGroup.delete(questionGroupId);
//
//      return ok("true");
//    }
}
