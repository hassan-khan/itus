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
package ca.uwaterloo.crysp.itus.oracle.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

import ca.uwaterloo.crysp.itus.FeatureVector;
import ca.uwaterloo.crysp.itus.machinelearning.ClassLabel;

/**
 * Functions to read and parse FeatureVectors from a file. 
 * @author Aaron Atwater
 * @author Hassan Khan
 */
public class FeatureVectorParser {
	
	/**
	 * Takes {@code fileName} which is the complete path of the file and returns
	 * an {@code ArrayList} containing lines in the file
	 * @param fileName Complete path of the file
	 * @return An {@code ArrayList} containing lines in the file
	 * @throws FileNotFoundException 
	 * @throws ParseException
	 */
	public static ArrayList<String> readFile(String fileName) 
			throws FileNotFoundException, ParseException {
		ArrayList <String> lines = new ArrayList<String>();
		Scanner scanner = new Scanner(new File(fileName));
		while (scanner.hasNextLine())
		    lines.add(scanner.nextLine());
		scanner.close();
		return lines;
	}

	/**
	 * Parses the provided string representation of FeatureVector and populates
	 * corresponding {@code FeatureVector} {@code fv}. Format of string is:
	 * ClassLabel<int>;FeatureNumber<int>:FeatureVal<double>...
	 * see res/deflatedFeatureVectors.txt for an example
	 * @param strFeatureVector string representation of FeatureVector
	 * @throws StringIndexOutOfBoundsException
	 */
	public static void parseFeatureVectorFromString (FeatureVector fv,
			String strFeatureVector) 
					throws StringIndexOutOfBoundsException {
		int classLabel = Integer.parseInt(strFeatureVector.substring(0,
				strFeatureVector.indexOf(';')));
		String [] toks = strFeatureVector.substring(
				strFeatureVector.indexOf(';')+1).split(";");
		if (fv.size() < toks.length)
			throw new IllegalArgumentException("String contains more than "
					+ String.valueOf(toks.length) + " features");
		fv.setClassLabel(ClassLabel.getClassLabel(classLabel));
		for (String feature : toks) {
			String featureNum = feature.substring(0, feature.indexOf(":"));
			String featureVal = feature.substring(feature.indexOf(":")+1);
			fv.set(Integer.parseInt(featureNum)-1, 
					Double.parseDouble(featureVal));
		}
	}
	
	/**
	 * Parses the provided string representation of FeatureVector and returns
	 * the number of features in it. Format of string is:
	 * ClassLabel<int>;FeatureNumber<int>:FeatureVal<double>...
	 * see res/deflatedFeatureVectors.txt for an example
	 * @param strFeatureVector string representation of FeatureVector
	 * return number of Features in the FeatureVector
	 */
	public static int strFeatureVectorLength (String strFeatureVector){
		String [] toks = strFeatureVector.substring(
				strFeatureVector.indexOf(';')+1).split(";");
		return toks.length;
	}
}
