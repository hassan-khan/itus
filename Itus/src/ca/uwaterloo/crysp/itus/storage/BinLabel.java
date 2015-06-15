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


/**
 * Typesafe enum pattern for data storage bins' labels 
 * 
 * @author Aaron Atwater
 * @author Hassan Khan
 */

public class BinLabel {
	/**
	 * String label for the bins
	 */
	private final String value;
	
	private BinLabel(String value) {
		this.value = value;
	}
	/**
	 * Get the String value for this label
	 * @return Returns the BinLabel
	 */
	public String getBinLabel() {
		return this.value;
	}
	
	@Override
	public String toString() {
		return this.value;
	}
	/**
	 * Label that represents the training BIN
	 */
	public static final BinLabel BIN_TRAIN = new BinLabel("BIN_TRAIN");
	/**
	 * Label that represents the recent BIN
	 */

	public static final BinLabel BIN_RECENT = new BinLabel("BIN_RECENT");
}
