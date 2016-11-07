package models.passages;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import play.data.validation.Constraints.Required;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;

import models.passages.Passage;

@Entity
public class Sentence extends Model {
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
	@ManyToOne
	Passage passage;

	@Required
	public int orderIndex;

	@Required
	@Column(columnDefinition = "TEXT")
	public String text;

	
	/********************************
	 CONSTRUCTORS
	 ********************************/
	
	public Sentence(Passage passage, int index, String text) {
		this.passage = passage;
		this.text = text;
		this.orderIndex = index;
	}
	
	
	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, Sentence> find = new Finder<Long, Sentence>(Sentence.class);


	/********************************
	 CREATE / DELETE 
	 ********************************/
	public static Sentence create(Sentence sentence) {
		sentence.save();
		return sentence;
	}

	public static void delete(Long id) {
		Sentence sentence = find.ref(id);
		if (sentence == null) {
			return;
		}

		sentence.retired = true;
		sentence.save();
	}
		
	
	/********************************
	 GETTERS 
	 ********************************/

	//-----------Single-------------//

	//Get Sentence by ID
	public static Sentence byId(Long id) {
		return find.where()
				.ne("retired", true)
				.eq("id", id)
				.findUnique();
	}

	// Get Sentence by text
	public static Sentence byText(String text) {
		return find.where()
				.ne("retired", true)
				.eq("text", text)
				.findUnique();
	}
	
	//-----------Group-------------//

	//Get all Sentences in the system 
	public static List<Sentence> getAll() {
		return find.where()
				.ne("retired", true)
				.findList();
	}

//	//Get all Sentences in a certain Passage
//	public static List<Sentence> getAllSentencesForPassage(Long passageId) {
//		return find.where()
//				.ne("retired", true)
//				.eq("passage.id", passageId)
//				.findList();
//	}
}