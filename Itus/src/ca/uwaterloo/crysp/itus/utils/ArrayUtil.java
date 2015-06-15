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
package ca.uwaterloo.crysp.itus.utils;

import java.util.Arrays;

/**
 * Useful Array statistic functions (max, variance, percentile,...)
 * @author Aaron Atwater
 * @author Hassan Khan
 */
public class ArrayUtil {

	/**
	 * Returns the maximum value in array
	 * @param array	A {@code double} array 
	 * @return Max value in {@code array}
	 * */
	public static double max(double [] array) {
		double max = Double.MIN_VALUE;
		for(int i = 0; i < array.length; i++)
			if (array[i] > max) 
				max = array[i];
		return max;
	}
	
	/**
	 * Returns variance among values in array
	 * @param array	A {@code double} array 
	 * @return returns variance among elements of {@code array}
	 */
	public static double variance(double [] array) {
		 double sum = 0, mean = 0, var = 0;
	     for(double d : array)
	    	 sum += d;
	     mean = sum/array.length;
	     double temp = 0;
         for(double d :array)
             temp += (mean-d)*(mean-d);
         var = temp/array.length;
         return Math.sqrt(var);
	}
	
	/**
	 * Returns the 'percentile' value in array
	 * @param array	A {@code double} array 
	 * @param percentile The percentile value to obtain between 0-1
	 * @return returns value at {@code percentile} in {@code array}
	 * */
	public static double percentile(double [] array, double percentile) {
		Arrays.sort(array);
		if (array.length == 0 || percentile < 0 || percentile > 1)
			throw new java.lang.IllegalArgumentException();
		double k = (array.length -1 ) * percentile;
		double f = Math.floor(k);
		double c = Math.ceil(k);
		if (f == c)
			return array[(int)k];
		return array[(int)f]* (c-k) + array[(int)c] * (k-f);
	}
	
}