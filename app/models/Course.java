package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import forms.CourseForm;
import models.common.Announcement;
import models.common.Institution;
import models.common.User;
import play.data.validation.Constraints.Required;
import utilities.common.NameGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
public class Course extends Model {
	
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
	public String name;
		
	public String description;
	
	@Required
	public Date startDate;
	
	public Date endDate;
	
	@Required
	public boolean isArchived;
	
	@Required
	public boolean hasOpenEnrollment;

	@Required
	public boolean isShared = true;

	@Required
	@ManyToOne
	public Institution institution;
	
	@ManyToOne
	public User instructor;

	@ManyToMany
	@JoinTable(name="course_coinstructor")
	public Set<User> coInstructors;

	@ManyToMany(cascade = CascadeType.ALL)
	public Set<Announcement> announcements = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL)
	public Set<Exercise> exercises = new HashSet<>();
	
	@ManyToMany
	@JoinTable(name="course_student")
	public Set<User> students = new HashSet<>();


	/********************************
	 CONSTRUCTORS
	 ********************************/
	public Course(String name, String description, Date startDate, Date endDate, Institution institution, boolean hasOpenEnrollment, boolean isShared) {
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.institution = institution;
		this.hasOpenEnrollment = hasOpenEnrollment;
		this.isShared = isShared;
	}

	public Course(CourseForm courseForm) throws ParseException {
		SimpleDateFormat from = new SimpleDateFormat("MM/dd/yyyy");
		Date startDate = from.parse(courseForm.start);
		Date endDate = from.parse(courseForm.end);

		this.instructor = User.byId(courseForm.instructorId);
		this.name = courseForm.name;
		this.description = courseForm.description;
		this.institution = Institution.byId(courseForm.institutionId);
		this.isShared = courseForm.isShared;
		this.startDate = startDate;
		this.endDate = endDate;

		Set<User> coInstructors = new HashSet<User>();
		for (String coinstructorId : courseForm.coInstructors.split(",")) {
			if (!coinstructorId.equals("-1") && !coinstructorId.equals("")) {
				coInstructors.add(User.byId(coinstructorId));
			}
		}
		this.coInstructors = coInstructors;
	}


	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, Course> find = new Finder<>(Course.class);


	/********************************
	 CREATE / DELETE
	 ********************************/
	public static Course create(Course course) {
		course.save();
		return course;
	}

	public void delete(Long id) {
		Course course = find.ref(id);
		if (course == null) {
			return;
		}

		course.retired = true;
		course.save();
	}


	/********************************
	 ADD / REMOVE STUDENTS
	 ********************************/
	public void addStudent(Course course, User student) {
		students.add(student);
		course.save();
	}

	public void removeStudent(Course course, User student) {
		students.remove(student);
		course.save();
	}



	public boolean isInstructor(User user) {
		return this.instructor.id == user.id;
	}

	public boolean isCoinstructor(User user) {
		return User.getAllCoinstructorsForCourse(this.id).contains(user);
	}

	public boolean isCreator(User user) {
		return this.instructor.id == user.id;
	}



	/********************************
	 GETTERS
	 ********************************/

	//-----------Single-------------//

	//Get Course by ID
	public static Course byId(Long id) {
		return find.where()
				.eq("retired", false)
				.eq("id", id)
				.findUnique();
	}

	//Get Course by name
	public static Course byName(String name) {
		return find.where()
				.eq("retired", false)
				.eq("name", name)
				.findUnique();
	}

	// Get Course by an Exercise in it
	public static Course getCourseForExercise(Long exerciseId) {
		return find
				.where()
					.eq("retired", false)
					.eq("exercises.id", exerciseId)
					.findUnique();
	}


	// Get Course by an Announcement in it
	public static Course getCourseForAnnouncement(Long announcementId) {
		String sql = "select * from course_announcement " +
					 "where announcement_id = " + announcementId + ";";

		return Course.byId(Ebean.createSqlQuery(sql).findUnique().getLong("course_id"));
	}

	// get Course ID by an Exercise in it
	public static Long getCourseIdForExercise(Long exerciseId) {
		return find
				.where()
				.eq("retired", false)
				.eq("exercises.id", exerciseId)
				.findUnique().id;
	}


	//-----------Group-------------//

	//Get all Courses in the system
	public static List<Course> getAll() {
		return find.where()
					.eq("retired", false)
				.findList();
	}


	//Get all Courses where a User is either the instructor or a co-instructor
	public static List<Course> getAllCoursesForInstructor(Long instructorId) {
		List<Course> coursesAsInstructor = find.where()
				.eq("retired", false)
				.eq("instructor_id", instructorId)
				.findList();

		String sql = "select * from course_coinstructor " +
				"where user_id = " + instructorId + ";";

		List<Course> coursesAsCoinstructor = new ArrayList<>();
		for (SqlRow row : Ebean.createSqlQuery(sql).findList()) {
			coursesAsCoinstructor.add(Course.byId(row.getLong("course_id")));
		}

		coursesAsInstructor.addAll(coursesAsCoinstructor);
		return coursesAsInstructor;
	}


	//Get all Courses for a student
	public static List<Course> getAllCoursesForStudent(Long studentId) {
		return find
				.where()
					.eq("retired", false)
					.eq("students.id", studentId)
				.findList();
	}


	//Get all Courses at an Institution
	public static List<Course> getAllCoursesForInstitution(Long institutionId) {
		String sql = "select * from course " +
					 "where instructor_id in " +
						"(select id from user " +
						"where institution_id="+institutionId +
					 	" and retired=0)" +
					" and retired=0";

		List<Course> allCourses = new ArrayList<>();
		for (SqlRow row : Ebean.createSqlQuery(sql).findList()) {
			allCourses.add(Course.byId(row.getLong("id")));
		}

		return allCourses;
	}


	// Return the number of days until this Course ends
	public static long calculateDaysRemainingForCourse(Course course) {
		Calendar endDay = Calendar.getInstance();
		endDay.setTime(course.endDate);

		return (endDay.getTimeInMillis() - Calendar.getInstance().getTimeInMillis()) / (24 * 60 * 60 * 1000);
	}

	// Add an Announcement to a given Course in the database
	public static void addAnnouncement(Course course, Announcement announcement) {
		course.announcements.add(announcement);
		course.save();
	}

	// Figure out whether or not a course is currently active. Returns true if today is in the course's active range (startDate to endDate) and false otherwise.
	public static boolean isInSession(Course course) {
		Date today = new Date();

		// Consider the course still active if it opens today (>=) or closes today (<=)
		return ((today.compareTo(course.startDate) >= 0) && (today.compareTo(course.endDate) <= 0));
	}

	// Ann an Exercise to a given Course in the database
    public static void addExercise(Course course, Exercise exercise) {
        course.exercises.add(exercise);
        course.save();
    }


	////////////////////////////////////////////
	//TEST
	////////////////////////////////////////////

	public static void createSomeCourses(int numCourses) {
		List<User> allStudents = User.getAllByRole("Student");

		Random r = new Random();

		for (int i = 0; i < numCourses; i++) {
			//get a random name for the course
			NameGenerator ng = new NameGenerator();

			//get a random start and end time for the course
			long offset = Timestamp.valueOf("2015-07-14 00:00:00").getTime();
			long end = Timestamp.valueOf("2016-12-30 00:00:00").getTime();
			long diff = end - offset + 1;
			Timestamp rand = new Timestamp(offset + (long)(Math.random() * diff));

			//get a random instructor in the system and set them as the course's instructor
			List<User> allInstructors = User.getAllByRole("Instructor");
			allInstructors.addAll(User.getAllByRole("Administrator"));
			int randomInst = r.nextInt((allInstructors.size() - 1) + 1) + 1;
			User randomInstructor = allInstructors.get(randomInst-1);


			//create a new course
			Course course = new Course(ng.getName(), "desc", new Timestamp(offset), rand, randomInstructor.institution, r.nextBoolean(), r.nextBoolean());
			course.instructor = randomInstructor;

			//get a random number of students in the system and add them to the course
			int numStudents = r.nextInt(User.getAllByRole("Student").size());
			course.students = new HashSet<>(allStudents.subList(0, numStudents));

			for (User student : course.students) {
				//if the student is not currently associated with the institution, add them to the institution
				//(if adding to an open course, student is not official; if adding to a closed course, student is official)
				if (StudentInInstitution.getByStudentAndInstitution(student.id, course.instructor.institution.id) == null) {
					StudentInInstitution.create(new StudentInInstitution(student.id, course.instructor.institution.id, !course.hasOpenEnrollment));
				}
			}
			
			course.save();
		}
	}




	/* ******************************** PERMISSIONS ******************************* */
	/* **************************************************************************** */

	// view course
	public boolean canViewCourse(User user) {
		return this.isInstructor(user) || this.isCoinstructor(user) || this.institution.isAdmin(user) || this.isShared;
	}

	// edit course
	public boolean canEditCourse(User user) {
		return this.isInstructor(user) || this.isCoinstructor(user) || this.institution.isAdmin(user);
	}

	// copy course
	public boolean canCopyCourse(User user) {
		return this.isInstructor(user) || this.isCoinstructor(user) || this.institution.isAdmin(user) || this.isShared;
	}

	// delete course
	public boolean canDeleteCourse(User user) {
		return this.isInstructor(user) || this.institution.isAdmin(user);
	}

	// manage students
	public boolean canManageStudents(User user) {
		return this.isInstructor(user) || this.isCoinstructor(user) || this.institution.isAdmin(user);
	}

	// create exercise
	public boolean canCreateExercise(User user) {
		return this.isInstructor(user) || this.isCoinstructor(user) || this.institution.isAdmin(user);
	}

	// create announcement
	public boolean canCreateAnnouncement(User user) {
		return this.isInstructor(user) || this.isCoinstructor(user) || this.institution.isAdmin(user);
	}

}
	