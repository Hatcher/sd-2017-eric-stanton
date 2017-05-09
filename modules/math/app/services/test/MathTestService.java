package services.test;

import java.io.IOException;
import java.util.Map;

import com.avaje.ebean.annotation.Transactional;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import beans.math.MathBean;
import beans.math.TestBean;
import models.math.MathTest;

public class MathTestService {

	// saves to database and returns the test ID
	@Transactional
	public Long saveTest(TestBean temporaryTest) {
		MathTest persistedTest = new MathTest();
		persistedTest.testJson = toJson(temporaryTest);
		persistedTest = MathTest.create(persistedTest);
		return persistedTest.testId;
	}
	
	public TestBean getTest(Long testId){
		MathTest mathTest = MathTest.find(testId);
		TestBean testBean = fromJson(mathTest.testJson);
		testBean.setTestId(mathTest.testId); // testId not stored in json for database because it does not exist on create
		return testBean;
	}
	

	
	private TestBean fromJson(String testJson){
		ObjectMapper mapper = new ObjectMapper();
		TestBean testBean;
		try {
			testBean = mapper.readValue(testJson, TestBean.class);
		} catch (IOException e) {
			e.printStackTrace();
			return new TestBean();
		}	
		return testBean;
	}
	
	private String toJson(TestBean test){
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(test);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "";
		}
	}

	public void populateSelectedAnswers(TestBean test, Map<String, String> selectedAnswers) {
		for (MathBean mathBean : test.getMathBeans()){
			mathBean.setSelectedAnswer(selectedAnswers.get("Q_"+mathBean.getId()));
		}
	}


}
