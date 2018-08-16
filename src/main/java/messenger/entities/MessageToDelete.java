package messenger.entities;

public class MessageToDelete extends Message {

    private int messageId;

    public MessageToDelete(int messageId) {
        super("");
        this.messageId = messageId;
    }

    public int getMessageId() {
        return messageId;
    }
}
