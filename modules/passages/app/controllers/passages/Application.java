package controllers.passages;

import play.Routes;
import play.mvc.Controller;
import play.mvc.Result;


public class Application extends Controller {

	/**
	 * Show the Passages dashboard view
	 */
	public static Result showIndexPage() {
		return ok(views.html.passages.index.render());
	}

	/**
	 * JavaScript routes that can be accessed by javaScript in a view.
	 */
	public static Result javascriptRoutesPassages() {
		response().setContentType("text/javascript");
		return ok(
				Routes.javascriptRouter("jsRoutes",
						routes.javascript.QuestionController.createQuestionsFromPassage()
				)
		);
	}
}
