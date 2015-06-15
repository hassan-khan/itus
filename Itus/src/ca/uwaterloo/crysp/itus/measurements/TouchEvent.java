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

import ca.uwaterloo.crysp.itus.FeatureVector;
import ca.uwaterloo.crysp.itus.machinelearning.ClassLabel;
import ca.uwaterloo.crysp.itus.utils.ArrayUtil;
import ca.uwaterloo.crysp.itus.utils.ComplexNumbers;

/**
 * TouchEvent Measurement class. 
 * @author Aaron Atwater
 * @author Hassan Khan
 *
 */
public abstract class TouchEvent extends Measurement {

	/**
	 * Touch Point class. stores the data retrieved from TouchEvent 
	 * @author Aaron Atwater
	 * @author Hassan Khan
	 */
	public class TouchPoint {
		public long eventTimestamp;
		public double xVal, yVal, pressure, width, orientation;
	}
	
	/**
	 * time of last swipe
	 */
	protected static double lastSwipeTimestamp = 0;
	
	/**
	 * List of all the features supported
	 */
	public static final String featureList [] = {
			"Start X",
			"Start Y",
			"End X",
			"End Y",
			"Duration in ms",
			"Inter-stroke time in ms",
			"Direct end-to-end distance",
			"Mean Resultant Length",
			"20% perc. pairwise velocity",
			"50% perc. pairwise velocity",
			"80% perc. pairwise velocity",
			"20% perc. pairwise acceleration",
			"50% perc. pairwise acceleration",
			"80% perc. pairwise acceleration",
			"Direction of End-to-End line",
			"Median velocity of last 3 points",
			"Length of Trajectory",
		    "Average Velocity",
		    "Median Acceleration at first 5 points",
		    "Pressure in the middle of stroke",
		    "Midstroke area covered",
		    "Phone Orientation",
		    "Direction Flag",
		    "Ratio of Direct Distance and Traj. Length",
		    "Average Direction of ensemble Pairs",
		    "Largest Deviation from end-end Line",
		    "20% perc. Deviation from end-to-end line",
		    "50% perc. Deviation from end-to-end line",
			"80% perc. Deviation from end-to-end line",
			"First touch area",
			"First moving direction",
			"Average moving direction",
			"Average moving curvature",
			"Average touch area",
			"Max area",
	};
	/**
	 * Number of features currently supported by this measurement
	 */
	public static final int NUM_FEATURES = featureList.length;
	
