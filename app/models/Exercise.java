package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import forms.ExerciseForm;
import models.common.Question;
import models.common.User;
import play.data.validation.Constraints.Required;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Entity
public class Exercise extends Model {
	
	/********************************
	 ENUMERATOR: For each Exercise type
	 ********************************/
	public static enum Type {
		EXERCISE,
		EVALUATION,
		SURVEY;
		
		public static Type getType(String typeString) {
			Type type = null;
			
			switch(typeString) {
				case "Exercise":
					type = EXERCISE;
					break;
				case "Evaluation":
					type = EVALUATION;
					break;
				case "Survey":
					type = SURVEY;
					break;
				}
			
			return type;
		}


		public static int getTypeInt(Type type) {
			int typeInt = -1;

			switch(type) {
				case EXERCISE:
					typeInt = 0;
					break;
				case EVALUATION:
					typeInt = 1;
					break;
				case SURVEY:
					typeInt = 2;
					break;
			}

			return typeInt;
		}

		public static String toString(Type type) {
			String typeString = "";

			switch(type) {
				case EXERCISE:
					typeString = "Exercise";
					break;
				case EVALUATION:
					typeString = "Evaluation";
					break;
				case SURVEY:
					typeString = "Survey";
					break;
			}

			return typeString;
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
	@ManyToOne
	public User creator;

	@Required
	public String name;
	
	public String description;
	
	@Required
	public int orderIndex;
	
	@Required
	public Date releaseDate;
	
	@Required
	public boolean hasSpacedRepetition;
	
	@Required
	public boolean isHidden;
	
	@Required
	public Type exerciseType;
	
	@ManyToMany (cascade = CascadeType.ALL)
	public Set<Question> questions = new HashSet<Question>();
	
	
	/********************************
	 CONSTRUCTORS
	 ********************************/
	public Exercise(User creator, String name, String description, Date releaseDate, boolean hasSpacedRepetition, boolean isHidden, Type type) {
		this.creator = creator;
		this.name = name;
		this.description = description;
		this.releaseDate = releaseDate;
		this.hasSpacedRepetition = hasSpacedRepetition;
		this.isHidden = isHidden;
		this.exerciseType = type;
	}

    public Exercise(ExerciseForm exerciseForm, String typeString, int orderIndex) throws ParseException {
        SimpleDateFormat from = new SimpleDateFormat("MM/dd/yyyy");
        Date startDate = from.parse(exerciseForm.start);

	    this.creator = User.byId(exerciseForm.creatorId);
        this.name = exerciseForm.name;
        this.description = exerciseForm.description;
        this.releaseDate = startDate;
        this.hasSpacedRepetition = true;
        this.isHidden = false;
        this.exerciseType = Type.getType(typeString);
        this.orderIndex = orderIndex;
    }
		

	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, Exercise> find = new Finder<Long, Exercise>(Exercise.class);
		

	/********************************
	 CREATE / DELETE 
	 ********************************/
	public static Exercise create(Exercise exercise) {
		exercise.save();
		return exercise;
	}

	public static void delete(Long id) {
		Exercise exercise = find.ref(id);
		if (exercise == null) {
			return;
		}

		exercise.retired = true;
		exercise.save();
	}


	public boolean isCreator(User user) {
		return Objects.equals(this.creator.id, user.id);
	}


	// Figure out whether or not the exercise is currently active. Returns true if today is in the exercise's active range (startDate to endDate) and false otherwise.
	public boolean isInSession() {
		Date today = new Date();

		// Consider the course still active if it opens today (>=) or closes today (<=)
		return ((today.compareTo(this.releaseDate) >= 0));
	}


	/********************************
	 SETTERS
	 ********************************/
	public void addQuestion(Question question) {
		this.questions.add(question);
		this.save();
	}

	
	/********************************
	 GETTERS 
	 ********************************/

	//-----------Single-------------//

	//Get Exercise by ID
	public static Exercise byId(Long id) {
		return find.where()
				.eq("retired", 0)
				.eq("id", id)
				.findUnique();
	}
	
	
	//-----------Group-------------//

	//Get all Exercises in the system 
	public static List<Exercise> getAll() {
		return find.where()
					.eq("retired", 0)
				.findList();
	}

	//Get all Exercises for an instructor
	public static List<Exercise> getAllExercisesForCourse(Long courseId) {
		return find.where()
				.eq("retired", 0)
				.eq("course_id", courseId)
				.orderBy("releaseDate asc")
				.findList();
	}

	//Get all Exercises of a certain type from a certain course
	public static List<Exercise> getAllExercisesForCourseByType(Long courseId, Type type) {
		return find.where()
				.eq("retired", 0)
				.eq("course_id", courseId)
				.eq("exercise_type", type)
				.findList();
	}

	//Get all Exercises of a certain type from a certain course
	public static List<Exercise> getAllExercisesForCourseByType(Long courseId, String typeString) {
		Type type = Type.getType(typeString);
		return find.where()
				.eq("retired", 0)
				.eq("course_id", courseId)
				.eq("exercise_type", type)
				.findList();
	}

	//Get all Exercises for the given instructor that release on the given day
	public static List<Exercise> getAllExercisesForInstructorByReleaseDate(Long instructorId, Date releaseDate) {
		List<Long> courseIds = new ArrayList<Long>();
		for (Course course : Course.getAllCoursesForInstructor(instructorId)) {
			courseIds.add(course.id);
		}

		return find.where()
				.eq("retired", 0)
				.in("course_id", courseIds)
				.eq("release_date", releaseDate)
				.findList();
	}

	//Get all Exercises of a certain type from a certain course that the given student has not completed
	public static List<Long> getAllFinishedExercisesForCourseForStudentByType(Long courseId, Long studentId, Type
			type) {

		String sql = "select exercise.id from exercise " +
						"INNER JOIN exercise_record " +
						"on exercise.id=exercise_record.exercise_id " +
					 "where exercise.retired=0 " +
					 "and submitter_id="+studentId+" " +
					 "and course_id="+courseId+" " +
					 "and exercise_type="+ Type.getTypeInt(type)+" " +
					 "and is_cleared=1;";


		List<Long> allUnfinishedExerciseIds = new ArrayList<Long>();
		for (SqlRow row : Ebean.createSqlQuery(sql).findList()) {
			allUnfinishedExerciseIds.add(row.getLong("id"));
		}

		return allUnfinishedExerciseIds;
	}	
	
	//Get all Exercises for an instructor
	public static List<Exercise> getAllExercisesForInstructor(Long instructorId) {
		List<Long> li = new ArrayList<Long>();

		//get all courses for the given instructor
		for(Course course: Course.find.select("id").where().eq("retired", false).eq("instructor_id", instructorId).findList()) {
		    li.add(course.id);
		}
		
		return find.where()
					.eq("retired", 0)
					.in("course_id", li)
				.findList();
	}

	//Get all Exercises of a certain type for an instructor
	public static List<Exercise> getAllExercisesForInstructorByType(Long instructorId, Type type) {
		List<Long> li = new ArrayList<Long>();

		//get all courses for the given instructor
		for(Course course: Course.find.select("id").where().eq("retired", false).eq("instructor_id", instructorId).findList()) {
			li.add(course.id);
		}

		return find.where()
				.eq("retired", 0)
				.in("course_id", li)
				.eq("exercise_type", type)
				.findList();
	}

	// Returns true if a given Exercise is active (today is on or after its start date and on or before its end date)
	public static boolean isActive(Exercise exercise) {
		Date today = new Date();

		// Error if course does not exist
		if (Course.getCourseForExercise(exercise.id) == null) {
			return false;
		}

		// Consider the course still active if it opens today (>=) or closes today (<=)
		return ((today.compareTo(exercise.releaseDate) >= 0) && (today.compareTo(Course.getCourseForExercise(exercise.id).endDate) <= 0));
	}



	/* ******************************** PERMISSIONS ******************************* */
	/* **************************************************************************** */

	// view exercise
	public boolean canViewExercise(User user) {
		Course course = Course.getCourseForExercise(this.id);
		return course.isInstructor(user) || this.isCreator(user) || course.isCoinstructor(user) || course.institution.isAdmin(user) || course.isShared;
	}

	// edit exercise
	public boolean canEditExercise(User user) {
		Course course = Course.getCourseForExercise(this.id);
		System.out.println(course.isInstructor(user) + " " + this.isCreator(user) + " " + course.institution.isAdmin(user));
		return course.isInstructor(user) || this.isCreator(user) || course.institution.isAdmin(user);
	}

	// delete exercise
	public boolean canDeleteExercise(User user) {
		Course course = Course.getCourseForExercise(this.id);
		return course.isInstructor(user) || this.isCreator(user) || course.institution.isAdmin(user);
	}
}
	