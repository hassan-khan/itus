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
package ca.uwaterloo.crysp.itus.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import ca.uwaterloo.crysp.itus.FeatureVector;
import ca.uwaterloo.crysp.itus.Parameters;

/**
 * 
 * Data storage class. Provides bins to store FeatureVectors and abstract
 * functions for device specific implementation of permanent storage
 * @author Aaron Atwater
 * @author Hassan Khan
 */
public class DataStorage {
	/**
	 * HashMap indexed by a String and contained ArrayList of FeatureVectors
	 * for storing of training/recent samples
	 */
	private static HashMap<BinLabel, ArrayList<FeatureVector>> data = new 
			HashMap<BinLabel, ArrayList<FeatureVector>>();
	
	
	/**
	 * Capacity of bin_recent
	 */
	private static int MAX_RECENTS = 10;
	
	/**
	 * Active bin that the incoming data is stored to
	 */
	private static BinLabel activeBin = BinLabel.BIN_RECENT;
	
	
	/**
	 * Returns Featurevector from {@code binType} at the index {@code index}
	 * @param binLabel Label of bin to get the FeatureVector from
	 * @param index		Index of the FeatureVector in the ArrayList of bin
	 * @return		FeatureVector
	 * @throws NoSuchElementException
	 */
	public static synchronized FeatureVector get(BinLabel binLabel, int index) 
			throws NoSuchElementException {
		if (!data.containsKey(binLabel)) 
			throw new NoSuchElementException(binLabel.toString() + " bin DNE");
		return data.get(binLabel).get(index);
	}

	/**
	 * Returns List with all the FeatureVectors of from binLabel 'type'
	 * @param binLabel Label of bin to get the FeatureVector from
	 * @return Returns List with all the FeatureVectors
	 * @throws NoSuchElementException
	 */
	public static List<FeatureVector> getAll(BinLabel binLabel) 
			throws NoSuchElementException{
		if (!data.containsKey(binLabel)) 
			throw new NoSuchElementException(binLabel.toString() + " bin DNE");
		return data.get(binLabel);
	}
	
	/**
	 * Adds the FeatureVector 'fv' to the BinLabel 'binLabel'
	 * @param binLabel BinLabel of bin to put the FeatureVector from
	 * @param fv FeatureVector to put in the bin
	 */
	public static synchronized void add(BinLabel binLabel, FeatureVector fv) {
		if (!data.containsKey(binLabel)) 
			data.put(binLabel, new ArrayList<FeatureVector>());
		data.get(binLabel).add(fv);
		if (binLabel == BinLabel.BIN_RECENT && binSize(binLabel) > MAX_RECENTS) 
			data.get(binLabel).remove(0);
		if (binLabel == BinLabel.BIN_TRAIN && 
				binSize(binLabel) >= Parameters.getTrainingThreshold())
			setDefaultBin(BinLabel.BIN_RECENT);
	}
	
	/**
	 * Clears the BinLabel 'binLabel' and removes FeatureVectors from it
	 * @param binLabel BinLabel of bin to put the FeatureVector from
	 * @throws NoSuchElementException
	 */
	public static synchronized void clear(BinLabel binLabel) 
			throws NoSuchElementException {
		if (!data.containsKey(binLabel)) 
			throw new NoSuchElementException(binLabel.toString() + " bin DNE");
		data.get(binLabel).clear();
	}
	
	/**
	 * Returns the size of the bin of BinLabel 'binLabel'
	 * @param binLabel BinLabel of bin to retrieve size
	 * @return Returns 0 if no such bin exists otherwise returns its size
	 */
	public static int binSize(BinLabel binLabel) {
		if (!data.containsKey(binLabel)) 
			return 0;
		return data.get(binLabel).size();
	}
	
	/**
	 * Adds the given FeatureVector to the bin that is currently the active bin
	 * @param fv FeatureVector to add
	 */
	public static synchronized void add(FeatureVector fv) {
		add(activeBin, fv);
		/*if (!activeBin.equals(BinLabel.BIN_RECENT))
			add(BinLabel.BIN_RECENT, fv);*/
	}
	
	/**
	 * Returns most recent FeatureVector from the RecentBin
	 * @return Most recent FeatureVector from the bin_recent
	 */
	public static synchronized FeatureVector getMostRecent() {
		if (data.get(BinLabel.BIN_RECENT) == null)
			return null;
		if (data.get(BinLabel.BIN_RECENT).size() == 0) 
			return null;
		return data.get(BinLabel.BIN_RECENT).remove(0);
	}
	
	/**
	 * Sets the Default Bin to 'newDefault'
	 * @param newDefault Type of the bin to add FeatureVectors to 
	 */
	public static synchronized void setDefaultBin(BinLabel binLabel) {
		//System.out.println("Bin set to: " + binLabel.toString());
		activeBin = binLabel;
	}
	
	/**
	 * Set the MAX_RECENT parameter
	 * @param maxRecents the new value of MAX_RECENT  
	 */
	public static void setMaxRecent(int maxRecents) {
		DataStorage.MAX_RECENTS = maxRecents;
	}
}

