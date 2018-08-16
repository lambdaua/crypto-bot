package messenger.entities;

public class MessageToEdit extends Message{

    private int messageId;

    public MessageToEdit(String message, int messageId) {
        super(message);
        this.messageId = messageId;

    }

    public int getMessageId() {
        return messageId;
    }
}
