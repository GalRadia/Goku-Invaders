package com.example.gokuinvader.Utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.gokuinvader.Interfaces.TiltCallbacks;

public class TiltDetector {
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;
    private TiltCallbacks tiltCallbacks;
    private long timestamp = 0L;

    public TiltDetector(Context context, TiltCallbacks tiltCallbacks) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.tiltCallbacks = tiltCallbacks;
        initEventListener();
    }

    private void initEventListener() {
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];
                tiltDirectionCheck(x, y);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // pass
            }
        };
    }

    private void tiltDirectionCheck(float x, float y) {
        if (System.currentTimeMillis() - timestamp > 500) {
            timestamp = System.currentTimeMillis();
                if (x < -2) {
                    tiltCallbacks.tiltRight();
                }
                else if (x > 2) {
                    tiltCallbacks.tiltLeft();
                }
                if (y < -2) {
                    tiltCallbacks.tiltOutward();
                }
                else if (y > 2) {
                    tiltCallbacks.tiltInward();
                }

        }
    }

    public void start() {
        sensorManager.registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
        );
    }

    public void stop() {
        sensorManager.unregisterListener(
                sensorEventListener,
                sensor
        );
    }
}
