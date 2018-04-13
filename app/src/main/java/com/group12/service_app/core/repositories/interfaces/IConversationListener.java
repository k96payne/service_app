package com.group12.service_app.core.repositories.interfaces;

import com.group12.service_app.data.models.Conversation;

public interface IConversationListener {
    void onNewConversation(Conversation conversation);
}
