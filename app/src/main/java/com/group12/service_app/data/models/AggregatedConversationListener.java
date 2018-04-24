package com.group12.service_app.data.models;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class AggregatedConversationListener implements ValueEventListener {

    private ArrayList<Conversation> conversations = new ArrayList<>();
    private int expectedCalls = 1;
    private int calls = 0;

    public AggregatedConversationListener(int expectedCalls) {
       this.expectedCalls = expectedCalls;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        this.calls++;

        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
            Conversation conversation = snapshot.getValue(Conversation.class);

            if(conversation != null) {
                this.conversations.add(conversation);
            }
        }


        if(this.calls == this.expectedCalls) {
            this.finish(this.conversations);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public void finish(ArrayList<Conversation> conversations) {

    }
}
