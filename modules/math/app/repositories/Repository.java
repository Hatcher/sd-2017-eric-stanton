package repositories;

import com.avaje.ebean.Model;

public abstract class Repository {

	public void save(Model model){
		model.save();
	}

	public abstract Model find(long id);
	
	
}
