package com.example.groupexpensetrackermobile.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupexpensetrackermobile.R;
import com.example.groupexpensetrackermobile.activities.MainActivity;
import com.example.groupexpensetrackermobile.activities.TripDetails;
import com.example.groupexpensetrackermobile.entities.SelectableUser;
import com.example.groupexpensetrackermobile.entities.Trip;
import com.example.groupexpensetrackermobile.viewholder.TripHolder;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripHolder> {

    // List to store all the contact details
    private List<Trip> tripList;
    private Context mContext;
    public static final int MAX_PAGE_SIZE = 4;

    public TripAdapter(List<Trip> contactsList, Context context) {
        this.tripList = contactsList;
        this.mContext = context;
    }

    public void setTripList(List<Trip> tripList) {
        this.tripList = tripList;
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long tripId = tripList.get(position).getId();
                Intent intent = new Intent(mContext, TripDetails.class);
                intent.putExtra("tripId", tripId);
                mContext.startActivity(intent);
            }
        });

        // You can set click listeners to indvidual items in the viewholder here
        // make sure you pass down the listner or make the Data members of the viewHolder public
    }

    @Override
    public int getItemCount() {
        return tripList == null? 0: tripList.size();
    }
}
