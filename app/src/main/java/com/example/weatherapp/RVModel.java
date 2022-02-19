package com.example.weatherapp;

public class RVModel {
    private String Time;
    private String WindSpeed;
    private String icon;
    private String temperature;
    public RVModel(String Time , String WindSpeed , String icon , String temperature){
        this.Time = Time;
        this.icon = icon;
        this.temperature = temperature;
        this.WindSpeed = WindSpeed;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getWindSpeed() {
        return WindSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        WindSpeed = windSpeed;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
