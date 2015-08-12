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

import haibison.android.lockpattern.LockPatternActivity;
import haibison.android.lockpattern.util.AlpSettings;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * A wrapper for the Android Pattern Lock library by Hai Bison
 * @author Aaron Atwater
 * @author Hassan Khan
 */
public class AndroidPatternWrapper {
	/**
	 * The context object of the calling Activity that will be used to 
	 * access shared preferences and to launch Intent
	 */
	Context ctx;
	
	/**
	 * The activity object of the calling Activity that will be used to 
	 * access shared preferences and to launch Intent
	 */
	Activity activity;
	public AndroidPatternWrapper(Context _ctx, Activity _activity) {
		ctx = _ctx;
		activity = _activity;
	}

	/**
	 * Configures Android pattern and autosaves the pattern
	 */
	public void configurePattern() {
		AlpSettings.Security.setAutoSavePattern(ctx, true);
		Intent intent = new Intent(LockPatternActivity.ACTION_CREATE_PATTERN, 
				null, ctx, LockPatternActivity.class);
		activity.startActivity(intent);
	}
	/**
	 * Compares android pattern
	 */
	public void comparePattern(){
		Intent intent = new Intent(LockPatternActivity.ACTION_COMPARE_PATTERN, 
				null, ctx, LockPatternActivity.class);
		activity.startActivity(intent);
	}

}
