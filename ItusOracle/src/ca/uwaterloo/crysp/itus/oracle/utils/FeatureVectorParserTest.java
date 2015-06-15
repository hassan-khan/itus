package ca.uwaterloo.crysp.itus.oracle.utils;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import ca.uwaterloo.crysp.itus.FeatureVector;
import ca.uwaterloo.crysp.itus.machinelearning.ClassLabel;

public class FeatureVectorParserTest {

	@Test
	public void testReadFile() {
		try {
		ArrayList<String> fvs = FeatureVectorParser.readFile(
				"./res/deflatedFeatureVectors.txt");
		if (fvs.size() != 8)
			fail("Failed to correctly read vectors from file");
		
		} catch (Exception e) {System.out.print("Exception raised");}
	}

	@Test
	public void testParseFeatureVectorFromString() {
		String strNegFV = "-1;1:1.0;2:2.0;3:3.0";
		String strPosFV = "1;1:1.0;2:2.0;3:3.0";
		String malformedFV = "1.0;1:1.0;2:2.0;3;3.0";
		FeatureVector fv = new FeatureVector(3);
		FeatureVectorParser.parseFeatureVectorFromString(fv, strNegFV);
		if (fv.size() != 3)
			fail("Failed to correctly parse String to FeatureVector");
		if(fv.getClassLabel() != ClassLabel.NEGATIVE)
			fail("Failed to correctly parse negative FeatureVector");
		for (int i = 0; i < fv.size(); i++)
			if (fv.get(i) != i + 1.0)
				fail("Failed to correctly parse String to FeatureVector");
		
		FeatureVectorParser.parseFeatureVectorFromString(fv, strPosFV);
		if (fv.size() != 3)
			fail("Failed to correctly parse String to FeatureVector");
		if(fv.getClassLabel() != ClassLabel.POSITIVE)
			fail("Failed to correctly parse negative FeatureVector");
		for (int i = 0; i < fv.size(); i++)
			if (fv.get(i) != i + 1.0)
				fail("Failed to correctly parse String to FeatureVector");
		try {
			FeatureVectorParser.parseFeatureVectorFromString(fv, malformedFV);
			fail("Failed to catch exception on malformed input");
		} catch(Exception e) {}

	}
}
