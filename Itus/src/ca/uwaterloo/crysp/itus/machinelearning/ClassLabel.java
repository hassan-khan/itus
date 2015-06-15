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
package ca.uwaterloo.crysp.itus.machinelearning;

/**
 * Typesafe enum pattern for positive, negative and unknown class labels 
 * 
 * @author Aaron Atwater
 * @author Hassan Khan
 */

@SuppressWarnings("serial")
public class ClassLabel implements java.io.Serializable {
	/**
	 * Integer label for the class
	 */
	private final int value;
	
	private ClassLabel(int value) {
		this.value = value;
	}
	/**
	 * Get the integer value for this label
	 * @return
	 */
	public int getClassLabel() {
		return this.value;
	}
	
	/**
	 * Get the integer value for this label
	 * @return
	 */
	public static ClassLabel getClassLabel(int intValue) {
		if (intValue == POSITIVE.value)
			return POSITIVE;
		else if (intValue == NEGATIVE.value)
			return NEGATIVE;
		return UNKNOWN;
	}
	/**
	 * The positive sample
	 */
	public static final ClassLabel POSITIVE = new ClassLabel(1);
	
	/**
	 * The negative sample
	 */
	public static final ClassLabel NEGATIVE = new ClassLabel(-1);
	
	/**
	 * The unknown sample
	 */
	public static final ClassLabel UNKNOWN = new ClassLabel(0);
}
