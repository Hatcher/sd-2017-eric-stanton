package controllers.common;

import forms.common.LoginForm;
import forms.common.UserForm;
import models.common.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static play.data.Form.form;

public class LoginController extends Controller {
	// log in a user after submitting login info
	public static Result login() {
		//get the return URL (assuming the user was directed to the login page from another page
		String returnUrl = session("returnUrl");
		if (returnUrl == null) {
			returnUrl = "none";
		}

		Form<LoginForm> loginForm = form(LoginForm.class).bindFromRequest();
		if (loginForm.hasErrors()) {
			return badRequest(views.html.common.login.render(loginForm, returnUrl));
		} else {
			session().clear();
			User user = User.byUsernameOrEmail(loginForm.get().username);
			session("userId", user.id.toString());
			session("userFirstName", user.firstName);
			session("institutionId", user.institution.id.toString());
			session("institutionName", user.institution.name);
			session("userRole", user.role.toString());
			return redirect((returnUrl.equals("none")) ? "/" : returnUrl);
		}
	}

	public static Result logout() {
		session().clear();
		return redirect(controllers.common.routes.LoginController.showLoginPage());
	}


	/**********************
	 * Load the page to login
	 * No permissions required
	 **********************/
	public Result showLoginPage() {
		return ok(views.html.common.login.render(form(LoginForm.class), ""));
	}

	/**********************
	 * Create a new instructor and add it to the database
	 * No permissions required
	 **********************/
	public static Result registerInstructor() throws InvalidKeySpecException, NoSuchAlgorithmException {
		Form<UserForm> newInstructorForm = form(UserForm.class).bindFromRequest();

		if (newInstructorForm.hasErrors()) {
			return badRequest(views.html.common.register.render(newInstructorForm));
		} else {
			session().clear();
			User newInstructor = new User(newInstructorForm.get());
			User.create(newInstructor);
		}
		return redirect(controllers.common.routes.LoginController.showLoginPage());
	}

	/**********************
	 * Load the page to register a new instructor
	 * No permissions required
	 **********************/
	public static Result showRegisterInstructorPage() {
		return ok(views.html.common.register.render(form(UserForm.class)));
	}
}