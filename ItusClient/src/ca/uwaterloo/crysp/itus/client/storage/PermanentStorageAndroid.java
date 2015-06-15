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
package ca.uwaterloo.crysp.itus.client.storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.os.Environment;

import ca.uwaterloo.crysp.itus.storage.PermanentStorage;

/**
 * Provides File IO support on Android. Appropriate permissions must be set in
 * the manifest file
 * @author Aaron Atwater
 * @author Hassan Khan
 */
public class PermanentStorageAndroid implements PermanentStorage {

	@Override
	public Object retrieveModel(String fileName) throws FileNotFoundException,
			IOException, ClassNotFoundException {
		File directory = Environment.getExternalStorageDirectory();
		Object obj = null;
		FileInputStream fis = null;
		ObjectInputStream in = null;
		fis = new FileInputStream(directory + "/" + fileName);
		in = new ObjectInputStream(fis);
		obj = in.readObject();
		in.close();
		return obj;
	}

	@Override
	public boolean saveModel(String fileName, Object model)
			throws FileNotFoundException, IOException {
		File directory = Environment.getExternalStorageDirectory();
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		fos = new FileOutputStream(directory + "/" + fileName);
		out = new ObjectOutputStream(fos);
		out.writeObject(model);
		out.close();
		return true;
	}

	@Override
	public void log(String fileName, String strFV) throws IOException {
		File directory = Environment.getExternalStorageDirectory();
		File file = new File(directory + "/" + fileName);
		BufferedWriter out = 
				new BufferedWriter(new FileWriter(file, true));
		out.write(strFV + "\n");
		out.close();
	}
}
