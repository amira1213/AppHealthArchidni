package com.example.lenovog50.pedometre2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import static com.example.lenovog50.pedometre2.Pedometer.TvSteps;
import static com.example.lenovog50.pedometre2.Pedometer.numSteps;
import static com.example.lenovog50.pedometre2.PedometerImpl.start;

public class TestActivity extends AppCompatActivity {

    PedometerInterface pedometerInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        pedometerInterface = new PedometerImpl();
        pedometerInterface.startPedometer(this);
        pedometerInterface.setNewStepListener(new PedometerInterface.OnNewStepListener() {
            @Override
            public void onNewStep(int stepCount,float distanceTraveled,float calorieBurnt) {
                if(start)
                Toast.makeText(TestActivity.this,"new step"+ TvSteps.getText(),Toast.LENGTH_LONG).show();
            }
        });
    }



        public void Weather(View view)
        {  //TvSteps.setText("grrr");
            Intent intent=new Intent(TestActivity.this,Weather.class);
            startActivity(intent);
        }


    public void Pedometer(View view) {
        Intent intent=new Intent(TestActivity.this,Pedometer.class);
        startActivity(intent);
    }
}
