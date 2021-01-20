package com.example.groupexpensetrackermobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupexpensetrackermobile.R;
import com.example.groupexpensetrackermobile.entities.User;
import com.example.groupexpensetrackermobile.viewholder.AddUserHolder;

import java.util.List;

public class TripMembersAdapter extends RecyclerView.Adapter<AddUserHolder> {
    private List<User> membersList;
    private LayoutInflater layoutInflater;
    private final Context mContext;

    public TripMembersAdapter(List<User> membersList, Context context) {
        this.membersList = membersList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public AddUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.add_users_card, parent, false);
        return new AddUserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddUserHolder holder, int position) {
        final User member = membersList.get(position);
        holder.setFirstNameLastName(member.getFirstName() + " " + member.getLastName());
        holder.setUsername(member.getLogin());
    }

    @Override
    public int getItemCount() {
        return membersList.size();
    }
}
