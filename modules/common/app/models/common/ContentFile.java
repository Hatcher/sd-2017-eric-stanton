package models.common;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import play.data.validation.Constraints.Required;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class ContentFile extends Model {
	
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
	public String name;
	
	@Required
	public String fileType;
	
	@Required
	public Long uploaderId;

	
	/********************************
	 CONSTRUCTORS
	 ********************************/
	
	public ContentFile(String name, String fileType, Long uploaderId) {
		this.name = name;
		this.fileType = fileType;
		this.uploaderId = uploaderId;
	}
	
	
	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, ContentFile> find = new Finder<Long, ContentFile>(ContentFile.class);


	/********************************
	 CREATE / DELETE 
	 ********************************/
	public static ContentFile create(ContentFile content_file) {
		content_file.save();
		return content_file;
	}

	public static void delete(Long id) {
		ContentFile content_file = find.ref(id);
		if (content_file == null) {
			return;
		}

		content_file.retired = true;
		content_file.save();
	}
}
