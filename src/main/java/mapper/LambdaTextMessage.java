package mapper;

public class LambdaTextMessage extends LambdaMessage {

    private String message;

    public LambdaTextMessage(long chatId, String messenger,
                             String message) {
        super(chatId, messenger);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
