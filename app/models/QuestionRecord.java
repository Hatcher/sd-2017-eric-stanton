package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import play.data.validation.Constraints.Required;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.sql.Timestamp;
import java.util.List;

@Entity
public class QuestionRecord extends Model {
	
	/********************************
	 ENUMERATOR: For each Question List type
	 ********************************/
	public static enum Type {
		EVALUATION,
		SURVEY;
		
		public static Type getType(String typeString) {
			Type type = null;
			
			switch(typeString) {
				case "Evaluation":
					type = EVALUATION;
					break;
				case "Survey":
					type = SURVEY;
					break;
				}
			
			return type;
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
	public double timeToComplete;
	
	@Required
	public int attemptNumber;
	
	@Required
	public boolean isCleared;
	
	@OneToOne
	public ExerciseRecord exerciseRecord;
	
	@ManyToOne
	public Long submitterId;
	
	@ManyToOne
	public Long questionId;
	
	
	/********************************
	 CONSTRUCTORS
	 ********************************/
	public QuestionRecord(Long submitterId, Long questionId, ExerciseRecord exerciseRecord, double timeToComplete, int attemptNumber, boolean isCleared) {
		this.submitterId = submitterId;
		this.questionId = questionId;
		this.exerciseRecord = exerciseRecord;
		this.timeToComplete = timeToComplete;
		this.attemptNumber = attemptNumber;
		this.isCleared = isCleared;
	}
	
	
	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, QuestionRecord> find = new Finder<Long, QuestionRecord>(QuestionRecord.class);
	

	/********************************
	 CREATE / DELETE 
	 ********************************/
	public static QuestionRecord create(QuestionRecord questionRecord) {
		questionRecord.save();
		return questionRecord;
	}

	public static void delete(Long id) {
		QuestionRecord questionRecord = find.ref(id);
		if (questionRecord == null) {
			return;
		}
		
		questionRecord.retired = true;
		questionRecord.save();
	}
	
	
	/********************************
	 GETTERS 
	 ********************************/

	//-----------Single-------------//

	//Get single List Record by ID
	public static QuestionRecord byId(Long id) {
		return find.where()
					.ne("retired", true)
					.eq("id", id)
				.findUnique();
	}
	
	//-----------Group-------------//

	//Get all List Records in the system 
	public static List<QuestionRecord> getAll() {
		return find.where()
					.ne("retired", true)
				.findList();
	}
}
	