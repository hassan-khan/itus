package ca.uwaterloo.crysp.itus.oracle.storage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import ca.uwaterloo.crysp.itus.storage.PermanentStorage;

public class PermanentStorageOracle implements PermanentStorage {

	@Override
	public Object retrieveModel(String fileName) 
			throws FileNotFoundException, IOException, ClassNotFoundException {
		Object obj = null;
		FileInputStream fis = null;
		ObjectInputStream in = null;
		fis = new FileInputStream(fileName);
		in = new ObjectInputStream(fis);
		obj = in.readObject();
		in.close();
		return obj;
	}

	@Override
	public boolean saveModel(String fileName, Object model) 
			throws FileNotFoundException, IOException {
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		fos = new FileOutputStream(fileName);
		out = new ObjectOutputStream(fos);
		out.writeObject(model);
		out.close();
		return true;
	}

	@Override
	public void log(String fileName, String strFV) throws IOException {
		// TODO Auto-generated method stub
	}
	
}
