package messenger.modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import database.DbUser;
import database.UserService;
import database.models.PendingBlock;
import mapper.LambdaCallbackMessage;
import mapper.LambdaMessage;
import mapper.LambdaTextMessage;
import messenger.entities.MessageToEdit;
import messenger.entities.UserMessage;
import messenger.entities.UserMessageWithKeyboard;
import messenger.intents.Currencies;
import messenger.logger.EventLogger;
import messenger.Messenger;
import org.apache.http.util.TextUtils;

import java.util.Arrays;

public class SubscribeModule extends BaseModule {

    public static final String RATES = "rates";

    private static final PendingBlock.Callback firsCurrencyCallback = PendingBlock.Callback.from(RATES + ".firstCurrency");
    private final Currencies currencies;

    public SubscribeModule(EventLogger eventLogger,
                           Messenger telegramMessenger,
                           UserService userService,
                           ObjectMapper mapper) {
        super(eventLogger, telegramMessenger, userService);

        currencies = new Currencies(mapper);
    }

    @Override
    public boolean canHandle(String message) {
        return message.equals("/subscribe");
    }

    @Override
    public boolean canHandlePending(PendingBlock.Callback callback) {
        return callback.getName().equals(RATES);
    }

    @Override
    public void handle(LambdaMessage lambdaMessage) {
        long chatId = lambdaMessage.getChatId();

        messenger.sendMessage(chatId, new UserMessage("Type the name or the key of a currency. " +
                "I can handle only \n1.Bitcoin(btc)\n2.Ethereum(eth)\n3.Ripple\n4.Bitcoin Cash\n5.EOS"));

        eventLogger.didSetPending(chatId, "rates.firstCurrency");
    }

    @Override
    public void handlePending(LambdaMessage lambdaMessage, PendingBlock.Callback callback) {
        long chatId = lambdaMessage.getChatId();
        String message;
        String oldMessage;
        int messageId;

        if(lambdaMessage instanceof LambdaCallbackMessage){
            message = ((LambdaCallbackMessage)lambdaMessage).getCallback();
            oldMessage = ((LambdaCallbackMessage)lambdaMessage).getOldMessage();
            messageId = ((LambdaCallbackMessage)lambdaMessage).getMessageId();
        }
        else{
            message = ((LambdaTextMessage)lambdaMessage).getMessage();
            oldMessage = ((LambdaTextMessage)lambdaMessage).getMessage();
            messageId = 0;
        }

        if (callback.equals(firsCurrencyCallback)) {
            String currency = currencies.currencyFor(message);
            if (!TextUtils.isEmpty(currency)) {
                eventLogger.didCurrencySet(chatId, currency);
                messenger.sendMessage(chatId, new UserMessageWithKeyboard("Now let's pick a range of change. " +
                        "For example 5% will notify you when bitcoin goes from 10 000 to 10 500 or 9 500",
                        Arrays.asList("5%", "10%", "20%", "50%")));
                eventLogger.didSetPending(chatId, "rates.end");
            } else {
                messenger.sendMessage(chatId, new UserMessage("Sorry, I can handle only five currencies for now." +
                        "1.Bitcoin\n2.Ethereum\n3.Ripple\n4.Bitcoin Cash\n5.EOS"));
                eventLogger.clearPending(chatId);
            }
        }

        if (callback.getValue().equals("end")) {
            DbUser dbUser = userService.get(chatId);
            String currency = dbUser.getMeta().getCurrency();

            messenger.editMessage(chatId, new MessageToEdit(oldMessage, messageId));
            messenger.sendMessage(chatId, new UserMessage(String.format("Here we go, you're now subscribed to %s with" +
                    " %s range of change.You can always modify all your subscriptions, " +
                    "just type /subscriptions", currency, message)));

            eventLogger.didAddSubscribe(chatId, message);
            eventLogger.clearPending(chatId);
        }
    }
}
