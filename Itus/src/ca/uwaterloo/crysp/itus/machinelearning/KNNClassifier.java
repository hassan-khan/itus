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

package ca.uwaterloo.crysp.itus.machinelearning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.uwaterloo.crysp.itus.FeatureVector;

/**
 * An implementation of kNN Classifier (Assumes two classes only)
 * 
 * @author Aaron Atwater
 * @author Hassan Khan
 */
public class KNNClassifier extends Classifier {

	/**
	 * Model for the KNN Classifier
	 * 
	 * @author Aaron Atwater
	 * @author Hassan Khan
	 */
	@SuppressWarnings("serial")
	public class KNNModel implements java.io.Serializable {
		/**
		 * A reference to training and testing instances
		 */
		public List<FeatureVector> data;
		/**
		 * Number of nearest neighbors 'k'
		 */
		public int k;
		
		/**
		 * Number of Features for the classifier
		 */
		public int numFeatures;
		
		/**
		 * constructor for KNNModel
		 * @param k Number of nearest neighbors 'k'
		 * @param numFeatures Number of Features for the classifier
		 */
		public KNNModel(int k, int numFeatures){
			this.k = k;
			this.numFeatures = numFeatures;
		}
		
		/**
		 * Copy constructor for KNNModel
		 * @param k Number of nearest neighbors 'k'
		 * @param numFeatures Number of Features for the classifier
		 */
		public KNNModel(Object objModel){
			KNNModel model = (KNNModel) objModel;
			this.k = model.k;
			this.numFeatures = model.numFeatures;
			this.data = model.data;
		}
	}
	
	/**
	 * Stores the distance  for K Nearest Neighbors
	 * 
	 * @author Aaron Atwater
	 * @author Hassan Khan
	 */
	public static class ComputedDistance {
		/**
		 * Label of the class
		 */
		int label;
		
		/**
		 * Distance from the sample that is being evaluated
		 */
		double distance;
		/**
		 * Constructor for ComputedDistance
		 * @param label
		 * @param distance
		 */
		public ComputedDistance(int label, double distance) {
			this.label = label;
			this.distance = distance;
		}
	}
	public KNNModel model = null;
	
	/**
	 * constructor for KNNClassifier
	 * @param k Number of nearest neighbors 'k'
	 * @param numFeatures Number of Features for the classifier
	 * @throws IllegalArgumentException
	 */
	public KNNClassifier(int k, int numFeatures) 
			throws IllegalArgumentException {
		if(k < 1 || numFeatures < 1)
			throw new IllegalArgumentException("Both 'k' and 'numFeatures' "+
					"must be greater than or equal to 1");
		model = new KNNModel(k, numFeatures);
		setState(ClassifierState.NOT_TRAINED);
	}
	
	/**
	 * constructor for KNNClassifier
	 * @param objModel Number of nearest neighbors 'k'
	 */
	public KNNClassifier(Object objModel) {
		model = new KNNModel(objModel);
		setState(ClassifierState.TRAINED);
	}
	
	
	@Override
	public boolean train(List<FeatureVector> data) 
			throws IllegalArgumentException{
		if (data == null || data.size() < 1) 
			throw new IllegalArgumentException("Provided data is invalid");
		model.data = data;
		setState(ClassifierState.TRAINED);
		return true;
	}

	@Override
	public int classify(FeatureVector featureVector) 
			throws IllegalStateException{
		/*Sanity checking*/
		if (getState() == ClassifierState.NOT_TRAINED)
			throw new IllegalStateException("Classifier state not trained");

		if (featureVector.size() != model.data.get(0).size()) {
			System.out.println("FeatureVector size mismatch in Classify");
			return 0;
		}
		List<ComputedDistance> computedDistance = 
				new ArrayList<ComputedDistance>();
		
		
		for (FeatureVector fv : model.data) {
			double eucDistance = 0;
			for (int i = 0; i < model.numFeatures; i++)
				eucDistance += Math.pow(fv.get(i)- featureVector.get(i), 2);
			eucDistance = Math.sqrt(eucDistance);
			computedDistance.add(new ComputedDistance(fv.getIntClassLabel(), 
					eucDistance));
		}
		
		List<ComputedDistance> sortedDistance = sortDistances(computedDistance);
		return getMajorityLabel(sortedDistance);
	}
	
	@Override
	public Object getModel() {
		return this.model;
	}
	
	/**
	 * 
	 * @param computedDistance A {@code List} containing computedDistances
	 * @return A {@code} List containing sorted distances
	 */
	public ArrayList<ComputedDistance> sortDistances(List<ComputedDistance> 
	computedDistance) {
		ArrayList<ComputedDistance> sortedDistance = 
				new ArrayList<ComputedDistance>();
		for (int i = 0; i < model.k; i++) {
			double minDistance = Double.MAX_VALUE;
			int minIndex = -1;
			for (int j = 0; j < computedDistance.size(); j++)
				if (minDistance > computedDistance.get(j).distance) {
					minIndex = j;
					minDistance = computedDistance.get(j).distance;
				}
			if (minIndex == -1)
				break;
			sortedDistance.add(new ComputedDistance(
					computedDistance.get(minIndex).label, 
					computedDistance.get(minIndex).distance));
			computedDistance.remove(minIndex);
		}
		return sortedDistance;
	}
	/**
	 * 
	 * @param sortedDistance A k-sized {@code ArrayList} of sortedDistances
	 * 				supported class labels are -1 or 1 only
	 * @return ClassLabel of the majority class; 0 if error
	 */
	public int getMajorityLabel(List<ComputedDistance> sortedDistance ) {
		if (sortedDistance.size() == 0 )
			return 0;
		Map <Integer, Integer> majoritySum = new HashMap<Integer, Integer>();
		for (ComputedDistance cd : sortedDistance) {
			if (majoritySum.containsKey(cd.label))
				majoritySum.put(cd.label, majoritySum.get(cd.label)+1);
			else
				majoritySum.put(cd.label, 1);
		}
		int majorityClassLabel = -1;
		int maxOccurences = -1;
		for (int classLabel : majoritySum.keySet()) {
			if (majoritySum.get(classLabel) > maxOccurences) {//XXX: break ties
				majorityClassLabel = classLabel;
				maxOccurences = majoritySum.get(classLabel);
			}
		}
		return majorityClassLabel;
	}
	/**
	 * @return the k
	 */
	public int getK() {
		return model.k;
	}

	/**
	 * @param k the k to set
	 * @throws IllegalArgumentException
	 */
	public void setK(int k) throws IllegalArgumentException {
		if (k < 1)
			throw new IllegalArgumentException("k must be greater than zero");
		model.k = k;
	}

	/**
	 * @return the numFeatures
	 */
	public int getNumFeatures() {
		return model.numFeatures;
	}

	/**
	 * @param numFeatures the numFeatures to set
	 * @return true if successful, false otherwise
	 * @throws IllegalArgumentException
	 */
	public boolean setNumFeatures(int numFeatures) 
			throws IllegalArgumentException {
		if (model.data.size() > 0)
			if (model.data.get(0).size() <= numFeatures)
				throw new IllegalArgumentException("restriction: "
						+ "numFeatures > 0 and existing dataset should "
						+ " contain <= numFeatures features"); 
		model.numFeatures = numFeatures;
		return true;
	}
	
	
}
