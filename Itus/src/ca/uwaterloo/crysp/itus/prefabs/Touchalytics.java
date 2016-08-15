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

import java.util.Arrays;

import ca.uwaterloo.crysp.itus.Itus;
import ca.uwaterloo.crysp.itus.Parameters;
import ca.uwaterloo.crysp.itus.machinelearning.Classifier;
import ca.uwaterloo.crysp.itus.machinelearning.KNNClassifier;
import ca.uwaterloo.crysp.itus.measurements.TouchEvent;

/**
 * An implementation of: 
 * Frank, Michael, Ralf Biedert, En-Di Ma, Ivan Martinovic, and Dong Song. 
 * "Touchalytics: On the applicability of touchscreen input as a behavioral 
 * biometric for continuous authentication." Information Forensics and 
 * Security, IEEE Transactions on 8, no. 1 (2013): 136-148.
 * 
 * @author Aaron Atwater
 * @author Hassan Khan
 *
 */
public class Touchalytics extends Itus {
	/**
	 * Number of features employed by Touchalytics
	 */
	static final int numFeatures = 29;
	static int k = 7;
	static double trainingSetRatio = 0.5;
	
	/**
	 * Default run configuration for Touchalytics
	 */
	private static RunConfiguration recommendedConfiguration;
	
	/**
	 * Default constructor for Touchalytics. 
	 * @param {@code touchEvent} is an object of a subclass 
	 * of {@code TouchEvent} 
	 */
	public Touchalytics(TouchEvent touchEvent) {
		super();
		//Touchalytics uses features defined at the index 0-29 in featureList
		touchEvent.setFeatureList(
				Arrays.copyOfRange(TouchEvent.featureList, 0, 29));
		Object model = Classifier.retreiveModelFromStorage();
		Classifier classifier = null;
		if (model == null)
			classifier = new KNNClassifier(k, numFeatures);
		else
			classifier = new KNNClassifier(model);
		
		useMeasurement(touchEvent);
		useClassifier(classifier);
		Parameters.setOnlineMode();
	}
	public static RunConfiguration getRecommendedConfiguration(
			TouchEvent touchEvent) {
		touchEvent.setFeatureList(
				Arrays.copyOfRange(TouchEvent.featureList, 0, 29));
		//Classifier  classifier = new KNNClassifier(k, numFeatures);
		recommendedConfiguration = RunConfiguration.getKNNRunConfiguration(
				k, numFeatures, trainingSetRatio, touchEvent);
		return recommendedConfiguration;
	}
}
