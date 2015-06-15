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

import java.util.ArrayList;
import java.util.List;

import ca.uwaterloo.crysp.itus.machinelearning.Classifier;
import ca.uwaterloo.crysp.itus.measurements.Dispatcher;
import ca.uwaterloo.crysp.itus.measurements.EventType;
import ca.uwaterloo.crysp.itus.measurements.Measurement;
import ca.uwaterloo.crysp.itus.storage.BinLabel;
import ca.uwaterloo.crysp.itus.storage.DataStorage;
import ca.uwaterloo.crysp.itus.storage.PermanentStorage;

/**
 * The main Itus thread that orchestrates other components to perform Implicit
 * Authentication
 * @author Aaron Atwater
 * @author Hassan Khan
 *
 */
public class Itus extends Thread {
	/**
	 * The main Itus thread
	 */
	private static Itus itus = null;

	/**
	 * The classifier to use with this Itus instance
	 */
	private static Classifier classifier = null;
	
	private Measurement measurement;
	/**
	 * The main event dispatcher for this Itus instance
	 */
	private Dispatcher dispatcher = null;
	
	/**
	 * Stores the past scores
	 */
	private static ArrayList<Integer> scores = new ArrayList<Integer>();
	
	/**
	 * Link to the permanent storage class
	 */
	private static PermanentStorage permanentStorage;
	
	public static boolean enoughDataTrigger = false;/**TODO**/
	
	/**
	 * Default constructor. 
	 * @param ctx Takes context from the user app
	 */
	public Itus() {
		itus = this;
		dispatcher = new Dispatcher();
		scores = new ArrayList<Integer>(Parameters.getScoreHistorySize());
		
		/* DEFAULT PARAMETER VALUES
		 * These are default values only.  Prefabs may overwrite them
		 * by calling setParam() after invoking this parent constructor.*/
		Parameters.setItusPeriod(1000);
		Parameters.setTrainingThreshold(10);
		Parameters.setScoreHistorySize(10);
		if (!enoughData()) 
			DataStorage.setDefaultBin(BinLabel.BIN_TRAIN);
	}

	/**
	 * Instructs Itus to use the Measurement type 'measurement'
	 * @param measurement type to use
	 */
	public void useMeasurement(Measurement _measurement) {
		_measurement.staticInitializer(getDispatcher());
		this.measurement = _measurement;
	}
	
	/**
	 * Instructs Itus to use the Classifier type 'classifier'
	 * @param c Classifier type to use
	 */
	public void useClassifier(Classifier _classifier) {
		classifier = _classifier;
	}
	
	/**
	 * The main thread of Itus.  Once the thread is started using .start(),
	 * this loop will not exit until Itus state is changed to pause.
	 */
	public void run() {
		if (Parameters.getMode() == Parameters.Mode.CONFIG_MODE)
			return;
		Parameters.setItusState(Parameters.State.RUNNING);
		
		while (Parameters.getItusState() != Parameters.State.STOPPED) {
			
			if (enoughData()) {	//System.out.println("enough data...");
				if (!enoughDataTrigger) {
					System.out.println("Triggering training!");
					/*grab default negative instances from the measurement
					 * XXX support for other constructions and different FV size*/
					boolean state = false;
					if (Parameters.getMode() == Parameters.Mode.ONLINE_MODE) {
						List <FeatureVector> dataset = 
								DataStorage.getAll(BinLabel.BIN_TRAIN);
						dataset.addAll(measurement.defaultNegativeInstances());
						state = classifier.train(dataset);
					}
					else {
						state = classifier.train(
								DataStorage.getAll(BinLabel.BIN_TRAIN));
					}
					/*state = classifier.train(
							DataStorage.getAll(BinLabel.BIN_TRAIN));*/
					if (state == false) {
						System.out.print("Failed to train the classifier");
						return;
					}
					enoughDataTrigger = true;
				}
				//DataStorage.setDefaultBin(BinLabel.BIN_RECENT);
				FeatureVector recentSample = DataStorage.getMostRecent();
				if (recentSample != null) {
					int score = classifier.classify(recentSample);
					if (score != 0)
						updateScore(score);
					//System.out.println("classification result " + score);
				}
			}
			//else
			//	System.out.println("Not enough data");
			try {
				dispatcher.procEvent(EventType.PERIODIC_EVENT, null);
				Thread.sleep(Parameters.getItusPeriod());
			} catch (InterruptedException e) {}
		}
	}
	

	/**
	 * Checks if there is enough training data
	 * @return true if their is enough training data
	 */
	protected boolean enoughData() {
		/*System.out.println("Train bin Size: " 
				+ String.valueOf(DataStorage.binSize(BinLabel.BIN_TRAIN)) + 
				" Training Threshold: " + Parameters.getTrainingThreshold());*/
		return DataStorage.binSize(BinLabel.BIN_TRAIN) >= 
				Parameters.getTrainingThreshold();
	}
	
	/**
	 * Returns instance of this Authenticator
	 * @return this 
	 */
	public static Itus getItus() {
		return itus;
	}
	
	/**
	 * Gets the main event dispatcher for this Itus Agent
	 * 
	 * @return 
	 */
	public Dispatcher getDispatcher() {
		return dispatcher;
	}
	
	/**
	 * Returns score history for the current classifier and clears history
	 * @return score history for the current classifier
	 */
	public synchronized ArrayList<Integer> getPastScores() {
		ArrayList<Integer> pastScores = new ArrayList<Integer>(scores.size());
		for (int score : scores)
			pastScores.add(score);
		scores.clear();
		return pastScores;
	}
	
	/**
	 * Adds {@code score} to the score history
	 * @param score the new score that should be added
	 */
	private synchronized void updateScore(int score) {
		//XXX: Don't want to limit the history size for ORACLE
		scores.add(score);
	}

	/**
	 * Gets the currently registered permanent storage class
	 * @return currently registered permanent storage class
	 */
	public static PermanentStorage getPermanentStorage() {
		return permanentStorage;
	}

	/**
	 * Sets the currently registered permanent storage class
	 * @param permanentStorage the permanent storage class t register
	 */
	public static void setPermanentStorage(PermanentStorage permanentStorage) {
		Itus.permanentStorage = permanentStorage;
	}
}