	@Override
	public List<FeatureVector> defaultPositiveInstances() {
		System.out.println("In default positiveInstances");
		double [][] data = {
				{868.00,591.00,891.00,1611.00,275.00,1757.00,1020.26,0.02,2.03,4.62,5.62,-0.07,-0.00,0.23,1.55,6.46,1022.37,3.72,-0.20,1.00,0.02,0.00,1.00,1.00,3.14,1019.34,44.18,430.00,874.60,0.02,-1.57,-3.96,-2.00,-1.98,42.00},
				{800.00,609.00,826.00,1629.00,297.00,659.00,1020.33,0.02,1.62,3.58,5.21,-0.06,0.00,0.26,1.55,7.02,1017.76,3.43,-0.20,1.00,0.02,0.00,1.00,1.00,3.14,1014.53,69.33,470.16,875.00,0.02,-1.57,-3.99,-2.01,-1.98,0.00},
				{820.00,615.00,756.00,1634.00,275.00,681.00,1021.01,0.02,1.75,4.25,5.72,-0.16,0.00,0.09,1.63,8.37,1023.73,3.72,-0.88,1.00,0.02,0.00,1.00,1.00,0.00,1020.70,68.91,465.00,910.10,0.02,-1.57,-3.88,-2.09,-1.98,42.00},
				{866.00,642.00,822.00,1555.00,418.00,824.00,914.06,0.02,0.66,2.64,4.95,-0.07,-0.00,0.06,1.62,8.04,1095.94,2.62,-0.26,1.00,0.02,0.00,1.00,0.83,3.14,1017.90,47.14,471.14,976.46,0.02,-1.57,-3.49,-2.00,-1.98,61.00},
				{781.00,615.00,759.00,1652.00,319.00,615.00,1037.23,0.02,1.80,3.43,5.38,-0.14,0.01,0.33,1.59,6.39,1041.56,3.27,-0.40,1.00,0.02,0.00,1.00,1.00,3.14,1038.11,51.57,439.00,902.14,0.02,-1.57,-3.92,-2.06,-1.98,48.00},
				{829.00,541.00,707.00,1615.00,363.00,769.00,1080.91,0.02,0.85,2.99,4.93,-0.06,0.00,0.12,1.68,6.08,1118.39,3.08,-0.28,1.00,0.02,0.00,1.00,0.97,3.14,1085.00,89.00,584.09,1042.00,0.02,-1.57,-3.66,-2.01,-1.98,53.00},
				{854.00,569.00,763.20,1708.00,342.00,694.00,1142.61,0.02,0.83,3.62,5.43,-0.10,0.01,0.19,1.65,6.46,1165.83,3.41,-0.35,1.00,0.02,0.00,1.00,0.98,3.14,1139.00,28.31,437.00,1006.40,0.02,-1.57,-3.72,-2.00,-1.98,51.00},
				{842.00,538.00,862.00,1746.00,274.00,624.00,1208.17,0.02,1.53,5.47,6.79,-0.03,0.01,0.14,1.55,8.28,1201.86,4.39,-0.43,1.00,0.02,0.00,1.00,1.01,3.14,1197.17,9.17,367.00,926.43,0.02,-1.57,-3.77,-2.00,-1.98,12.00},
				{824.00,604.00,787.00,1682.00,296.00,681.00,1078.63,0.02,1.51,4.52,5.83,-0.13,0.01,0.15,1.61,8.27,1080.65,3.65,-0.61,1.00,0.02,0.00,1.00,1.00,3.14,1077.23,51.00,493.14,959.00,0.02,-1.57,-3.86,-2.00,-1.98,45.00},
				{861.00,526.00,788.00,1751.00,329.00,670.00,1227.17,0.02,2.08,4.07,5.35,-0.09,0.04,0.19,1.63,6.18,1228.98,3.74,-0.32,1.00,0.02,0.00,1.00,1.00,3.14,1224.15,59.68,511.21,1053.36,0.02,-1.57,-3.87,-2.00,-1.98,50.00},
				{842.00,638.00,773.52,1730.92,258.00,599.00,1095.06,0.03,2.46,4.71,5.86,-0.08,0.02,0.22,1.63,6.40,1085.69,4.21,-0.18,1.00,0.02,0.00,1.00,1.01,3.14,1081.00,51.11,423.06,860.49,0.02,-1.57,-3.82,-2.00,-1.98,3.00},
				{816.00,621.00,773.00,1733.41,289.00,668.00,1113.24,0.02,2.18,4.77,5.53,-0.10,0.00,0.20,1.61,9.33,1109.24,3.84,-0.22,1.00,0.02,0.00,1.00,1.00,3.14,1106.00,38.20,427.98,897.96,0.02,-1.57,-3.75,-2.00,-1.98,43.00},
				{835.00,592.00,768.38,1799.54,299.00,669.00,1209.38,0.02,2.10,4.90,6.14,-0.04,0.01,0.16,1.63,6.60,1208.05,4.04,-0.12,1.00,0.02,0.00,1.00,1.00,3.14,1203.00,17.00,446.17,958.64,0.02,-1.57,-3.78,-2.00,-1.98,44.00},
				{875.00,555.00,808.00,1667.00,285.00,678.00,1114.02,0.02,1.75,4.56,5.88,-0.05,0.01,0.22,1.63,7.59,1105.66,3.88,-0.29,1.00,0.02,0.00,1.00,1.01,3.14,1099.78,36.80,432.44,925.87,0.02,-1.57,-3.82,-2.00,-1.98,43.00},
				{857.00,549.00,799.43,1722.05,290.00,642.00,1174.46,0.02,2.45,4.65,5.79,-0.09,0.00,0.13,1.62,7.14,1168.86,4.03,-0.53,1.00,0.02,0.00,1.00,1.00,3.14,1162.00,22.00,415.54,901.35,0.02,-1.57,-3.76,-2.00,-1.98,10.00},
				{850.00,583.00,816.02,1736.88,255.00,668.00,1154.38,0.03,3.01,5.04,6.09,-0.08,0.00,0.31,1.60,7.20,1151.72,4.52,-0.20,1.00,0.02,0.00,1.00,1.00,3.14,1146.00,33.41,408.01,884.41,0.02,-1.57,-3.82,-2.00,-1.98,38.00},
				{856.00,624.00,848.00,1733.00,231.00,712.00,1109.03,0.03,3.18,5.75,6.72,-0.06,0.04,0.31,1.58,7.92,1093.97,4.74,-0.11,1.00,0.02,0.00,1.00,1.01,3.14,1081.23,24.63,357.26,814.00,0.02,-1.57,-3.80,-2.00,-1.98,35.00},
				{869.00,611.00,799.07,1708.77,242.00,692.00,1099.99,0.03,3.05,4.92,6.10,-0.08,0.00,0.19,1.63,7.41,1105.71,4.57,-0.18,1.00,0.02,0.00,1.00,0.99,2.68,1097.00,53.63,418.00,871.54,0.02,-2.03,-3.82,-2.00,-1.98,36.00},
				{868.00,648.00,886.00,1778.00,253.00,670.00,1130.14,0.03,2.46,4.77,6.22,-0.11,0.01,0.23,1.55,7.17,1133.39,4.48,-0.34,1.00,0.02,0.00,1.00,1.00,3.14,1125.22,29.78,355.52,841.02,0.02,-1.57,-3.86,-2.00,-1.98,39.00},
				{882.00,607.00,791.00,1712.00,219.00,626.00,1108.74,0.03,2.40,5.49,7.24,-0.05,0.08,0.45,1.65,8.01,1047.37,4.78,-0.25,1.00,0.02,0.00,1.00,1.06,3.14,1039.24,33.27,324.00,751.80,0.02,-1.57,-3.74,-2.00,-1.98,33.00}};

		List<FeatureVector> positiveInstances = new ArrayList<FeatureVector>();
		for (int i = 0; i < data.length; i++) {
			FeatureVector fv = new FeatureVector(data[i], ClassLabel.POSITIVE);
			positiveInstances.add(fv);
		}
		return positiveInstances;
	}

