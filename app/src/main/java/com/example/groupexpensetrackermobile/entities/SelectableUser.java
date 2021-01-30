package com.example.groupexpensetrackermobile.entities;

public class SelectableUser extends User {

    private boolean isSelected;

    public SelectableUser(long id, long appUserId, String login, String firstName, String lastName, String email, boolean isSelected) {
        super(id, appUserId, login, firstName, lastName, email);
        this.isSelected = isSelected;
    }

    public SelectableUser(User user) {
        super(user.getId(), user.getAppUserId(), user.getLogin(), user.getFirstName(), user.getLastName(), user.getEmail());
        this.isSelected = false;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
