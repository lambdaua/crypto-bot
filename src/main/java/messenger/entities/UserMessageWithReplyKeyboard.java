package messenger.entities;

import java.util.List;

public class UserMessageWithReplyKeyboard extends Message {

    private List<String> names;

    public UserMessageWithReplyKeyboard(String message, List<String> names) {
        super(message);
        this.names = names;
    }

    public List<String> getNames() {
        return names;
    }
}
