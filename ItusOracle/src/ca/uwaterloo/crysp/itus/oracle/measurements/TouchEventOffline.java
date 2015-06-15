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
package ca.uwaterloo.crysp.itus.oracle.measurements;


import ca.uwaterloo.crysp.itus.FeatureVector;
import ca.uwaterloo.crysp.itus.measurements.Dispatcher;
import ca.uwaterloo.crysp.itus.measurements.EventType;
import ca.uwaterloo.crysp.itus.measurements.Measurement;
import ca.uwaterloo.crysp.itus.measurements.TouchEvent;

/**
 * Offline TouchEvent Measurement class for Oracle. 
 * @author Aaron Atwater
 * @author Hassan Khan
 *
 */
public class TouchEventOffline extends TouchEvent {

	/**
	 * Default constructor for Touch
	 */
	public TouchEventOffline(){
		fv = new FeatureVector(NUM_FEATURES);
		super.setFeatureList(featureList);
	}
	@Override
	public void staticInitializer(Dispatcher eventDispatcher) {
		eventDispatcher.registerCallback(EventType.TOUCH_INPUT, this);

	}

	@Override
	public boolean procEvent(Object ev, EventType eventType) {
		assert(eventType == EventType.TOUCH_INPUT);
		//String strFeatureVector = (String) ev;
		fv = (FeatureVector) ev;
		return true;
		/*try {
			FeatureVectorParser.parseFeatureVectorFromString(fv,
						strFeatureVector);
			return true;
		}	catch(Exception e) {
				return false;
		}*/
	}

	@Override
	public Measurement newInstance() {
		return new TouchEventOffline();
	}

	/*@Override
	public List<FeatureVector> defaultPositiveInstances() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FeatureVector> defaultNegativeInstances() {
		// TODO Auto-generated method stub
		return null;
	}*/
}
