package com.example.groupexpensetrackermobile.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupexpensetrackermobile.R;
import com.example.groupexpensetrackermobile.entities.SelectableUser;
import com.example.groupexpensetrackermobile.entities.User;
import com.example.groupexpensetrackermobile.viewholder.TripMemberHolder;

import java.util.List;
import java.util.stream.Collectors;

public class TripMembersAdapter extends RecyclerView.Adapter<TripMemberHolder> {
    private List<User> membersList;
    private LayoutInflater layoutInflater;
    private final Context mContext;

    public TripMembersAdapter(List<User> membersList, Context context) {
        this.membersList = membersList;
        this.mContext = context;
    }

    @SuppressLint("NewApi")
    public void setMembersList(List<SelectableUser> membersList) {
        this.membersList = membersList.stream().map(p -> (SelectableUser) p).collect(Collectors.toList());
    }

    @NonNull
    @Override
    public TripMemberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.member_card, parent, false);
        return new TripMemberHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripMemberHolder holder, int position) {
        final User member = membersList.get(position);
        holder.setFirstNameLastName(member.getFirstName() + " " + member.getLastName());
        holder.setUsername(member.getLogin());
        holder.setWealth(member.getBalance());
    }

    @Override
    public int getItemCount() {
        return membersList.size();
    }
}
