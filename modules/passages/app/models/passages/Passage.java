package models.passages;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import com.avaje.ebean.PagedList;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import edu.stanford.nlp.util.StringUtils;
import generators.passages.FITBGenerator;
import org.apache.commons.lang3.text.WordUtils;
import org.jsoup.Jsoup;
import play.data.validation.Constraints.Required;

import javax.persistence.*;
import java.io.*;
import java.sql.Timestamp;
import java.util.*;

import forms.passages.PassageForm;

import models.common.Tag;
import models.common.User;
import models.passages.Sentence;

@Entity
public class Passage extends Model {
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
	public String title;

	@Required
	@Column(columnDefinition = "TEXT")
	public String text;

	@Required
	public String source;

	@Required
	public Long length;

	public Long level;

	public String textType;

	@ManyToOne
	public User uploader;

	@Required
	public Boolean isGlobal;

//	@Required
//	@OneToMany
//	public List<Sentence> sentences = new ArrayList<Sentence>();

	@ManyToMany (cascade = CascadeType.ALL)
	public Set<Tag> tags = new HashSet<Tag>();


	/********************************
	 CONSTRUCTORS
	 ********************************/
	public Passage(String title, String source, Long level, String textType, User uploader, Boolean isGlobal) {
		this.title = title;
		this.source = source;
		this.level = level;
		this.textType = textType;
		this.uploader = uploader;
		this.isGlobal = isGlobal;
	}

	public Passage(PassageForm passageForm) {
		this.uploader = User.byId(passageForm.uploaderId);
		this.title = passageForm.title;
		this.source = passageForm.source;
		this.level = passageForm.level;
		this.textType = passageForm.type;
		this.isGlobal = passageForm.isGlobal;

		// add tags:
		String[] passageTags = passageForm.tags.split(",");

		for (String t : passageTags) {
			if (t.trim().length() <= 0) {
				continue;
			}

			Tag tag = Tag.byName(t);

			// If a tag by this name does not exist, create a new one and save it
			if (tag == null) {
				tag = new Tag(t);
				Tag.create(tag);
			}

			this.tags.add(tag);
		}
	}

	// Create a new Passage by saving it to the database and creating each of its sentences
	public static void addNewPassage(Passage newPassage, String text) {
		newPassage.text = text;
		newPassage.length = (long) text.split(" ").length;
		Passage.create(newPassage);

		FITBGenerator.pickPlaceholderBlanksForPassage(newPassage);
	}


	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, Passage> find = new Finder<Long, Passage>(Passage.class);


	/********************************
	 CREATE / DELETE 
	 ********************************/
	public static Passage create(Passage passage) {
		passage.save();
		return passage;
	}

	public static void delete(Long id) {
		Passage passage = find.ref(id);
		if (passage == null) {
			return;
		}

		passage.retired = true;
		passage.save();
	}


	/********************************
	 GETTERS 
	 ********************************/

	//-----------Single-------------//

	//Get Passage by ID
	public static Passage byId(Long id) {
		return find.where()
				.ne("retired", true)
				.eq("id", id)
				.findUnique();
	}

	//-----------Group-------------//

	//Get all Passages in the system 
	public static List<Passage> getAll() {
		return find.where()
				.ne("retired", true)
				.findList();
	}


	//Get all Passages uploaded by a specific Instructor
	public static List<Passage> getAllForInstructor(Long instructorId) {
		return find.where()
				.ne("retired", true)
				.eq("uploader", User.byId(instructorId))
				.findList();
	}


	//Get all Passages in the system marked as public
	public static PagedList<Passage> getAllPublic(int page) {
		return find.where()
				.ne("retired", true)
				.eq("isGlobal", true)
				.findPagedList(page, 10);
	}


	public static List<String> getWordsInPassage(Passage passage) {
		String parsedText = Jsoup.parse(passage.text).text();

		List<String> words = new ArrayList<String>();

		StringTokenizer st = new StringTokenizer(parsedText, " .,?!:;()<>[]\b\t\n\f\r\"\'\\\"");
		while (st.hasMoreTokens()) {
			words.add(st.nextToken());
		}
		return words;
	}



	/////////////////////////////////////////////////////
	// TESTING
	////////////////////////////////////////////////////

	public static void uploadPassages() throws IOException {
		final File folder = new File("/home/jen/Desktop/FITB/passages/readworks_text_cleaned_ASCII/");
		listFilesForFolder(folder);
	}

	public static void listFilesForFolder(final File folder) throws IOException {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				try (BufferedReader br = new BufferedReader(new FileReader(fileEntry))) {
					// read the contents of the file into a string
					StringBuilder sb = new StringBuilder();
					String line = br.readLine();

					while (line != null) {
						sb.append(line);
						sb.append(System.lineSeparator());
						line = br.readLine();
					}
					String passageText = sb.toString();

					//get a random instructor in the system and set them as the passage's uploader
					Random rand = new Random();
					List<User> allInstructors = User.getAllByRole("Instructor");
					allInstructors.addAll(User.getAllByRole("Administrator"));
					int randomInstructor = rand.nextInt((allInstructors.size() - 1) + 1) + 1;

					//clean up title to remove underscores and extension
					String title = WordUtils.capitalize(fileEntry.getName().split("_")[1].replace("-", " ").split(".txt")[0]);

					// add passage to the system
					Passage passage = new Passage(title, "ReadWorks", (long) rand.nextInt(12), "Informational", User.byId((long) randomInstructor), rand.nextBoolean());
					Passage.addNewPassage(passage, passageText);
				}
			}
		}
	}
}