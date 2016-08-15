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
import java.util.HashMap;

import ca.uwaterloo.crysp.itus.FeatureVector;
import ca.uwaterloo.crysp.itus.Itus;
import ca.uwaterloo.crysp.itus.Parameters;
import ca.uwaterloo.crysp.itus.storage.DataStorage;

/**
 * The dispatcher class dispatches Events from the android subsystem to the
 * Measurement classes.
 * 
 * @author Aaron Atwater
 * @author Hassan Khan
 */
public class Dispatcher {
	/**
	 * Callback register to save the callbacks against various event types
	 */
	protected HashMap<EventType, ArrayList<Measurement>> 
		callbackRegister = new HashMap<EventType, ArrayList<Measurement>>();

	/**
	 * Registers a callback against 'eventType' for Measurement 'm'.
	 * 
	 * @param eventType Type of the event
	 * @param measurement Measurement class to register against 'event'
	 */
	public void registerCallback(EventType eventType, Measurement measurement) {
		if (!callbackRegister.containsKey(eventType))
			callbackRegister.put(eventType, new ArrayList<Measurement>());
		callbackRegister.get(eventType).add(measurement);
	}

	/**
	 * Unregisters a callback against 'eventType'
	 * 
	 * @param eventType Type of the event
	 * @return true if successfully unregistered
	 */
	public boolean unRegisterCallback(EventType eventType) {
		if (!callbackRegister.containsKey(eventType))
			return false;
			
		callbackRegister.remove(eventType);
		return true;
	}

	/**
	 * If an event of type 'eventType' is received, the event object 'ev' is 
	 * passed along measurement class for event processing
	 * @param eventType Type of the event
	 * @param ev Event data
	 */
	public void procEvent(EventType eventType, Object ev) {
		if (!callbackRegister.containsKey(eventType)) 
			return;
		ArrayList<Measurement> measurements = callbackRegister.get(eventType);
		for (int i = 0; i < measurements.size(); i++) {
			Measurement measurement = measurements.get(i);
			if (measurement.procEvent((Object)ev, eventType)) {
				//System.out.println("Add to DS");
				if(Parameters.getMode() ==  Parameters.Mode.CONFIG_MODE) {
					try{ Itus.getPermanentStorage().log(
							Parameters.getTouchlogfilename(), 
							deflateFeatureVector(
									measurement.getFeatureVector()));
					} catch (Exception e) {
						System.out.print(e.getMessage());
					}
				}
				else {
					DataStorage.add(measurement.getFeatureVector());
					measurements.set(i, measurement.newInstance());
				}
			}
		}
	}
	public static String deflateFeatureVector(FeatureVector fv) {
		StringBuilder builder = new StringBuilder();
		builder.append(fv.getIntClassLabel());
		builder.append(';');
		for (int i = 0; i< fv.size(); i++) {
			builder.append(i+1);
			builder.append(':');
			builder.append(fv.get(i));
			if (i != fv.size() - 1)
				builder.append(';');
		}
		return builder.toString();
	}
}
