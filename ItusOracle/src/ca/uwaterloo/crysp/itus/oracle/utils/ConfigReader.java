package ca.uwaterloo.crysp.itus.oracle.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Scanner;

public class ConfigReader {
	/**
	 * Reads the configuration file located at './res' and populates the 
	 * configuration for the oracle
	 * @return a {@code HashMap} containing config:value
	 * @throws FileNotFoundException
	 */
	public static HashMap<String, String> readConfig() 
			throws FileNotFoundException, ParseException {
		HashMap <String,String> config = new HashMap<String,String>();
		Scanner scanner = new Scanner(new File("./res/oracle_config"));
		while (scanner.hasNextLine()) {
		    String line = scanner.nextLine();
		    if (line.length() == 0)
		    	continue;
		    if (line.charAt(0) == '#')
		    	continue;
			String [] toks = line.split("=");
			if (toks[0].length() == 0 || toks[1].length() == 0) {
				scanner.close();
				throw new ParseException("Failed to parse string: " + line, 0);
			}
			config.put(toks[0].trim(), toks[1].trim());
		}
		scanner.close();
		return config;
	}
}
