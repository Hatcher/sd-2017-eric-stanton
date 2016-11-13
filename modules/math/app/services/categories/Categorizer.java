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
	List <CategoryDetector> detectors = new ArrayList<CategoryDetector>();
	
	public Categorizer(){
		populateListOfDetectors();
	}
	
	public void populateCategories(MathBean mathBean){
		for (CategoryDetector detector : detectors){
			if (detector.isCategory(mathBean)){
				mathBean.getTypes().add(detector.getClass().getSimpleName().replaceAll("Detector", ""));
			}
		}
		if ("".equals(mathBean.getChosenType())){
			mathBean.setChosenType("Arithmetic");
		}
	}
	private void populateListOfDetectors(){
		detectors.add(new AreaDetectorCircle());
		detectors.add(new AreaDetectorParallelogram());
		detectors.add(new AreaDetectorRectangle());
		detectors.add(new AreaDetectorSquare());
		detectors.add(new AreaDetectorTrapezoid());
		detectors.add(new AreaDetectorTriangle());
		detectors.add(new DistanceDetector());
		detectors.add(new InterestDetector());
		detectors.add(new MeanDetector());
		detectors.add(new MedianDetector());
		detectors.add(new PerimeterDetectorCircle());
		detectors.add(new PerimeterDetectorRectangle());
		detectors.add(new PerimeterDetectorSquare());
		detectors.add(new PerimeterDetectorTriangle());
		detectors.add(new PointSlopeDetector());
		detectors.add(new PythagoreanTheoremDetector());
		detectors.add(new QuandraticDetectorStandardForm());
		detectors.add(new SlopeInterceptDetector());
		detectors.add(new SlopeOfLineDetector());
		detectors.add(new SurfaceAreaDetectorCone());
		detectors.add(new SurfaceAreaDetectorCylinder());
		detectors.add(new SurfaceAreaDetectorPyramid());
		detectors.add(new SurfaceAreaDetectorRectangularPrism());
		detectors.add(new SurfaceAreaDetectorRightPrism());
		detectors.add(new SurfaceAreaDetectorSphere());
		detectors.add(new TotalCostDetector());
		detectors.add(new VolumeDetectorCone());
		detectors.add(new VolumeDetectorCylinder());
		detectors.add(new VolumeDetectorPyramid());
		detectors.add(new VolumeDetectorRectangularPrism());
		detectors.add(new VolumeDetectorRightPrism());
		detectors.add(new VolumeDetectorSphere());
	}
}
