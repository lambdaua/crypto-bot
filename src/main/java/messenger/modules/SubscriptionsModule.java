package messenger.modules;

import database.DbUser;
import database.UserService;
import database.models.PendingBlock;
import database.models.Subscription;
import mapper.LambdaCallbackMessage;
import mapper.LambdaMessage;
import mapper.LambdaTextMessage;
import messenger.entities.MessageToDelete;
import messenger.entities.MessageToEdit;
import messenger.entities.UserMessageWithKeyboard;
import messenger.entities.UserMessageWithReplyKeyboard;
import messenger.logger.EventLogger;
import messenger.Messenger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SubscriptionsModule extends BaseModule {

    private static final String UNSUBSCRIBE = "unsubscribe";
    public static final String SNIPPET_USER_SUBSCRIPTIONS = "There is a list of your subscriptions";
    public static final String SNIPPET_NO_SUBSCRIPTIONS = "You don't have any subscriptions";

    public SubscriptionsModule(EventLogger eventLogger, Messenger messenger, UserService userService) {
        super(eventLogger, messenger, userService);
    }

    @Override
    public boolean canHandle(String message) {
        return message.equals("/subscriptions") || message.contains(" +- ");
    }

    @Override
    public boolean canHandlePending(PendingBlock.Callback callback) {
        return callback.getName().equals(UNSUBSCRIBE);
    }

    @Override
    public void handle(LambdaMessage lambdaMessage) {
        long chatId = lambdaMessage.getChatId();
        String message;

        if(lambdaMessage instanceof LambdaCallbackMessage){
            message = ((LambdaCallbackMessage)lambdaMessage).getCallback();
        }
        else{
            message = ((LambdaTextMessage)lambdaMessage).getMessage();
        }

        if (message.equals("/subscriptions")) {
            DbUser user = userService.get(chatId);

            List<Subscription> subscriptions = user.getSubscriptions();

            if (subscriptions.size() == 0) {
                messenger.editMessage(chatId, new UserMessageWithReplyKeyboard(SNIPPET_NO_SUBSCRIPTIONS, Collections.emptyList()));
                return;
            }

            List<String> names = new ArrayList<>(subscriptions.size());

            for (Subscription subscription : subscriptions) {
                String currency = subscription.getCurrency();
                String diff = subscription.getDiff();

                names.add(String.format("%s +- %s%%", currency, diff));
            }

            messenger.sendMessage(chatId, new UserMessageWithReplyKeyboard(SNIPPET_USER_SUBSCRIPTIONS, names));
        } else {
            messenger.sendMessage(chatId, new UserMessageWithKeyboard(String.format("Do you want to unsubscribe from \"%s\"", message), Arrays.asList("unsubscribe.yes", "unsubscribe.no")));
            eventLogger.didSetPending(chatId, String.format("%s.%s", UNSUBSCRIBE, message));
        }
    }

    @Override
    public void handlePending(LambdaMessage lambdaMessage, PendingBlock.Callback callback) {
        long chatId = lambdaMessage.getChatId();
        String message;
        int messageId;

        if(lambdaMessage instanceof LambdaCallbackMessage){
            message = ((LambdaCallbackMessage)lambdaMessage).getCallback();
            messageId = ((LambdaCallbackMessage)lambdaMessage).getMessageId();
        }
        else{
            message = ((LambdaTextMessage)lambdaMessage).getMessage();
            messageId = 0;
        }

        if (message.equals(UNSUBSCRIBE + ".yes")) {
            String currencyToDelete = callback.getValue().split(" ")[0];
            String diffToDelete = callback.getValue().split(" ")[2];
            messenger.editMessage(chatId, new MessageToEdit(String.format("All set\nYou're no longer subscribed to %s", callback.getValue()), messageId));

            eventLogger.didUnsubscribeCurrency(chatId, currencyToDelete, diffToDelete);
            eventLogger.clearPending(chatId);

            DbUser user = userService.get(chatId);

            //TODO .map operator
            List<Subscription> userSubscriptions = user.getSubscriptions();
            List<String> names = new ArrayList<>(userSubscriptions.size());

            for (Subscription subscription : userSubscriptions) {
                String currency = subscription.getCurrency();
                String diff = subscription.getDiff();

                names.add(String.format("%s +- %s%%", currency, diff));
            }

            if (names.size() == 0) {
                messenger.editMessage(chatId, new UserMessageWithReplyKeyboard(SNIPPET_NO_SUBSCRIPTIONS, Collections.emptyList()));
            } else {
                messenger.sendMessage(chatId, new UserMessageWithReplyKeyboard(SNIPPET_USER_SUBSCRIPTIONS, names));
            }
        } else {
            messenger.deleteMessage(chatId, new MessageToDelete(messageId));
        }
        eventLogger.clearPending(chatId);
    }
}
