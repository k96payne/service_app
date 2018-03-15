package com.group12.service_app.data.models;

import java.util.Date;

/**
 * Created by james on 3/10/18.
 */

public class Message {

    public Message() { }

    public Message(String sender, String message) {
        this.sender = sender;
        this.message = message;
        this.sent = new Date();
    }

    public String sender;
    public String message;
    public Date sent;
}
