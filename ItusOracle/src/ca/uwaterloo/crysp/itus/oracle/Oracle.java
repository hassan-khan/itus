package ca.uwaterloo.crysp.itus.oracle;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ca.uwaterloo.crysp.itus.FeatureVector;
import ca.uwaterloo.crysp.itus.Itus;
import ca.uwaterloo.crysp.itus.Parameters;
import ca.uwaterloo.crysp.itus.machinelearning.ClassLabel;
import ca.uwaterloo.crysp.itus.machinelearning.Classifier;
import ca.uwaterloo.crysp.itus.machinelearning.KNNClassifier;
import ca.uwaterloo.crysp.itus.measurements.EventType;
import ca.uwaterloo.crysp.itus.measurements.Measurement;
import ca.uwaterloo.crysp.itus.measurements.TouchEvent;
import ca.uwaterloo.crysp.itus.oracle.measurements.TouchEventOffline;
import ca.uwaterloo.crysp.itus.oracle.storage.PermanentStorageOracle;
import ca.uwaterloo.crysp.itus.oracle.utils.ConfigReader;
import ca.uwaterloo.crysp.itus.oracle.utils.DatasetPartitioner;
import ca.uwaterloo.crysp.itus.oracle.utils.FeatureVectorParser;
import ca.uwaterloo.crysp.itus.oracle.utils.FileIO;
import ca.uwaterloo.crysp.itus.prefabs.RunConfiguration;
import ca.uwaterloo.crysp.itus.prefabs.Touchalytics;
import ca.uwaterloo.crysp.itus.storage.BinLabel;
import ca.uwaterloo.crysp.itus.storage.DataStorage;

public class Oracle extends Itus {
	private String datasetPath = "./res/";
	private int minDataSources = 4;
	private String touchPrefix = "itus_touch_data_";
	private int minSwipes = 20;
	private String  keystrokePrefix = "itus_keystroke_data_";
	private int minKeystrokes = 50;
	private double trainTestRatio = 0.5;
	private ArrayList<String> touchFiles, keystrokeFiles;
	ArrayList<ArrayList<FeatureVector>> touchFVs;
	ArrayList<ArrayList<FeatureVector>> keystrokeFVs;
	public static void main(String [] args) {
		Oracle oracle = new Oracle();
		//oracle.evaluateTouchClassifier();
	}
	
	private void loadDataset() {
		File path = new File(datasetPath);
		File [] fileList = path.listFiles();
		System.out.println("Scanning for dataset at: " + 
				path.getAbsolutePath());
		
		if (fileList == null)
			return;
		
		for (File f : fileList) {
			if (f.isDirectory())
				continue;
			try {
				if (f.getName().startsWith(touchPrefix)) 
					if (FileIO.getNumLines(f) >= this.minSwipes)
						touchFiles.add(f.getName());
				if (f.getName().startsWith(keystrokePrefix)) 
					if (FileIO.getNumLines(f) >= this.minKeystrokes)
						keystrokeFiles.add(f.getName());
			} catch (Exception e) {
				System.out.println("Failed to read dataset from: " + 
						datasetPath);
				return;
			}
		}
		if (touchFiles.size() >=  minDataSources) {
			System.out.println("Found " + String.valueOf(touchFiles.size()) +
					" touch datasources");
			touchFVs = readDataset(touchFiles, TouchEvent.NUM_FEATURES);
			if (touchFVs.size() > 1)
				evaluateTouchClassifier();
		}
		if (keystrokeFiles.size() >=  minDataSources) {
			//evaluateKeystrokeClassifier();
			System.out.println("Found " + String.valueOf(keystrokeFiles.size()) +
					" keystroke datasources");
		}
		if (touchFiles.size() == 0 && keystrokeFiles.size() == 0) {
			System.out.println("No data found in " + datasetPath + 
					" to evalaute");
		}
	}
	
