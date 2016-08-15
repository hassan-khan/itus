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
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

/**
 * A wrapper over SVMlib (assumes two classes only)
 * 
 * @author Aaron Atwater
 * @author Hassan Khan
 */
public class SVMClassifier extends Classifier {
	
	/**
	 * Parameter to the SVM
	 */
	svm_parameter parameter;
	/**
	 * Learned SVM model
	 */
	public svm_model model; /*XXX*/ // = (svm_model)DataStorage.getModel(this);
	/**
	 * Number of Features for the classifier
	 */
	private int numFeatures;
	public final double threshold = 0.5;
	
	/**
	 * A constructor that sets most parameter values of the classifier
	 *  to their default value. Training samples to be provided separately
	 * @param numFeatures Number of features 
	 * @throws IllegalArgumentException
	 */
	public SVMClassifier(int numFeatures) 
			throws IllegalArgumentException {
		if (numFeatures < 1)
			throw new IllegalArgumentException("numFeatures must be greater " +
					"than zero");
		this.numFeatures = numFeatures;
		setState(ClassifierState.NOT_TRAINED);
		this.parameter = new svm_parameter();
		setSVMParameter(
			1,
			0.5,
			0.5,
			1,
			svm_parameter.C_SVC,
			svm_parameter.RBF,
			20000,
			0.001
		);
	}
	
	/**
	 * 
	 * @param probability SVM param -- see SVMlib doc for details
	 * @param gamma SVM param -- see SVMlib doc for details
	 * @param nu SVM param -- see SVMlib doc for details
	 * @param C SVM param -- see SVMlib doc for details
	 * @param svmType SVM param -- see SVMlib doc for details
	 * @param kernelType SVM param -- see SVMlib doc for details
	 * @param cacheSize SVM param -- see SVMlib doc for details
	 * @param eps SVM param -- see SVMlib doc for details
	 */
	public void setSVMParameter(int probability, double gamma, 
			double nu, double C, int svmType, int kernelType,
			double cacheSize, double eps) {
		this.parameter.probability = probability;
		this.parameter.gamma = gamma;
		this.parameter.nu = nu;
		this.parameter.C = C;
		this.parameter.svm_type = svmType;
		this.parameter.kernel_type = kernelType;       
		this.parameter.cache_size = cacheSize;
		this.parameter.eps = eps;      
	}
	

	@Override
	public boolean train(List<FeatureVector> data) 
			throws IllegalArgumentException{
		if (data == null || data.size() < 1)
			throw new IllegalArgumentException("Invalid data");
				
		svm_problem problem = new svm_problem();
		
	    problem.l = data.size();
	    problem.y = new double[data.size()];
	    problem.x = new svm_node[data.size()][];     
	    
	    for (int i = 0; i < data.size(); i++){
	        problem.x[i] = new svm_node[this.numFeatures];
	        for (int j = 0; j < this.numFeatures; j++){
	            svm_node node = new svm_node();
	            node.index = j;
	            node.value = data.get(i).get(j, 0);
	            problem.x[i][j] = node;
	        }           
	        problem.y[i] = (double) data.get(i).getIntClassLabel();
	    }               
	    this.model = svm.svm_train(problem, this.parameter);
	    if (this.model == null) 
	    	throw new IllegalArgumentException("Malformed data. "
	    			+ "Failed to train");
	    //XXX
	    //DataStorage.setModel(this, model);
	    //svm.svm_save_model(model_file_name,model);
	    setState(ClassifierState.TRAINED);
	    return true;
	}

	@Override
	public int classify(FeatureVector fv) throws IllegalStateException {
		if (this.model == null || getState() == ClassifierState.NOT_TRAINED)
			throw new IllegalArgumentException("Ivalid state of classifier");
		
	    svm_node[] nodes = new svm_node[this.numFeatures];
	    for (int i = 0; i < this.numFeatures; i++) {
	        svm_node node = new svm_node();
	        node.index = i;
	        node.value = fv.get(i, 0);
	        nodes[i] = node;
	    }

	    int totalClasses = 2;
	    int[] labels = new int[totalClasses];
	    svm.svm_get_labels(this.model, labels);

	    double[] prob_estimates = new double[totalClasses];
	    svm.svm_predict_probability(this.model, nodes, prob_estimates);
	    
	    if (prob_estimates[0] >= prob_estimates[1])
	    	return 1;
	    else
	    	return -1;
	}
	
	@Override
	public Object getModel() {
		return this.model;
	}
}