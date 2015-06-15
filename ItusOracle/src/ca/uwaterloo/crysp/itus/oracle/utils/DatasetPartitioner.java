/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uwaterloo.crysp.itus.oracle.utils;

import java.util.ArrayList;

import ca.uwaterloo.crysp.itus.FeatureVector;
import ca.uwaterloo.crysp.itus.machinelearning.ClassLabel;
import ca.uwaterloo.crysp.itus.prefabs.RunConfiguration;

/**
 * Partitions the dataset across positive and negative instances
 * 
 * @author Aaron Atwater
 * @author Hassan Khan
 */
public class DatasetPartitioner {
	/**
	 * Size of the training set represented as a fraction of the dataset
	 */
	double trainingSetRatio;
	
	/**
	 * Size of the test set 
	 */
	private int testSetSize;
	
	/**
	 * Size of the training set 
	 */
	private int trainingSetSize;

	/**
	 * Number of partitions supported by this dataset
	 */
	private int numPartitions;
	
	private static ArrayList<ArrayList<FeatureVector>> FVs;
	private static DatasetPartitioner partioner = null;
	private static ArrayList<FeatureVector> trainingInstances;
	private static ArrayList<FeatureVector> testingInstances;
	
	private DatasetPartitioner(double _trainingSetRatio, 
			ArrayList<ArrayList<FeatureVector>> _FVs) {
		trainingSetRatio = _trainingSetRatio;
		FVs = _FVs;
		numPartitions = FVs.size();
		int min = Integer.MAX_VALUE;
		for (int i = 0; i< FVs.size(); i++) 
			if (FVs.get(i).size() < min)
				min = FVs.get(i).size();
		
		trainingSetSize = (int) Math.round(min * trainingSetRatio);
		testSetSize = min - trainingSetSize;
		//Equal number of positive & negative instances
		trainingSetSize *= 2; 
		testSetSize *= 2; 
		trainingInstances = new ArrayList<FeatureVector>(trainingSetSize);
		testingInstances = new ArrayList<FeatureVector>(testSetSize);
	}

	/**
	 * Returns a datasetPartiton instance on top of {@code FVs} split with
	 * {@code trainingSetRatio}
	 * @param rc the {@code RunConfiguration} object
	 * @param FVs an {@code ArrayList<ArrayList<FeatureVector>>} containing 
	 * Feature Vectors
	 * @return the dataset partitioner configured to the parameters of this 
	 * functions
	 */
	public static DatasetPartitioner getDatasetPartitioner(RunConfiguration rc, 
			ArrayList<ArrayList<FeatureVector>> FVs) {
		partioner = new DatasetPartitioner(rc.getTrainingSetRatio(), FVs);
		return partioner;
	}
	
	/**
	 * Returns a datasetPartiton instance on top of {@code FVs} split with
	 * {@code trainingSetRatio}
	 * @param trainingSetRatio the ratio in which to divide the training and
	 * test instances
	 * @param FVs an {@code ArrayList<ArrayList<FeatureVector>>} containing 
	 * Feature Vectors
	 * @return the dataset partitioner configured to the parameters of this 
	 * functions
	 */
	public static DatasetPartitioner getDatasetPartitioner(
			double trainingSetRatio, ArrayList<ArrayList<FeatureVector>> FVs) {
		partioner = new DatasetPartitioner(trainingSetRatio, FVs);
		return partioner;
	}
	
	/**
	 * Returns the number of partitions associated with this classifier
	 * @return the number of partitions 
	 */
	public int getNumPartitions() {
		return numPartitions;
	}

	/**
	 * Returns the training samples for the partitionIndex
	 * @param partitionIndex The parition index against which the training 
	 * samples should be returned
	 * @return an {@code ArrayList} containing the training samples
	 * @throws IllegalArgumentException
	 */
	public ArrayList<FeatureVector> getTrainingSamples(int partitionIndex) 
			throws IllegalArgumentException{
		
		if (partitionIndex > numPartitions)
			throw new IllegalArgumentException("Partition Index out of bounds");
		trainingInstances.clear();
		int i = 0;
		for (; i < trainingSetSize/2; i++) {
			trainingInstances.add(FVs.get(partitionIndex).get(i));
			trainingInstances.get(i).setClassLabel(ClassLabel.POSITIVE);
		}
		int k = i; 
		while (i <= trainingSetSize) {
			for (int j = 0; j < numPartitions; j++) {
				if (j == partitionIndex)
					continue;
				trainingInstances.add(FVs.get(j).get(k));
				trainingInstances.get(i).setClassLabel(ClassLabel.NEGATIVE);
				i++;
			}
			k++;
		}
		return trainingInstances;
	}
	
	/**
	 * Returns the testing samples for the partitionIndex
	 * @param partitionIndex The parition index against which the testing 
	 * samples should be returned
	 * @return an {@code ArrayList} containing the testing samples
	 * @throws IllegalArgumentException
	 */
	public ArrayList<FeatureVector> getTestingSamples(int partitionIndex) 
				throws IllegalArgumentException {
		
		if (partitionIndex > numPartitions)
			throw new IllegalArgumentException("Partition Index out of bounds");
		testingInstances.clear();
		int i = trainingSetSize/2; //remaining samples are to be tested
		int count = 0;
		for (; i < FVs.get(partitionIndex).size(); i++) {
			testingInstances.add(FVs.get(partitionIndex).get(i));
			//add label for stats collection
			testingInstances.get(count).setClassLabel(ClassLabel.POSITIVE);
			count++;
		}
		//find remaining samples' index and ceil to avoid overlap
		int k = trainingSetSize / (numPartitions-1);
		k += 1;
		while (testingInstances.size() <= testSetSize) {
			for (int j = 0; j < numPartitions; j++) {
				if (j == partitionIndex)
					continue;
				testingInstances.add(FVs.get(j).get(k));
				testingInstances.get(count).setClassLabel(ClassLabel.NEGATIVE);
				count++;
			}
			k++;
		}
		return testingInstances;
	}
	
	/**
	 * Returns the training set size for the current partitioner
	 * @return the training set size 
	 */	
	public int getTrainingSetSize() {
		return trainingSetSize;
	}
	
	/**
	 * Returns the test set size for the current partitioner
	 * @return the test set size 
	 */
	public int getTestSetSize() {
		return testSetSize;
	}
}
