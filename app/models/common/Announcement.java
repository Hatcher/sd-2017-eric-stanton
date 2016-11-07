package models.common;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import models.Course;
import play.data.validation.Constraints.Required;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;

@Entity
public class Announcement extends Model {
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
	public User creator;

	@ManyToOne
	public Prompt prompt;

	@ManyToMany (cascade = CascadeType.ALL)
	public Set<Content> content = new HashSet<Content>();

	
	/********************************
	 CONSTRUCTORS
	 ********************************/
	public Announcement(User creator, Prompt prompt) {
		this.creator = creator;
		this.prompt = prompt;
	}

	public Announcement(User creator, String text) {
		Prompt prompt = Prompt.byText(text);
		if (prompt == null) {
			prompt = Prompt.create(new Prompt(text));
		}
		this.creator = creator;
		this.prompt = prompt;
	}


	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, Announcement> find = new Finder<Long, Announcement>(Announcement.class);


	/********************************
	 CREATE / DELETE
	 ********************************/
	public static Announcement create(Announcement message) {
		message.save();
		return message;
	}

	public static void delete(Long id) {
		Announcement message = find.ref(id);
		if (message == null) {
			return;
		}

		message.retired = true;
		message.save();
	}


	public String getCreatedTimeString() {
		return this.createdTime.toString();
	}

	public boolean isCreator(User user) {
		return this.creator.id == user.id;
	}


	/********************************
	 GETTERS
	 ********************************/

	//-----------Single-------------//

	//Get Announcement by ID
	public static Announcement byId(Long id) {
		return find.where()
				.ne("retired", true)
				.eq("id", id)
				.findUnique();
	}


	//-----------Group-------------//

	//Get all Announcements in the system
	public static List<Announcement> getAll() {
		return find.where()
				.ne("retired", true)
				.findList();
	}


	// Get all Announcements in a given Course
	public static List<Announcement> getAllAnnouncementsForCourse(Long courseId) {
		String sql = "select * from announcement " +
					 "where id in " +
						"(select announcement_id from course_announcement " +
						"where course_id=" +courseId + ")" +
					"order by created_time desc;";

		List<Announcement> allAnnouncements = new ArrayList<>();
		for (SqlRow row : Ebean.createSqlQuery(sql).findList()) {
			if (!row.getBoolean("retired")) {
				allAnnouncements.add(Announcement.byId(row.getLong("id")));
			}
		}

		return allAnnouncements;
	}




	/* ******************************** PERMISSIONS ******************************* */
	/* **************************************************************************** */

	// edit announcement
	public boolean canDeleteAnnouncement(User user) {
		return this.isCreator(user) || Course.getCourseForAnnouncement(this.id).institution.isAdmin(user);
	}
}