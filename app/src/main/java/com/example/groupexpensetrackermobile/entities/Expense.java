package com.example.groupexpensetrackermobile.entities;

public class Expense {
    private long id;
    private long amount;
    private String description;
    private long createdById;
    private long tripId;
    private ExpenseType type;

    public Expense() {
    }

    public Expense(long id, long amount, String description, long createdById, long tripId, ExpenseType type) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.createdById = createdById;
        this.tripId = tripId;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(long createdById) {
        this.createdById = createdById;
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    public ExpenseType getType() {
        return type;
    }

    public void setType(ExpenseType type) {
        this.type = type;
    }
}
