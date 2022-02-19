package com.example.weatherapp;

import static android.view.View.GONE;

import static com.example.weatherapp.WeatherAdapter.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBindings;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.databinding.ActivityMainBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout RLHome;
    private ProgressBar PBLoading;
    private TextView TVCityName  , TVTemperature , TVCondition;
    private ImageView IVSearch , IVIcon , IVBack;
    private RecyclerView RVWeather;
    private TextInputEditText ETCity;
    private TextInputLayout TILCity;
    private ArrayList<RVModel> RVModelArrayList;
    private ActivityMainBinding binding;
    private WeatherAdapter weatherAdapter;
    private LocationManager LocManager;
    private int PERMISSION_code = 1;
    private String CityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        RLHome = binding.RLHome;
        TVCityName = binding.TVCityName;
        TVCondition = binding.TVCondition;
        TVTemperature = binding.TVTemperature;
        IVSearch = binding.IVSearch;
        IVIcon = binding.IVIcon;
        IVBack = binding.IVBack;
        ETCity = binding.ETCity;
        TILCity = binding.TILCity;
        RVWeather = binding.RVWeather;
        PBLoading = binding.PBLoading;
        RVModelArrayList = new ArrayList<>();
        weatherAdapter = new WeatherAdapter(this, RVModelArrayList);
        RVWeather.setAdapter(weatherAdapter);
        LocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_code);
    }

    Location location = LocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    CityName = getCityName(location.getLongitude() , location.getLatitude());
    getWeatherInfo(CityName);

    IVSearch.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String city = ETCity.getText().toString();
            if(city.isEmpty()){
                Toast.makeText(MainActivity.this , "Please enter city Name" , Toast.LENGTH_SHORT).show();
            }
            else{
                TVCityName.setText(CityName);
                getWeatherInfo(city);
            }
        }
    });
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_code) {
            if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this , "Permission granted" ,Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this , "Please provide the necessary permissions" , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    private String getCityName(double Longitude , double Latitude){
    String cityname = "Not Found";
            Geocoder gc = new Geocoder(getBaseContext() , Locale.getDefault());
            try {
                List<Address> addresses =  gc.getFromLocation(Latitude , Longitude , 10);
            for(Address adr: addresses){
                if(adr!=null){
                    String city = adr.getLocality();
                    if(city !=null && !city.equals("")){
                        cityname = city;
                    }else{
                        Log.d("TAG", "City Not found");
                        Toast.makeText(this , "User city Not found" , Toast.LENGTH_SHORT).show();
                    }
                }
            }
            }
            catch (IOException e){
            e.printStackTrace();
            }
            return cityname;
        }

    private void getWeatherInfo(String CityName){
        String APIurl = "http://api.weatherapi.com/v1/forecast.json?key=0d81570049a04fbaabf43058221902&q="+CityName+"&days=1&aqi=yes&alerts=yes";
        TVCityName.setText(CityName);
        RequestQueue reqqueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET ,APIurl ,null , response -> {
        PBLoading.setVisibility(View.GONE);
        RLHome.setVisibility(View.VISIBLE);
        RVModelArrayList.clear();

            try {
                String temp = response.getJSONObject("current").getString("temp_c");
                TVTemperature.setText(temp+"°C");
                int isDay = response.getJSONObject("current").getInt("is_day");
                String condition  = response.getJSONObject("current").getJSONObject("condition").getString("text");
                String cond_icon  = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                Picasso.get().load("http:".concat(cond_icon)).into(IVIcon);
                TVCondition.setText(condition);
                if(isDay == 1 ){
                    //Morning
                    Picasso.get().load("https://images.unsplash.com/photo-1519414442781-fbd745c5b497?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1782&q=80").into(IVBack);
                }else{
                    //Night
                    Picasso.get().load("https://images.unsplash.com/photo-1488866022504-f2584929ca5f?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2062&q=80").into(IVBack);
                }
                JSONObject forecast  = response.getJSONObject("forecast");
                JSONObject obj0 = forecast.getJSONArray("forecastday").getJSONObject(0);
                JSONArray hour = obj0.getJSONArray("hour");
                for(int i =0;i<hour.length();i++){
                    JSONObject hourObj = hour.getJSONObject(i);
                    String time = hourObj.getString("time");
                    String temper = hourObj.getString("temp_c");
                    String img = hourObj.getJSONObject("condition").getString("icon");
                    String wind = hourObj.getString("wind_kph");
                    RVModelArrayList.add(new RVModel(time ,wind,img,temper));

                }
                weatherAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Please enter valid city name",Toast.LENGTH_SHORT).show();
            }
        });
        reqqueue.add(jsonObjectRequest);
    }
}