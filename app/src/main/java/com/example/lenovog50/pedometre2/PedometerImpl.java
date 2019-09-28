package com.example.lenovog50.pedometre2;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import static android.content.Context.SENSOR_SERVICE;
import static com.example.lenovog50.pedometre2.Pedometer.numSteps;

/**
 * Created by lenovo g50 on 26/11/2018.
 */

public class PedometerImpl implements PedometerInterface,SensorEventListener,StepListener {

    private SensorManager sensorManager;
    private Sensor accel;
    private StepDetector simpleStepDetector;
    private OnNewStepListener onNewStepListener;
    private int step;
    private float distanceTraveled;
    private float caloriesBurnt;
    public static boolean start=false;

    @Override
    public void startPedometer(Context context) {
        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void setNewStepListener(OnNewStepListener onNewStepListener) {
        this.onNewStepListener = onNewStepListener;
    }

    @Override
    public void stopPedometer(Context context) {
        sensorManager.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void step(long timeNs) {
        {
            step++;
            distanceTraveled+=70;
            caloriesBurnt+=0.03f;
            if (onNewStepListener != null) {
                onNewStepListener.onNewStep(step,distanceTraveled,caloriesBurnt);
            }

        }
    }
}
