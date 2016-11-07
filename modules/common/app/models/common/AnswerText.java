package models.common;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import play.data.validation.Constraints.Required;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class AnswerText extends Model {
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
	public String answer;

	
	/********************************
	 CONSTRUCTORS
	 ********************************/
	
	public AnswerText(String text) {
		this.answer = text;
	}
	
	
	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, AnswerText> find = new Finder<Long, AnswerText>(AnswerText.class);


	/********************************
	 CREATE / DELETE 
	 ********************************/
	public static AnswerText create(AnswerText answer_text) {
		answer_text.save();
		return answer_text;
	}

	public static void delete(Long id) {
		AnswerText answer_text = find.ref(id);
		if (answer_text == null) {
			return;
		}

		answer_text.retired = true;
		answer_text.save();
	}


	/********************************
	 GETTERS
	 ********************************/
	 
	//-----------Single-------------//

	//Get AnswerText by ID
	public static AnswerText byId(Long id) {
		return find.where()
				.eq("retired", false)
				.eq("id", id)
				.findUnique();
	}

	//Get AnswerText by text
	public static AnswerText byAnswer(String answer) {
		return find.where()
				.eq("retired", false)
				.eq("answer", answer)
				.findUnique();
	}
}