	@Override
	public List<FeatureVector> defaultNegativeInstances() {
		double [][] data = {
				{536.00,612.00,522.00,1664.00,285.00,5243.00,1052.09,0.02,1.83,4.18,5.65,-0.06,0.02,0.29,1.58,6.79,1050.69,3.69,-0.23,1.00,0.02,0.00,1.00,1.00,3.14,1043.34,46.20,471.23,895.80,0.02,-1.57,-3.87,-2.00,-1.98,43.00},
				{511.00,534.00,528.00,1624.00,319.00,627.00,1090.13,0.02,0.91,4.11,6.52,-0.11,0.00,0.09,1.56,8.38,1103.53,3.46,-0.30,1.00,0.02,0.00,1.00,0.99,-2.36,1090.00,6.61,407.00,983.29,0.02,-0.78,-3.65,-2.00,-1.98,48.00},
				{480.00,536.00,479.00,1582.00,308.00,626.00,1046.00,0.02,1.28,3.77,5.82,-0.11,-0.00,0.14,1.57,6.78,1049.52,3.41,-0.33,1.00,0.02,0.00,1.00,1.00,1.57,1046.17,96.38,607.90,992.93,0.02,-3.14,-3.99,-2.10,-1.98,46.00},
				{179.00,843.00,157.00,1714.00,176.00,3972.00,871.28,0.04,2.36,5.39,7.54,-0.20,-0.00,0.08,1.60,8.23,876.91,4.98,-0.48,1.00,0.02,0.00,1.00,0.99,1.49,869.94,133.58,566.00,821.03,0.02,-3.22,-3.97,-2.00,-1.98,27.00},
				{175.00,647.00,158.16,1720.31,253.00,615.00,1073.44,0.03,2.28,4.56,6.33,-0.08,0.03,0.30,1.59,8.81,1079.90,4.27,-0.20,1.00,0.02,0.00,1.00,0.99,1.57,1073.00,76.34,504.60,949.03,0.02,-3.14,-4.00,-2.00,-1.98,38.00},
				{200.00,640.00,167.00,1809.00,264.00,571.00,1169.47,0.03,2.60,5.18,7.19,-0.16,-0.00,0.40,1.60,8.57,1171.31,4.44,-0.27,1.00,0.02,0.00,1.00,1.00,3.14,1166.81,30.00,499.75,1027.00,0.02,-1.57,-3.84,-2.00,-1.98,40.00},
				{967.00,1628.00,177.00,1657.00,285.00,2253.00,790.53,0.02,0.56,3.55,6.09,-0.15,-0.00,0.13,3.10,8.54,912.36,3.20,-0.61,1.00,0.02,0.00,2.00,0.87,3.14,29.00,1.82,8.00,23.40,0.02,-1.57,-2.37,-2.00,-1.98,44.00},
				{1004.00,1629.00,172.00,1647.00,318.00,593.00,832.19,0.02,0.77,2.17,4.97,-0.05,0.00,0.10,3.12,6.05,852.41,2.68,-0.34,1.00,0.02,0.00,2.00,0.98,-2.68,15.14,3.48,11.00,13.06,0.02,-1.11,-2.57,-1.99,-1.98,48.00},
				{964.00,1605.00,203.00,1594.00,274.00,648.00,761.08,0.02,0.64,3.22,5.25,-0.07,0.01,0.09,-3.13,5.89,807.75,2.95,-0.22,1.00,0.02,0.00,2.00,0.94,3.14,11.00,1.00,5.00,9.54,0.02,-1.57,-2.30,-2.00,-1.98,42.00},
				{951.00,1616.00,110.00,1719.00,285.00,615.00,847.28,0.02,0.94,3.48,5.60,-0.08,0.00,0.18,3.02,6.01,894.34,3.14,-0.24,1.00,0.02,0.00,2.00,0.95,3.14,110.17,5.75,59.69,108.13,0.02,-1.57,-2.41,-2.00,-1.98,43.00},
				{156.00,488.00,264.00,1604.00,352.00,4356.00,1121.21,0.02,2.12,3.45,5.35,-0.12,0.02,0.16,1.47,6.56,1131.56,3.21,-0.55,1.00,0.03,0.00,1.00,0.99,0.00,1114.20,74.18,448.85,938.47,0.02,-1.57,-4.05,-2.00,-1.98,53.00},
				{175.00,583.00,314.00,1463.00,351.00,735.00,890.91,0.02,0.55,2.52,4.16,-0.07,0.01,0.14,1.41,6.73,910.14,2.59,-0.37,1.00,0.03,0.00,1.00,0.98,0.00,882.00,14.43,428.00,823.88,0.02,1.57,-3.71,-2.00,-1.97,53.00},
				{157.00,607.00,223.00,1382.00,352.00,704.00,777.81,0.02,0.41,2.93,4.71,-0.10,0.00,0.33,1.49,5.01,801.49,2.28,-0.43,1.00,0.03,0.00,1.00,0.97,0.00,774.58,78.59,489.24,763.80,0.02,-1.57,-3.81,-2.00,-1.97,51.00},
				{194.00,1715.00,934.00,1705.00,264.00,1943.00,740.07,0.03,0.71,3.32,4.84,-0.07,-0.00,0.22,-0.01,8.92,744.97,2.82,-0.29,1.00,0.02,0.00,3.00,0.99,0.00,16.37,2.00,8.16,14.59,0.02,1.57,-1.32,-2.00,-1.98,40.00},
				{222.00,1742.00,934.15,1696.28,244.00,563.00,713.62,0.03,0.92,3.67,5.32,-0.15,0.00,0.31,-0.06,6.70,720.82,2.95,-1.22,1.00,0.02,0.00,3.00,0.99,0.00,47.00,4.01,28.64,46.00,0.02,1.57,0.17,-2.00,-1.98,36.00},
				{149.00,1727.00,910.00,1633.00,274.00,612.00,766.78,0.02,0.88,3.15,4.75,-0.10,0.02,0.16,-0.12,5.86,768.04,2.80,-0.23,1.00,0.02,0.00,3.00,1.00,-0.79,93.01,10.45,36.66,82.84,0.02,0.78,0.31,-1.97,-1.98,41.00},
				{79.00,1717.00,905.00,1704.00,274.00,626.00,826.10,0.02,1.31,3.43,4.74,-0.12,-0.00,0.10,-0.02,8.59,828.49,3.02,-0.39,1.00,0.02,0.00,3.00,1.00,-1.57,16.00,7.31,10.00,14.39,0.02,0.00,-0.97,-2.00,-1.98,42.00},
				{949.00,585.00,797.00,1457.00,209.00,112684116.00,885.15,0.03,1.65,5.29,7.03,-0.05,0.10,0.55,1.74,8.05,894.10,4.28,-0.30,1.00,0.02,0.00,1.00,0.99,3.14,859.71,10.27,223.00,620.53,0.02,-1.57,-3.65,-2.00,-1.98,32.00},
				{882.00,619.00,795.00,1496.00,241.00,736.00,881.30,0.03,1.23,4.48,6.82,-0.06,0.04,0.42,1.67,7.78,889.30,3.69,-0.45,1.00,0.03,0.00,1.00,0.99,-1.57,862.50,2.83,170.21,583.55,0.03,0.00,-3.66,-2.00,-1.98,11.00},
				{908.00,575.00,779.31,1409.20,231.00,814.00,844.07,0.03,1.11,4.36,5.43,-0.03,0.02,0.31,1.72,6.44,858.08,3.71,-0.20,1.00,0.02,0.00,1.00,0.98,3.14,832.00,13.00,228.27,612.91,0.02,-1.57,-3.65,-2.00,-1.98,34.00}};
		
		List<FeatureVector> negativeInstances = new ArrayList<FeatureVector>();
		for (int i = 0; i < data.length; i++) {
			FeatureVector fv = new FeatureVector(data[i], ClassLabel.NEGATIVE);
			negativeInstances.add(fv);
		}
		
		return negativeInstances;
	}
	
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
	
