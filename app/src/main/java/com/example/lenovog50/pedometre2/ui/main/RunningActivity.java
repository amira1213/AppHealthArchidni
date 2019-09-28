package com.example.lenovog50.pedometre2.ui.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.lenovog50.pedometre2.ArchidniGoogleMap;
import com.example.lenovog50.pedometre2.Coordinate;
import com.example.lenovog50.pedometre2.LocationListener;
import com.example.lenovog50.pedometre2.PedometerImpl;
import com.example.lenovog50.pedometre2.PedometerInterface;
import com.example.lenovog50.pedometre2.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RunningActivity extends AppCompatActivity {

    PedometerInterface pedometerInterface;
    @BindView(R.id.text_steps)
    TextView stepsText;
    @BindView(R.id.text_calories)
    TextView caloriesText;
    @BindView(R.id.text_distance)
    TextView distanceText;
    @BindView(R.id.text_finish)
    TextView finishText;
    ArchidniGoogleMap archidniGoogleMap;

    private ArrayList<Coordinate> userCoordonates = new ArrayList<>();

    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);
        locationListener = new LocationListener(this);
        ButterKnife.bind(this);

        pedometerInterface = new PedometerImpl();
        pedometerInterface.startPedometer(this);
        pedometerInterface.setNewStepListener(new PedometerInterface.OnNewStepListener() {
            @Override
            public void onNewStep(int stepCount,float distanceTraveled,float caloriesBurnt) {
                stepsText.setText(stepCount+ " Pas");
                distanceText.setText((int)(distanceTraveled/100)+" metres");
                caloriesText.setText((int)caloriesBurnt+" Kcals brulées");
            }
        });
        MapFragment mapView = (MapFragment) getFragmentManager().findFragmentById(R.id.mapView);
        archidniGoogleMap = new ArchidniGoogleMap(this, mapView, new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                archidniGoogleMap.setMyLocationEnabled(true);
                archidniGoogleMap.trackUser();
                locationListener.listenForLocationUpdates(new LocationListener.OnUserLocationUpdated() {
                    @Override
                    public void onUserLocationUpdated(Coordinate userLocation) {
                        archidniGoogleMap.animateCamera(userLocation,14,200);
                        userCoordonates.add(userLocation);
                        if (userCoordonates.size()>1)
                        {
                            archidniGoogleMap.clearMap();
                            archidniGoogleMap.preparePolyline(RunningActivity.this,userCoordonates,R.color.colorGreen);
                        }
                    }
                });
            }
        });

        finishText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
