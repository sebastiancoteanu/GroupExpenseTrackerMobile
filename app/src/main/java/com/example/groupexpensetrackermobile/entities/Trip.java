package com.example.groupexpensetrackermobile.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Trip {

    private long id;
    private String title;
    private String description;
    private LocalDate createdAt;
    private BigDecimal balance;

    public Trip(long id, String title, String description, LocalDate createdAt, BigDecimal balance) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
