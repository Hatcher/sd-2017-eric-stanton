package controllers;

import java.util.*;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import forms.ExerciseForm;
import forms.passages.QuestionGenerationForm;
import models.Course;
import models.Exercise;
import models.common.User;
import models.common.User.Role;

import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import static play.data.Form.form;

public class ExerciseController extends Controller {
    /**********************
     * Create a new exercise inside a specified course and add it to the database
     * @permission A, I
     **********************/
    public static Result createExercise(Long courseId, String typeString) throws InvalidKeySpecException, NoSuchAlgorithmException, ParseException {
	    /* ***************************************** */
	    Boolean[] permissions = {true,  true,  false};

	    if (!User.validateUser(session("userId"), permissions)) { return redirect(routes.Application.showIndexPage()); }
	    User loggedInUser = User.byId(Long.parseLong(session("userId")));
		/* ***************************************** */

        Course course = Course.byId(courseId);

	    /** PERMISSIONS **/
	    if(!course.canCreateExercise(loggedInUser)) {
		    return redirect(routes.Application.showIndexPage());
	    }

	    Form<ExerciseForm> newExerciseForm = form(ExerciseForm.class).bindFromRequest();


	    if (newExerciseForm.hasErrors()) {
            return badRequest(newExercise.render(newExerciseForm, course, typeString));
        } else {
            Exercise newExercise = new Exercise(newExerciseForm.get(), typeString, Exercise.getAllExercisesForCourse(course.id)
                    .size() + 1);
            Course.addExercise(course, newExercise);
        }
        return redirect(routes.CourseController.showCoursePage(courseId));
    }

    /**********************
     * Delete a specific exercise.
     * @permission A, I
     **********************/
    public static Result deleteExercise(long exerciseId) {
	    /* ***************************************** */
	    Boolean[] permissions = {true,  true,  false};

	    if (!User.validateUser(session("userId"), permissions)) { return redirect(routes.Application.showIndexPage()); }
	    User loggedInUser = User.byId(Long.parseLong(session("userId")));
		/* ***************************************** */

	    Exercise exercise = Exercise.byId(exerciseId);
        if (exercise == null) {
            return ok("false");
        }

	    /** PERMISSIONS **/
	    if(!exercise.canDeleteExercise(loggedInUser)) {
		    return redirect(routes.Application.showIndexPage());
	    }

        Exercise.delete(exerciseId);
        return ok("true");
    }

    /**********************
     * Edit a exercise.
     * @permission A, I
     **********************/
    public static Result editExercise(long exerciseId) throws ParseException {
	    /* ***************************************** */
	    Boolean[] permissions = {true,  true,  false};

	    if (!User.validateUser(session("userId"), permissions)) { return redirect(routes.Application.showIndexPage()); }
	    User loggedInUser = User.byId(Long.parseLong(session("userId")));
		/* ***************************************** */

	    Form<ExerciseForm> form = form(ExerciseForm.class).bindFromRequest();

        if (form.hasErrors()) {
            return badRequest(editExercise.render(form, Exercise.byId(exerciseId)));
        } else {
            Exercise exercise = Exercise.byId(exerciseId);

	        /** PERMISSIONS **/
	        if(!exercise.canEditExercise(loggedInUser)) {
		        return redirect(routes.Application.showIndexPage());
	        }

            ExerciseForm exerciseForm = form.get();
            exercise.name = exerciseForm.name;
            exercise.description = exerciseForm.description;
            SimpleDateFormat from = new SimpleDateFormat("MM/dd/yyyy");
            Date releaseDate = from.parse(exerciseForm.start);
            exercise.releaseDate = releaseDate;
            exercise.save();
        }
        flash("success", "Exercise has been modified.");
        return redirect(routes.ExerciseController.showExercisePage(exerciseId));
    }


	/**********************
	 * Select all release dates for any Exercise owned by a given Instructor
	 * @permission A, I
	 **********************/
	public static Result returnAllExerciseReleaseDates(Long instructorId) {
		/* ***************************************** */
		Boolean[] permissions = {true,  true,  false};

		if (!User.validateUser(session("userId"), permissions)) { return redirect(routes.Application.showIndexPage()); }
		User loggedInUser = User.byId(Long.parseLong(session("userId")));
		/* ***************************************** */

		ObjectNode result = Json.newObject();
		ObjectNode datesObject = Json.newObject();

		//ArrayNode has no "contains" method, so we add all unique dates to an ArrayList
		//and then add each element in that list to the ArrayNode
		List<Date> exerciseDates = new ArrayList<Date>();
		for (Exercise exercise : Exercise.getAllExercisesForInstructor(instructorId)) {
			if (!exerciseDates.contains(exercise.releaseDate)) {
				exerciseDates.add(exercise.releaseDate);

				ArrayNode exercisesArray = result.arrayNode();
				for(Exercise releasingExercise : Exercise.getAllExercisesForInstructorByReleaseDate(instructorId, exercise.releaseDate)) {
					exercisesArray.add(releasingExercise.id);
				}
				datesObject.put(exercise.releaseDate.toString(), exercisesArray);
			}
		}

		result.put("releaseDates", datesObject);

		return ok(result);
	}

