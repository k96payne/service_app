package com.group12.service_app.data.models;

/**
 * Created by User on 3/31/2018.
 */

public class listings {

    public String description, id, title;
    public listings(){}

    public listings(String description, String id, String title) {
        this.description = description;
        this.id = id;
        this.title = title;
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
}