package com.example.groupexpensetrackermobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupexpensetrackermobile.R;
import com.example.groupexpensetrackermobile.entities.Trip;
import com.example.groupexpensetrackermobile.viewholder.TripHolder;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripHolder> {

    // List to store all the contact details
    private List<Trip> tripList;
    private Context mContext;

    public TripAdapter(List<Trip> contactsList, Context context) {
        this.tripList = contactsList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public TripHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.trip_card, parent, false);
        return new TripHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripHolder holder, int position) {
        final Trip contact = tripList.get(position);

        // Set the data to the views here
        holder.setTitle(contact.getTitle());
        holder.setBalance(contact.getBalance());
        holder.setCreatedDate(contact.getCreatedAt());

        // You can set click listners to indvidual items in the viewholder here
        // make sure you pass down the listner or make the Data members of the viewHolder public
    }

    @Override
    public int getItemCount() {
        return tripList == null? 0: tripList.size();
    }
}
