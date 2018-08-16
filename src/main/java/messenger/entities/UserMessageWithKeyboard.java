package messenger.entities;

import java.util.List;

public class UserMessageWithKeyboard extends Message{

    private List<String> callbacks;

    public UserMessageWithKeyboard(String message, List<String> callbacks) {
        super(message);
        this.callbacks = callbacks;
    }

    public List<String> getCallbacks() {
        return callbacks;
    }
}
