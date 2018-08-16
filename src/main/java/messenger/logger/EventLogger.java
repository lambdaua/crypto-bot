package messenger.logger;

import database.UserService;

public class EventLogger {
    private UserService userService;

    public EventLogger(UserService userService) {
        this.userService = userService;
    }

    public void didAddUser(long chatId) {
        userService.addUser(chatId);
    }

    public void didAddSubscribe(long chatId, String diff) {
        userService.addSubscribe(chatId, diff);
    }

    public void didCurrencySet(long chatId, String currency) {
        userService.setCurrency(chatId, currency);
    }

    public void didUnsubscribeCurrency(long chatId, String currency, String diffToDelete) {
        userService.didUnsubscribeCurrency(chatId, currency, diffToDelete);
    }

    public void clearPending(long chatId) {
        userService.setPending(chatId, false, "");
    }

    public void didSetPending(long chatId, String block) {
        userService.setPending(chatId, true, block);
    }

}
