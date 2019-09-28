package com.example.lenovog50.pedometre2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovog50.pedometre2.data.model.Hospital;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HospitalsActivity extends AppCompatActivity {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.root)
    View root;

    ArchidniGoogleMap archidniGoogleMap;
    GoogleMap googleMap;

    @BindView(R.id.textview_hospital_name)
    TextView hospitalNameText;

    ArrayList<Hospital> hospitals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospitals);
        ButterKnife.bind(this);
        MapFragment mapView = (MapFragment) getFragmentManager().findFragmentById(R.id.mapView);
        archidniGoogleMap = new ArchidniGoogleMap(this, mapView, new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                if (ActivityCompat.checkSelfPermission(HospitalsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(HospitalsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                HospitalsActivity.this.googleMap = googleMap;
                LocationListener locationListener = new LocationListener(HospitalsActivity.this);
                locationListener.getLastKnownUserLocation(new LocationListener.OnUserLocationUpdated() {
                    @Override
                    public void onUserLocationUpdated(Coordinate userLocation) {
                        archidniGoogleMap.animateCamera(userLocation,15,250);
                        getHospitals(userLocation);
                    }
                });
            }
        });

    }


    private void getHospitals (Coordinate location)
    {


        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?radius=15000&type=hospital&key=AIzaSyCofX_CIgt2BxM-82n1ariNR2Gb-mTAcm8"+
                "&location="+location.getLatitude()+","+location.getLongitude()+"&language=fr";

        AsyncTask<String,Void,String> asyncTask = new AsyncTask<String, Void, String>() {

            @Override
            protected void onPreExecute() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... strings) {
                return FunctionWeather.excuteGet(strings[0]);
            }

            @Override
            protected void onPostExecute(String s) {
                if (s!=null)
                {
                    hospitals = new ArrayList<>();
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        JSONArray results = jsonObject.getJSONArray("results");
                        for (int i=0;i<results.length();i++)
                        {
                            JSONObject hospitalObj = results.getJSONObject(i);
                            JSONObject geometry = hospitalObj.getJSONObject("geometry");
                            JSONObject location = geometry.getJSONObject("location");
                            Coordinate coordinate = new Coordinate(location.getDouble("lat"),
                                    location.getDouble("lng"));
                            String name = hospitalObj.getString("name");
                            Hospital hospital = new Hospital(name,null,coordinate);
                            hospitals.add(hospital);

                        }
                        showHospitalsOnMap();
                        return;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(HospitalsActivity.this,"Une erreur s'est produite",Toast.LENGTH_LONG).show();
            }
        };
        asyncTask.execute(url);
    }

    private void showHospitalsOnMap ()
    {
        for (Hospital hospital : hospitals)
        {
            MarkerOptions markerOptions = new MarkerOptions()
                    .title(hospital.getName())
                    .position(new LatLng(hospital.getCoordinate().getLatitude(),hospital.getCoordinate().getLongitude()));
            Marker marker = googleMap.addMarker(markerOptions);
            marker.setTag(hospital);
        }
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Hospital hospital = (Hospital) marker.getTag();
                hospitalNameText.setText(hospital.getName());
                return false;
            }
        });
        progressBar.setVisibility(View.GONE);
    }

}
