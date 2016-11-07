package controllers;

import forms.common.UserForm;
import models.common.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import static play.data.Form.form;


public class UserController extends Controller {

	/**********************
	 * Create a new instructor and add it to the database
	 * @permission A
	 **********************/
	public static Result createInstructor() throws InvalidKeySpecException, NoSuchAlgorithmException {
		/* ***************************************** */
		Boolean[] permissions = {true,  false,  false};

		if (!User.validateUser(session("userId"), permissions)) { return redirect(routes.Application.showIndexPage()); }
		User loggedInUser = User.byId(Long.parseLong(session("userId")));
		/* ***************************************** */

		Form<UserForm> newInstructorForm = form(UserForm.class).bindFromRequest();

		if (newInstructorForm.hasErrors()) {
			return badRequest(newInstructor.render(newInstructorForm));
		} else {
			User newInstructor = new User(newInstructorForm.get());
			User.create(newInstructor);
		}
		return redirect(routes.UserController.showAllInstructorsPage());
	}


	/**********************
	 * Edit an existing instructor and overwrite their attributes in the database
	 * @permission A, I
	 **********************/
	public static Result modifyInstructor(String username) throws InvalidKeySpecException, NoSuchAlgorithmException {
		/* ***************************************** */
		Boolean[] permissions = {true,  true,  false};

		if (!User.validateUser(session("userId"), permissions)) { return redirect(routes.Application.showIndexPage()); }
		User loggedInUser = User.byId(Long.parseLong(session("userId")));
		/* ***************************************** */

		Form<UserForm> form = form(UserForm.class).bindFromRequest();

		if (form.hasErrors()) {
			return badRequest(editInstructor.render(form, User.byUsername(username)));
		} else {
			User instructor = User.byUsername(username);

			/** PERMISSIONS **/
			if(instructor.id != loggedInUser.id && !instructor.institution.isAdmin(loggedInUser)) {
				return redirect(routes.Application.showIndexPage());
			}

			UserForm instructorForm = form.get();
			instructor.firstName = instructorForm.firstName;
			instructor.lastName = instructorForm.lastName;
			instructor.username = instructorForm.username;
			instructor.email = instructorForm.email;
			instructor.password = instructorForm.password;
			instructor.save();
		}
		
		flash("success", "Instructor's profile has been modified.");
		return redirect(routes.UserController.showInstructorProfilePage(username));
	}

	/**********************
	 * Edit an existing student and overwrite their attributes in the database
	 * @permission A
	 **********************/
	public static Result modifyStudent(String username) throws InvalidKeySpecException, NoSuchAlgorithmException {
		/* ***************************************** */
		Boolean[] permissions = {true,  false,  false};

		if (!User.validateUser(session("userId"), permissions)) { return redirect(routes.Application.showIndexPage()); }
		User loggedInUser = User.byId(Long.parseLong(session("userId")));
		/* ***************************************** */

		Form<UserForm> form = form(UserForm.class).bindFromRequest();

		if (form.hasErrors()) {
			return badRequest(editStudent.render(form, User.byUsername(username)));
		} else {
			User student = User.byUsername(username);
			UserForm studentForm = form.get();
			student.firstName = studentForm.firstName;
			student.lastName = studentForm.lastName;
			student.username = studentForm.username;
			student.email = studentForm.email;
			student.password = studentForm.password;

			User.create(student);
		}

		flash("success", "Student's profile has been modified.");
		return redirect(routes.UserController.showStudentProfilePage(username));
	}

	/**********************
	 * Remove a single instructor from the database (that is, set their record to retired)
	 * @permission A, I
	 * @param userId - the ID of the instructor that should be deleted
	 **********************/
	public static Result deleteInstructor(long userId) {
		/* ***************************************** */
		Boolean[] permissions = {true,  true,  false};

		if (!User.validateUser(session("userId"), permissions)) { return redirect(routes.Application.showIndexPage()); }
		User loggedInUser = User.byId(Long.parseLong(session("userId")));
		/* ***************************************** */

		User instructor = User.byId(userId);
		if (instructor == null) {
			return ok("false");
		}

		/** PERMISSIONS **/
		if(instructor.id != loggedInUser.id && !instructor.institution.isAdmin(loggedInUser)) {
			return redirect(routes.Application.showIndexPage());
		}

		User.delete(userId);
      	return ok("true");
	}

	/**********************
	 * Remove a single student from the database (that is, set their record to retired)
	 * @permission A
	 * @param userId - the ID of the student that should be deleted
	 **********************/
	public static Result deleteStudent(long userId) {
		/* ***************************************** */
		Boolean[] permissions = {true,  false,  false};

		if (!User.validateUser(session("userId"), permissions)) { return redirect(routes.Application.showIndexPage()); }
		User loggedInUser = User.byId(Long.parseLong(session("userId")));
		/* ***************************************** */

		User student = User.byId(userId);
		if (student == null) {
			return ok("false");
		}

		User.delete(userId);
      	return ok("true");
	}



	/* ********************************** RENDER ********************************** */
	/* **************************************************************************** */

