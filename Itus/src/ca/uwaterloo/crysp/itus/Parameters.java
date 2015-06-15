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

import ca.uwaterloo.crysp.itus.storage.PermanentStorage;


/**
 * Various parameters used by Itus. These are default values only.  
 * Prefabs may overwrite them by calling setParam() 
 * 
 * @author Aaron Atwater
 * @author Hassan Khan
 */
public class Parameters {
	/**
	 * Current operation mode 
	 */
	private static Mode mode = Mode.ONLINE_MODE;

	/**
	 * How often we invoke the Itus train/classify loop, in ms
	 */
	private static long itusPeriod = 5000;
	
	/**
	 * minimum number of instances required before training kicks in
	 * note: this is a minimum -- training might be invoked with more
	 * instances than this
	 */
	private static int trainingThreshold = 8;
	
	/**
	 * Used to pause/resume the Itus thread after it has been launched.
	 */
	private static State itusState = State.STOPPED;
	
	/**
	 * History size of past scores
	 */
	private static int scoreHistorySize = 10;
	
	/**
	 * The default filename for the touch log file
	 */
	private static final String touchLogFileName = "itus_touch_data_";
	
	/**
	 * Name of the model file
	 */
	private static String modelFileName = "itus_classifier_model_file";
	
	private static PermanentStorage permanentStorageInstance = null;
	/**
	 * Gets the Itus' operation mode
	 * @return the mode
	 */
	public static Mode getMode() {
		return mode;
	}

	
	/**
	 * Sets the Itus' operation mode to Oracle
	 * @param mode the mode to set
	 */
	public static void setOracleMode(PermanentStorage permanentStorage) {
		Parameters.mode = Parameters.Mode.ORACLE_MODE;
		Itus.setPermanentStorage(permanentStorage);
	} 

	/**
	 * Sets the Itus' operation mode to Config
	 * @param mode the mode to set
	 */
	public static void setConfigMode(PermanentStorage permanentStorage) {
		Parameters.mode = Parameters.Mode.CONFIG_MODE;
		Itus.setPermanentStorage(permanentStorage);
	}
	
	/**
	 * Sets the Itus' operation mode to Online
	 * @param mode the mode to set
	 */
	public static void setOnlineMode() {
		Parameters.mode = Parameters.Mode.ONLINE_MODE;
	} 
	/**
	 * Gets the Itus' period/How often it is invoked in ms
	 * @return the itusPeriod
	 */
	public static long getItusPeriod() {
		return itusPeriod;
	}

	/**
	 * Sets the Itus' period/How often it is invoked in ms
	 * @param itusPeriod the itusPeriod to set
	 */
	public static void setItusPeriod(long itusPeriod) {
		Parameters.itusPeriod = itusPeriod;
	}


	/**
	 * Gets the training threshold
	 * @return the trainingThreshold
	 */
	public static int getTrainingThreshold() {
		return trainingThreshold;
	}

	/**
	 * Sets the training threshold
	 * @param trainingThreshold the trainingThreshold to set
	 */
	public static void setTrainingThreshold(int trainingThreshold) {
		Parameters.trainingThreshold = trainingThreshold;
	}


	/**
	 * Gets the execution state of Itus
	 * @return the itusState
	 */
	public static synchronized State getItusState() {
		return itusState;
	}

	/**
	 * Sets the execution state of Itus
	 * @param itusState the itusState to set
	 */
	public static synchronized void setItusState(State itusState) {
		Parameters.itusState = itusState;
	}


	/**
	 * Returns the scoreHistorySize
	 * @return the scoreHistorySize
	 */
	public static int getScoreHistorySize() {
		return scoreHistorySize;
	}

	/**
	 * Sets the score history size
	 * @param scoreHistorySize the scoreHistorySize to set
	 */
	public static void setScoreHistorySize(int scoreHistorySize) {
		Parameters.scoreHistorySize = scoreHistorySize;
	}


	/**
	 * Returns the filename for the touch log file
	 * @return Returns the filename for the touch log file
	 */
	public static String getTouchlogfilename() {
		return touchLogFileName;
	}
	
	/**
	 * Returns the model file name
	 * @return Name of the model file
	 */
	public static String getModelFileName() {
		return modelFileName;
	}

	/**
	 * Set the file name of model for permanent storage
	 * @param modelFileName name of the model file
	 */
	public static void setModelFileName(String modelFileName) {
		Parameters.modelFileName = modelFileName;
	}


	/**
	 * Returns the permanentStorageInstance
	 * @return the permanentStorageInstance
	 */
	public static PermanentStorage getPermanentStorageInstance() {
		return permanentStorageInstance;
	}


	/**
	 * Sets the permanent storage instance
	 * @param permanentStorageInstance the permanentStorageInstance to set
	 */
	public static void setPermanentStorageInstance(
			PermanentStorage permanentStorageInstance) {
		Parameters.permanentStorageInstance = permanentStorageInstance;
	}


	/**
	 * Typesafe enum pattern for supported operation modes
	 * 
	 * @author Aaron Atwater
	 * @author Hassan Khan
	 */
	public static class Mode {
		/**
		 * String label for Mode
		 */
		private final String name;
		
		private Mode(String name) {
			this.name = name;
		}
		
		/**
		 * Gets the string value for this mode
		 * @return Returns the name of the event type
		 */
		public String toString() {
			return this.name;
		}
		
		/**
		 * Represents the online/normal Itus operation mode
		 */
		public static final Mode ONLINE_MODE = new Mode("ONLINE_MODE");
		/**
		 * Represents the configuration mode
		 */
		public static final Mode CONFIG_MODE = new Mode("CONFIG_MODE");
		/**
		 * Represents the oracle mode 
		 */
		public static final Mode ORACLE_MODE = new Mode("ORACLE_MODE");
	}
	
	/**
	 * Typesafe enum pattern for itus thread state
	 * 
	 * @author Aaron Atwater
	 * @author Hassan Khan
	 */
	public static class State {
		/**
		 * String label for state
		 */
		private final String name;
		
		private State(String name) {
			this.name = name;
		}
		
		/**
		 * Gets the string value for this state
		 * @return Returns the name of the state type
		 */
		public String toString() {
			return this.name;
		}
		
		/**
		 * Represents the stopped state of Itus 
		 */
		public static final State STOPPED = new State("STOPPED_STATE");
		
		/**
		 * Represents the in-execution state of Itus  
		 */
		public static final State RUNNING = new State("RUNNING_STATE");
	}
}
