package forms.common;

import models.common.User;

public class LoginForm {

	public String username;
	public String password;

	public String validate() {
		User user = User.byLogin(username, password);
		if (user == null) {
			return "Invalid username/email and/or password! Please try again.";
		}

		if (user.role != User.Role.INSTRUCTOR && user.role != User.Role.ADMIN) {
			return "Invalid username/email and/or password! Please try again.";
		}

		return null;
	}

}