	/**********************
	 * Load the page to register a new instructor
	 * No permissions required
	 **********************/
	public static Result showRegisterInstructorPage() {
		/* ***************************************** */
		Boolean[] permissions = {true,  true,  true};

		if (!User.validateUser(session("userId"), permissions)) { return redirect(routes.Application.showIndexPage()); }
		User loggedInUser = User.byId(Long.parseLong(session("userId")));
		/* ***************************************** */

		return ok(views.html.common.register.render(form(UserForm.class)));
	}

	/**********************
	 * Load the page to view a single Student’s profile
	 * @permission A, I, S
	 * @param username - username of the student User being viewed
	 **********************/
	public Result showStudentProfilePage(String username) {
		/* ***************************************** */
		Boolean[] permissions = {true,  true,  true};

		if (!User.validateUser(session("userId"), permissions)) { return redirect(routes.Application.showIndexPage()); }
		User loggedInUser = User.byId(Long.parseLong(session("userId")));
		/* ***************************************** */



		return ok(studentProfile.render(User.byUsername(username)));
	}

	/**********************
	 * Load the page to view a single Instructor’s profile
	 * @permission A, I
	 * @param username - username of the instructor User being viewed
	 **********************/
	public Result showInstructorProfilePage(String username) {
		/* ***************************************** */
		Boolean[] permissions = {true,  true,  false};

		if (!User.validateUser(session("userId"), permissions)) { return redirect(routes.Application.showIndexPage()); }
		User loggedInUser = User.byId(Long.parseLong(session("userId")));
		/* ***************************************** */

		if (User.byUsername(username) == null) {
			return redirect(routes.Application.showIndexPage());
		}
		return ok(instructorProfile.render(User.byUsername(username)));
	}

	/**********************
	 * Load the page to view all Instructors in an Institution
	 * @permission A
	 **********************/
	public Result showAllInstructorsPage() {
		/* ***************************************** */
		Boolean[] permissions = {true,  false,  false};

		if (!User.validateUser(session("userId"), permissions)) { return redirect(routes.Application.showIndexPage()); }
		User loggedInUser = User.byId(Long.parseLong(session("userId")));
		/* ***************************************** */


		return ok(allInstructors.render(loggedInUser.institution));
	}

	/**********************
	 * Load the page to view all Students in an Institution
	 * @permission A, I
	 **********************/
	public Result showAllStudentsPage() {
		/* ***************************************** */
		Boolean[] permissions = {true,  true,  false};

		if (!User.validateUser(session("userId"), permissions)) { return redirect(routes.Application.showIndexPage()); }
		User loggedInUser = User.byId(Long.parseLong(session("userId")));
		/* ***************************************** */

		return ok(allStudents.render(loggedInUser.institution));
	}

	/**********************
	 * Load the page to create a new instructor
	 * @permission A
	 **********************/
	public static Result showCreateInstructorPage() {
		/* ***************************************** */
		Boolean[] permissions = {true,  false,  false};

		if (!User.validateUser(session("userId"), permissions)) { return redirect(routes.Application.showIndexPage()); }
		User loggedInUser = User.byId(Long.parseLong(session("userId")));
		/* ***************************************** */


		return ok(newInstructor.render(form(UserForm.class)));
	}



	/**********************
	 * Load the page to edit an instructor
	 * @permission A, I
	 **********************/
	public static Result showEditInstructorPage(String username) {
		/* ***************************************** */
		Boolean[] permissions = {true,  true,  false};

		if (!User.validateUser(session("userId"), permissions)) { return redirect(routes.Application.showIndexPage()); }
		User loggedInUser = User.byId(Long.parseLong(session("userId")));
		/* ***************************************** */

		User instructor = User.byUsername(username);

		/** PERMISSIONS **/
		if(instructor.id != loggedInUser.id && !instructor.institution.isAdmin(loggedInUser)) {
			return redirect(routes.Application.showIndexPage());
		}

		//TODO: hash password when editing
		UserForm data = new UserForm(instructor.firstName, instructor.lastName, instructor.email, instructor.username, instructor.password, instructor.password, instructor.creatorId, instructor.institution.name);
		Form<UserForm> form = Form.form(UserForm.class).fill(data);

		return ok(editInstructor.render(form, User.byUsername(username)));
	}

	/**********************
	 * Load the page to edit a student
	 * @permission A
	 **********************/
	public static Result showEditStudentPage(String username) {
		/* ***************************************** */
		Boolean[] permissions = {true,  false,  false};

		if (!User.validateUser(session("userId"), permissions)) { return redirect(routes.Application.showIndexPage()); }
		User loggedInUser = User.byId(Long.parseLong(session("userId")));
		/* ***************************************** */

		User student = User.byUsername(username);

		UserForm data = new UserForm(student.firstName, student.lastName, student.email, student.username, student.password, student.password, student.creatorId, student.institution.name);
		Form<UserForm> form = Form.form(UserForm.class).fill(data);

		return ok(editStudent.render(form, User.byUsername(username)));
	}
}