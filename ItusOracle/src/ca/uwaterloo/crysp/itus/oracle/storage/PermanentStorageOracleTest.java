package ca.uwaterloo.crysp.itus.oracle.storage;

import static org.junit.Assert.*;

import java.util.ArrayList;

import libsvm.svm_model;

import org.junit.Test;

import ca.uwaterloo.crysp.itus.FeatureVector;
import ca.uwaterloo.crysp.itus.machinelearning.ClassLabel;
import ca.uwaterloo.crysp.itus.machinelearning.KNNClassifier;
import ca.uwaterloo.crysp.itus.machinelearning.SVMClassifier;

public class PermanentStorageOracleTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testknn() {
		KNNClassifier knnClassifier;
		int size, k, numFeatures;
		ArrayList<FeatureVector> data;
		double [] positiveFV = {1.0,1.0,1.0};
		double [] negativeFV = {10.0,10.0,10.0};
		size = 6;
		k = 3;
		numFeatures = 3;
		knnClassifier = new KNNClassifier(k, numFeatures);
		data = new ArrayList<FeatureVector>();
		for (int i = 0 ; i < size/2; i++) {
			FeatureVector featureVector = new FeatureVector(positiveFV, 
					ClassLabel.POSITIVE);
			data.add(featureVector);
			
		}
		for (int i = size/2 ; i < size; i++) { 
			FeatureVector featureVector = new FeatureVector(negativeFV, 
					ClassLabel.NEGATIVE);
			data.add(featureVector);
		}
		
		if (!knnClassifier.train(data))
			fail("Train failed");
		PermanentStorageOracle storageHandle = new PermanentStorageOracle();
		try {
			if (!storageHandle.saveModel("knn_model", knnClassifier.model.data))
				fail("Failed to saveModel");
		} catch (Exception e) {
			fail("Failed to saveModel");
		} 
		
		try {
			knnClassifier.model.data = (ArrayList<FeatureVector>)
					storageHandle.retrieveModel("knn_model");
		} catch (Exception e) {
			fail("Failed to retreive model");
		}
		
		if (knnClassifier.model.data == null 
				|| knnClassifier.model.data.size() != 6)
			fail("Retrieved null object/ malformed kNN data from storage");
		
	}
	/*
	@Test
	public void testKNNModelStorage() {
		KNNClassifier knnClassifier;
		int size, k, numFeatures;
		double [] fv;
		ClassLabel label;
		ArrayList<FeatureVector> data;
		double [] positiveFV = {1.0,1.0,1.0};
		double [] negativeFV = {10.0,10.0,10.0};
		size = 6;
		k = 3;
		numFeatures = 3;
		knnClassifier = new KNNClassifier(k, numFeatures);
		data = new ArrayList<FeatureVector>();
		for (int i = 0 ; i < size/2; i++) {
			FeatureVector featureVector = new FeatureVector(positiveFV, 
					ClassLabel.POSITIVE);
			data.add(featureVector);
			
		}
		for (int i = size/2 ; i < size; i++) { 
			FeatureVector featureVector = new FeatureVector(negativeFV, 
					ClassLabel.NEGATIVE);
			data.add(featureVector);
		}
		
		if (!knnClassifier.train(data))
			fail("Train failed");
		
		PermanentStorageOracle storageHandle = new PermanentStorageOracle();
		storageHandle.saveModel(knnClassifier, "knn_model", knnClassifier.data);
		
		svmClassifier.model = (svm_model)
				storageHandle.retrieveModel(svmClassifier, "svm_model");
	}
*/
	@Test
	public void testSVMModelStorage() {
		/*Test SVM Model permanent storage and Retrieval*/
		SVMClassifier svmClassifier;
		int size, numFeatures;
		ArrayList<FeatureVector> data;
		double [] positiveFV = {1.0,1.0,1.0};
		double [] negativeFV = {10.0,10.0,10.0};
		size = 6;
		numFeatures = 3;
		svmClassifier = new SVMClassifier(numFeatures);
		data = new ArrayList<FeatureVector>();
		for (int i = 0 ; i < size/2; i++) {
			FeatureVector featureVector = new FeatureVector(positiveFV, 
					ClassLabel.POSITIVE);
			data.add(featureVector);
			
		}
		for (int i = size/2 ; i < size; i++) { 
			FeatureVector featureVector = new FeatureVector(negativeFV, 
					ClassLabel.NEGATIVE);
			data.add(featureVector);
		}
		
		svmClassifier.train(data);
		
		PermanentStorageOracle storageHandle = new PermanentStorageOracle();
		try {
			if (!storageHandle.saveModel("svm_model", svmClassifier.model))
				fail("Failed to saveModel");
		} catch (Exception e) {
			fail("Failed to saveModel");
		} 
		
		try {
			svmClassifier.model = (svm_model)
					storageHandle.retrieveModel("svm_model");
		} catch (Exception e) {
			fail("Failed to retrieveModel");
		} 
		
		if (svmClassifier.model == null)
			fail("Retrieved null object from storage");
		
		double [] positiveSample = {1.0,2.0,3.0};
		double [] negativeSample = {10.0,9.0,9.0};

		FeatureVector positive = new FeatureVector(positiveSample, 
				ClassLabel.UNKNOWN);
		FeatureVector negative = new FeatureVector(negativeSample, 
				ClassLabel.UNKNOWN);

		assertEquals("Test of known positive sample failed", 
				svmClassifier.classify(positive), 1);

		assertEquals("Test of known negative sample failed", 
				svmClassifier.classify(negative), -1);
	}

}
