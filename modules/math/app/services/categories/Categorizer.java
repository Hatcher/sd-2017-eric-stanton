package services.categories;

import java.util.ArrayList;
import java.util.List;

import beans.math.MathBean;
import services.categories.detector.AreaDetectorCircle;
import services.categories.detector.AreaDetectorParallelogram;
import services.categories.detector.AreaDetectorRectangle;
import services.categories.detector.AreaDetectorSquare;
import services.categories.detector.AreaDetectorTrapezoid;
import services.categories.detector.AreaDetectorTriangle;
import services.categories.detector.ArithmeticDetector;
import services.categories.detector.ArithmeticDetectorAdd;
import services.categories.detector.ArithmeticDetectorDivide;
import services.categories.detector.ArithmeticDetectorMultiply;
import services.categories.detector.ArithmeticDetectorSubtract;
import services.categories.detector.CategoryDetector;
import services.categories.detector.DistanceDetector;
import services.categories.detector.InterestDetector;
import services.categories.detector.MeanDetector;
import services.categories.detector.MedianDetector;
import services.categories.detector.PerimeterDetectorCircle;
import services.categories.detector.PerimeterDetectorRectangle;
import services.categories.detector.PerimeterDetectorSquare;
import services.categories.detector.PerimeterDetectorTriangle;
import services.categories.detector.PointSlopeDetector;
import services.categories.detector.PythagoreanTheoremDetector;
import services.categories.detector.QuandraticDetectorStandardForm;
import services.categories.detector.SlopeInterceptDetector;
import services.categories.detector.SlopeOfLineDetector;
import services.categories.detector.SurfaceAreaDetectorCone;
import services.categories.detector.SurfaceAreaDetectorCylinder;
import services.categories.detector.SurfaceAreaDetectorPyramid;
import services.categories.detector.SurfaceAreaDetectorRectangularPrism;
import services.categories.detector.SurfaceAreaDetectorRightPrism;
import services.categories.detector.SurfaceAreaDetectorSphere;
import services.categories.detector.TotalCostDetector;
import services.categories.detector.VolumeDetectorCone;
import services.categories.detector.VolumeDetectorCylinder;
import services.categories.detector.VolumeDetectorPyramid;
import services.categories.detector.VolumeDetectorRectangularPrism;
import services.categories.detector.VolumeDetectorRightPrism;
import services.categories.detector.VolumeDetectorSphere;

public class Categorizer {
	List<CategoryDetector> detectors = new ArrayList<CategoryDetector>();

	public Categorizer(List<String> enabledSkills) {
		populateListOfDetectors(enabledSkills);
	}

	public void populateCategories(MathBean mathBean) {
		for (CategoryDetector detector : detectors) {
			if (detector.isCategory(mathBean)) {
				String type = detector.getClass().getSimpleName().replaceAll("Detector", "");
				mathBean.getTypes().add(type);
				
				if (type.toUpperCase().startsWith("ARITHMETIC")){
					mathBean.setDisplayType("ARITHMETIC");
					detector.label(mathBean);
					detector.populateQuestion(mathBean);
				}
				else if(type.toUpperCase().endsWith("TRIANGLE")){
					mathBean.setDisplayType("TRIANGLE");
					detector.label(mathBean);
					detector.populateQuestion(mathBean);
				}
				else if(type.toUpperCase().endsWith("SQUARE")){
					mathBean.setDisplayType("SQUARE");
					detector.label(mathBean);
					detector.populateQuestion(mathBean);
				}
				else if(type.toUpperCase().endsWith("RECTANGLE")){ 
					mathBean.setDisplayType("RECTANGLE");
					detector.label(mathBean);
					detector.populateQuestion(mathBean);
				}
				else if(type.toUpperCase().endsWith("CIRCLE")){ 
					mathBean.setDisplayType("CIRCLE");	
					detector.label(mathBean);
					detector.populateQuestion(mathBean);
				}
				else if(type.toUpperCase().endsWith("RECTANGULARPRISM")){ 
					mathBean.setDisplayType("CUBE");	
					detector.label(mathBean);
					detector.populateQuestion(mathBean);
				}
				
				
			}
		}
		
		
		
	}

	public List<CategoryDetector> getDetectors() {
		return detectors;
	}

