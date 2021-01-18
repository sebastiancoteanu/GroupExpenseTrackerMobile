package com.example.groupexpensetrackermobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupexpensetrackermobile.R;
import com.example.groupexpensetrackermobile.entities.User;
import com.example.groupexpensetrackermobile.viewholder.SelectedTripUserHolder;
import com.example.groupexpensetrackermobile.viewholder.TripHolder;

import java.util.List;

public class SelectedTripUserAdapter extends RecyclerView.Adapter<SelectedTripUserHolder> {

    // List to store all the contact details
    private List<User> userList;
    private Context mContext;

    public SelectedTripUserAdapter(List<User> contactsList, Context context) {
        this.userList = contactsList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public SelectedTripUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.selected_user_card, parent, false);
        return new SelectedTripUserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedTripUserHolder holder, int position) {
        final User user = userList.get(position);

        // Set the data to the views here
        holder.setFirstNameLastName(user.getFirstName() + " " + user.getLastName());
        holder.setUserName(user.getLogin());

        holder.getRemoveItemButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userList.remove(position);
                SelectedTripUserAdapter.this.notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList == null? 0 : userList.size();
    }
}
