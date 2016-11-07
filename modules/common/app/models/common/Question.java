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
public class Question extends Model {
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
	@Constraints.Required
	public int orderIndex;

	@Constraints.Required
	@ManyToOne
	public QuestionType questionType;

	@ManyToOne
	public Prompt prompt;

	@ManyToOne
	public Content content;

	@ManyToOne
	public User submitter;

	@Constraints.Required
	public boolean isGlobal;

	@Constraints.Required
	@ManyToOne
	public Project project;

	@Constraints.Required
	public boolean wasGenerated;

	@ManyToMany(cascade = CascadeType.ALL)
	public Set<QuestionPart> questionParts = new HashSet<QuestionPart>();


	/********************************
	 CONSTRUCTORS
	 ********************************/
	// question question with text content
	public Question(String promptText, ContentText contentText, QuestionType questionType, User submitter, Project project, boolean isGlobal, boolean wasGenerated) {
		Prompt prompt = Prompt.byText(promptText);
		if (promptText == null || promptText.trim().length() <= 0) {
			prompt = null;
		} else if (Prompt.byText(promptText) == null) {
			prompt = Prompt.create(new Prompt(promptText));
		}
		this.prompt = prompt;
		this.content = Content.create(new Content(contentText.id));
		this.questionType = questionType;
		this.submitter = submitter;
		this.project = project;
		this.isGlobal = isGlobal;
		this.wasGenerated = wasGenerated;
	}

	// Question with no content
	public Question(String promptText, QuestionType questionType, User submitter, Project project, boolean isGlobal, boolean wasGenerated) {
		Prompt prompt = Prompt.byText(promptText);
		if (promptText == null || promptText.trim().length() <= 0) {
			prompt = null;
		} else if (Prompt.byText(promptText) == null) {
			prompt = Prompt.create(new Prompt(promptText));
		}
		this.prompt = prompt;
		this.content = null;
		this.questionType = questionType;
		this.submitter = submitter;
		this.project = project;
		this.isGlobal = isGlobal;
		this.wasGenerated = wasGenerated;
	}

	// Question with no content or prompt
	public Question() {
		this.prompt = null;
		this.content = null;
	}


	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, Question> find = new Finder<Long, Question>(Question.class);


	/********************************
	 CREATE / DELETE
	 ********************************/
	public static Question create(Question question) {
		question.save();
		return question;
	}

	public static void delete(Long id) {
		Question question = find.ref(id);
		if (question == null) {
			return;
		}

		question.retired = true;
		question.save();
	}


	public void setOrderIndex(int index) {
		this.orderIndex = index;
		this.save();
	}


	public void addQuestionPart(QuestionPart questionPart) {
		this.questionParts.add(questionPart);
		this.save();
	}


	// Create a new Question that is a duplicate of the given Question
	public static Question copy(Question question) {
		Question newQuestion = new Question();
		newQuestion.prompt = question.prompt;
		newQuestion.content = question.content;
		newQuestion.questionType = question.questionType;
		newQuestion.submitter = question.submitter;
		newQuestion.project = question.project;
		newQuestion.isGlobal = question.isGlobal;
		newQuestion.wasGenerated = question.wasGenerated;

		for (QuestionPart questionPart : QuestionPart.getAllPartsForQuestion(question.id)) {
			QuestionPart.copy(questionPart, newQuestion);
		}

		return newQuestion;
	}


	/********************************
	 GETTERS
	 ********************************/

	//-----------Single-------------//

	//Get Question by ID
	public static Question byId(Long id) {
		return find.where()
				.ne("retired", true)
				.eq("id", id)
				.findUnique();
	}

	//-----------question-------------//

	//Get all Questions in the system
	public static List<Question> getAll() {
		return find.where()
				.ne("retired", true)
				.findList();
	}

	// Get all Questions that belong to a given Exercise
	public static List<Question> getAllQuestionsForExercise(Long exerciseId) {
		String sql = "select question_id from exercise_question " +
				"where exercise_id = " + exerciseId;

		List<Long> questionIds = new ArrayList<Long>();
		for (SqlRow row : Ebean.createSqlQuery(sql).findList()) {
			questionIds.add(row.getLong("question_id"));
		}

		return find.where()
				.ne("retired", true)
				.in("id", questionIds)
				.orderBy("order_index")
				.findList();
	}
}