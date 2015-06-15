package ca.uwaterloo.crysp.itus.machinelearning;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import ca.uwaterloo.crysp.itus.FeatureVector;

public class SVMClassifierTest {
	private SVMClassifier svmClassifier;
	int size, numFeatures;
	double [] fv;
	ClassLabel label;
	ArrayList<FeatureVector> data;
	double [] positiveFV = {1.0,1.0,1.0};
	double [] negativeFV = {10.0,10.0,10.0};
	
	@Before
	public void setUp(){
		size = 6;
		numFeatures = 3;
		svmClassifier = new SVMClassifier(numFeatures);
		data = new ArrayList<FeatureVector>();
		for (int i = 0 ; i < size/2; i++) {
			FeatureVector fv = new FeatureVector(positiveFV, 
					ClassLabel.POSITIVE);
			data.add(fv);
			
		}
		for (int i = size/2 ; i < size; i++) { 
			FeatureVector fv = new FeatureVector(negativeFV, 
					ClassLabel.NEGATIVE);
			data.add(fv);
		}
	}
	
	@Test
	public void testTrain() {
		try {
			svmClassifier.train(null);
			fail("SVM Train with null data fail");
		} catch (Exception e) {
			
		}
		
		assertEquals("SVM Train with valid data fail",
				svmClassifier.train(data), true);

	}
	
	@Test
	public void testClassify() {
		double [] positiveSample = {1.0,2.0,3.0};
		double [] negativeSample = {10.0,9.0,9.0};

		FeatureVector positive = new FeatureVector(positiveSample, 
				ClassLabel.UNKNOWN);
		FeatureVector negative = new FeatureVector(negativeSample, 
				ClassLabel.UNKNOWN);
		try {
			svmClassifier.classify(positive);
			fail("Test of NOT_CONFIGURED state of classifier failed");
		} catch (Exception e) {
			
		}
	
		if(!svmClassifier.train(data))
			fail("SVM train failed");
		assertEquals("Test of known positive sample failed", 
				svmClassifier.classify(positive), 1);

		assertEquals("Test of known negative sample failed", 
				svmClassifier.classify(negative), -1);
	
	}
}
