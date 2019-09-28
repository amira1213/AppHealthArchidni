package com.example.lenovog50.pedometre2;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.Toast;

import com.example.lenovog50.pedometre2.data.SharedPrefsUtils;
import com.example.lenovog50.pedometre2.data.model.WeatherModel;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Locale;

public class WeatherGetter {
    private Context context;
    String OPEN_WEATHER_MAP_API = "3be90c56fbaecabfb8437af5f7931373";
    String city = "Algiers, DZ";
    String SHARED_PREFS_KEY = "WEATHER";

    public WeatherGetter(Context context) {
        this.context = context;
    }


    private WeatherModel getFromOffLine() {
        String w = SharedPrefsUtils.loadString(context, SHARED_PREFS_KEY);
        if (w == null)
            return null;
        else
            return new Gson().fromJson(w, WeatherModel.class);
    }

    private void saveWeatherModel(WeatherModel weatherModel) {
        String s = new Gson().toJson(weatherModel);
        SharedPrefsUtils.saveString(context, SHARED_PREFS_KEY, s);
    }

    public void getWeather(final OnWeatherDownloadListener onWeatherDownloadListener) {
        AsyncTask<String, Void, String> weatherTask = new AsyncTask<String, Void, String>() {

            protected String doInBackground(String... args) {
                String xml = FunctionWeather.excuteGet("http://api.openweathermap.org/data/2.5/weather?q=" + args[0] +
                        "&units=metric&lang=fr&appid=" + OPEN_WEATHER_MAP_API);
                return xml;
            }

            @Override
            protected void onPostExecute(String xml) {
                if (xml == null) {
                    WeatherModel weatherModel = getFromOffLine();
                    if (weatherModel == null || weatherModel.getTimeTaken() - System.currentTimeMillis() > 1000 * 3600 * 24) {
                        onWeatherDownloadListener.onError();
                        return;
                    } else {
                        Toast.makeText(context, "Connexion impossible, récupération des données offlines", Toast.LENGTH_LONG).show();
                        onWeatherDownloadListener.onWeatherDownload(weatherModel.getTemp(), weatherModel.getHumidity(), weatherModel.getPressure(), weatherModel.getDescription());
                        return;
                    }
                }
                try {
                    JSONObject json = new JSONObject(xml);
                    if (json != null) {
                        JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                        JSONObject main = json.getJSONObject("main");
                        DateFormat df = DateFormat.getDateTimeInstance();
                        int temp = (int) main.getDouble("temp");
                        String humidity = main.getString("humidity");
                        String pressure = main.getString("pressure");
                        String description = details.getString("description");
                        WeatherModel weatherModel1 = new WeatherModel(humidity, temp, pressure, description, System.currentTimeMillis());
                        saveWeatherModel(weatherModel1);
                        onWeatherDownloadListener.onWeatherDownload(temp, humidity, pressure, description);
                    }
                } catch (JSONException e) {
                    Toast.makeText(context,"Probleme de connexion",Toast.LENGTH_LONG).show();
                    onWeatherDownloadListener.onError();
                }


            };


        };
        weatherTask.execute(city);
    }

    public interface OnWeatherDownloadListener {
        void onWeatherDownload(int temp, String humidity, String pressure, String description);

        void onError();
    }
}
