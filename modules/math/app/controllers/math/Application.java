package controllers.math;

import java.math.BigInteger;
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

    public Result generateTest() {
    	
        
    	
    	
    	TestBean test = new TestBean();
    	test.getMathBeans().addAll(equationGenerator.generateQuestions(30, getQuestionTypes()));
    	return ok( views.html.math.generatetest.render( test ) );
    }
    
    public Result collectSkills() {
    	return ok( views.html.math.skills.render() );
    }
    
    public Result chooseSkills() {
    	return ok( views.html.math.skillselection.render() );
    }
    
    public static Result generateRectangle() {
    	MathBean bean = new MathBean();
    	bean.getIntegers().add(BigInteger.valueOf(2));
    	bean.getIntegers().add(BigInteger.valueOf(3));
    	bean.getTypes().add("RECTANGLE_AREA");
    	
    	return ok( views.html.math.question.templates.rectangle.render( bean ) );
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
    
}
