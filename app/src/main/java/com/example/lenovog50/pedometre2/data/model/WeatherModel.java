package com.example.lenovog50.pedometre2.data.model;

public class WeatherModel {
    private String humidity;
    private int temp;
    private String pressure;
    private String description;
    private long timeTaken;


    public WeatherModel(String humidity, int temp, String pressure, String description, long timeTaken) {
        this.humidity = humidity;
        this.temp = temp;
        this.pressure = pressure;
        this.description = description;
        this.timeTaken = timeTaken;
    }

    public String getDescription() {
        return description;
    }

    public String getHumidity() {
        return humidity;
    }

    public int getTemp() {
        return temp;
    }

    public String getPressure() {
        return pressure;
    }

    public long getTimeTaken() {
        return timeTaken;
    }
}
