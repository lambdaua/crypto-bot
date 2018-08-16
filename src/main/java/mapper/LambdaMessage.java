package mapper;

public abstract class LambdaMessage {

    private long chatId;
    private String messenger;

    public LambdaMessage(long chatId, String messenger) {
        this.chatId = chatId;
        this.messenger = messenger;
    }

    public String getMessenger() {
        return messenger;
    }

    public long getChatId() {
        return chatId;
    }

}
