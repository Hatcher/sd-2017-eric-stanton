package repositories;

import com.avaje.ebean.Model.Finder;

import models.math.Test;

public class TestRepository extends Repository{
	public static Finder<Long, Test> find = new Finder<>(Test.class);

	@Override
	public Test find(long id) {
		return find.byId(id);
	}
	
}
