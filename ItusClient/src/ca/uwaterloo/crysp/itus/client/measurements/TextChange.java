/*   This program is free software: you can redistribute it and/or modify
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
package ca.uwaterloo.crysp.itus.client.measurements;

import java.util.ArrayList;
import java.util.List;

import ca.uwaterloo.crysp.itus.FeatureVector;
import ca.uwaterloo.crysp.itus.machinelearning.ClassLabel;
import ca.uwaterloo.crysp.itus.measurements.Dispatcher;
import ca.uwaterloo.crysp.itus.measurements.EventType;
import ca.uwaterloo.crysp.itus.measurements.Measurement;
/**
 * KeyEvent processor
 * @author Aaron Atwater
 * @author Hassan Khan
 */
public class TextChange extends Measurement {
	
	String lastStr = "";
	char lastChar = '.';
	long lastTimestamp = 0;
	protected FeatureVector fv;
	
	/**
	 * Number of features supported currently
	 */
	public static final int NUM_FEATURES = 27 * 27; /* a-z*a-z & space*/
	public static String featureList[] = new String[TextChange.NUM_FEATURES];
	
	/**
	 * Default constructor
	 */
	public TextChange() {
		
		char ch[] = {' ', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
				'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 
				'u', 'v', 'w', 'x', 'y', 'z'};
		for (int i = 0; i < ch.length; i++) 
			for(int j = 0; j < ch.length; j++)
				featureList[i*27 + j] = String.valueOf(ch[i]) + 
				  String.valueOf(ch[j]);
		
		super.setFeatureList(featureList);
		fv = new FeatureVector(1); /*feature vector can only store one keystroke*/
	}

	/* (non-Javadoc)
	 * @see ca.uwaterloo.crysp.itus.measurements.Measurement#staticInitializer()
	 */
	@Override
	public void staticInitializer(Dispatcher eventDispatcher) {
		eventDispatcher.registerCallback(EventType.KEY_INPUT, this);
	}

	/* (non-Javadoc)
	 * @see ca.uwaterloo.crysp.itus.measurements.Measurement#getFeatureVector()
	 */
	@Override
	public FeatureVector getFeatureVector() {
		return fv;
	}

	/* (non-Javadoc)
	 * @see ca.uwaterloo.crysp.itus.measurements.Measurement#newInstance()
	 */
	@Override
	public Measurement newInstance() {
		return new TextChange();
	}

	/* (non-Javadoc)
	 * @see ca.uwaterloo.crysp.itus.measurements.Measurement#procEvent(java.lang.Object, ca.uwaterloo.crysp.itus.measurements.EventType)
	 */
	@Override
	public boolean procEvent(Object ev, EventType eventType) {
		System.out.println("procEvent() :D");
		String currentInput = new String((String)ev);
		char currentChar = '.';
		long currentTimestamp = 0;
		int key = -1;
		boolean rv = false;
		
		/*find the key that was pressed by the user*/
		/*if this was a suggestion by android return*/
		if (currentInput.length() == this.lastStr.length())
			return false;
		
		/*if a character was deleted, backspace was pressed*/
		else if (currentInput.length() == this.lastStr.length() - 1)
    		; /*do nothing, we are only interested in chars 'a-z'*/
    	/*case of a simple append*/
    	else if(currentInput.length() == this.lastStr.length() + 1 && 
    			currentInput.startsWith(this.lastStr)) { 
    		currentChar = currentInput.charAt(currentInput.length() -1 );
    		currentTimestamp = System.nanoTime();
    		key = getBigramFeatureKey(this.lastChar, currentChar);
    		if (key != -1) {
    			fv.clear();
    			fv.set(key, currentTimestamp - this.lastTimestamp);
    			fv.setClassLabel(ClassLabel.UNKNOWN);
    			rv = true;
    		}
    	}
    	else /*user made a change within string by navigating within string*/
    		; /*not interested as interstroke time is skewed*/
    	
		this.lastChar = currentChar;
		this.lastTimestamp = currentTimestamp;
		this.lastStr = new String(currentInput);
    	
    	return rv;
	}
	
	/**
	 * Get the feature key against this bigram
	 * @param c last keystroke
	 * @param d this keystroke
	 * @return returns featureKey
	 */
	private int getBigramFeatureKey(char c, char d) {
		int i = 0, j = 0, key = 0;
		
		if (!Character.isLetter(c) && c != ' ') 
			return -1;
		
		if (!Character.isLetter(d)  && d != ' ') 
			return -1;
		
		if (Character.isLetter(c))
			i = Character.getNumericValue(Character.toLowerCase(c)) - 9;
		if (Character.isLetter(d))
			j = Character.getNumericValue(Character.toLowerCase(d)) - 9;
		key = i*27 + j;
		return key;
	}
	
	/* (non-Javadoc)
	 * @see ca.uwaterloo.crysp.itus.measurements.Measurement#defaultPositiveInstances()
	 */
	@Override
	public List<FeatureVector> defaultPositiveInstances() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.uwaterloo.crysp.itus.measurements.Measurement#defaultNegativeInstances()
	 */
	@Override
	public List<FeatureVector> defaultNegativeInstances() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.uwaterloo.crysp.itus.measurements.Measurement#getRecommendedClassifier()
	 */
	@Override
	public Class<?> getRecommendedClassifier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Class<?>> getSupportedClassifier() {
		// TODO Auto-generated method stub
		return null;
	}
}
