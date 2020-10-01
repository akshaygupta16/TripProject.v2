package com.example.tripprojectv2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripHolder>{
    Context context;
    ArrayList<Trip> trips;
    IJoinButton iJoinButton;

    public TripAdapter(Context context, ArrayList<Trip> trips, IJoinButton iJoinButton) {
        this.context = context;
        this.trips = trips;
        this.iJoinButton=iJoinButton;
    }

    @NonNull
    @Override
    public TripHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trip_layout,parent,false);
        TripHolder tripHolder = new TripHolder(view);
        return tripHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TripHolder holder, int position) {
        Trip trip = trips.get(position);
        holder.title.setText(trip.title);
        holder.noOfPeople.setText(trip.members.size()+"people going");
        // set up picasso for cover photo here

        holder.join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iJoinButton.joinButtonPressed();
            }
        });
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }


    public class TripHolder extends RecyclerView.ViewHolder{
        ImageView cover;
        TextView title,noOfPeople;
        Button join;
        public TripHolder(@NonNull View itemView) {
            super(itemView);
            this.title=itemView.findViewById(R.id.home_screen_trip_title);
            this.cover = itemView.findViewById(R.id.home_screen_trip_cover);
            this.noOfPeople = itemView.findViewById(R.id.home_screen_trip_members);
            this.join = itemView.findViewById(R.id.home_screen_join_button);
        }
    }
}
