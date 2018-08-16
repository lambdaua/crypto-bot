package messenger;

import messenger.entities.Message;

public interface Messenger {

    void sendMessage(long chatId, Message message);

    void editMessage(long chatId,Message message);

    void deleteMessage(long chatId,Message message);
}
