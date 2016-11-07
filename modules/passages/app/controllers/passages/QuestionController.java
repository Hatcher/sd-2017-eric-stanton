package controllers.passages;

import actors.ActorSysContainer;
import actors.passages.MainActor;
import actors.TerminatorActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import forms.passages.PassageForm;
import java.util.*;
import models.common.User;
import models.passages.Passage;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.passages.loader;
import forms.passages.QuestionGenerationForm;

import static play.data.Form.form;

public class QuestionController extends Controller {

	// Generate Questions from a Passage by loading up the Actor system and redirecting to the loading page
	public static Result createQuestionsFromPassage() {
		Form<QuestionGenerationForm> passageForm = form(QuestionGenerationForm.class).bindFromRequest();
		QuestionGenerationForm form = passageForm.get();
		String passage = form.passage;
		int num = Integer.parseInt(form.num);

		User loggedInUser = User.byId(Long.parseLong(session("userId")));

		//create actor system
		ActorSystem system = ActorSysContainer.getInstance().getSystem();
		//create generator actor
		ActorRef a = system.actorOf(Props.create(MainActor.class, loggedInUser, passage, num), "main");
		//create terminator actor
		ActorRef t = system.actorOf(Props.create(TerminatorActor.class, a), "terminator");

		//render interim loading page
		return ok(loader.render(t.path().name()));
	}
}