/*   This program is free software: you can redistribute it and/or modify
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

package ca.uwaterloo.crysp.itus.measurements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ca.uwaterloo.crysp.itus.FeatureVector;

/**
* Measurement class that can be extended by different data sources to convert
* raw measurements to FeatureVectors
* @author Aaron Atwater
* @author Hassan Khan
*
*/
public abstract class Measurement {
	/**
	 * The subset of Features supported by this measurement
	 */
	public static HashMap<Class<?>, ArrayList<Boolean>> employedFeatures = 
			new HashMap<Class<?>, ArrayList<Boolean>>();
	/**
	 * Features name-index mapping
	 */
	public static HashMap<Class<?>, HashMap<String,Integer>> featureIndexMap = 
			new HashMap<Class<?>, HashMap<String,Integer>>();

	/**
	 * FeatureVector to store the converted features
	 */
	protected FeatureVector fv;
	
	/**
	 * The default constructor
	 */
	public Measurement() {
		
	}
	
	/**
	 * Static initializer is used to create Factory objects
	 * @param eventDispatcher An instance of EventDispatcher
	 */
	public abstract void staticInitializer(Dispatcher eventDispatcher);

	/**
	 * procEvent will be used to process the events that arrive from 
	 * Measurement
	 * @param ev Data of the event object
	 * @param eventType type of the event
	 * @return returns true if FeatureVectors are ready to be consumed
	 */
	public abstract boolean procEvent(Object ev, EventType eventType);

	/**
	 * Returns a new instance of this Measurement
	 * @return a new instance of measurement
	 */
	public abstract Measurement newInstance();
	
	
	/**
	 * To set the name of the FeatureVectors. By default all the features are
	 * added to the employedFeatures and FeatureIndexMap list
	 * @param featureNames name of the features, indexed by array index
	 */
	public void setFeatureList(String[] featureNames) {
		ArrayList<Boolean> arrayList = new ArrayList<Boolean>(featureNames.length);
		HashMap<String,Integer> hashMap = new HashMap<String,Integer>();
		/*clear in case this is an update call*/
		employedFeatures.clear();
		featureIndexMap.clear();
		
		for (int i=0; i<featureNames.length; i++) {
			arrayList.add(true);
			hashMap.put(featureNames[i], i);
		}
		employedFeatures.put(getClass(), arrayList);
		featureIndexMap.put(getClass(), hashMap);
	}
	
	
	/**
	 * Returns true if  feature with featureName is to be used for 
	 * classification
	 * @param featureName name of the feature
	 * @return true if the feature is to be used for classification
	 */
	public boolean usesFeature(String featureName) {
		if (featureName.equals("class")) 
			return true;
		
		if (!employedFeatures.containsKey(getClass())) 
			return true;
		
		int idx = featureIndexMap.get(getClass()).get(featureName);
		
		if (employedFeatures.get(getClass()).size() <= idx) 
			return true;
		
		return employedFeatures.get(getClass()).get( 
				featureIndexMap.get(getClass()).get(featureName) );
	}

	/**
	 * Returns true if  feature at feature index is to be used for 
	 * classification
	 * @param featureIndex index of the feature
	 * @return true if the feature is to be used for classification
	 */
	public boolean usesFeature(int featureIndex) {
		if (featureIndex == fv.getIntClassLabel()) 
			return true;
		
		if (!employedFeatures.containsKey(getClass())) 
			return true;
		
		if (employedFeatures.get(getClass()).size() <= featureIndex) 
			return true;
		
		return employedFeatures.get(getClass()).get(featureIndex);
	}
	
	/**
	 * return all the FeatureVectors that have been processed so far
	 * @return all the FeatureVectors that have been processed so far
	 */
	public FeatureVector getCompleteFeatureVector() {
		return fv;
	}
	
	/**
	 * Gets the FeatureVector that uses {@code employedFeatures}
	 * @return the FeatureVector
	 */
	public FeatureVector getFeatureVector() {
		ArrayList<Double> features = new ArrayList<Double>();
		
		for (int i = 0; i < fv.size(); i++) 
			if (usesFeature(i)) 
				features.add(fv.get(i));
		
		double[] recentFeatureVector = new double[features.size()];
		for (int i = 0; i < recentFeatureVector.length; i++) 
			recentFeatureVector[i] = features.get(i);
		
		return new FeatureVector(recentFeatureVector, fv.getClassLabel());
	}
	
	/**
	 * Returns the default ''positive'' instances (factory instances) against 
	 * this Measurement. Positive instances are from single source and can 
	 * be used for testing or can be combined with the negativeInstances
	 * @return List of FeatureVector
	 */
	public abstract List<FeatureVector> defaultPositiveInstances();
	
	/**
	 * Returns the default negative instances (factory instances) against this
	 * Measurement
	 * @return List of FeatureVector
	 */
	public abstract List<FeatureVector> defaultNegativeInstances();
	
	/**
	 * Gets the default recommended classifier for this Measurement
	 * @return Class of the Classifier
	 */
	public abstract Class<?> getRecommendedClassifier();
	
	/**
	 * Gets the default recommended classifier for this Measurement
	 * @return Class of the Classifier
	 */
	public abstract ArrayList<Class<?>> getSupportedClassifier();
}


