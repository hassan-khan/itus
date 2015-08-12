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
 *   
 * 
*/
package ca.uwaterloo.crysp.itus.client.measurements;

import java.util.ArrayList;

import android.view.MotionEvent;
import ca.uwaterloo.crysp.itus.FeatureVector;
import ca.uwaterloo.crysp.itus.measurements.Dispatcher;
import ca.uwaterloo.crysp.itus.measurements.EventType;
import ca.uwaterloo.crysp.itus.measurements.Measurement;
import ca.uwaterloo.crysp.itus.measurements.TouchEvent;

/**
 * Processes the TouchEvent to generate FeatureVectors 
 * @author Aaron Atwater
 * @author Hassan Khan
 *
 */
public class TouchEventLive extends TouchEvent {

	/**
	 * TouchPoints for the current swipe
	 */
	private ArrayList<TouchPoint> touchPoints = new ArrayList<TouchPoint>();
	/**
	 * Default constructor for Touch
	 */
	public TouchEventLive(){
		fv = new FeatureVector(NUM_FEATURES);
		super.setFeatureList(featureList);
	}
	
	@Override
	public void staticInitializer(Dispatcher eventDispatcher) {
		eventDispatcher.registerCallback(EventType.TOUCH_INPUT, this);
	}

	@Override
	public boolean procEvent(Object ev, EventType eventType) {
		MotionEvent event = (MotionEvent) ev;
		int action = event.getAction();
        
        switch(action) {
	        case MotionEvent.ACTION_DOWN:  /* primary pointer */
			case MotionEvent.ACTION_POINTER_DOWN: /* any subsequent pointer */
				/*No need for a swipe ID*/
				break;
			case MotionEvent.ACTION_MOVE: /* any number of pointers move */
				for (int hIndx = 0; hIndx < event.getHistorySize(); hIndx++) {
					for (int pIndex = 0; pIndex < event.getPointerCount(); 
							pIndex++) {
						TouchPoint tp = new TouchPoint();
						tp.xVal = event.getHistoricalX(pIndex, hIndx);
						tp.yVal = event.getHistoricalY(pIndex, hIndx);
						tp.pressure = event.getHistoricalPressure(pIndex, hIndx);
						tp.width = event.getHistoricalSize(pIndex, hIndx);
						tp.orientation = event.getHistoricalOrientation(pIndex, hIndx);
						tp.eventTimestamp = event.getHistoricalEventTime(hIndx);
						this.touchPoints.add(tp);
					}
				}
				
				for (int pIndex = 0; pIndex < event.getPointerCount(); 
						pIndex++) {
					TouchPoint tp = new TouchPoint();
					tp.xVal = event.getX(pIndex);
					tp.yVal = event.getY(pIndex);
					tp.pressure = event.getPressure(pIndex);
					tp.width = event.getSize(pIndex);
					tp.eventTimestamp = event.getEventTime();
					tp.orientation = event.getOrientation(pIndex);
					this.touchPoints.add(tp);
				}
				break;
			case MotionEvent.ACTION_POINTER_UP: /* all pointers are up */
			case MotionEvent.ACTION_UP: 
			case MotionEvent.ACTION_CANCEL:
				/* XXX return if length of swipe is less than 6 touchpoints*/
				if (this.touchPoints.size() < 10) {
					this.touchPoints.clear();
					return false;
				} 
				else {
					fv = computeFeatureVector(touchPoints);
					TouchEvent.lastSwipeTimestamp = this.touchPoints.get(
						this.touchPoints.size() - 1).eventTimestamp;
					this.touchPoints.clear();
					return true;
				}
        }
		return false;
	}

	@Override
	public Measurement newInstance() {
		return new TouchEventLive();
	}
}
