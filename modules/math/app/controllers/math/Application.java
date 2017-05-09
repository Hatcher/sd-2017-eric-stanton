package controllers.math;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import beans.math.MathBean;
import beans.math.TestBean;
import models.math.MathQuestion;
import play.mvc.Controller;
import play.mvc.Result;
import services.equation.EquationGenerator;
import services.test.MathTestService;

public class Application extends Controller {

	private EquationGenerator equationGenerator = new EquationGenerator();
	private MathTestService testService = new MathTestService();

	public Result getIndex() {
		TestBean test = new TestBean();
		test.getMathBeans().addAll(equationGenerator.generateQuestions(30, getQuestionTypes()));
		// persist to database
		testService.saveTest(test);
		return ok(views.html.math.index.render(test));
	}

	public Result collectSkills() {
		return ok(views.html.math.skills.render());
	}

	private List<String> getQuestionTypes() {
		List<String> enabledSkills = new ArrayList<String>();
		final Set<Map.Entry<String, String[]>> entries = request().queryString().entrySet();
		for (Map.Entry<String, String[]> entry : entries) {
			final String key = entry.getKey();
			final String value = Arrays.toString(entry.getValue());
			if (value.equals("[1]")) {
				enabledSkills.add(key);
			}
		}
		return enabledSkills;
	}

	public Result demoGetIndex() {
		TestBean test = new TestBean();
		test.getMathBeans().addAll(equationGenerator.generateQuestions(5, getQuestionTypes()));
		test.setTestId(testService.saveTest(test));
		return ok(views.html.math.demo.index.render(test));
	}

	public Result demoGetLogin() {
		return ok(views.html.math.demo.login.render());
	}

	public Result demoGetMenu() {
		return ok(views.html.math.demo.menu.render());
	}

	public Result demoGetProgress() {
		return ok(views.html.math.demo.progress.render());
	}

	public Result demoCollectSkills() {
		return ok(views.html.math.demo.skills.render());
	}

	public Result demoEditSkills() {
		return ok(views.html.math.demo.adminskills.edit.render());
	}

	public Result demoGetGrade() {
    	// getTestId from submitted values
    	TestBean test = testService.getTest(getTestIdFromInput());
    	testService.populateSelectedAnswers(test, getSelectedAnswers());
    	return ok( views.html.math.demo.evaluate.render( test ) );
    }

	private Long getTestIdFromInput() {
		final Map<String, String[]> entries = request().body().asFormUrlEncoded();
		String[] inputValues = entries.get("testId");
		String testIdString = inputValues[0];
		return Long.parseLong(testIdString);
	}
	private Map<String,String> getSelectedAnswers() {
		Map<String,String> selectedAnswers = new HashMap<>();
		final Map<String, String[]> entries = request().body().asFormUrlEncoded();

		for (Map.Entry<String, String[]> entry : entries.entrySet()) {
			final String key = entry.getKey();
			final String value = entry.getValue()[0];
			if (key.startsWith("Q_")) {
				selectedAnswers.put(key, value);
			}
		}
		return selectedAnswers;
	}

}
