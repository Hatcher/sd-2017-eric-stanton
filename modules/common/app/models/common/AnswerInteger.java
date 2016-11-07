package models.common;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import play.data.validation.Constraints.Required;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.List;

@Entity
public class AnswerInteger extends Model {
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
	public int answer;


	/********************************
	 CONSTRUCTORS
	 ********************************/
	
	public AnswerInteger(int number) {
		this.answer = number;
	}
	
	
	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, AnswerInteger> find = new Finder<Long, AnswerInteger>(AnswerInteger.class);


	/********************************
	 CREATE / DELETE 
	 ********************************/
	public static AnswerInteger create(AnswerInteger answer_integer) {
		answer_integer.save();
		return answer_integer;
	}

	public static void delete(Long id) {
		AnswerInteger answer_integer = find.ref(id);
		if (answer_integer == null) {
			return;
		}

		answer_integer.retired = true;
		answer_integer.save();
	}

	/********************************
	 GETTERS 
	 ********************************/
	
	//-----------Single-------------//

	//Get AnswerInteger by ID
	public static AnswerInteger byId(Long id) {
		return find.where()
				.eq("retired", false)
				.eq("id", id)
				.findUnique();
	}

	//Get AnswerInteger by answer
	public static AnswerInteger byAnswer(int answer) {
		return find.where()
				.eq("retired", false)
				.eq("answer", answer)
				.findUnique();
	}

	//-----------Group-------------//

	//Get all AnswerDecimals in the system
	public static List<AnswerInteger> getAll() {
		return find.where()
				.eq("retired", false)
				.findList();
	}
}