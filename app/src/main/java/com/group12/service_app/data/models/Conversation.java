package com.group12.service_app.data.models;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by james on 3/10/18.
 */

public class Conversation {
    public String listingId;
    public String recipient1;
    public String recipient2;

    public Message[] messages;

    public static String CreateConversationKey(String recipient1, String recipient2) {

        Comparator<String> ALPHABETICAL_ORDER = new Comparator<String>() {
            public int compare(String str1, String str2) {
                int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
                if (res == 0) {
                    res = str1.compareTo(str2);
                }
                return res;
            }
        };

        List<String> recipients = new ArrayList<String>();

        recipients.add(recipient1);
        recipients.add(recipient2);

        Collections.sort(recipients, ALPHABETICAL_ORDER);

        String key = "";

        for (String recipient: recipients) {
            key += recipient;
        }

        return key;
    }
}
