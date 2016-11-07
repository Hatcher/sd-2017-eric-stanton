package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import play.data.validation.Constraints.Required;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.List;


@Entity
public class FlaggedQuestion extends Model {
	/********************************
	 ENUMERATOR: For ethe different possible statuses of a Flagged Question
	 ********************************/
	public static enum Status {
		IN_REVIEW,
		ACCEPTED,
		REJECTED;
		
		public static Status getType(String typeString) {
			Status type = null;
			
			switch(typeString) {
				case "In Reciew":
					type = IN_REVIEW;
					break;
				case "Accepted":
					type = ACCEPTED;
					break;
				case "Rejected":
					type = REJECTED;
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
	public Long exerciseId;
	
	@Required
	public Long questionId;
	
	@Required
	public Status status;
	
	
	/********************************
	 CONSTRUCTORS
	 ********************************/
	public FlaggedQuestion(Long exerciseId, Long questionId, Status status) {
		this.exerciseId = exerciseId;
		this.questionId = questionId;
		this.status = status;
	}
	
	
	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, FlaggedQuestion> find = new Finder<Long, FlaggedQuestion>(FlaggedQuestion.class);
	
	
	/********************************
	 CREATE / DELETE 
	 ********************************/
	public static FlaggedQuestion create(FlaggedQuestion questionRecord) {
		questionRecord.save();
		return questionRecord;
	}

	public static void delete(Long id) {
		FlaggedQuestion questionRecord = find.ref(id);
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

	//Get List Record by ID
	public static FlaggedQuestion getFlaggedQuestionById(Long id) {
		return find.where()
					.ne("retired", true)
					.eq("id", id)
				.findUnique();
	}

	//-----------Group-------------//

	//Get all List Records in the system 
	public static List<FlaggedQuestion> getAll() {
		return find.where()
					.ne("retired", true)
				.findList();
	}
}
	