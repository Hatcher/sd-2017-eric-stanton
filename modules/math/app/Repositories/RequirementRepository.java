package Repositories;

import com.avaje.ebean.Model.Finder;
import models.math.Requirement;

public class RequirementRepository extends Repository{
	public static Finder<Long, Requirement> find = new Finder<>(Requirement.class);

	@Override
	public Requirement find(long id) {
		return find.byId(id);
	}
	
}
