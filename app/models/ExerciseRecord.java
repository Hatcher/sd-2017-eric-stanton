package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import play.data.validation.Constraints.Required;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ExerciseRecord extends Model {
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
	public double timeToComplete;

	@Required
	public int attemptNumber;

	@Required
	public boolean isCleared;

	@Required
	public int numberCorrect;

	@Required
	public int numberAnswered;

	@ManyToOne
	public Long submitterId;

	@ManyToOne
	public Long exerciseId;


	/********************************
	 CONSTRUCTORS
	 ********************************/
	public ExerciseRecord(double timeToComplete, int attemptNumber, boolean isCleared, int numberCorrect, int 
			numberAnswered, Long submitterId, Long exerciseId) {
		this.timeToComplete = timeToComplete;
		this.attemptNumber = attemptNumber;
		this.isCleared = isCleared;
		this.numberCorrect = numberCorrect;
		this.numberAnswered = numberAnswered;
		this.submitterId = submitterId;
		this.exerciseId = exerciseId;
	}


	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, ExerciseRecord> find = new Finder<Long, ExerciseRecord>(ExerciseRecord.class);


	/********************************
	 CREATE / DELETE
	 ********************************/
	public static ExerciseRecord create(ExerciseRecord exerciseRecord) {
		exerciseRecord.save();
		return exerciseRecord;
	}

	public static void delete(Long id) {
		ExerciseRecord exerciseRecord = find.ref(id);
		if (exerciseRecord == null) {
			return;
		}

		exerciseRecord.retired = true;
		exerciseRecord.save();
	}


	/********************************
	 GETTERS
	 ********************************/

	//-----------Single-------------//

	//Get single List Record by ID
	public static ExerciseRecord byId(Long id) {
		return find.where()
				.ne("retired", true)
				.eq("id", id)
				.findUnique();
	}

	// Return true if a Record for the nth attempt for this Exercise by this Student exists
	public static boolean recordExists(Long studentId, Long exerciseId, int attemptNum) {
		ExerciseRecord existingRecord = find.where()
				.ne("retired", true)
				.eq("exercise_id", exerciseId)
				.eq("submitter_id", studentId)
				.eq("attempt_number", attemptNum)
				.findUnique();

		return existingRecord != null;
	}

	// Return the x most recent ExerciseRecords for any Exercise of a given type
	public static List<ExerciseRecord> getXMostRecentExerciseRecordsOfType(int x, String typeString) {
		String sql = "select exercise_record.id from exercise_record " +
						"INNER JOIN exercise " +
						"on exercise_record.exercise_id=exercise.id " +
					 "where exercise.exercise_type="+ Exercise.Type.getTypeInt(Exercise.Type.getType(typeString)) + " " +
					 "order by exercise_record.updated_time DESC " +
					 "limit "+x+";";

		List<ExerciseRecord> exerciseRecords = new ArrayList<ExerciseRecord>();
		for (SqlRow row : Ebean.createSqlQuery(sql).findList()) {
			exerciseRecords.add(ExerciseRecord.byId(row.getLong("id")));
		}

		return exerciseRecords;
	}

	// Get the most recent Exercise Record that a given Student has completed for a given Exercise
	public static ExerciseRecord getMostRecentExerciseRecordForStudent(Long exerciseId, Long studentId) {
		String sql = "select exercise_record.id, exercise_record.is_cleared " +
					 "from exercise_record " +
							"INNER JOIN exercise " +
							"on exercise_record.exercise_id=exercise.id " +
							"where exercise_id="+exerciseId+" " +
							"and submitter_id="+studentId+" " +
					 "order by attempt_number DESC " +
				     "limit 1;";

		SqlRow row = Ebean.createSqlQuery(sql).findUnique();

		if (row == null) {
			return null;
		} else {
			return ExerciseRecord.byId(row.getLong("id"));
		}
	}

	// Get the most recent score a given Student has achieved for a given Exercise
	public static String getMostRecentScoreForUser(Long exerciseId, Long studentId) {
		ExerciseRecord mostRecentRecord = ExerciseRecord.getMostRecentExerciseRecordForStudent(exerciseId, studentId);

		if (mostRecentRecord == null) {
			return "--";
		} else {
			return Double.toString(((double)mostRecentRecord.numberCorrect / (double)mostRecentRecord.numberAnswered)*100);
		}
	}

	//-----------Group-------------//

	//Get all List Records in the system
	public static List<ExerciseRecord> getAll() {
		return find.where()
				.ne("retired", true)
				.findList();
	}
}
