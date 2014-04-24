package com.example.shakersample;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.util.Log;

public class Shaker {
	private SensorManager sensorManager = null;
	private long lastShakeTimestamp = 0;
	private double threshold = 1.0d;
	private long gap = 0;
	private Shaker.Callback callback = null;
	public static final String TAG = "Shaker";
	
	public Shaker(Context ctxt, double threshold, long gap, Shaker.Callback shakerCallback) {
		this.threshold = threshold * threshold;
		this.threshold = this.threshold * SensorManager.GRAVITY_EARTH
				* SensorManager.GRAVITY_EARTH;
		this.gap = gap;
		this.callback = shakerCallback;

		sensorManager = (SensorManager) ctxt.getSystemService(Context.SENSOR_SERVICE);
		sensorManager.registerListener(listener,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_UI);
	}

	public void close() {
		sensorManager.unregisterListener(listener);
	}

	private void isShaking() {
		Log.i(TAG, "isShaking");
		long now = SystemClock.uptimeMillis();
		try {
			if (lastShakeTimestamp == 0) {
				lastShakeTimestamp = now;

				if (callback != null) {
					callback.shakingStarted();
				}
			} else {
				lastShakeTimestamp = now;
			}
		} catch (NullPointerException e) {

		}
	}

	private void isNotShaking() {
		Log.i(TAG, "isNotShaking");
		long now = SystemClock.uptimeMillis();

		if (lastShakeTimestamp > 0) {
			if (now - lastShakeTimestamp > gap) {
				lastShakeTimestamp = 0;

				if (callback != null) {
					callback.shakingStopped();
				}
			}
		}
	}

	public interface Callback {
		void shakingStarted();

		void shakingStopped();
	}

	private final SensorEventListener listener = new SensorEventListener() {
		@Override
		public void onSensorChanged(SensorEvent e) {
			if (e.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				
				Log.i(TAG, "0:" + e.values[0] + "	1:"
						+ e.values[1] + "		2:" + e.values[2]);
				
				double netForce = e.values[0] * e.values[0];
				netForce += e.values[1] * e.values[1];
				netForce += e.values[2] * e.values[2];

				if (threshold < netForce) {
					isShaking();
				} else {
					isNotShaking();
				}
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// unused
		}
	};
}