package beans.math;

import java.util.*;

public class TestBean{
	private Long testId = -1L;
	private List<MathBean> mathBeans = new ArrayList<MathBean>();
	
	public List<MathBean> getMathBeans() {
		return mathBeans;
	}
	public Long  getTestId(){
		return testId;
	}
	public void setTestId(Long testId){
		this.testId = testId;
	}

}
	