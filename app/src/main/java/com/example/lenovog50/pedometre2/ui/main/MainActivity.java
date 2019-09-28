package com.example.lenovog50.pedometre2.ui.main;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovog50.pedometre2.DialogUtils;
import com.example.lenovog50.pedometre2.HeartBeatActivity;
import com.example.lenovog50.pedometre2.HospitalsActivity;
import com.example.lenovog50.pedometre2.LoginActivity;
import com.example.lenovog50.pedometre2.R;
import com.example.lenovog50.pedometre2.Weather;
import com.example.lenovog50.pedometre2.WeatherGetter;
import com.example.lenovog50.pedometre2.data.UserRepository;
import com.example.lenovog50.pedometre2.data.model.User;
import com.example.lenovog50.pedometre2.data.model.WeatherModel;
import com.google.gson.Gson;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private MainPresenter mainPresenter;

    @BindView(R.id.text_hello)
    TextView helloText;
    @BindView(R.id.image_disconnect)
    ImageView disconnectImage;
    @BindView(R.id.text_weather_today)
    TextView weatherText;
    @BindView(R.id.text_temperature)
    TextView temperatureText;
    @BindView(R.id.text_start_workout)
    TextView startWorkoutText;

    @BindView(R.id.text_current_weight)
    TextView currentWeightText;
    @BindView(R.id.text_current_height)
    TextView currentHeightText;
    @BindView(R.id.text_imc)
    TextView imcText;
    @BindView(R.id.text_weather_details)
    TextView favorableText;
    @BindView(R.id.text_remarque)
    TextView rmqText;
    @BindView(R.id.root)
    View root;
    @BindView(R.id.text_heartbeat)
    TextView heartBeatText;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.text_nearbyHospitals)
    TextView nearbyHospitalsText;

    boolean timeOut = false;
    boolean weatherGot = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainPresenter = new MainPresenter(this);
        ButterKnife.bind(this);



        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 0);
        }*/

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA}, 0);
            //return;
        }

        startWorkoutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,RunningActivity.class);
                startActivity(intent);
            }
        });
        disconnectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.buildClickableDialog(MainActivity.this, "Se déconnecter", "Voulez vous vous déconnecter ?",
                        "Oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UserRepository userRepository = new UserRepository(MainActivity.this);
                                userRepository.disconnectUser();
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });

        nearbyHospitalsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HospitalsActivity.class);
                startActivity(intent);
            }
        });

        heartBeatText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HeartBeatActivity.class);
                startActivity(intent);
            }
        });

        tryWeather();
    }


    private void tryWeather () {
        WeatherGetter weatherGetter = new WeatherGetter(this);

        weatherGetter.getWeather(new WeatherGetter.OnWeatherDownloadListener() {
            @Override
            public void onWeatherDownload(final int temp, final String humidity, final String pressure, final String description) {
                if (!timeOut)
                {
                    weatherGot = true;
                    progressBar.setVisibility(View.GONE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            root.setVisibility(View.VISIBLE);
                        }
                    },250);
                    mainPresenter.onCreate(MainActivity.this);
                    temperatureText.setText("Température : "+temp+"°");
                    weatherText.setText(description);
                    favorableText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, Weather.class);
                            intent.putExtra(Weather.INTENT_STR,new Gson().toJson(new WeatherModel(humidity,temp,pressure,description,System.currentTimeMillis())));
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onError() {
                Toast.makeText(MainActivity.this,
                        "Impossible de récupérer les données météo veuillez vérifier votre connexion internet",Toast.LENGTH_LONG).show();
                weatherGot = true;
                progressBar.setVisibility(View.GONE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        root.setVisibility(View.VISIBLE);
                    }
                },250);
                mainPresenter.onCreate(MainActivity.this);
                temperatureText.setText("Données météo non disponibles");
                weatherText.setText("Données météo non disponibles");
                favorableText.setVisibility(View.GONE);
                weatherText.setVisibility(View.GONE);
            }
        });

       /* Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                timeOut = true;
                if (!weatherGot)
                {
                    progressBar.setVisibility(View.GONE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            root.setVisibility(View.VISIBLE);
                        }
                    },250);
                    mainPresenter.onCreate(MainActivity.this);
                }
            }
        },10000);*/
    }

    @Override
    public void showTodayInfo(int degrees, String description, int favorable) {
        //temperatureText.setText("Température "+degrees);
        //weatherText.setText(description);
        /*switch (favorable)
        {
            case 0:favorableText.setText("Favorable");
            favorableText.setTextColor(getResources().getColor(R.color.colorGreen));
            break;
            case 1:favorableText.setText("Non favorable");
            favorableText.setTextColor(getResources().getColor(R.color.colorRed));
            break;

        }*/
    }

    @Override
    public void showWeatherThisWeek(String days) {

    }

    @Override
    public void showUserInfo(User user, float imc, String rmq) {
        helloText.setText("Bonjour "+user.getFirstname());
        currentHeightText.setText("Taille : "+user.getHeight()+"cm");
        currentWeightText.setText("Poids : "+user.getWeight()+"Kg");
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        imcText.setText("Imc : "+df.format(imc));
        rmqText.setText("Observation : "+rmq);
    }


}
