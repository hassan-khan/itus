package ca.uwaterloo.crysp.itus.machinelearning;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import ca.uwaterloo.crysp.itus.FeatureVector;
import ca.uwaterloo.crysp.itus.machinelearning.KNNClassifier.ComputedDistance;

public class KNNClassifierTest {
	private KNNClassifier knnClassifier;
	int size, k, numFeatures;
	double [] fv;
	ClassLabel label;
	ArrayList<FeatureVector> data;
	double [] positiveFV = {1.0,1.0,1.0};
	double [] negativeFV = {10.0,10.0,10.0};
	
	@Before
	public void setUp(){
		size = 6;
		k = 3;
		numFeatures = 3;
		knnClassifier = new KNNClassifier(k, numFeatures);
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
		if (!knnClassifier.train(data))
			fail("Train failed");
	}

	@Test
	public void testClassify() {
		double [] positiveSample = {1.0,2.0,3.0};
		double [] negativeSample = {7.0,8.0,9.0};
		double [] malformedSample = {7.0,8.0,9.0,10.0};
		
		FeatureVector positive = new FeatureVector(positiveSample, 
				ClassLabel.UNKNOWN);
		FeatureVector negative = new FeatureVector(negativeSample, 
				ClassLabel.UNKNOWN);
		FeatureVector malformed = new FeatureVector(malformedSample, 
				ClassLabel.UNKNOWN);
		
		try {
			assertEquals("Test of NOT_CONFIGURED state of classifier failed", 
				knnClassifier.classify(null), 0);
			fail("Failed to catch Illegal state exception");
		} catch(Exception e) {
			
		}
		
		if (!knnClassifier.train(data))
			fail("KnnClassifier.Classify failed in train");

		assertEquals("Test of malformed FeatureVector for classify failed", 
				knnClassifier.classify(malformed), 0);

		assertEquals("Test of known positive sample failed", 
				knnClassifier.classify(positive), 1);

		assertEquals("Test of known negative sample failed", 
				knnClassifier.classify(negative), -1);
	}

	@Test
	public void testGetK() {
		assertEquals("KnnClassifier.getK failed",
				knnClassifier.getK(), k);
	}

	@Test
	public void testSetK() {
		int newK = 5;
		knnClassifier.setK(newK);
		assertEquals("KnnClassifier.setK failed",
				newK, knnClassifier.getK());
	}

	@Test
	public void testGetNumFeatures() {
		assertEquals("KnnClassifier.getNumFeatures failed",
				knnClassifier.getNumFeatures(),numFeatures);
	}

	@Test
	public void testSetNumFeatures() {
		if (!knnClassifier.train(data))
			fail("Train failed");
		assertEquals("KnnClassifier.setNumFeatures fails for valid args",
				knnClassifier.setNumFeatures(3), true);
		
		try {
			knnClassifier.setNumFeatures(2);
			fail("KnnClassifier.setNumFeatures fails for invalid args");
		} catch (Exception e) {
			
		}

		
	}

	@Test
	public void testGetMajorityLabel() {
		ArrayList<ComputedDistance> sortedDistance = 
				new ArrayList<ComputedDistance>();
		for (int i = 0; i < 4; i++) {
			ComputedDistance cd = new ComputedDistance(1, 5);
			sortedDistance.add(cd);
		}
		for (int i = 0; i < 2; i++) {
			ComputedDistance cd = new ComputedDistance(-1, 5);
			sortedDistance.add(cd);
		}
		int label = knnClassifier.getMajorityLabel(sortedDistance);
		assertEquals("knnClassifier.getSortedDistance failed", label, 1);
		
		for (int i = 0; i < 3; i++) {
			ComputedDistance cd = new ComputedDistance(-1, 5);
			sortedDistance.add(cd);
		}
		label = knnClassifier.getMajorityLabel(sortedDistance);
		assertEquals("knnClassifier.getSortedDistance failed", label, -1);
	}
	
	@Test
	public void testSortDistances() {
		ArrayList<ComputedDistance> computedDistance = 
				new ArrayList<ComputedDistance>();
		for (int i = 8; i > 0; i--) {
			ComputedDistance cd = new ComputedDistance(1, i*2.0);
			computedDistance.add(cd);
		}
		ArrayList<ComputedDistance> sortedDistance = 
				knnClassifier.sortDistances(computedDistance);
		assertEquals("Elements returned by sortDistance != k",
				sortedDistance.size(), k);
		for (int i = 0; i < sortedDistance.size(); i++) {
			assertEquals("Elements sorting failed by sortDistance",
					sortedDistance.get(i).distance, (i+1)*2.0, 0.0);
		}
			
	}
}
