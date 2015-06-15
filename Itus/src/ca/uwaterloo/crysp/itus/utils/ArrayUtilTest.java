/**
 * 
 */
package ca.uwaterloo.crysp.itus.utils;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Hassan
 *
 */
public class ArrayUtilTest {

	/**
	 * Test method for {@link ca.uwaterloo.crysp.itus.utils.ArrayUtil#max(double[])}.
	 */
	@Test
	public void testMax() {
		double [] array = new double[10];
		int j = -5;
		for (int i = 0; i < 10; i++)
			array[i] = j++;
		assertEquals("ArrayUtil.max failed", ArrayUtil.max(array),4.0, 0.99);
	}



	/**
	 * Test method for {@link ca.uwaterloo.crysp.itus.utils.ArrayUtil#percentile(double[], double)}.
	 */
	@Test
	public void testPercentile() {
		double [] array = new double[10];
		double [] zeroSizedArray = new double[0];
		for (int i = 0; i < 10; i++)
			array[i] = i;
		assertEquals("ArrayUtil.percentile computed invalid result", 
				ArrayUtil.percentile(array,0.5),5, 0.9);
		
		try{
			ArrayUtil.percentile(array,-1.0);
			fail("Failed to catch IllegalArgumentException: percentile < 0.0");
		} catch(Exception e) { }
		
		try{
			ArrayUtil.percentile(array,-1.0);
			fail("Failed to catch IllegalArgumentException: percentile > 1.0");
		} catch(Exception e) { }
		
		try{
			ArrayUtil.percentile(zeroSizedArray,-1.0);
			fail("Failed to catch IllegalArgumentException: |array| = 0");
		} catch(Exception e) { }
		
		
	}

}
