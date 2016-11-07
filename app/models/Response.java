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
public class Response extends Model {
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
	public Long entityId;
	
	@Required
	public Long submitterId;
	
	
	/********************************
	 CONSTRUCTORS
	 ********************************/
	public Response(Long entityId, Long submitterId) {
		this.entityId = entityId;
		this.submitterId = submitterId;
	}
	
	
	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, Response> find = new Finder<Long, Response>(Response.class);
	

	/********************************
	 CREATE / DELETE 
	 ********************************/
	public static Response create(Response questionRecord) {
		questionRecord.save();
		return questionRecord;
	}

	public static void delete(Long id) {
		Response questionRecord = find.ref(id);
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
	public static Response byId(Long id) {
		return find.where()
					.ne("retired", true)
					.eq("id", id)
				.findUnique();
	}

	//-----------Group-------------//

	//Get all List Records in the system 
	public static List<Response> getAll() {
		return find.where()
					.ne("retired", true)
				.findList();
	}
}
	