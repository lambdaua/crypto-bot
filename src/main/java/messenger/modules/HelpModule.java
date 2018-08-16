package messenger.modules;

import database.UserService;
import database.models.PendingBlock;
import mapper.LambdaMessage;
import messenger.entities.UserMessage;
import messenger.logger.EventLogger;
import messenger.Messenger;

public class HelpModule extends BaseModule {

    public HelpModule(EventLogger eventLogger, Messenger telegramMessenger, UserService userService) {
        super(eventLogger, telegramMessenger, userService);
    }

    @Override
    public boolean canHandle(String message) {
        return message.equals("/help");
    }

    @Override
    public boolean canHandlePending(PendingBlock.Callback callback) {
        return false;
    }

    @Override
    public void handle(LambdaMessage lambdaMessage) {
        long chatId = lambdaMessage.getChatId();

        StringBuilder sb = new StringBuilder();

        sb.append("/subscribe - command that let you subscribe to a currency changes").append("\n");
        sb.append("/subscriptions - here you can manage all of your subscriptions").append("\n");
        sb.append("/help - is me. I'm help");

        messenger.sendMessage(chatId, new UserMessage(sb.toString()));
    }

    @Override
    public void handlePending(LambdaMessage lambdaMessage, PendingBlock.Callback callback) {

    }
}
