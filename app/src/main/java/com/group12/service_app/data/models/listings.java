package com.group12.service_app.data.models;

/**
 * Created by User on 3/31/2018.
 */

public class listings {

    public String description, id, title, ownerId;
    public double price;
    public String zipCode;
    public listings(){}

    public listings(String description, String id, String title,String zipCode, double price, String ownerId) {
        this.description = description;
        this.id = id;
        this.title = title;
        this.price = price;
        this.zipCode = zipCode;
        this.ownerId = ownerId;

    }

    public String getZipCode() {
        return zipCode;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public  void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getOwnerId() {return ownerId;}

}