	/**
	 * Takes an {@code ArrayList} of {@code TouchPoints} and returns 
	 * FeatureVector computed from {@code touchPoints}
	 * @param touchPoints
	 * @return FeatureVector computed from {@code touchPoints}
	 */
	protected FeatureVector computeFeatureVector(
			ArrayList<TouchPoint> touchPoints) {
		FeatureVector fv = new FeatureVector(NUM_FEATURES);
		/*since this is a live collection, 
		 * set to positive instance for training*/
		fv.setClassLabel(ClassLabel.POSITIVE);
		
		/*Feature List*/
		/**Start X (F1)*/
		/**Start Y (F2)*/
		/**End X (F3)*/
		/**End Y (F4)*/
		/**Duration in ms (F5)*/
		/**Inter-stroke time in ms (F6)*/
		/**Direct end-to-end distance (F7)*/
		/**Mean Resultant Length (F8)*/
		/**20% perc. pairwise velocity (F9)*/
		/**50% perc. pairwise velocity (F10)*/
		/**80% perc. pairwise velocity (F11)*/
		/**20% perc. pairwise acceleration (F12)*/
		/**50% perc. pairwise acceleration (F13)*/
		/**80% perc. pairwise acceleration (F14)*/
		/**Direction of End-to-End line (F15)*/
		/**Median velocity of last 3 points (F16)*/
		/**Length of Trajectory (F17)*/
	    /**Average Velocity (F18)*/
	    /**Median Acceleration at first 5 points (F19)*/
	    /**Pressure in the middle of stroke (F20)*/
	    /**Midstroke area covered (F21)*/
	    /**Phone Orientation (F22)*/
	    /**Direction Flag (F23)*/
	    /**Ratio of Direct Distance and Traj. Length (F24)*/
	    /**Average Direction of ensemble Pairs (F25)*/
	    /**Largest Deviation from end-end Line (F26)*/
	    /**20% perc. Deviation from end-to-end line (F27)*/
	    /**50% perc. Deviation from end-to-end line (F28)*/
		/**80% perc. Deviation from end-to-end line (F29)*/
		/**First touch area: touch area of first touch co-ord (F30)*/
		/**First moving direction: angle b/w horizontal line and this (F31)*/
		/**Average moving direction: Avg val. of moving direction of 
		* n-1 pts (F32)*/
		/**Average moving curvature: Avg val. of moving curvature of n-1 pts
		* 					b/w consecutive pts ABC it is angle ABC (F33)*/
		/**Average touch area: Avg. of all touch areas (F34)*/
		/**Max area: Index of Max-area pt./Total pts (F35)*/
		
		int numPoints = touchPoints.size();
		/*F 1-7 are pretty straightforward*/
		fv.set(0, touchPoints.get(0).xVal); /*startX*/
		fv.set(1, touchPoints.get(0).yVal); /*startY*/
		fv.set(2, touchPoints.get(numPoints - 1).xVal); /*endX*/
		fv.set(3, touchPoints.get(numPoints - 1).yVal); /*endY*/
		fv.set(4, touchPoints.get(numPoints - 1).eventTimestamp - 
				touchPoints.get(0).eventTimestamp); /*duration*/
		fv.set(5, touchPoints.get(numPoints - 1).eventTimestamp -
				TouchEvent.lastSwipeTimestamp); /*interStrokeTime*/
		fv.set(6, Math.sqrt(Math.pow(fv.get(2) - fv.get(0), 2) + 
				  Math.pow(fv.get(3) - fv.get(1), 2))); /*directDistance*/
		
		/*Calculate pairwise displacement, velocity and acceleration*/
		double xDisplacement[] = new double[numPoints - 1];
		double yDisplacement[] = new double[numPoints - 1];
		double tDisplacement[] = new double[numPoints - 1];
		double pairwAngle[] = new double[numPoints - 1];
		double pairwDistance[] = new double[numPoints - 1];
		double pairwVelocity[] = new double[numPoints - 1];
		double pairwAcceleration[] = new double[numPoints - 2];
		
		for (int i = 0; i < numPoints - 2; i++) {
			xDisplacement[i] = touchPoints.get(i+1).xVal - 
					touchPoints.get(i).xVal;
			yDisplacement[i] = touchPoints.get(i+1).yVal - 
					touchPoints.get(i).yVal;
			tDisplacement[i] = touchPoints.get(i+1).eventTimestamp - 
					touchPoints.get(i).eventTimestamp;
			pairwAngle[i] =  Math.atan2(yDisplacement[i], xDisplacement[i]);
			pairwDistance[i] =  Math.sqrt(Math.pow(xDisplacement[i], 2) + 
									  Math.pow(yDisplacement[i], 2));
			if (tDisplacement[i] == 0) 
				pairwVelocity[i] = 0;
			else 
				pairwVelocity[i] = pairwDistance[i]/tDisplacement[i];
		}
		/*correct pairwVelocity by setting '0' to maxVelocity*/
		double maxVelocity = ArrayUtil.max(pairwVelocity);
		for (int i = 0; i < pairwVelocity.length - 1; i++) 
			if (pairwVelocity[i] == 0)
				pairwVelocity[i] = maxVelocity;
		
		for (int i = 0; i < pairwVelocity.length - 2; i++) {
			pairwAcceleration[i] = pairwVelocity[i+1] - pairwVelocity[i];
			if (tDisplacement[i] == 0) 
				pairwAcceleration[i] = 0;
			else 
				pairwAcceleration[i] = pairwAcceleration[i]/tDisplacement[i];
		}
		/*calculate the max values for acceleration and replace
		 * values for which tDisplacement = 0 to max*/
		double maxAcceleration = 0;

		maxAcceleration = ArrayUtil.max(pairwAcceleration);
		
		for (int i = 0; i < pairwAcceleration.length - 1; i++) 
			if (pairwAcceleration[i] == 0)
				pairwAcceleration[i] = maxAcceleration;	
		
		/*F8-15*/
		fv.set(7, ComplexNumbers.circ_r(pairwAngle)); /*meanResultantLength*/
		fv.set(8, ArrayUtil.percentile(pairwVelocity, 0.20)); /*velocity20*/
		fv.set(9, ArrayUtil.percentile(pairwVelocity, 0.50)); /*velocity50*/
		fv.set(10, ArrayUtil.percentile(pairwVelocity, 0.80)); /*velocity80*/
		fv.set(11, ArrayUtil.percentile(pairwAcceleration, 0.20)); /*acceleration20*/
		fv.set(12, ArrayUtil.percentile(pairwAcceleration, 0.50)); /*acceleration50*/
		fv.set(13, ArrayUtil.percentile(pairwAcceleration, 0.80)); /*acceleration80*/
		fv.set(14, Math.atan2(fv.get(3) - fv.get(1), 
				fv.get(2) - fv.get(0))); /*lineDirection*/
		
		/*F16 last 3 velocity points*/
		double velocityPoints [] = {pairwVelocity[pairwVelocity.length - 1],
				pairwVelocity[pairwVelocity.length-2],
				pairwVelocity[pairwVelocity.length-3]};
		 fv.set(15, ArrayUtil.percentile(velocityPoints, 0.50)); /*medVelocity*/
		
		/*F17-18: trajectoryLength & averageVelocity*/
		double temp = 0;
		 
		for (int i = 0; i < pairwDistance.length; i++) {
			 temp += pairwDistance[i]; /*trajectoryLength*/
		}
		fv.set(16, temp);
		
		if(fv.get(4) == 0)
			fv.set(17, 0);
		else
			fv.set(17, fv.get(16)/fv.get(4));
		
		/*F19 - First 5 acceleration points; medianAcceleration*/
		double accelerationPoints [] = {pairwAcceleration[0],
				pairwAcceleration[1], pairwAcceleration[2],
				pairwAcceleration[3],pairwAcceleration[4],
				pairwAcceleration[5]};
		fv.set(18, ArrayUtil.percentile(accelerationPoints, 0.50));
		
		/*F20-22: midPressure, midArea, phoneOrientation*/
		fv.set(19, touchPoints.get(numPoints/2).pressure);
		fv.set(20, touchPoints.get(numPoints/2).width);
		fv.set(21, touchPoints.get(numPoints/2).orientation);
		
		/*F23 - Direction Flag. up, down, left, right are 0,1,2,3*/
		fv.set(22, 1);
		double xDiff = fv.get(2) - fv.get(0);
		double yDiff = fv.get(3) - fv.get(1);
		if (Math.abs(xDiff) > Math.abs(yDiff))
			if (xDiff < 0)
				fv.set(22, 2); //left
			else
				fv.set(22, 3); //right
		else
			if (yDiff < 0)
				fv.set(22, 0); //up
		
		/*F24-25: distToTrajRatio; averageDirection*/
		if (fv.get(16) == 0)
			fv.set(23, 0);
		else
			fv.set(23, fv.get(6)/fv.get(16));
		
		fv.set(24 ,ComplexNumbers.circ_mean(pairwAngle));
		
		/*F26-29 - Largest/20%/50%/80% deviation from end-to-end line*/
		double xVek [] = new double[numPoints];
		double yVek [] = new double[numPoints];
		for (int i = 0; i < numPoints - 1; i++) {
			xVek[i] = touchPoints.get(i).xVal - fv.get(0);
			yVek[i] = touchPoints.get(i).yVal - fv.get(1);
		}
		double perVek[] = {yVek[yVek.length-1], xVek[xVek.length-1] - 1, 0};
		temp = Math.sqrt(Math.pow(perVek[0], 2) + Math.pow(perVek[1], 2));
		if (temp == 0)
			perVek[0] = perVek[1] = perVek[2]  = 0;
		else {
			perVek[0] /= temp;
			perVek[1] /= temp;
			perVek[2] /= temp;
		}
		
		double absProj [] = new double[numPoints];
		for (int i = 0; i < numPoints - 1; i++) 
			absProj[i] = Math.abs(xVek[i] * perVek[0] + yVek[i] * perVek[1]);
		fv.set(25, ArrayUtil.max(absProj));
		fv.set(26, ArrayUtil.percentile(absProj, 0.2));
		fv.set(27, ArrayUtil.percentile(absProj, 0.5));
		fv.set(28, ArrayUtil.percentile(absProj, 0.8));
		
		/*F30 First touch area*/
		fv.set(29, touchPoints.get(0).width);
		/*F31 First moving direction*/
		fv.set(30, movingDirection(touchPoints.get(0), touchPoints.get(1)));
		/*F32-35 Avg. mov. direction, Avg. mov. curvature, Avg. area, Max area*/
		double movDir = 0, movCurv = 0, avgArea = 0, maxArea = -1;
		for (int i = 0 ; i < touchPoints.size(); i++) {
			if (i < touchPoints.size() - 2)
				movDir += movingDirection(touchPoints.get(i), 
						touchPoints.get(i+1));
			if (i < touchPoints.size() - 2)
				movCurv = movingCurvature(touchPoints.get(i), 
						touchPoints.get(i+1), touchPoints.get(i+2));
			avgArea += touchPoints.get(i).width;
			if (touchPoints.get(i).width > maxArea) {
				maxArea = touchPoints.get(i).width;
				fv.set(34, i);
			}
		}
		fv.set(31, movDir/touchPoints.size()-1);
		fv.set(32, movCurv/touchPoints.size()-2);
		fv.set(33, avgArea/touchPoints.size()-2);
		
		return fv;
	}
	private static double movingDirection(TouchPoint pt1, TouchPoint pt2) {
		double angleSelf = Math.atan2(pt1.yVal - pt2.yVal, 
				pt1.xVal - pt2.xVal);
		double angleHorizontal = Math.atan2(pt1.yVal , pt1.xVal - pt2.xVal);
		return angleSelf - angleHorizontal;
	}
	
	private static double movingCurvature(TouchPoint pt1, TouchPoint pt2, 
			TouchPoint pt3) {
		double anglePt1Pt2 = Math.atan2(pt1.yVal - pt2.yVal, 
				pt1.xVal - pt2.xVal);
		double anglePt2Pt3 = Math.atan2(pt2.yVal - pt3.yVal, 
				pt2.xVal - pt3.xVal);
		return anglePt1Pt2 - anglePt2Pt3; 
	}
}