	private void populateListOfDetectors(List<String> enabledSkills) {
		boolean addAllowed = enabledSkills.contains(SkillNames.ADD);
		boolean subtractAllowed = enabledSkills.contains(SkillNames.SUBTRACT);
		boolean divideAllowed = enabledSkills.contains(SkillNames.DIVIDE);
		boolean multiplyAllowed = enabledSkills.contains(SkillNames.MULTIPLY);
		List<String> supportedOperators = new ArrayList<String>();
		if (addAllowed){
			detectors.add(new ArithmeticDetectorAdd());
			supportedOperators.add("+");
		}
		if (subtractAllowed){
			detectors.add(new ArithmeticDetectorSubtract());
			supportedOperators.add("-");
		}
		if (divideAllowed){
			detectors.add(new ArithmeticDetectorDivide());
			supportedOperators.add("/");
		}
		if (multiplyAllowed){
			detectors.add(new ArithmeticDetectorMultiply());
			supportedOperators.add("*");
		}
		detectors.add(new ArithmeticDetector(supportedOperators));
		
		
		boolean circlesAllowed = enabledSkills.contains(SkillNames.CIRCLE);
		boolean rectanglesAllowed = enabledSkills.contains(SkillNames.RECTANGLE);
		boolean trianglesAllowed = enabledSkills.contains(SkillNames.TRIANGLE);

		if (circlesAllowed) {
			detectors.add(new AreaDetectorCircle());
			detectors.add(new PerimeterDetectorCircle());
		}

		if (rectanglesAllowed) {
			if (trianglesAllowed) { // TODO, consider adding skill for
				// parrallelograms
				detectors.add(new AreaDetectorParallelogram());
				detectors.add(new AreaDetectorTrapezoid());
			}
			detectors.add(new AreaDetectorRectangle());
			detectors.add(new AreaDetectorSquare());

		}

		if (trianglesAllowed) {
			// TODO figure out a better way to make questions related to this
			// (maybe better used with future concept of question series
			detectors.add(new PythagoreanTheoremDetector());
			detectors.add(new PerimeterDetectorTriangle());
			detectors.add(new AreaDetectorTriangle());
		}

		boolean conesAllowed = enabledSkills.contains(SkillNames.CONE);
		boolean cylindersAllowed = enabledSkills.contains(SkillNames.CYLINDER);
		boolean pyramidsAllowed = enabledSkills.contains(SkillNames.PYRAMID);
		boolean rectangularPrismsAllowed = enabledSkills.contains(SkillNames.RECTANGULAR_PRISM);
		boolean rightPrismsAllowed = enabledSkills.contains(SkillNames.RIGHT_PRISM);
		boolean spheresAllowed = enabledSkills.contains(SkillNames.SPHERE);
		
		
		if (conesAllowed){
			detectors.add(new SurfaceAreaDetectorCone());
			detectors.add(new VolumeDetectorCone());
		}
		if (cylindersAllowed){
			detectors.add(new SurfaceAreaDetectorCylinder());
			detectors.add(new VolumeDetectorCylinder());
		}
		if (pyramidsAllowed){
			detectors.add(new SurfaceAreaDetectorPyramid());
			detectors.add(new VolumeDetectorPyramid());
		}
		if (rectangularPrismsAllowed){
			detectors.add(new SurfaceAreaDetectorRectangularPrism());
			detectors.add(new VolumeDetectorRectangularPrism());
		}
		if (rightPrismsAllowed){
			detectors.add(new SurfaceAreaDetectorRightPrism());
			detectors.add(new VolumeDetectorRightPrism());
		}
		if (spheresAllowed){
			detectors.add(new SurfaceAreaDetectorSphere());
			detectors.add(new VolumeDetectorSphere());
		}
		
//		detectors.add(new DistanceDetector());
//
//		detectors.add(new MeanDetector());
//		detectors.add(new MedianDetector());
//		
//
//		// TODO will go with finances skill
//		detectors.add(new InterestDetector());
//		detectors.add(new TotalCostDetector());
//		
//		
//		// TODO will go with algebra skill
//		detectors.add(new QuandraticDetectorStandardForm());
//		
//		// TODO will go with graphing skill
//		detectors.add(new PointSlopeDetector());
//		detectors.add(new SlopeInterceptDetector());
//		detectors.add(new SlopeOfLineDetector());
		

	}
}
