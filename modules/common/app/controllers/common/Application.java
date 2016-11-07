package controllers.common;

import forms.common.UserForm;
import models.common.User;
import play.Routes;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static play.data.Form.form;


public class Application extends Controller {

	public static Result javascriptRoutesCommon() {
		response().setContentType("text/javascript");
		return ok(
//				Routes.javascriptRouter("jsRoutesCommon",
//						routes.javascript.QuestionController.createQuestionFromJSON()
//				)
		);
	}
}
