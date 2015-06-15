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

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ca.uwaterloo.crysp.itus.machinelearning.ClassLabel;

/**
 * Testclass for the FeatureVector 
 * 
 * @author Aaron Atwater
 * @author Hassan Khan
 */

public class FeatureVectorTest {
	
	int size = 10;
	double [] fv;
	ClassLabel label;
	FeatureVector featureVector;
	
	@Before
	public void setUp() {
		size = 10;
		fv = new double[size];
		for (int i = 0; i < fv.length; i++)
			fv[i] = i * 1.0;
		label = ClassLabel.NEGATIVE;
		featureVector = new FeatureVector(fv, label);
	}
	@Test
	public void testFeatureVectorDoubleArrayLabel() {
		for (int i = 0; i < featureVector.size(); i++)
			assertEquals("FeatureVector constructor value match fail", 
					featureVector.get(i), fv[i], 0.0);
		
	}

	@Test
	public void testSize() {
		assertEquals("FeatureVector constructor size mismatch fail",
				featureVector.size(), size);
	}

	@Test
	public void testGetClassLabel() {
		assertEquals("FeatureVector getClassLabel match fail",
				featureVector.getClassLabel(), label);
		assertEquals("FeatureVector getIntClassLabel match fail",
				featureVector.getIntClassLabel(), -1);
	}

	@Test
	public void testSet() {
		featureVector.set(0, 100.0);
		assertEquals("FeatureVector.set() failed",
				featureVector.get(0), 100.0, 0);
	}

	@Test
	public void testClear() {
		featureVector.clear();
		for (int i = 0; i < featureVector.size(); i++)
			assertEquals("FeatureVector.size() failed",
					featureVector.get(i), 0.0, 0.0);

	}
}
