package messenger.modules;

import database.UserService;
import database.models.PendingBlock;
import mapper.LambdaMessage;
import messenger.logger.EventLogger;
import messenger.Messenger;

public class RatesModule extends BaseModule {

    public RatesModule(EventLogger eventLogger, Messenger telegramMessenger, UserService userService) {
        super(eventLogger, telegramMessenger, userService);
    }

    @Override
    public boolean canHandle(String message) {
        return message.equals("/rate");
    }

    @Override
    public boolean canHandlePending(PendingBlock.Callback callback) {
        return false;
    }

    @Override
    public void handle(LambdaMessage lambdaMessage) {
        //long chatId = update.hasMessage() ? update.getMessage().getChatId() : update.getCallbackQuery().getMessage().getChatId();

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("I can send you updates for these currencies.\n"));
        sb.append(String.format("Just choose name or symbol and write it in subscribes\n\n\n"));

/*        for(Map.Entry<String, List<String>> currencies : Currencies.intents.entrySet()) {
            String symbol = currencies.getValue().get(0);
            String currencyName = currencies.getValue().get(1);

            sb.append(String.format("Name: _%s_  -  Symbol: _%s_\n\n",currencyName,symbol));
        }*/

       // messenger.sendMessage(chatId,new UserMessage(sb.toString()));
    }

    @Override
    public void handlePending(LambdaMessage lambdaMessage, PendingBlock.Callback callback) {

    }
}
