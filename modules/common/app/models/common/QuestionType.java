package models.common;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
public class QuestionType extends Model {
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
	public String name;


	/********************************
	 CONSTRUCTORS
	 ********************************/
	public QuestionType(String name) {
		this.name = name;
	}
	

	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Model.Finder<Long, QuestionType> find = new Model.Finder<Long, QuestionType>(QuestionType.class);


	/********************************
	 CREATE / DELETE
	 ********************************/
	public static QuestionType create(QuestionType questionType) {
		questionType.save();
		return questionType;
	}

	public static void delete(Long id) {
		QuestionType questionType = find.ref(id);
		if (questionType == null) {
			return;
		}

		questionType.retired = true;
		questionType.save();
	}

	// Get the corresponding Type by name if it exists, and create it if it does not
	public static QuestionType getType(String name) {
		QuestionType questionType = QuestionType.byName(name);

//		if (questionType == null) {
//			questionType = create(new QuestionType(name));
//		}
		return questionType;
	}


	/********************************
	 GETTERS
	 ********************************/

	//-----------Single-------------//

	//Get QuestionType by ID
	public static QuestionType byId(Long id) {
		return find.where()
				.ne("retired", true)
				.eq("id", id)
				.findUnique();
	}

	//Get all QuestionTypes in the system
	public static QuestionType byName(String name) {
		return find.where()
				.ne("retired", true)
				.eq("name", name)
				.findUnique();
	}

	//-----------Group-------------//

	//Get all QuestionTypes in the system
	public static List<QuestionType> getAll() {
		return find.where()
				.ne("retired", true)
				.findList();
	}


	// Get the defined abbreviation for the given QuestionType
	public static String getTypeAbbrev(QuestionType type) {
		String name = type.name;
		if (name.equals("Fill in the Blank")) {
			return "fitb";
		} else if (name.equals("Matching")) {
			return "matching";
		} else if (name.equals("Multiple Choice")) {
			return "mc";
		} else if (name.equals("Free Response")) {
			return "fr";
		} else if (name.equals("Sentence Reorder")) {
			return "sr";
		}
		return "";
	}



//
//
//
//
	// POPULATE: start with all the question types in the database
	public static void addQuestionTypes() {
		QuestionType.create(new QuestionType("Free Response"));
		QuestionType.create(new QuestionType("Multiple Choice"));
		QuestionType.create(new QuestionType("Matching"));
		QuestionType.create(new QuestionType("Sentence Reorder"));
		QuestionType.create(new QuestionType("Fill in the Blank"));
	}
}