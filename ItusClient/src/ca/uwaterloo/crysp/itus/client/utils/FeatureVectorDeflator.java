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

package ca.uwaterloo.crysp.itus.client.utils;

import ca.uwaterloo.crysp.itus.FeatureVector;

/**
 * Deflates the {@code FeatureVectors} to String representation for logging.
 * @author Aaron Atwater
 * @author Hassan Khan
 */
public class FeatureVectorDeflator {
	
	public static String deflateFeatureVector(FeatureVector fv) {
		StringBuilder builder = new StringBuilder();
		builder.append(fv.getIntClassLabel());
		builder.append(';');
		for (int i = 0; i< fv.size(); i++) {
			builder.append(i+1);
			builder.append(':');
			builder.append(fv.get(i));
			//builder.append(String.format("%.4f", fv.get(i)));
			if (i != fv.size() - 1)
				builder.append(';');
		}
		return builder.toString();
	}
}
