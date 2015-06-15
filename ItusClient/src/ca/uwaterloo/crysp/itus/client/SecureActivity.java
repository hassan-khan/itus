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

package ca.uwaterloo.crysp.itus.client;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.EditText;
import ca.uwaterloo.crysp.itus.Itus;
import ca.uwaterloo.crysp.itus.measurements.EventType;

/**
 * The SecureActivity class that is supposed to be the parent class of
 * the activity of user app
 * @author Aaron Atwater
 * @author Hassan Khan
 *
 */
public class SecureActivity extends Activity {
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		Itus.getItus().getDispatcher().procEvent(EventType.TOUCH_INPUT, ev);
		return super.dispatchTouchEvent(ev);
	}

	/*HK: Newer Android APIs have stopped treating soft events through
	 * these keyEvent classes. This will not work anymore. Should register
	 * the EditText or other Android InputMethods using the following*/
	@Override
	public boolean dispatchKeyEvent (KeyEvent ev) {
		//Dispatcher.procEvent(EventType.KEY_INPUT, ev);
		return super.dispatchKeyEvent(ev);
	}
	
	/**
	 * This function should be called with the EditText handle that should be
	 * monitored for keystroke classification. Note that it currently only 
	 * supports English text
	 * @param et EditText instance that should be monitored for keystroke 
	 * classification
	 * @return True if successful
	 */
	public static boolean setEditText(EditText et) {
		if (!(et instanceof EditText))
			return false;
		et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {           
            }

            @Override
            public void afterTextChanged(Editable s) {
            	Itus.getItus().getDispatcher().procEvent(EventType.KEY_INPUT, s.toString());
            }
        });
		return true;
	}
}