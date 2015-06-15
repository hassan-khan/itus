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

import java.util.List;

import ca.uwaterloo.crysp.itus.FeatureVector;
import ca.uwaterloo.crysp.itus.Parameters;

/**
 * Contains Interface that is to be implemented by 
 * machine learning algorithms
 * 
 * @author Aaron Atwater
 * @author Hassan Khan
 */
public abstract class Classifier {

	
	/**
	 * Classifier state
	 */
	ClassifierState state;
	
	/**
	 * Default constructor for Classifier
	 */
	public Classifier(){
		
	}
	
	/**
	 * Sets the state of the classifier
	 * @param state state to set for the classifier
	 */
	void setState(ClassifierState state) {
		this.state = state;
	}
	
	/**
	 * Returns the state of the classifier
	 * @return the current state of the classifier
	 */
	ClassifierState getState() {
		return this.state;
	}
	
	/**
	 * Trains the classifier using {@code data} 
	 * 
	 * @param data contains positive and negative training samples 
	 * @return returns true is training is successful, false otherwise
	 * @throws IllegalArgumentException
	 */
	public abstract boolean train(List<FeatureVector> data)
			throws IllegalArgumentException;
	
	/**
	 * Classifies the given {@code featureVector} 
	 * 
	 * @param featureVector An instance of {@code FeatureVector} to classify 
	 * @return returns -1 or 1 for negative or positive instance, respectively;
	 * 			0 otherwise to indicate error
	 * @throws IllegalStateException
	 */
	public abstract int classify(FeatureVector featureVector) 
			throws IllegalStateException;
	
	/**
	 * Returns the Model associated with this classifier
	 * @return model associated with this classifier as an object
	 */
	public abstract Object getModel();
	
	/**
	 * Saves the model to the permannet storage
	 * @param model the model to save
	 * @return True of successful
	 */
	public boolean saveModel(Object model){
		Object _model = this.getModel() ; 
		if (Parameters.getPermanentStorageInstance() == null ||
				_model == null)
			return false;
		
		try{
			Parameters.getPermanentStorageInstance().saveModel(
					Parameters.getModelFileName(), _model);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Retrieves the model file from storage 
	 * @return retrieved model file
	 */
	public Object retreiveModelFromStorage() {
		Object model = null;
		try{
			model = Parameters.getPermanentStorageInstance().retrieveModel(
				Parameters.getModelFileName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
}

