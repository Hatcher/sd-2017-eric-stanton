package controllers.math;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import beans.math.MathBean;
import models.math.MathQuestion;
import models.math.Test;
import beans.math.TestBean;
import play.mvc.Controller;
import play.mvc.Result;
import services.categories.Categorizer;
import services.equation.EquationGenerator;


public class Application extends Controller {

	private EquationGenerator equationGenerator = new EquationGenerator();
	
    public Result getIndex() {
    	TestBean test = new TestBean();
    	test.getMathBeans().addAll(equationGenerator.generateQuestions(30, getQuestionTypes()));
    	return ok( views.html.math.index.render( test ) );
    }

    public Result collectSkills() {
    	return ok( views.html.math.skills.render() );
    }
    
    private List<String> getQuestionTypes(){
    	List<String> enabledSkills = new ArrayList<String>();
    	final Set<Map.Entry<String,String[]>> entries = request().queryString().entrySet();
    	for (Map.Entry<String,String[]> entry : entries) {
            final String key = entry.getKey();
            final String value = Arrays.toString(entry.getValue());
            if (value.equals("[1]")){
            	enabledSkills.add(key);
            }
        }
    	return enabledSkills;
    }

    
    public Result demoGetIndex() {
    	TestBean test = new TestBean();
    	test.getMathBeans().addAll(equationGenerator.generateQuestions(30, getQuestionTypes()));
    	return ok( views.html.math.demo.index.render( test ) );
    }
    
    public Result demoGetLogin() {
    	return ok( views.html.math.demo.login.render() );
    }
    public Result demoGetMenu() {
    	return ok( views.html.math.demo.menu.render() );
    }
    
    public Result demoGetProgress() {
    	return ok( views.html.math.demo.progress.render() );
    }

    public Result demoCollectSkills() {
    	return ok( views.html.math.demo.skills.render() );
    }
    
    public Result demoEditSkills() {
    	return ok( views.html.math.demo.adminskills.edit.render() );
    }
    
    public Result demoGetGrade() {
    	TestBean test = new TestBean();
    	test.getMathBeans().addAll(equationGenerator.generateQuestions(30, getQuestionTypes()));
    	return ok( views.html.math.demo.evaluate.render( test ) );
    }
    

}
