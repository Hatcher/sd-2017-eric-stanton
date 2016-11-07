package models.common;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.Model;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.google.common.base.CharMatcher;
import forms.common.UserForm;
import org.apache.commons.lang3.text.WordUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import utilities.common.PasswordHash;

import javax.imageio.ImageIO;
import javax.persistence.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;
import java.util.*;
import java.util.List;

@Entity
public class User extends Model {
	/********************************
	 ENUMERATOR: For each User role
	 ********************************/
	public static enum Role {
		SUPERADMIN,
		ADMIN,
		INSTRUCTOR,
		STUDENT;
		
		public static Role getRole(String roleString) {
			Role role = null;
			
			switch(roleString) {
				case "Super Administrator":
					role = SUPERADMIN;
					break;
				case "Administrator":
					role = ADMIN;
					break;
				case "Instructor":
					role = INSTRUCTOR;
					break;
				case "Student":
					role = STUDENT;
					break;
				}
			
			return role;
		}
	}
	
	
	/********************************
	 FIELDS
	 ********************************/
	/* Universal */
	/*===========*/
	@Id
	public Long id;
	
	@Required
	public boolean retired = false;
	
	@CreatedTimestamp
	public Timestamp createdTime;
	
	@UpdatedTimestamp
	public Timestamp updatedTime;
	
	
	/* Specific */
	/*===========*/

	@Required
	public String firstName;

	@Required
	public String lastName;
	
	@Required
	@Column(unique = true)
	public String username;

	@Required
	public String password;	// hashed using utilities.PasswordHash
	
	@Email
	public String email;
		
	@Required
	public Role role;
	
	public Long creatorId;
	
	@ManyToOne (cascade = CascadeType.ALL)
	public Institution institution;
	
	
	/********************************
	 CONSTRUCTORS
	 ********************************/
	public User(String email, String username, String password, String firstName, String lastName, Role role, Long creatorId) throws NoSuchAlgorithmException, InvalidKeySpecException {
		this.email = email;
		this.username = username;
		this.password = PasswordHash.createHash(password);
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		this.creatorId = creatorId;
	}

	public User(UserForm userForm) throws InvalidKeySpecException, NoSuchAlgorithmException {
		this.email = userForm.email;
		this.username = userForm.username;
		this.password = PasswordHash.createHash(userForm.password);
		this.firstName = userForm.firstName;
		this.lastName = userForm.lastName;
		this.role = Role.INSTRUCTOR;
		this.creatorId = userForm.creatorId;
		this.institution = Institution.byName(userForm.institutionName);
	}

	
	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, User> find = new Finder<Long, User>(User.class);
	
	
	/********************************
	 CREATE / DELETE 
	 ********************************/
	public static User create(User user) {
		user.save();
		return user;
	}

	public static void delete(Long id) {
		User user = find.ref(id);
		if (user == null) {
			return;
		}
		
		user.retired = true;
		user.save();
	}


	/********************************
	 VALIDATION
	 ********************************/

	public static boolean validateUser(String loggedInUserId, Boolean[] permissions) {
		if (loggedInUserId == null || User.byId(Long.parseLong(loggedInUserId)) == null) {
			return false;
		}

		//only allow users with permission to view this page
		return User.hasPermission(loggedInUserId, Arrays.asList(permissions));
	}

	
	//check if a user has the proper permissions to view a page
	public static boolean hasPermission(String userId, List<Boolean> pflags) {
		User user = User.byId(Long.parseLong(userId));

		List<Role> roles = new ArrayList<>();
		roles.add(User.Role.values()[0]); //SA always has permission
		for (int i=0; i < pflags.size(); i++) {
			if (pflags.get(i)) {
				roles.add(User.Role.values()[i+1]);
			}
		}

		return roles.contains(user.role);
	}


	/********************************
	 RETURN ATTRIBUTES
	 ********************************/
	public String getFullName() {
		return this.firstName + " " + this.lastName;
	}

	
	/********************************
	 GETTERS 
	 ********************************/
	
	//-----------Single-------------//
	
	//Get User by ID (Long)
	public static User byId(Long id) {
		return find.where()
					.ne("retired", true)
					.eq("id", id)
				.findUnique();
	}

