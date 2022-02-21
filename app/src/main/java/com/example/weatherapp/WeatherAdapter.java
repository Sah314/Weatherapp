package com.example.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.databinding.LayoutweatherBinding;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<RVModel> RVModelArrayList;


    public WeatherAdapter(Context context, ArrayList<RVModel> RVModelArrayList) {
        this.context = context;
        this.RVModelArrayList = RVModelArrayList;

    }

    @NonNull
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.layoutweather ,parent, false);
        return new ViewHolder(view);}

    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.ViewHolder holder, int position) {
        RVModel model = RVModelArrayList.get(position);
        Picasso.get().load("https:".concat(model.getIcon())).into(holder.IVCondition);
        holder.TVTemp.setText(model.getTemperature()+"°C");
        holder.TVWindSpeed.setText(model.getWindSpeed()+"kmph");
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd  hh:mm");
        SimpleDateFormat output = new SimpleDateFormat("hh:mm aa");
        try{
            Date t = input.parse(model.getTime());
            holder.TVTime.setText(output.format(t));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return RVModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView TVTime , TVTemp , TVWindSpeed;
        private ImageView IVCondition;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


}