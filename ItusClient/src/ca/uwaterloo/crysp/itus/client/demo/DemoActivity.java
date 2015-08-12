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
package ca.uwaterloo.crysp.itus.client.demo;

import java.util.ArrayList;

import ca.uwaterloo.crysp.itus.Parameters;
import ca.uwaterloo.crysp.itus.client.R;
import ca.uwaterloo.crysp.itus.client.SecureActivity;
import ca.uwaterloo.crysp.itus.client.measurements.TouchEventLive;
import ca.uwaterloo.crysp.itus.client.storage.PermanentStorageAndroid;
import ca.uwaterloo.crysp.itus.client.utils.AndroidPatternWrapper;
import ca.uwaterloo.crysp.itus.client.utils.SharedPreferenceWrapper;
import ca.uwaterloo.crysp.itus.machinelearning.ClassifierState;
import ca.uwaterloo.crysp.itus.prefabs.Touchalytics;
import ca.uwaterloo.crysp.itus.storage.BinLabel;
import ca.uwaterloo.crysp.itus.storage.DataStorage;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.TextView;

/**
 * A demo activity for touch behaviour based IA 
 * @author Aaron Atwater
 * @author Hassan Khan
 *
 */
public class DemoActivity extends SecureActivity {

	Touchalytics touchalytics;
	ArrayList<Integer> localScore;
	final int scoreWinSize = 7; 
	AndroidPatternWrapper apw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_demo);
        
        Parameters.setConfigMode(new PermanentStorageAndroid());
        apw = new AndroidPatternWrapper(this, this);
        if (!SharedPreferenceWrapper.getBoolean(this, "pattern_configured", 
        		false)) {
        	apw.configurePattern();
        	SharedPreferenceWrapper.saveBoolean(this, "pattern_configured", 
        			true);
        }
        Parameters.setPermanentStorageInstance(new PermanentStorageAndroid());
        localScore = new ArrayList<Integer>();
        TouchEventLive touchEventLive = new TouchEventLive();
        touchalytics = new Touchalytics(touchEventLive);
        touchalytics.start();
    }

    @Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
    	StringBuilder strScore = new StringBuilder();
    	TextView textView = (TextView) findViewById(R.id.itus_state);
    	if(touchalytics.getClassifier().getState() == ClassifierState.NOT_TRAINED) {
    		textView.setText("Training");
        	textView = (TextView) findViewById(R.id.training_threshold);
        	textView.setText(String.valueOf(Parameters.getTrainingThreshold()));
        	
        	textView = (TextView) findViewById(R.id.training_size);
        	textView.setText(String.valueOf(DataStorage.binSize(
        			BinLabel.BIN_TRAIN)));

    	}
    	else {
    		textView.setText("Classification");
        	textView = (TextView) findViewById(R.id.classification_score);
        	ArrayList<Integer> scores = touchalytics.getPastScores();
        	for (int score : scores)
        		localScore.add(score);
        	while(localScore.size() > this.scoreWinSize)
        		localScore.remove(0);
        	if (localScore.size() > 0)
        		strScore.append(" | ");
        	
        	int scoreSum = 0;
        	for (int score : localScore) {
            	strScore.append(score);
            	strScore.append(" | ");
            	if (score == 1)
            		scoreSum++;
        	}
        	if (localScore.size() == scoreWinSize & scoreSum < scoreWinSize/2) {
        		apw.comparePattern();
        		localScore.clear();
        	}
        	textView.setText(strScore.toString());
    	}
		return super.dispatchTouchEvent(ev);
	}
    
    //11572; 6555
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.demo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
