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

import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * Interface for permanent storage that is to be implemented by 
 * device specific libraries
 * 
 * @author Aaron Atwater
 * @author Hassan Khan
 */
public interface PermanentStorage {
	/**
	 * Returns the training model of the current classifier object from 
	 * the permanent storage
	 * @param fileName Name of the file that was used to store the model
	 * @return returns the training model as an Object file
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Object retrieveModel(String fileName)
			throws FileNotFoundException, IOException, ClassNotFoundException;
	
	/**
	 * Saves the training model 'model' of the classifier  to the permanent 
	 * storage
	 * @param fileName Name of the file that will store the model
	 * @param model Model that should be stored in the permanent storage
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public boolean saveModel(String fileName, Object model)
			throws FileNotFoundException, IOException;
	
	/**
	 * Writes deflated {@code FeatureVector} {@code strFV} in {@code fileName}
	 * and inserts a newline
	 * @param fileName
	 * @param strFV
	 * @throws IOException
	 */
	public void log(String fileName, String strFV) throws IOException;
}
