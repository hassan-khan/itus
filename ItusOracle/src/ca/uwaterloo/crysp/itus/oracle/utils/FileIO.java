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
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Functions to read files. 
 * @author Aaron Atwater
 * @author Hassan Khan
 */
public class FileIO {
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
	 * Returns the number of lines in this file
	 * @param file {@code File} object
	 * @return Number of lines in the file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static int getNumLines(File file) 
			throws FileNotFoundException, IOException{
		LineNumberReader lnr = new LineNumberReader(new FileReader(file));
		lnr.skip(Long.MAX_VALUE);
		int lines = lnr.getLineNumber()+1;
		lnr.close();
		return lines;
	}
	
	/**
	 * Returns the number of lines in this file
	 * @param fileName {@code String} containing Filename 
	 * @return Number of lines in the file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static int getNumLines(String fileName) 
			throws FileNotFoundException, IOException{
		LineNumberReader lnr = new LineNumberReader(new FileReader(
				new File(fileName)));
		lnr.skip(Long.MAX_VALUE);
		int lines = lnr.getLineNumber()+1;
		lnr.close();
		return lines;
	}
}
