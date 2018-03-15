package com.group12.service_app.data.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Currency;
import java.util.UUID;

/**
 * Created by james on 3/3/18.
 */

public class Listing {

    public Listing() { }

    public Listing(String title, String description) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.price = 0.00;
    }

    public String id;
    public String title;
    public String description;
    public String zipCode;
    public Double price;

}
