package forms.common;

import models.common.Institution;
import models.common.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserForm {
	private static final String EMAIL_PATTERN =
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
					+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public String firstName;
	public String lastName;
	public String institutionName;
	public String email;
	public String username;
	public String password;
	public String passwordRepeat;
	public Long creatorId;


	public UserForm() {}
	public UserForm(String firstName, String lastName, String email, String username, String password, String passwordRepeat, Long creatorId, String institutionName) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.institutionName = institutionName;
		this.email = email;
		this.username = username;
		this.password = password;
		this.passwordRepeat = passwordRepeat;
		this.creatorId = creatorId;
	}

	public String validate() {

		String [] varArray = {firstName, lastName, institutionName, email, username, password};
		String [] varStrArray = {"first name", "last name", "institution name", "email address", "username", "password"};

		for(int i = 0; i < varArray.length; i++) {

			if(varArray[i].length() <= 0) {
				return "No " + varStrArray[i] + " specified! Please provide one, then try again.";
			}

			else if(varArray[i].length() > 255) {
				return "Your chosen " + varStrArray[i] + " is too long! Please provide a different one, then try again.";
			}

		}

		//Create a new Institution if the Institution the user specified does not already exist
		if(institutionName != null && Institution.byName(institutionName) == null) {
			Institution institution = new Institution(institutionName, "locationnnn", "descriptionnnn");
			Institution.create(institution);
		}

		User userByUsername = User.byUsername(username);
		if(userByUsername != null) {
			return "The username you specified is already in use! Please provide a different one, then try again.";
		}

		User userByEmail = User.byEmail(email);
		if(userByEmail != null) {
			return "The email you specified is already in use! Please provide a different one, then try again.";
		}


		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		if (!matcher.matches()) {
			return "Invalid email address! Please provide a different one, then try again.";
		}

		if(!password.equals(passwordRepeat)) {
			return "Password mismatch! Please retype your password, then try again.";
		}

		return null;
	}
}
