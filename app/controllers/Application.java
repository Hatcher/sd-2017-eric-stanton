package controllers;

import forms.common.LoginForm;
import models.Course;
import models.common.Institution;
import models.common.QuestionType;
import models.common.User;
import play.Routes;
import play.mvc.*;

import static play.data.Form.*;
import play.data.Form;

import views.html.*;

public class Application extends Controller {

	/**
	 * Fill the database with some dummy data for testing.
	 */
	public Result populate() {
		Institution.createSomeInstitutions(4);

		User.createSomeUsers(1, User.Role.SUPERADMIN);
		User.createSomeUsers(2, User.Role.ADMIN);
		User.createSomeUsers(3, User.Role.INSTRUCTOR);
		User.createSomeUsers(10, User.Role.STUDENT);

		Course.createSomeCourses(10);

		QuestionType.addQuestionTypes();

		return redirect(controllers.common.routes.LoginController.showLoginPage());
	}

	/**
	 * Render the dashboard view, or the login page if not logged in.
	 */
	public Result showIndexPage() {
		//redirect to login page if not already logged in
		if (session("userId") == null || User.byId(Long.parseLong(session("userId"))) == null) {
			return redirect(controllers.common.routes.LoginController.showLoginPage());
		}
		return ok(index.render());
	}




	public static Result showInboxPage() {
		return ok(inbox.render());
	}


	/**
	 * JavaScript routes that can be accessed by JavaScript in views.
	 */
	public static Result javascriptRoutes() {
		response().setContentType("text/javascript");
		return ok(
				Routes.javascriptRouter("jsRoutes",
						routes.javascript.AnnouncementController.delete(),
//						routes.javascript.AnnouncementController.modifyAnnouncement(),
						routes.javascript.AnnouncementController.createAnnouncement(),
						routes.javascript.CourseController.deleteCourse(),
						routes.javascript.CourseController.deleteAllCoursesForInstructor(),
						routes.javascript.CourseController.enrollStudent(),
						routes.javascript.CourseController.unenrollStudent(),
						routes.javascript.UserController.deleteInstructor(),
						routes.javascript.UserController.deleteStudent(),
						routes.javascript.ExerciseController.returnAllExerciseReleaseDates(),
						routes.javascript.ExerciseController.returnAllExercisesWithReleaseDate(),
						routes.javascript.ExerciseController.deleteExercise(),
						routes.javascript.MainQuestionController.createQuestionFromJSON(),
//						routes.javascript.MainQuestionController.editQuestionGroupFromJSON(),
						routes.javascript.MainQuestionController.getAllQuestionsForExercise(),
//						routes.javascript.MainQuestionController.getQuestionGroup(),
//						routes.javascript.MainQuestionController.reorderQuestionGroups(),
//						routes.javascript.MainQuestionController.importQuestionGroups(),
						routes.javascript.AnnouncementController.canDeleteAnnouncement()
				)
		);
	}
}