package com.group12.service_app.core.repositories.interfaces;

import com.group12.service_app.data.models.Message;

public interface IConversationMessageListener {
    void onNewMessage(Message message);
}