	/**********************
	 * Select all Exercises owned by a given Instructor for a given release date
	 * @permission A, I
	 **********************/
	public static Result returnAllExercisesWithReleaseDate(Long instructorId, Long releaseDateTime) {
		/* ***************************************** */
		Boolean[] permissions = {true,  true,  false};

		if (!User.validateUser(session("userId"), permissions)) { return redirect(routes.Application.showIndexPage()); }
		User loggedInUser = User.byId(Long.parseLong(session("userId")));
		/* ***************************************** */

		Date releaseDate = new Date(releaseDateTime);
		ObjectNode result = Json.newObject();

		int c = 0;
		for(Exercise exercise : Exercise.getAllExercisesForInstructorByReleaseDate(instructorId, releaseDate)) {
			ObjectNode exercisesObject = Json.newObject();
			exercisesObject.put("id", exercise.id);
			exercisesObject.put("name", exercise.name);
			exercisesObject.put("course", Course.getCourseForExercise(exercise.id).name);
			result.put(Integer.toString(c), exercisesObject);
			c += 1;
		}
		
		return ok(result);
	}



	/* ********************************** RENDER ********************************** */
	/* **************************************************************************** */

	/**********************
	 * Load the page to create a new exercise in the designated course
	 * @permission A, I
	 **********************/
	public static Result showCreateExercisePage(Long courseId, String typeString) {
		/* ***************************************** */
		Boolean[] permissions = {true,  true,  false};

		if (!User.validateUser(session("userId"), permissions)) { return redirect(routes.Application.showIndexPage()); }
		User loggedInUser = User.byId(Long.parseLong(session("userId")));
		/* ***************************************** */

		Course course = Course.byId(courseId);

		/** PERMISSIONS **/
		if(!course.canCreateExercise(loggedInUser)) {
			return redirect(routes.Application.showIndexPage());
		}

		return ok(newExercise.render(form(ExerciseForm.class), course, typeString));
	}

	/**********************
	 * 	Load the page to create a new Exercise
	 * @permission A, I
	 **********************/
	public static Result showExercisePage(Long exerciseId) {
		/* ***************************************** */
		Boolean[] permissions = {true,  true,  false};

		if (!User.validateUser(session("userId"), permissions)) { return redirect(routes.Application.showIndexPage()); }
		User loggedInUser = User.byId(Long.parseLong(session("userId")));
		/* ***************************************** */

		System.out.println(exerciseId);

		Exercise exercise = Exercise.byId(exerciseId);
		if (exercise == null) {
			return redirect(routes.Application.showIndexPage());
		}
		Course course = Course.getCourseForExercise(exerciseId);

		/** PERMISSIONS **/
		if(!exercise.canViewExercise(loggedInUser)) {
			return redirect(routes.Application.showIndexPage());
		}

		return ok(viewExercise.render(exercise));
	}

	/**********************
	 *  Load the page to create a new Exercise
	 *  This must be the instructor of this Exercise's Course, or an administrator
	 * @permission A, I
	 **********************/
	public static Result showEditExercisePage(Long exerciseId) {
		/* ***************************************** */
		Boolean[] permissions = {true,  true,  false};

		if (!User.validateUser(session("userId"), permissions)) { return redirect(routes.Application.showIndexPage()); }
		User loggedInUser = User.byId(Long.parseLong(session("userId")));
		/* ***************************************** */

		Exercise exercise = Exercise.byId(exerciseId);
		if (exercise == null) {
			return redirect(routes.Application.showIndexPage());
		}

		/** PERMISSIONS **/
		if(!exercise.canEditExercise(loggedInUser)) {
			return redirect(routes.Application.showIndexPage());
		}

		SimpleDateFormat from = new SimpleDateFormat("MM/dd/yyyy");
		String releaseDate = from.format(exercise.releaseDate);
		ExerciseForm data = new ExerciseForm(loggedInUser.id, exercise.name, exercise.description, releaseDate, Exercise.Type.toString(exercise.exerciseType));
		Form<ExerciseForm> form = Form.form(ExerciseForm.class);
		form = form.fill(data);
		return ok(editExercise.render(form, exercise));
	}
}