	private void loadConfiguration() {
		//read configurations, if not successful, continue with default values
		HashMap<String, String> config = null;
		touchFiles = new ArrayList<String>();
		keystrokeFiles = new ArrayList<String>();
		try {
			config = ConfigReader.readConfig();
			for (String key : config.keySet()) {
				if (key.equals("dataset_path"))
					datasetPath = config.get(key);
				else if (key.equals("min_data_sources"))
					minDataSources = Integer.parseInt(config.get(key));
				else if (key.equals("touch_prefix"))
					touchPrefix = config.get(key);
				else if (key.equals("keystroke_prefix"))
					keystrokePrefix = config.get(key);
				else if (key.equals("min_swipes"))
					minSwipes = Integer.parseInt(config.get(key));
				else if (key.equals("min_keystrokes"))
					minKeystrokes = Integer.parseInt(config.get(key));
				else if (key.equals("train_test_ratio"))
					trainTestRatio = Double.parseDouble(config.get(key));
				else
					System.out.println("Unsupported config parameter: " + 
							config.get(key));
			}
		} catch (Exception e) {
			System.out.println("Error configuring... using default values");
		}
		System.out.println("Configuration completed!!!");
	}
	
	
	private ArrayList<ArrayList<FeatureVector>>  readDataset(
			ArrayList<String> filenames, int maxNumFeatures) {
		ArrayList<ArrayList<FeatureVector>> FVs = 
				new ArrayList<ArrayList<FeatureVector>>();
		try {
			for (int i = 0; i < filenames.size(); i++) {
				ArrayList<String> strFVs = FileIO.readFile(datasetPath + 
						filenames.get(i));
				ArrayList<FeatureVector> FV = new ArrayList<FeatureVector>();
				for (String strFV : strFVs) {
					FeatureVector fv = new FeatureVector(maxNumFeatures);
					FeatureVectorParser.parseFeatureVectorFromString(fv, strFV);
					FV.add(fv);
				}
				FVs.add(FV);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return FVs;
		}
		return FVs;
	}
	
	private void evaluateTouchClassifier() {
		TouchEvent touchEventOffline = new TouchEventOffline();
		RunConfiguration touchalyticsConfig = 
				Touchalytics.getRecommendedConfiguration(touchEventOffline);
		useMeasurement(touchalyticsConfig.getMeasurement());
		useClassifier(touchalyticsConfig.getClassifier());
		
		ArrayList<ArrayList<FeatureVector>> FVs = readDataset(touchFiles, 
				touchEventOffline.NUM_FEATURES);
		DatasetPartitioner partitioner = 
				DatasetPartitioner.getDatasetPartitioner(
				touchalyticsConfig.getTrainingSetRatio(), FVs);
		//partition to test
		final int partitionIndex = 0;
		
		ArrayList <FeatureVector> trainingInstances = 
				partitioner.getTrainingSamples(partitionIndex);
		ArrayList <FeatureVector> testingInstances = 
				partitioner.getTestingSamples(partitionIndex);
		Parameters.setScoreHistorySize(testingInstances.size());
		DataStorage.setMaxRecent(testingInstances.size());
		Parameters.setTrainingThreshold(trainingInstances.size());
		Parameters.setItusPeriod(0);
		
		this.start();
		for (int i = 0; i < Parameters.getTrainingThreshold(); i++){
			Itus.getItus().getDispatcher().procEvent(EventType.TOUCH_INPUT, 
					trainingInstances.get(i));
		}

		for (int i = 0; i < testingInstances.size(); i++) {
			Itus.getItus().getDispatcher().procEvent(EventType.TOUCH_INPUT, 
					testingInstances.get(i));
		}
		
		ArrayList<Integer> scores = new ArrayList<Integer>();
		while (scores.size() < testingInstances.size()) {
			scores.addAll(getPastScores());
			try {sleep(Parameters.getItusPeriod()+500);}
			catch (Exception e) {e.printStackTrace();}
		}
		Parameters.setItusState(Parameters.State.STOPPED);
		
		int TP = 0, FP = 0, TN = 0, FN = 0;
		for (int i = 0; i < testingInstances.size(); i++) {
			if (testingInstances.get(i).getClassLabel() == 
					ClassLabel.POSITIVE) {
				if (scores.get(i) == 1)
					TP++;
				else
					FN++;
			}
			if (testingInstances.get(i).getClassLabel() == 
					ClassLabel.NEGATIVE) {
				if (scores.get(i) == -1)
					TN++;
				else
					FP++;
			}
		}
		System.out.println("TP: " + TP + "; FP: " + FP + "; TN: " + TN + 
				"; FN: " + FN);
		System.out.print("Stopping simulation");
	}
	
	public Oracle() {
		super();
		Parameters.setOracleMode(new PermanentStorageOracle());
		loadConfiguration();
		loadDataset();
	}
}
