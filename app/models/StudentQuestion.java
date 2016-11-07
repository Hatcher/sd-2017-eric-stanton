package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import play.data.validation.Constraints.Required;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
public class StudentQuestion extends Model {
	/********************************
	 ENUMERATOR: For the different possible statuses of a Student Question
	 ********************************/
	public static enum Status {
		IN_REVIEW,
		ACCEPTED,
		REJECTED;
		
		public static Status getType(String typeString) {
			Status type = null;
			
			switch(typeString) {
				case "In Review":
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
	public Long questionId;
	
	@Required
	public Long courseId;
	
	@Required
	public Status status;
	
	
	/********************************
	 CONSTRUCTORS
	 ********************************/
	public StudentQuestion(Long courseId, Long questionId, Status status) {
		this.courseId = courseId;
		this.questionId = questionId;
		this.status = status;
	}
	
	
	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, StudentQuestion> find = new Finder<Long, StudentQuestion>(StudentQuestion.class);
	

	/********************************
	 CREATE / DELETE 
	 ********************************/
	public static StudentQuestion create(StudentQuestion questionRecord) {
		questionRecord.save();
		return questionRecord;
	}

	public static void delete(Long id) {
		StudentQuestion questionRecord = find.ref(id);
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

	//Get single Student_Question relation by ID
	public static StudentQuestion byId(Long id) {
		return find.where()
					.ne("retired", true)
					.eq("id", id)
				.findUnique();
	}
	
	
	//-----------Group-------------//

	//Get all Student_Question relations in the system 
	public static List<StudentQuestion> getAll() {
		return find.where()
					.ne("retired", true)
				.findList();
	}
	
	//Aidan
	//Retuns a list of all Student_Questions that are marked with the given status
	public static List<StudentQuestion> getAllStudentQuestionsForStudentByStatus(Long studentId, String status) {
		String sql = "select * from student_question" +
					 " where question_id in " +
						"(select id from question" +
						" where submitter_id = " + studentId + 
						" and retired = 0)" +
					 " and status = " + Status.getType(status).ordinal();

		List<StudentQuestion> questions = new ArrayList<>();
		for (SqlRow row : Ebean.createSqlQuery(sql).findList()) {
			questions.add(StudentQuestion.byId(row.getLong("id")));
		}
		return questions;
	}
}
	