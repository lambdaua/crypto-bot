package mapper;

public class LambdaCallbackMessage extends LambdaMessage {

    private int messageId;
    private String callback;
    private String oldMessage;

    public LambdaCallbackMessage(long chatId, String messenger, int messageId, String callback, String oldMessage) {
        super(chatId, messenger);
        this.messageId = messageId;
        this.callback = callback;

        this.oldMessage = oldMessage;
    }

    public int getMessageId() {
        return messageId;
    }

    public String getCallback() {
        return callback;
    }

    public String getOldMessage() {
        return oldMessage;
    }
}
