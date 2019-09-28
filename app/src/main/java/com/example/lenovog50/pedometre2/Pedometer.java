package com.example.lenovog50.pedometre2;



import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.example.lenovog50.pedometre2.PedometerImpl.start;

public class Pedometer extends AppCompatActivity implements SensorEventListener, StepListener {
    public static TextView textView,TvSteps;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel,temp;
    public static int numSteps=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer);

        TvSteps.setText("" + numSteps);

        // Get an instance of the SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        temp=sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);

        TvSteps = (TextView) findViewById(R.id.tv_steps);
        Button BtnStart = (Button) findViewById(R.id.btn_start);
        Button BtnStop = (Button) findViewById(R.id.btn_stop);
        Button BtnReset = (Button) findViewById(R.id.btn_reset);



        BtnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
          start=true;
                numSteps = 1;
                sensorManager.registerListener(Pedometer.this, accel, SensorManager.SENSOR_DELAY_NORMAL);

            }
        });


        BtnStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                start=false;
                numSteps=0;
                sensorManager.unregisterListener(Pedometer.this);

            }
        });


        BtnReset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
             start=true;
                numSteps = 1;
                sensorManager.registerListener(Pedometer.this, accel, SensorManager.SENSOR_DELAY_NORMAL);

            }
        });

    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {


        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }


    }

    @Override
    public void step(long timeNs) {
if(start) {
    numSteps++;
    TvSteps.setText("" + numSteps);
}

    }


    

}
