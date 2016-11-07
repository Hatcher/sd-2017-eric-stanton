package controllers;

import forms.AnnouncementForm;
import models.Course;
import models.common.Announcement;
import models.common.Prompt;
import models.common.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.editAnnouncement;
import views.html.newAnnouncement;

import java.util.Arrays;

import static play.data.Form.form;

public class AnnouncementController extends Controller {
	/**********************
	 * Create a new Message of type Announcement and add it to the database
	 * @permission A, I
	 **********************/
	public static Result createAnnouncement(Long courseId) {
		/* ***************************************** */
		Boolean[] permissions = {true,  true,  false};

		if (!User.validateUser(session("userId"), permissions)) { return redirect(routes.Application.showIndexPage()); }
		User loggedInUser = User.byId(Long.parseLong(session("userId")));
		/* ***************************************** */

		Course course = Course.byId(courseId);

		/** PERMISSIONS **/
		if(!course.isCreator(loggedInUser) && !course.isCoinstructor(loggedInUser) && !course.institution.isAdmin(loggedInUser)) {
			return redirect(routes.Application.showIndexPage());
		}


		Form<AnnouncementForm> newAnnouncementForm = form(AnnouncementForm.class).bindFromRequest();
		Announcement announcement;

		if (newAnnouncementForm.hasErrors()) {
			return badRequest(newAnnouncement.render(newAnnouncementForm, course));
		} else {
			announcement = new Announcement(User.byId(Long.parseLong(session("userId"))), newAnnouncementForm.get().msg);
			Course.addAnnouncement(course, announcement);
		}
		flash("success", "Success! Your Announcement has been created.");
		return redirect("/createAnnouncement/" + announcement.id);
	}


//	/**********************
//	 * Edit an existing Announcement and overwrite its attributes in the database
//	 * @permission A, I
//	 **********************/
//	public static Result modifyAnnouncement(Long announcementId) {
//		/* ***************************************** */
//		Boolean[] permissions = {true,  true,  false};
//
//		if (!User.validateUser(session("userId"), permissions)) { return redirect(routes.Application.showIndexPage()); }
//		User loggedInUser = User.byId(Long.parseLong(session("userId")));
//		/* ***************************************** */
//
//		Form<AnnouncementForm> form = form(AnnouncementForm.class).bindFromRequest();
//		Announcement announcement = null;
//
//		if (form.hasErrors()) {
////			return badRequest(editAnnouncement.render(form, Announcement.byId(announcementId)));
//		} else {
//			announcement = Announcement.byId(announcementId);
//			AnnouncementForm announcementForm = form.get();
//
//			/** PERMISSIONS **/
//			if(!announcement.isCreator(loggedInUser) && !Course.getCourseForAnnouncement(announcementId).institution.isAdmin(loggedInUser)) {
//				return redirect(routes.Application.showIndexPage());
//			}
//
//			Prompt prompt = announcement.prompt;
//			prompt.text = announcementForm.msg;
//			Prompt.create(prompt);
//			announcement.prompt = prompt;
//
//			Announcement.create(announcement);
//		}
//
//		flash("success", "Success! Announcement has been modified.");
//		return redirect("/editAnnouncement/" + announcement.id);
//	}

	/**********************
	 * Delete an Announcement
	 * @permission A, I
	 **********************/
	public static Result delete(Long announcementId) {
		/* ***************************************** */
		Boolean[] permissions = {true,  true,  false};

		if (!User.validateUser(session("userId"), permissions)) { return redirect(routes.Application.showIndexPage()); }
		User loggedInUser = User.byId(Long.parseLong(session("userId")));
		/* ***************************************** */

		Announcement announcement = Announcement.byId(announcementId);
		if (announcement == null) {
			return ok("false");
		}

		/** PERMISSIONS **/
		if(!announcement.canDeleteAnnouncement(loggedInUser)) {
			return redirect(routes.Application.showIndexPage());
		}

		Announcement.delete(announcementId);

		return ok("true");
	}



	/* ********************************** RENDER ********************************** */
	/* **************************************************************************** */

	/**********************
	 * 	Load the page to create a new Announcement
	 * @permission A, I
	 **********************/
	public static Result showCreateAnnouncementPage(Long courseId) {
		/* ***************************************** */
		Boolean[] permissions = {true,  true,  false};

		if (!User.validateUser(session("userId"), permissions)) { return redirect(routes.Application.showIndexPage()); }
		User loggedInUser = User.byId(Long.parseLong(session("userId")));
		/* ***************************************** */

		Course course = Course.byId(courseId);

		/** PERMISSIONS **/
		if(!course.isCreator(loggedInUser) && !course.isCoinstructor(loggedInUser) && !course.institution.isAdmin(loggedInUser)) {
			return redirect(routes.Application.showIndexPage());
		}

		return ok(newAnnouncement.render(form(AnnouncementForm.class), course));
	}



	public static Result canDeleteAnnouncement(String id, String userId) {
		System.out.println(id);
		return ok(String.valueOf(Announcement.byId(Long.parseLong(id)).canDeleteAnnouncement(User.byId(Long.parseLong(userId)))));
	}


//	/**********************
//	 * 	Load the page to edit an existing Announcement
//	 * @permission A, I
//	 **********************/
//	public static Result showEditAnnouncementPage(Long announcementId) {
//		/* ***************************************** */
//		Boolean[] permissions = {true,  true,  false};
//
//		if (!User.validateUser(session("userId"), permissions)) { return redirect(routes.Application.showIndexPage()); }
//		User loggedInUser = User.byId(Long.parseLong(session("userId")));
//		/* ***************************************** */
//
//		Announcement announcement = Announcement.byId(announcementId);
//
//		/** PERMISSIONS **/
//		if(!announcement.isCreator(loggedInUser) && !Course.getCourseForAnnouncement(announcement).institution.isAdmin(loggedInUser)) {
//			return redirect(routes.Application.showIndexPage());
//		}
//
//		AnnouncementForm data = new AnnouncementForm(loggedInUser.id, announcement.prompt.text);
//		Form<AnnouncementForm> form = form(AnnouncementForm.class).fill(data);
//
//		return ok(editAnnouncement.render(form, announcement));
//	}
}