    //Get User by ID (String)
    public static User byId(String idString) {
        Long id = Long.parseLong(idString);
        return find.where()
                .ne("retired", true)
                .eq("id", id)
                .findUnique();
    }

	//Get User by email address
	public static User byEmail(String email) {
		return find.where()
					.ne("retired", true)
					.eq("email", email)
				.findUnique();
	}

	//Get User by username/email and password
	public static User byLogin(String usernameOrEmail, String password) {
		User user = byUsername(usernameOrEmail);
		if (user == null) {
			user = byEmail(usernameOrEmail);
			if (user == null) {
				return null;
			}
		}

		try {
			if (!PasswordHash.validatePassword(password, user.password)) {
				return null;
			}
		} catch (Exception e) {
			return null;
		}

		return user;
	}

	//Get User by username
	public static User byUsername(String username) {
		return find.where()
					.eq("username", username)
				.findUnique();
	}


	//Get User by username OR email address (used for logging in)
	public static User byUsernameOrEmail(String usernameOrEmail) {
		return find.where()
					.ne("retired", true)
					.or(Expr.eq("username", usernameOrEmail), Expr.eq("email", usernameOrEmail))
				.findUnique();
	}
	
	//-----------Group-------------//

	//Get all Users in the system 
	public static List<User> getAll() {
		return find.where()
					.ne("retired", true)
				.findList();
	}
	
	// Get all Users in the system by role
	public static List<User> getAllByRole(String role) {
			return find.where()
						.ne("retired", true)
						.eq("role", Role.getRole(role))
						.orderBy().asc("lastName")
					.findList();
	}
	
	// Get all instructor Users at a given institution
	public static List<User> getAllInstructorsForInstitution(Long institutionId) {
		return find.where()
					.eq("retired", false)
					.eq("role", Role.INSTRUCTOR)
					.eq("institution_id", institutionId)
					.eq("institution.retired", false)
					.orderBy().asc("lastName")
				.findList();
	}


	// Get all co-instructor Users at a given institution
	public static List<User> getAllCoinstructorsForCourse(Long courseId) {
		String sql = "select * from course_coinstructor " +
					 "where course_id=" + courseId;

		List<User> allCoinstructors = new ArrayList<User>();
		for (SqlRow row : Ebean.createSqlQuery(sql).findList()) {
			allCoinstructors.add(User.byId(row.getLong("user_id")));
		}

		return allCoinstructors;
	}


	// Get all student Users at a given institution
	public static List<User> getAllStudentsForInstitution(Long institutionId) {
		System.out.print(institutionId);
		String sql = "select * from user" +
					 " where id in " +
							"(select student_id from student_in_institution" +
					 		" where institution_id = " + institutionId + "" +
							" and retired=0)" +
					" and retired=0" +
					" order by last_name";

		List<User> allStudents = new ArrayList<User>();
		for (SqlRow row : Ebean.createSqlQuery(sql).findList()) {
			allStudents.add(User.byId(row.getLong("id")));
		}
		
		return allStudents;
	}
	
	// Get all student Users in a given course
	public static List<User> getAllStudentsForCourse(Long courseId) {
		String sql = "select * from user" +
					 " where retired=0" +
					 " and role="+ Role.STUDENT.ordinal() +
					 " and id in " +
						"(select user_id from course_student " +
						"where retired=0 " +
						"and course_id="+courseId+")" +
					" order by last_name";

		List<User> allStudents = new ArrayList<User>();
		for (SqlRow row : Ebean.createSqlQuery(sql).findList()) {
			allStudents.add(User.byId(row.getLong("id")));
		}

		return allStudents;
	}

	// Get all student Users in any of an Instructor's courses
	public static List<User> getAllStudentsForInstructor(Long instructorId) {
		String sql = "select * from user" +
				" where retired=0" +
				" and role="+ Role.STUDENT.ordinal() +
				" and id in " +
					"(select user_id from course_student " +
					"where retired=0 " +
					"and course_id IN (" +
						"select id from course " +
						"where instructor_id=" + instructorId + "" +
					")" +
				") order by last_name";


		List<User> allStudents = new ArrayList<User>();
		for (SqlRow row : Ebean.createSqlQuery(sql).findList()) {
			allStudents.add(User.byId(row.getLong("id")));
		}

		return allStudents;
	}
	
