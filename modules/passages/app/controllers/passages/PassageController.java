package controllers.passages;

import java.io.IOException;

import forms.passages.PassageForm;
import models.common.User;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.passages.*;

import models.passages.Passage;

import static play.data.Form.form;

public class PassageController extends Controller {
	/**********************
	 * Create a new passage and add it to the database
	 * @permission A, I
	 **********************/
	public static Result createPassage() {
		/* ***************************************** */
		Boolean[] permissions = {true,  true,  false};

		if (!User.validateUser(session("userId"), permissions)) { return redirect(controllers.passages.routes.Application.showIndexPage()); }
		User loggedInUser = User.byId(Long.parseLong(session("userId")));
		/* ***************************************** */

		Form<PassageForm> newPassageForm = form(PassageForm.class).bindFromRequest();

		if (newPassageForm.hasErrors()) {
			return badRequest(newPassage.render(newPassageForm));
		} else {
			Passage newPassage = new Passage(newPassageForm.get());
			Passage.addNewPassage(newPassage, newPassageForm.get().text);
		}
		return redirect(controllers.passages.routes.Application.showIndexPage());
	}

	/**********************
	 * Load the page to upload a new passage
	 * @permission A, I
	 **********************/
	public static Result showUploadPassagePage() {
		/* ***************************************** */
		Boolean[] permissions = {true,  true,  false};

		if (!User.validateUser(session("userId"), permissions)) {
			return redirect(controllers.passages.routes.Application.showIndexPage());
		}
		User loggedInUser = User.byId(Long.parseLong(session("userId")));
		/* ***************************************** */

		return ok(newPassage.render(form(PassageForm.class)));
	}

	/**********************
	 * Load the page to show a given passage
	 * @permission A, I
	 **********************/
	public static Result showPassagePage(Long passageId) {
		/* ***************************************** */
		Boolean[] permissions = {true,  true,  false};

		if (!User.validateUser(session("userId"), permissions)) { return redirect(controllers.passages.routes.Application.showIndexPage()); }
		User loggedInUser = User.byId(Long.parseLong(session("userId")));
		/* ***************************************** */

		Passage passage = Passage.byId(passageId);

		if (passage == null) {
			return redirect(controllers.passages.routes.Application.showIndexPage());
		}

		return ok(views.html.passages.passage.render(passage));
	}

	/**
	 * Display the paginated list of passages.
	 */
	public static Result showBrowsePassagesPage(int p, String s, String o) {
		return ok(
				browsePassages.render(
						models.passages.Passage.getAllPublic(p)
				));
	}


	/////////////////////////////////////////////////////
	// TESTING
	////////////////////////////////////////////////////
	public static Result populatePassages() {
		try {
			Passage.uploadPassages();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ok();
	}
}