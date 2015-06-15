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

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import ca.uwaterloo.crysp.itus.FeatureVector;
import ca.uwaterloo.crysp.itus.measurements.Dispatcher;
import ca.uwaterloo.crysp.itus.measurements.EventType;
import ca.uwaterloo.crysp.itus.measurements.Measurement;
/**
 * Interfaces to the Accelerometer and the Orientation sensors
 * @author Aaron Atwater
 * @author Hassan Khan
 */
public class PhysicalMotionEvent extends Measurement{

	/**
	 * Time of measurement
	 */
	long eventTime = 0;
	
	/**
	 * The pitch of orientation sensor
	 */
	double pitch;
	
	/**
	 * The roll of orientation sensor
	 */
	double roll;
	
	/**
	 * The azimuth of orientation sensor
	 */
	double azimuth;
	
	/**
	 * The acceleration along x-axis
	 */
	double x_accel;
	
	/**
	 * The acceleration along y-axis 
	 */
	double y_accel;
	
	/**
	 * The acceleration along z-axis
	 */
	double z_accel;
	
	/**
	 * Interface to the android sensor manager
	 */
	SensorManager sm;
	
	/**
	 * The sensor listener to catch all the sensor values 
	 */
	SensorEventListener sl;
	
	/**
	 * Current number of features supported by this Measurement 
	 */
	public static final int NUM_FEATURES = 7;
	
	/**
	 * FeatureVectors obtained from the raw values
	 */
	protected FeatureVector fv;
	
	/**
	 * Constructor for PhysicalMotionEvent
	 * @param context of the user app
	 */
	public PhysicalMotionEvent(Context ctx) {
		fv = new FeatureVector(PhysicalMotionEvent.NUM_FEATURES);
		sm = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
		sl = new SensorEventListener() {
		  
			@Override
		    public void onAccuracyChanged(Sensor arg0, int arg1) {
		    }

		    @SuppressWarnings("deprecation")
			@Override
		    public void onSensorChanged(SensorEvent event) {
		    	//System.out.println(event.timestamp);
		        Sensor sensor = event.sensor;
		        eventTime = event.timestamp;
		        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
		            x_accel = event.values[0];
		            y_accel = event.values[1];
		            z_accel = event.values[2];
		        } else if (sensor.getType() == Sensor.TYPE_ORIENTATION) {
		            pitch = event.values[0];
		            roll = event.values[1];
		            azimuth = event.values[2];
		        }
		    }
		};
	}

	/* (non-Javadoc)
	 * @see ca.uwaterloo.crysp.itus.measurements.Measurement#staticInitializer()
	 */
	@Override
	public void staticInitializer(Dispatcher eventDispatcher) {
		eventDispatcher.registerCallback(EventType.MOTION_EVENT, this);		
	}

	/* (non-Javadoc)
	 * @see ca.uwaterloo.crysp.itus.measurements.Measurement#getFeatureVector()
	 */
	@Override
	public FeatureVector getFeatureVector() {
		return fv;
	}

	/* (non-Javadoc)
	 * @see ca.uwaterloo.crysp.itus.measurements.Measurement#procEvent(java.lang.Object, ca.uwaterloo.crysp.itus.measurements.EventType)
	 */
	@Override
	public boolean procEvent(Object ev, EventType eventType) {
		boolean rv = false;
		if(this.eventTime != 0) {
			fv.set(0, this.eventTime);
			fv.set(1, this.x_accel);
			fv.set(2, this.y_accel);
			fv.set(3, this.z_accel);
			fv.set(4, this.pitch);
			fv.set(5, this.roll);
			fv.set(6, this.azimuth);
			this.eventTime = 0;
			rv = true;
		}
		return rv;
	}

	/**
	 * Registors the accelerometer and orientation sensors for listening
	 */
	@SuppressWarnings("deprecation")
	public void registerSensors() {
		sm.registerListener(sl, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 
				SensorManager.SENSOR_DELAY_UI);
		sm.registerListener(sl, sm.getDefaultSensor(Sensor.TYPE_ORIENTATION), 
				SensorManager.SENSOR_DELAY_UI);
	}
	
	/**
	 * unregisters the sensor 
	 */
	public void unregisterSensors() {
		sm.unregisterListener(sl);
	}

	/* (non-Javadoc)
	 * @see ca.uwaterloo.crysp.itus.measurements.Measurement#newInstance()
	 */
	@Override
	public Measurement newInstance() {
		//User context as param for newInstance of PME
		//return new PhysicalMotionEvent(ctx);
		return null;
	}

	public Measurement newInstance(Context ctx) {
		return new PhysicalMotionEvent(ctx);
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