	//Aidan and Dhruv
	// Returns all Students for a given Institution where the student is not in the specified course
	public static List<User> getAllStudentsForInstitutionNotInCourse(Long institutionId, Long courseId) {
		String sql = "select * from user " +
					 "where id in " + 
						"(select student_id from student_in_institution" + 
						" where institution_id = " + institutionId + 
						" and retired=0) " +
					 "and id not in " +
						"(select user_id from course_student"+
						" where course_id = " + courseId+") " +
					 "and retired=0 " +
					 "order by last_name";
		
		List<User> students = new ArrayList<User>();
		for (SqlRow row : Ebean.createSqlQuery(sql).findList()) {
			students.add(User.byId(row.getLong("id")));
		}
		return students;
	}
	
	//Aidan
	// Returns all Students that belong to a given question list
	public static List<User> getAllStudentsForQuestionList(Long questionListId) {
		//when to put retired?
		String sql = "select * from user " +
					 "where id in " + 
						"(select user_id from question_list_user" + 
						" where question_list_id = " + questionListId + ") " +
					 "and retired=0" +
					 "order by last_name";
		
		List<User> students = new ArrayList<User>();
		for (SqlRow row : Ebean.createSqlQuery(sql).findList()) {
			students.add(User.byId(row.getLong("id")));
		}
		return students;
	}

	
	//////////////////////////////
	// TEST
	/////////////////////////////
	public static void createSomeUsers(int numUsers, Role role) {
		int numGenerated = 0;
		while (numGenerated < numUsers) {
			String out = null;
			try {
//				URL url = new URL("http://api.randomuser.me/");
				out = new Scanner(new URL("http://api.randomuser.me/").openStream(), "UTF-8").useDelimiter("\\A").next();
			} catch (IOException e) {
				e.printStackTrace();
			}

			JSONParser parser = new JSONParser();
			JSONObject json = null;
			try {
				json = (JSONObject) parser.parse(out);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			JSONArray results = (JSONArray) json.get("results");

			Iterator it = results.iterator();

			while (it.hasNext()) {
				JSONObject slide = (JSONObject) it.next();
				String lastName = (String) ((JSONObject) slide.get("name")).get("last");
				String firstName = (String) ((JSONObject) slide.get("name")).get("first");

				String imageURL = (String) ((JSONObject) slide.get("picture")).get("large");


				firstName = WordUtils.capitalize(firstName);
				lastName = WordUtils.capitalize(lastName);


				String username = firstName.toLowerCase();
				String password = username;
				String email = username + "@email.com";


				if (!CharMatcher.ASCII.matchesAllOf(firstName + " " + lastName)) {
					continue;
				}

				User user;
				if (User.byUsername(username) != null) {
					continue;
				}
				try {
					Random r = new Random();
					user = User.create(new User(email, username, password, firstName, lastName, role, 0L));
					int instId = r.nextInt((Institution.getAll().size() - 1) + 1) + 1;
					if (role != Role.STUDENT) {
						user.institution = Institution.byId((long) instId);
					}

					try {
						URL url = new URL(imageURL);
						BufferedImage bufferedImage = ImageIO.read(url);

						File usersFolder = new File("public/images/users/");

						if (!usersFolder.exists()) {
							usersFolder.mkdir();
						}

						BufferedImage resized = new BufferedImage(100, 100, bufferedImage.getType());
						Graphics2D g = resized.createGraphics();
						g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
						g.drawImage(bufferedImage, 0, 0, 100, 100, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null);
						g.dispose();

						File convertedFile = new File("public/images/users", user.id.toString() + ".png");
						resized.getScaledInstance(100, 100, BufferedImage.SCALE_SMOOTH);
						BufferedImage croppedImage = resized.getSubimage(0, 0, 100, 100);
						ImageIO.write(croppedImage, "png", convertedFile);
					} catch (Exception ioe) {
						user.delete();
					}

					user.save();
					numGenerated++;
				} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
