package messenger.modules;

import database.UserService;
import database.models.PendingBlock;
import mapper.LambdaMessage;
import messenger.entities.UserMessage;
import messenger.logger.EventLogger;
import messenger.Messenger;

/**
 * @author by AlexBlokh, 12/5/17 (aleksandrblokh@gmail.com)
 */
public class StartModule extends BaseModule {
    public StartModule(EventLogger eventLogger, Messenger messenger, UserService userService) {
        super(eventLogger, messenger, userService);
    }

    @Override
    public boolean canHandle(String message) {
        return message.equals("/start");
    }

    @Override
    public boolean canHandlePending(PendingBlock.Callback callback) {
        return false;
    }

    @Override
    public void handle(LambdaMessage lambdaMessage) {
        long chatId = lambdaMessage.getChatId();
        messenger.sendMessage(chatId, new UserMessage("Hi! My name is Andrew"));
        messenger.sendMessage(chatId, new UserMessage("I spend ma days watching Bitcoin price"));
        messenger.sendMessage(chatId, new UserMessage("So I can notify you, when it's needed :)"));
        messenger.sendMessage(chatId, new UserMessage("If you got any questions - you can always use /help"));
        messenger.sendMessage(chatId, new UserMessage("And now lets setup your first currency subscription\n" +
                "Type /subscribe and pick type a currency you want to watch"));
    }

    @Override
    public void handlePending(LambdaMessage lambdaMessage, PendingBlock.Callback callback) {

    }
}
