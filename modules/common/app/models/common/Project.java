package models.common;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
public class Project extends Model {
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
	public Project(String name) {
		this.name = name;
	}


	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Model.Finder<Long, Project> find = new Model.Finder<Long, Project>(Project.class);


	/********************************
	 CREATE / DELETE
	 ********************************/
	public static Project create(Project project) {
		project.save();
		return project;
	}

	public static void delete(Long id) {
		Project project = find.ref(id);
		if (project == null) {
			return;
		}

		project.retired = true;
		project.save();
	}

	// Get a Project by this name and create one if none exists
	public static Project getProject(String name) {
		Project project = Project.byName(name);

		if (project == null) {
			project = create(new Project(name));
		}
		return project;
	}


	/********************************
	 GETTERS
	 ********************************/

	//-----------Single-------------//

	//Get Project by ID
	public static Project byId(Long id) {
		return find.where()
				.ne("retired", true)
				.eq("id", id)
				.findUnique();
	}

	// Get Project by name
	public static Project byName(String name) {
		return find.where()
				.ne("retired", true)
				.eq("name", name)
				.findUnique();
	}

	//-----------Group-------------//

	//Get all Projects in the system
	public static List<Project> getAll() {
		return find.where()
				.ne("retired", true)
				.findList();
	}
}