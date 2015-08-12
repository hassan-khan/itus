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
package ca.uwaterloo.crysp.itus.client.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/*A wrapper for permanent storage of shared preferences*/
public class SharedPreferenceWrapper {

	/**
	 * Saves a boolean value
	 * @param ctx Context of the app
	 * @param key key against which the 'value' is saved
	 * @param value value to save
	 */
	public static void saveBoolean(Context ctx, String key, boolean value) {
		SharedPreferences settings = 
				PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.commit();
	}
	
	public static boolean getBoolean(Context ctx, String key, boolean defaultValue){
		SharedPreferences settings = 
        		PreferenceManager.getDefaultSharedPreferences(ctx);
		return settings.getBoolean(key, defaultValue);
	}
}
