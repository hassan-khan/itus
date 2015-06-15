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


/**
 * Typesafe enum pattern for supported event types
 * 
 * @author Aaron Atwater
 * @author Hassan Khan
 */
public class EventType {
	/**
	 * String label for event types
	 */
	private final String name;
	
	private EventType(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the string value for this label
	 * @return Returns the name of the event type
	 */
	public String toString() {
		return this.name;
	}
	
	/**
	 * Represents the touch input event
	 */
	public static final EventType TOUCH_INPUT = new EventType("TOUCH_INPUT");
	/**
	 * Represents the Soft keyboard input event
	 */
	public static final EventType KEY_INPUT = new EventType("KEY_INPUT");
	/**
	 * Represents the MOTION event i.e. accelerometer and Gyroscope 
	 */
	public static final EventType MOTION_EVENT = new EventType("MOTION_EVENT");
	/**
	 * Represents other periodic events
	 */
	public static final EventType PERIODIC_EVENT = 
			new EventType("PERIODIC_EVENT");
}
