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
package ca.uwaterloo.crysp.itus.measurements;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ca.uwaterloo.crysp.itus.FeatureVector;
import ca.uwaterloo.crysp.itus.machinelearning.ClassLabel;

/**
 * Meta-measurement used to combine multiple measurement sources into a single FeatureVector 
 * 
 * @author Aaron Atwater
 * @author Hassan Khan
 *
 */
public class MultiMeasurement extends Measurement {
	List<Measurement> measurements = new ArrayList<Measurement>();
	
	SubDispatcher subDispatcher = null;
	
	/**
	 * List of all the features supported
	 */
	protected  String featureList [] = {};
	
	/**
	 * Default constructor for MultiMeasurement
	 */
	public MultiMeasurement(Measurement[] msrs){
		//fv = new FeatureVector(Touch.NUM_FEATURES);
		//super.setFeatureList(featureList);
		
		for (Measurement m : msrs) {
			measurements.add(m);
		}
		
		subDispatcher = new SubDispatcher(measurements.size());
	}

	/* (non-Javadoc)object
	 * @see ca.uwaterloo.crysp.itus.measurements.Measurement#newInstance()
	 */
	public Measurement newInstance() {
		Measurement[] msrs = new Measurement[measurements.size()];
		
		for (int i=0; i<measurements.size(); i++) {
			msrs[i] = measurements.get(i).newInstance();
		}
		
		return new MultiMeasurement(msrs);
	}
	
	/* (non-Javadoc)
	 * @see ca.uwaterloo.crysp.itus.measurements.Measurement#staticInitializer()
	 */
	public void staticInitializer(Dispatcher eventDispatcher) {
		// have submeasurements register with our subdispatcher
		for (Measurement m : measurements) {
			m.staticInitializer(subDispatcher);
		}
		
		// register with the main dispatcher, so we can delegate to submeasurements later
		for (EventType et : subDispatcher.getEventList()) {
			eventDispatcher.registerCallback(et, this);
		}
	}

	/* (non-Javadoc)
	 * @see ca.uwaterloo.crysp.itus.measurements.Measurement#procEvent(java.lang.Object, ca.uwaterloo.crysp.itus.measurements.EventType)
	 */
	public boolean procEvent(Object ev, EventType eventType) {
		subDispatcher.procEvent(eventType, ev);
		return subDispatcher.readyToExport();
	}

	/* 
	 * Export all submeasurement getFeatureVector() values and combine them into
	 * a single measurement.  NOTE: this must be called ONLY when subDispatcher.readyToExport()
	 * returns <tt>true</tt>
	 * 
	 * @return FeatureVector consisting of the union of FeatureVectors 
	 * exported from all submeasurements
	 */
	@Override
	public FeatureVector getFeatureVector() {
		ArrayList<Double> agg = new ArrayList<Double>();
		ClassLabel lbl = ClassLabel.UNKNOWN;
		
		for (Measurement m : measurements) {
			FeatureVector f = m.getFeatureVector();
			lbl = f.getClassLabel(); //XXX: do something more clever with the labels?
			for (double d : f.getAll()) agg.add(d);
		}
		
		double[] aggA = new double[agg.size()];
		for (int i=0; i<agg.size(); i++) aggA[i] = agg.get(i);
		
		return new FeatureVector(aggA, lbl);
	}

	/* (non-Javadoc)
	 * @see ca.uwaterloo.crysp.itus.measurements.Measurement#defaultPositiveInstances()
	 */
	public List<FeatureVector> defaultPositiveInstances() {
		return null;//stub
	}

	/* (non-Javadoc)
	 * @see ca.uwaterloo.crysp.itus.measurements.Measurement#defaultNegativeInstances()
	 */
	public List<FeatureVector> defaultNegativeInstances() {
		return null;//stub
	}
	
	/* (non-Javadoc)
	 * @see ca.uwaterloo.crysp.itus.measurements.Measurement#getRecommendedClassifier()
	 */
	public Class<?> getRecommendedClassifier() {
		return null;//stub
	}
	
	/**
	 * Subtype of the Dispatcher, for intercepting measurements before they register
	 * with the real dispatcher in order to create more complex multi-measurement
	 * FeatureVector export policies
	 * 
	 * @author aaron atwater
	 * @author hassan khan
	 *
	 */
	protected class SubDispatcher extends Dispatcher {
		protected boolean[] haveDataFor = null;
		
		/**
		 * Create a new SubDispatcher for managing submeasurements. NOTE: The number
		 * of submeasurements MUST be constant for this class to function properly.
		 * 
		 * @param size number of submeasurements the SubDispatcher will be managing
		 */
		public SubDispatcher(int size) {
			super();
			haveDataFor = new boolean[size];
		}
		
		/**
		 * Fetch the aggregate list of events that any submeasurement has registered for
		 * 
		 * @return set of event types that have been registered for
		 */
		public Set<EventType> getEventList() {
			 return callbackRegister.keySet();
		}
		
		/**
		 * If an event of type 'eventType' is received, the event object 'ev' is 
		 * passed along measurement class for event processing
		 * 
		 * @param eventType Type of the event
		 * @param ev Event data
		 */
		public void procEvent(EventType eventType, Object ev) {
			if (!callbackRegister.containsKey(eventType)) 
				return;
			
			ArrayList<Measurement> measurements = callbackRegister.get(eventType);
			for (int i = 0; i < measurements.size(); i++) {
				Measurement m = measurements.get(i);
				if (m.procEvent((Object)ev, eventType)) {
					//DataStorage.add(m.getFeatureVector());
					//measurements.set(i, m.newInstance());
					haveDataFor[i] = true;
				}
			}
		}
		
		/**
		 * Check to see if data is ready to be exported to a FeatureVector yet
		 * 
		 * @return true if all submeasurements have indicated readiness to have getFeatureVector() called
		 */
		public boolean readyToExport() {
			for (boolean b : haveDataFor)
				if (!b) return false;
			return true;
		}
	}

	@Override
	public ArrayList<Class<?>> getSupportedClassifier() {
		// TODO Auto-generated method stub
		return null;
	}
}