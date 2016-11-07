package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import play.data.validation.Constraints.Required;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class StudentInInstitution extends Model {
	
	/********************************
	 FIELDS
	 ********************************/
	/* Universal */
	/*===========*/
	@Id
	public Long id;
	
	@CreatedTimestamp
	public Timestamp createdTime;
	
	/* Specific */
	/*===========*/
	@Required
	public Long studentId;
	
	@Required
	public Long institutionId;
	
	@Required
	public boolean official;
	
	
	/********************************
	 CONSTRUCTORS
	 ********************************/
	public StudentInInstitution(Long studentId, Long institutionId, boolean official) {
		this.studentId = studentId;
		this.institutionId = institutionId;
		this.official = official;
	}
	
	
	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, StudentInInstitution> find = new Finder<Long, StudentInInstitution>(StudentInInstitution.class);
	
	
	/********************************
	 CREATE / DELETE 
	 ********************************/
	public static StudentInInstitution create(StudentInInstitution studentInInstitution) {
		studentInInstitution.save();
		return studentInInstitution;
	}

	public static void delete(Long id) {
		StudentInInstitution institution = find.ref(id);
		if (institution == null) {
			return;
		}
		
		institution.save();
	}
	
	
	/********************************
	 GETTERS 
	 ********************************/

	 // Get single relation by student and institution (null if no such relation exists)
	public static StudentInInstitution getByStudentAndInstitution(Long studentId, Long institutionId) {
		return find.where()
				.eq("student_id", studentId)
				.eq("institution_id", institutionId)
			.findUnique();
	}
}