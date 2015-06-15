# itus
Itus: An Implicit Authentication Framework for Android

Refactored source for Itus. Itus Oracle is ported to Java.
ItusClient contains a demo activity to show usage on the device.
ItusOracle contains Oracle.java that demonstrates DatasetPartitioner and RunConfigurations constructs of Java

Todo:
1- Keystroke event handlers are broken since Android now treats soft events separately and they do not fall under the KeyEvent Handlers of secure Activity
2- Itus users are supposed to save and retreive the model files using the PermanentStorageAndroid class. This needs to be automated
3- Java version of Itus Oracle does not support information gain calculations. Need to happen for the base Oracle class

