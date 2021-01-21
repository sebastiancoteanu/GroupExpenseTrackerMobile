package com.example.groupexpensetrackermobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupexpensetrackermobile.R;
import com.example.groupexpensetrackermobile.config.CredentialManager;
import com.example.groupexpensetrackermobile.entities.SelectableUser;
import com.example.groupexpensetrackermobile.entities.User;
import com.example.groupexpensetrackermobile.viewholder.AddUserHolder;
import com.example.groupexpensetrackermobile.viewholder.SelectedUserHolder;
import com.example.groupexpensetrackermobile.viewholder.TripHolder;

import java.util.ArrayList;
import java.util.List;

public class AddUsersAdapter extends RecyclerView.Adapter<AddUserHolder> implements Filterable {

    private List<SelectableUser> originalUserList;
    private LayoutInflater layoutInflater;
    private List<SelectableUser> currentUserList;

    private Context mContext;

    public AddUsersAdapter(List<SelectableUser> originalUserList, Context context) {
        this.originalUserList = originalUserList;
        this.currentUserList = new ArrayList<>();
        this.mContext = context;
    }

    public List<SelectableUser> getOriginalUserList() {
        return originalUserList;
    }

    public List<SelectableUser> getCurrentUserList() {
        return currentUserList;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                currentUserList = (List<SelectableUser>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<SelectableUser> filteredSelectableUsers = new ArrayList<>();

                if (constraint == null || constraint.length() <= 2) {
                    // Don't show results if we don't have at least 3 characters in the filtering field
                    results.count = 0;
                    results.values = new ArrayList<SelectableUser>();
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < originalUserList.size(); i++) {

                        StringBuilder searchableData = new StringBuilder();
                        SelectableUser selectableUser = originalUserList.get(i);
                        if(selectableUser.getFirstName() != null) {
                            searchableData.append(selectableUser.getFirstName());
                        }
                        if(selectableUser.getLastName() != null) {
                            searchableData.append(selectableUser.getLastName());
                        }
                        if(selectableUser.getLogin() != null) {
                            searchableData.append(selectableUser.getLogin());
                        }

                        if (searchableData.toString().toLowerCase().contains(constraint.toString())) {
                            filteredSelectableUsers.add(selectableUser);
                        }
                    }
                    // set the Filtered result to return
                    results.count = filteredSelectableUsers.size();
                    results.values = filteredSelectableUsers;
                }
                return results;
            }
        };
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
        final SelectableUser user = currentUserList.get(position);

        // Set the data to the views here
        holder.setFirstNameLastName(user.getFirstName() + " " + user.getLastName());
        holder.setUsername(user.getLogin());

        if(user.isSelected()) {
            holder.getAddUserCheckbox().setChecked(true);
        } else {
            holder.getAddUserCheckbox().setChecked(false);
        }

        // Can't remove the user who creates the trip
        if(user.getAppUserId() != CredentialManager.getInstance().getCurrentUser().getAppUserId()) {
            holder.getAddUserCheckbox().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isChecked = ((CompoundButton) v).isChecked();
                    System.out.println("Checked state changed for index " + position + ". New state is " + isChecked);
                    if (isChecked) {
                        user.setSelected(true);
                    } else {
                        user.setSelected(false);
                    }
                }
            });
        }

        /*holder.getAddUserCheckbox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                System.out.println("Checked state changed for index " + position);
                if(isChecked) {
                    user.setSelected(true);
                } else {
                    user.setSelected(false);
                }

                holder.
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return currentUserList.size();
    }
}
