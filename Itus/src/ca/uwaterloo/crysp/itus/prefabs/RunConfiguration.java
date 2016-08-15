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
package ca.uwaterloo.crysp.itus.prefabs;


import libsvm.svm_parameter;
import ca.uwaterloo.crysp.itus.machinelearning.Classifier;
import ca.uwaterloo.crysp.itus.machinelearning.KNNClassifier;
import ca.uwaterloo.crysp.itus.machinelearning.SVMClassifier;
import ca.uwaterloo.crysp.itus.measurements.Measurement;

/**
 * A definition of run configuration for the oracle. 
 * @author Aaron Atwater
 * @author Hassan Khan
 */
public class RunConfiguration {
	private Measurement measurement;
	private Classifier classifier;
	private int numFeatures;
	private double trainingTestRatio;
	private int k;
	//private svm_model svmModel;
	//private svm_parameter svmParameter;
	

	/**
	 * Returns a run configuration with given parameters for KNNClassifier
	 * @param k
	 * @param numFeatures
	 * @param trainingSetRatio
	 * @param measurement
	 * @return RunConfiguration for KNNClassifier
	 */
	public static RunConfiguration getKNNRunConfiguration(int k, 
			int numFeatures, double trainingSetRatio,Measurement measurement) {

		KNNClassifier knnClassifier = new KNNClassifier(k, numFeatures);
		RunConfiguration rc = new RunConfiguration(k, numFeatures, 
				trainingSetRatio, knnClassifier, measurement);
		
		return rc;
	}
	
	/**
	 * Returns an SVM classifier with configuration parameters set to params
	 * @param numFeatures
	 * @param trainingSetRatio
	 * @param measurement
	 * @return Returns an SVM classifier based run configuration
	 */
	public static RunConfiguration getSVMRunConfiguration(int numFeatures, 
			double trainingSetRatio, Measurement measurement) {
		SVMClassifier svmClassifier = new SVMClassifier(numFeatures);
		RunConfiguration rc = new RunConfiguration(numFeatures, 
				trainingSetRatio, svmClassifier, measurement);
		return rc;
	}
	
	/**
	 * Returns an SVM classifier with configuration parameters set to params
	 * @param svmParameter
	 * @param numFeatures
	 * @param trainingSetRatio
	 * @return Returns an SVM classifier based run configuration
	 */
	public static RunConfiguration getSVMRunConfiguration(
			svm_parameter svmParameter, int numFeatures, double trainingSetRatio, 
			Measurement measurement) {
		SVMClassifier svmClassifier = new SVMClassifier(numFeatures);
		if (svmParameter != null)
			svmClassifier.setSVMParameter(svmParameter.probability, 
					svmParameter.gamma, svmParameter.nu, svmParameter.C, 
					svmParameter.svm_type, svmParameter.kernel_type, 
					svmParameter.cache_size, svmParameter.eps);
			
		RunConfiguration rc = new RunConfiguration(numFeatures, 
				trainingSetRatio, svmClassifier, measurement);
		
		return rc;
	}
	private RunConfiguration (int numFeatures, double trainingSetRatio,
			SVMClassifier classifier, Measurement measurement) {
		this.classifier = classifier;
		init(numFeatures, trainingSetRatio, measurement);
	}
	
	private RunConfiguration(int k, 
			int numFeatures, double trainingSetRatio, 
			KNNClassifier classifier, Measurement measurement) {
		this.setK(k);
		this.classifier = classifier;
		init(numFeatures, trainingSetRatio, measurement);
	}
	
	private void init(int numFeatures, double trainingSetRatio, 
			Measurement measurement) {
		this.numFeatures = numFeatures;
		this.trainingTestRatio = trainingSetRatio;
		this.measurement = measurement;
	}
		
	/**
	 * @return the classifier
	 */
	public Classifier getClassifier() {
		return classifier;
	}
	/**
	 * @param classifier the classifier to set
	 */
	public void setClassifier(Classifier classifier) {
		this.classifier = classifier;
	}
	/**
	 * @return the numFeatures
	 */
	public int getNumFeatures() {
		return numFeatures;
	}
	/**
	 * @param numFeatures the numFeatures to set
	 */
	public void setNumFeatures(int numFeatures) {
		this.numFeatures = numFeatures;
	}
	/**
	 * @return the trainingSetRatio
	 */
	public double getTrainingSetRatio() {
		return trainingTestRatio;
	}
	/**
	 * @param trainingSetRatio the trainingSetRatio to set
	 */
	public void setTrainingSetRatio(float trainingSetRatio) {
		this.trainingTestRatio = trainingSetRatio;
	}
	
	/**
	 * @return the k
	 */
	public int getK() {
		return k;
	}

	/**
	 * @param k the k to set
	 */
	public void setK(int k) {
		this.k = k;
	}

	/**
	 * @return the measurement
	 */
	public Measurement getMeasurement() {
		return measurement;
	}

	/**
	 * @param measurement the measurement to set
	 */
	public void setMeasurement(Measurement measurement) {
		this.measurement = measurement;
	}
	
	
}
