package ca.uwaterloo.crysp.itus.storage;

import static org.junit.Assert.*;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import ca.uwaterloo.crysp.itus.FeatureVector;
import ca.uwaterloo.crysp.itus.machinelearning.ClassLabel;

public class DataStorageTest {

	FeatureVector fv;
	double [] features = {1.0,2.0,3.0};
	
	@Before
	public void setUp() {
		fv = new FeatureVector(features,ClassLabel.UNKNOWN);
	}
	@Test
	public void testGet() {
		try{
			DataStorage.get(BinLabel.BIN_RECENT, 0);
			fail("Get failed on empty bin: BIN_RECENT");
		}
		catch(NoSuchElementException e) {
		}
		try{
			DataStorage.get(BinLabel.BIN_TRAIN, 0);
			fail("Get failed on empty bin: BIN_TRAIN");
		}
		catch(NoSuchElementException e) {
		}
		DataStorage.add(BinLabel.BIN_RECENT, fv);
		FeatureVector recentFV = DataStorage.get(BinLabel.BIN_RECENT, 0);
		for (int i = 0; i < recentFV.size(); i++)
			assertEquals("DataStorage.get failed at index 0",
					recentFV.get(i), fv.get(i), 0.0);
		assertEquals("DataStorage.get failed",
				recentFV.getIntClassLabel(), fv.getIntClassLabel());
		DataStorage.clear(BinLabel.BIN_RECENT);
	}

	@Test
	public void testGetAll() {
		DataStorage.add(BinLabel.BIN_TRAIN, fv);
		DataStorage.add(BinLabel.BIN_TRAIN, fv);
		List<FeatureVector> allFV = DataStorage.getAll(BinLabel.BIN_TRAIN);
		assertEquals("DataStorage.getAll failed",
				DataStorage.binSize(BinLabel.BIN_TRAIN), allFV.size());
		DataStorage.clear(BinLabel.BIN_TRAIN);
	}

	@Test
	public void testAddBinLabelFeatureVector() {
		DataStorage.add(BinLabel.BIN_RECENT, fv);
		assertEquals("DataStorage.addBinLabel failed to add in BIN_RECENT",
			DataStorage.binSize(BinLabel.BIN_RECENT), 1);
		DataStorage.clear(BinLabel.BIN_RECENT);
	}

	@Test
	public void testBinSize() {
		DataStorage.setDefaultBin(BinLabel.BIN_TRAIN);
		DataStorage.add(fv);
		assertEquals("DataStorage.binSize failed to add in BIN_TRAIN",
			DataStorage.binSize(BinLabel.BIN_TRAIN), 1);
		DataStorage.clear(BinLabel.BIN_TRAIN);
	}
	@Test
	public void testClear() {
		DataStorage.setDefaultBin(BinLabel.BIN_TRAIN);
		DataStorage.add(fv);
		DataStorage.clear(BinLabel.BIN_TRAIN);
		assertEquals("DataStorage.clear failed to add in BIN_TRAIN",
				DataStorage.binSize(BinLabel.BIN_TRAIN), 0);
	}
	@Test
	public void testAddFeatureVector() {
		DataStorage.setDefaultBin(BinLabel.BIN_TRAIN);
		DataStorage.add(fv);
		assertEquals("DataStorage.addFeature failed to add in BIN_TRAIN",
			DataStorage.binSize(BinLabel.BIN_TRAIN), 1);
		assertEquals("DataStorage.addFeature failed to add in BIN_RECENT",
				DataStorage.binSize(BinLabel.BIN_RECENT), 1);
		DataStorage.clear(BinLabel.BIN_TRAIN);
		DataStorage.clear(BinLabel.BIN_RECENT);
	}

	@Test
	public void testGetMostRecent() {
		DataStorage.add(BinLabel.BIN_RECENT, fv);
		FeatureVector recentFV = DataStorage.getMostRecent();
		for (int i = 0; i < recentFV.size(); i++)
			assertEquals("DataStorage.getMostRecent failed",
					recentFV.get(i), fv.get(i), 0.0);
		assertEquals("DataStorage.getMostRecent failed",
				recentFV.getIntClassLabel(), fv.getIntClassLabel());
		DataStorage.clear(BinLabel.BIN_RECENT);
	}

}
