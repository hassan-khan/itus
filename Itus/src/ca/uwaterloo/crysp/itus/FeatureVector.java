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
package ca.uwaterloo.crysp.itus;

import java.util.Arrays;

import ca.uwaterloo.crysp.itus.machinelearning.ClassLabel;

/**
 * FeatureVector is consumed by the {@code Classifier}
 * 
 * @author Aaron Atwater
 * @author Hassan Khan
 */

@SuppressWarnings("serial")
public class FeatureVector implements java.io.Serializable {
	/**
	 * An array to store the features.
	 * and 1 for positive)
	 */
	protected double[] features;
	
	/**
	 * Label of the class i.e. positive instance or negative instance
	 * 
	 */
	private ClassLabel classLabel;
	
	/**
	 * Copy constructor for FeatureVector class
	 * @param _fv Uses this FeatureVector to generate a copy constructor
	 */
	public FeatureVector(FeatureVector _fv) {
		this.features = new double[_fv.features.length];
		for(int i = 0; i <_fv.features.length; i++)
			set(i, _fv.get(i));
		this.classLabel = _fv.classLabel;
	}

	/**
	 * Constructs FeatureVector with a capacity of numFeatures features
	 * @param numFeatures Capacity of the FeatureVector including class label
	 */
	public FeatureVector(int numFeatures) throws IllegalArgumentException {
		if (numFeatures < 1)
			throw new IllegalArgumentException("numFeatures cannot be <= 0");
		this.features = new double[numFeatures]; 
	}
	
	
	/**
	 * Creates a FeatureVector using the feature values in {@code vals} & 
	 * {@code classlabel} in '_classlabel'
	 * @param vals double array containing the feature score and class at index 0
	 * @param _classLabel class Label of the FeatureVector
	 */
	public FeatureVector(double [] vals, ClassLabel _classLabel) {
		this.features = new double[vals.length];
		for (int i = 0; i < vals.length; i++)
			this.set(i, vals[i]);
		this.classLabel = _classLabel;
	}

	/**
	 * Returns size of the FeatureVector
	 * @return size of the FeatureVector
	 */
	public int size() {
		return this.features.length;
	}

	/**
	 * Returns class label of this FeatureVector
	 * @return Class Label of this FeatureVector
	 */
	public ClassLabel getClassLabel() {
		return classLabel;
	}
	
	/**
	 * Returns Integer value of class label of this FeatureVector
	 * @return Integer value of Class Label of this FeatureVector
	 */
	public int getIntClassLabel() {
		return classLabel.getClassLabel();
	}
	/**
	 * Sets class label of this FeatureVector
	 * @param classLabel class label to set
	 */
	public void setClassLabel(ClassLabel classLabel) {
		this.classLabel = classLabel;
	}
	
	/**
	 * Sets the value at a particular index of the FeatureVector
	 * @param index Index of the FeatureVector to modify
	 * @param value New value to set at the index
	 */
	public void set(int index, double value) 
			throws ArrayIndexOutOfBoundsException {
		if (index < 0 || index >= features.length) 
			throw new ArrayIndexOutOfBoundsException(String.valueOf(index) +
					" out of bounds for FeatureVector with size "+ this.size());
		features[index] = value;
	}
	
	/**
	 * Get the value of the feature at a specific index.
	 * 
	 * @param index index of the attribute to return
	 * @return the value of the feature at <tt>index</tt>
	 */
	public double get(int index) 
			throws ArrayIndexOutOfBoundsException {
		if (index < 0 || index >= features.length) 
			throw new ArrayIndexOutOfBoundsException(String.valueOf(index) +
					" out of bounds for FeatureVector with size "+ this.size());
		return features[index];
	}
	
	/**
	 * Gets the feature score at the index 'index'; if no value exists, returns
	 * the default '_default'
	 * @param index index of the FeatureVector to get the feature score from
	 * @param _default default value to return if sparse array index is not
	 * populated
	 * @return returns the feature score if index exists or default otherwise
	 */
	public double get(int index, double _default) {
		if (index < 0 || index >= features.length) 
			return _default; 
		return features[index];
	}
	/**
	 * Get the feature values as an array
	 * 
	 * @return a double array containing a *copy* of all feature values
	 */
	public double[] getAll() {
		return Arrays.copyOf(features, features.length);
	}
	
	/**
	 * Reset all feature values to zero.
	 */
	public void clear() {
		for (int i=0; i<features.length; i++)
			features[i] = 0;
	}
}
