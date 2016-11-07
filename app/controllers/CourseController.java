package controllers;

import forms.AnnouncementForm;
import forms.CourseForm;
import models.Course;
import models.Exercise;
import models.common.*;
import models.common.User.Role;
import static play.data.Form.*;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CourseController extends Controller {
	/**********************
	 * Create a new course and add it to the database
	 * @permission A, I
	 **********************/
	public static Result createCourse() throws ParseException {
		/* ***************************************** */
		Boolean[] permissions = {true,  true,  false};

		if (!User.validateUser(session("userId"), permissions)) { return redirect(routes.Application.showIndexPage()); }
		User loggedInUser = User.byId(Long.parseLong(session("userId")));
		/* ***************************************** */

		Form<CourseForm> newCourseForm = form(CourseForm.class).bindFromRequest();

		if (newCourseForm.hasErrors()) {
			return badRequest(newCourse.render(Institution.byId(Long.parseLong(session("institutionId"))), newCourseForm));
		} else {
			Course newCourse = new Course(newCourseForm.get());
			Course.create(newCourse);
			return redirect(routes.CourseController.showCoursePage(newCourse.id));
		}
	}

	/**********************
	 * Edit a course given information from a CourseForm and save it to the database
	 * @permission A, I
	 **********************/
	public static Result editCourse(long courseId) throws ParseException {
		/* ***************************************** */
		Boolean[] permissions = {true,  true,  false};

		if (!User.validateUser(session("userId"), permissions)) { return redirect(routes.Application.showIndexPage()); }
		User loggedInUser = User.byId(Long.parseLong(session("userId")));
		/* ***************************************** */

		Form<CourseForm> form = form(CourseForm.class).bindFromRequest();

		if (form.hasErrors()) {
			return badRequest(editCourse.render(form, Course.byId(courseId)));
		} else {
			Course course = Course.byId(courseId);
			CourseForm courseForm = form.get();
			course.name = courseForm.name;
			course.description = courseForm.description;
			SimpleDateFormat from = new SimpleDateFormat("MM/dd/yyyy");
			Date startDate = courseForm.start.equals("NULL") ? null : from.parse(courseForm.start);
			Date endDate = courseForm.end.equals("NULL") ? null : from.parse(courseForm.end);
			course.startDate = startDate;
			course.endDate = endDate;
			course.isShared = courseForm.isShared;

			Set<User> coInstructors = new HashSet<User>();
			for (String coinstructorId : courseForm.coInstructors.split(",")) {
				if (!Objects.equals(coinstructorId, "-1")) {
					coInstructors.add(User.byId(coinstructorId));
				}
			}
			course.coInstructors = coInstructors;
			
			course.save();
		}
		flash("success", "Course has been modified.");
		return redirect(routes.CourseController.showCoursePage(courseId));
	}

	/**********************
	 * Create a new Course that is an exact copy of an already-existing Course and save it to the database
	 * @permission A, I
	 **********************/
	public static Result copyCourse(long courseId) {
		/* ***************************************** */
		Boolean[] permissions = {true,  true,  false};

		if (!User.validateUser(session("userId"), permissions)) { return redirect(routes.Application.showIndexPage()); }
		User loggedInUser = User.byId(Long.parseLong(session("userId")));
		/* ***************************************** */

		Course originalCourse = Course.byId(courseId);

		/** PERMISSIONS **/
		if(!originalCourse.canCopyCourse(loggedInUser)) {
			return redirect(routes.Application.showIndexPage());
		}

		// make an exact copy of an existing course
		Course newCourse = new Course(originalCourse.name, originalCourse.description, originalCourse.startDate,
				originalCourse.endDate, loggedInUser.institution, originalCourse.hasOpenEnrollment, originalCourse.isShared);

		newCourse.instructor = loggedInUser;
		newCourse.save();


		// copy all exercises from original course to new course
		for (Exercise originalExercise : Exercise.getAllExercisesForCourse(courseId)) {
			Exercise newExercise = new Exercise(loggedInUser, originalExercise.name, originalExercise.description, originalExercise.releaseDate, originalExercise.hasSpacedRepetition, originalExercise.isHidden, originalExercise.exerciseType);
			Course.addExercise(newCourse, newExercise);

			int index = 0;
			// copy all questions from original exercise to new exercise
//			for (QuestionGroup qg : QuestionGroup.getAllQuestionGroupsForExercise(originalExercise.id)) {
//				if (!qg.retired) {
//					// copy the original question group into an identical question group
//					QuestionGroup newQG = QuestionGroup.copy(qg);
//					// copy each question from the original question group into its duplicate
//					for (QuestionPart part : QuestionPart.getAllPartsForGroup(qg.id)) {
//						QuestionPart newQuestionPart = QuestionPart.copy(part, newQG);
//						newQG.questionParts.add(newQuestionPart);
//					}
//					newQG.setOrderIndex(index);
//					Exercise.addQuestionGroup(newQG, newExercise);
//					index++;
//				}
//			}
		}


		flash("success", "Course has been copied.");
		return redirect(routes.CourseController.showEditCoursePage(newCourse.id));
	}

	/**********************
	 * Delete an existing course from the database (that is, set it to retired)
	 * @permission A, I
	 **********************/
	public static Result deleteCourse(long courseId) {
		/* ***************************************** */
		Boolean[] permissions = {true,  true,  false};

		if (!User.validateUser(session("userId"), permissions)) { return redirect(routes.Application.showIndexPage()); }
		User loggedInUser = User.byId(Long.parseLong(session("userId")));
		/* ***************************************** */

		Course course = Course.byId(courseId);
		if (course == null) {
			return ok("false");
		}

		/** PERMISSIONS **/
		if(!course.canDeleteCourse(loggedInUser)) {
			return redirect(routes.Application.showIndexPage());
		}

		course.delete(courseId);
		return ok("true");
	}

	/**********************
	 * Delete all Courses owned by one given Instructor
	 * @permission A, I
	 **********************/
	public static Result deleteAllCoursesForInstructor(long instructorId) {
		/* ***************************************** */
		Boolean[] permissions = {true,  true,  false};

		if (!User.validateUser(session("userId"), permissions)) { return redirect(routes.Application.showIndexPage()); }
		User loggedInUser = User.byId(Long.parseLong(session("userId")));
		/* ***************************************** */

		List<Course> courses = Course.getAllCoursesForInstructor(instructorId);

		if (courses == null) {
			return ok("false");
		}

		for (Course course : courses) {
			course.delete(course.id);
		}

		return ok("true");
	}


	/**********************
	 * Add an existing user (student) to a course
	 * @permission A, I
	 **********************/
	public static Result enrollStudent(Long courseId, Long studentId) {
		Course course = Course.byId(courseId);
		User student = User.byId(studentId);

		if (course == null || student == null) {
			return ok("false");
		}
		course.addStudent(course, student);
		return ok("true");
	}

	/**********************
	 * Remove an existing user (student) from a course
	 * @permission A, I
	 **********************/
	public static Result unenrollStudent(Long courseId, Long studentId) {
		Course course = Course.byId(courseId);
		User student = User.byId(studentId);

		if (course == null || student == null) {
			return ok("false");
		}
		course.removeStudent(course, student);
		return ok("true");
	}


	
	
	/* ********************************** RENDER ********************************** */
	/* **************************************************************************** */

	/**********************
	 * Render the page to create a new Course
	 * @permission A, I
	 **********************/
	public static Result showCreateCoursePage() {
		/* ***************************************** */
		Boolean[] permissions = {true,  true,  false};

		if (!User.validateUser(session("userId"), permissions)) { return redirect(routes.Application.showIndexPage()); }
		User loggedInUser = User.byId(Long.parseLong(session("userId")));
		/* ***************************************** */

		return ok(newCourse.render(loggedInUser.institution, form(CourseForm.class)));
	}

	/**********************
	 * Render the page to view a specific Course
	 * @permission A, I
	 **********************/
	public static Result showCoursePage(Long courseId) {
		/* ***************************************** */
		Boolean[] permissions = {true,  true,  false};

		if (!User.validateUser(session("userId"), permissions)) { return redirect(routes.Application.showIndexPage()); }
		User loggedInUser = User.byId(Long.parseLong(session("userId")));
		/* ***************************************** */

		Course course = Course.byId(courseId);

		/** PERMISSIONS **/
		if(!course.canViewCourse(loggedInUser)) {
			return redirect(routes.Application.showIndexPage());
		}

		return ok(viewCourse.render(form(AnnouncementForm.class), Course.byId(courseId)));
	}

	/**********************
	 * Render the page to show all Courses for the logged-in instructor
	 * @permission A, I
	 **********************/
	public Result showMyCoursesPage() {
		/* ***************************************** */
		Boolean[] permissions = {true,  true,  false};

		if (!User.validateUser(session("userId"), permissions)) { return redirect(routes.Application.showIndexPage()); }
		User loggedInUser = User.byId(Long.parseLong(session("userId")));
		/* ***************************************** */

		return ok(myCourses.render(loggedInUser));
	}

	/**********************
	 * Render the page to show all Courses in the admin's institution
	 * @permission A
	 **********************/
	public Result showAllCoursesPage() {
		/* ***************************************** */
		Boolean[] permissions = {true,  false,  false};

		if (!User.validateUser(session("userId"), permissions)) { return redirect(routes.Application.showIndexPage()); }
		User loggedInUser = User.byId(Long.parseLong(session("userId")));
		/* ***************************************** */

		return ok(allCourses.render(loggedInUser.institution));
	}

	/**********************
	 * Render the edit Course page
	 * @permission A, I
	 **********************/
	public static Result showEditCoursePage(Long courseId) {
		/* ***************************************** */
		Boolean[] permissions = {true,  true,  false};

		if (!User.validateUser(session("userId"), permissions)) { return redirect(routes.Application.showIndexPage()); }
		User loggedInUser = User.byId(Long.parseLong(session("userId")));
		/* ***************************************** */

		Course course = Course.byId(courseId);
		if (course == null) {
			return redirect(routes.Application.showIndexPage());
		}

		/** PERMISSIONS **/
		if(!course.canEditCourse(loggedInUser)) {
			return redirect(routes.Application.showIndexPage());
		}

		SimpleDateFormat from = new SimpleDateFormat("MM/dd/yyyy");
		String startDate = from.format(course.startDate);
		String endDate = from.format(course.endDate);
		CourseForm data = new CourseForm(course.instructor.id, course.name, course.description, startDate, endDate, course.institution.id, course.isShared, "");
		Form<CourseForm> form = Form.form(CourseForm.class);
		form = form.fill(data);
		return ok(editCourse.render(form, course));
	}

	/**********************
	 * Render the manage student enrollment page
	 * @permission A, I
	 **********************/
	public Result showManageStudentsPage(Long courseId){
		/* ***************************************** */
		Boolean[] permissions = {true,  true,  false};

		if (!User.validateUser(session("userId"), permissions)) { return redirect(routes.Application.showIndexPage()); }
		User loggedInUser = User.byId(Long.parseLong(session("userId")));
		/* ***************************************** */

		Course course = Course.byId(courseId);
		if (course == null) {
			return redirect(routes.Application.showIndexPage());
		}

		/** PERMISSIONS **/
		if(!course.canManageStudents(loggedInUser)) {
			return redirect(routes.Application.showIndexPage());
		}

		return ok(manageStudents.render(course));
	}
}
