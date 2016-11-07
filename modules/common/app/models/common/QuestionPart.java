package models.common;


import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class QuestionPart extends Model {
	/********************************
	 FIELDS
	 ********************************/
	/* Universal */
	/*===========*/
	@Id
	public Long id;

	@Constraints.Required
	public boolean retired = false;

	@CreatedTimestamp
	public Timestamp createdTime;

	@UpdatedTimestamp
	public Timestamp updatedTime;


	/* Specific */
	/*===========*/
	@ManyToOne
	public Prompt prompt;

	@ManyToOne
	public Content content;

	@OneToMany(cascade = CascadeType.ALL)
	public Set<Choice> choices = new HashSet<Choice>();


	/********************************
	 CONSTRUCTORS
	 ********************************/
	public QuestionPart() {}

	public QuestionPart(Prompt prompt) {
		this.prompt = prompt;
	}


	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Model.Finder<Long, QuestionPart> find = new Model.Finder<Long, QuestionPart>(QuestionPart.class);


	/********************************
	 CREATE / DELETE
	 ********************************/
	public static QuestionPart create(QuestionPart questionPart) {
		questionPart.save();
		return questionPart;
	}

	public static void delete(Long id) {
		//delete all choices first
		for (Choice choice : Choice.getAllChoicesForQuestionPart(id)) {
			choice.retired = true;
		}

		QuestionPart questionPart = find.ref(id);
		if (questionPart == null) {
			return;
		}

		questionPart.retired = true;
		questionPart.save();
	}

	// Create a new QuestionPart that is a duplicate of the given questionPart,
	// and assign it to the given QuestionGroup
	public static QuestionPart copy(QuestionPart questionPart, Question question) {
		QuestionPart newQuestionPart = new QuestionPart(questionPart.prompt);

		for (Choice choice : Choice.getAllChoicesForQuestionPart(questionPart.id)) {
			Choice.copy(choice, newQuestionPart);
		}

		question.addQuestionPart(newQuestionPart);
		newQuestionPart.save();
		return newQuestionPart;
	}


	/********************************
	 GETTERS
	 ********************************/

	//-----------Single-------------//

	//Get QuestionPart by ID
	public static QuestionPart byId(Long id) {
		return find.where()
				.ne("retired", true)
				.eq("id", id)
				.findUnique();
	}

	//-----------Group-------------//

	//Get all Questions in the system
	public static List<QuestionPart> getAll() {
		return find.where()
				.ne("retired", true)
				.findList();
	}

	//Get all Questions in a QuestionGroup
	public static List<QuestionPart> getAllPartsForQuestion(Long questionId) {
		String sql = "select question_part_id from question_question_part " +
				"where question_id = " + questionId;

		List<QuestionPart> questionParts = new ArrayList<QuestionPart>();
		for (SqlRow row : Ebean.createSqlQuery(sql).findList()) {
			questionParts.add(QuestionPart.byId(row.getLong("question_part_id")));
		}

		return questionParts;
	}
}
