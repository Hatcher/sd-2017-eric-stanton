package models.passages;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import org.jsoup.Jsoup;
import play.data.validation.Constraints.Required;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;

import models.passages.Sentence;

@Entity
public class Blank extends Model {
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
	public Passage passage;

	@Required
	public int startIndex;

	@Required
	public int endIndex;


	/********************************
	 CONSTRUCTORS
	 ********************************/

	public Blank(Passage passage, int startIndex, int endIndex) {
		this.passage = passage;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}


	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, Blank> find = new Finder<Long, Blank>(Blank.class);


	/********************************
	 CREATE / DELETE 
	 ********************************/
	public static Blank create(Blank blank) {
		blank.save();
		return blank;
	}

	public static void delete(Long id) {
		Blank blank = find.ref(id);
		if (blank == null) {
			return;
		}

		blank.retired = true;
		blank.save();
	}


	/********************************
	 GETTERS 
	 ********************************/

	//-----------Single-------------//

	//Get Blank by ID
	public static Blank byId(Long id) {
		return find.where()
				.ne("retired", true)
				.eq("id", id)
				.findUnique();
	}

	//-----------Group-------------//

	//Get all Blanks in the system 
	public static List<Blank> getAll() {
		return find.where()
				.ne("retired", true)
				.findList();
	}


	//Get all Blanks in the system
	public static List<Blank> getAllBlanksForPassage(Long passageId) {
		return find.where()
				.ne("retired", true)
				.eq("passage.id", passageId)
				.findList();
	}


	public static String getTextForBlank(Blank blank) {
		List<String> words = Passage.getWordsInPassage(blank.passage);
		int start = blank.startIndex;
		int end = blank.endIndex;

		String phrase = "";
		for (int i = start; i <= end; i++) {
			phrase += words.get(i) + " ";
		}
		phrase = phrase.substring(0, phrase.length() - 1);

		return phrase;
	}
}