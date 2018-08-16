package messenger;

import com.fasterxml.jackson.databind.ObjectMapper;
import database.DbUser;
import database.models.PendingBlock;
import database.UserService;
import mapper.LambdaCallbackMessage;
import mapper.LambdaMessage;
import mapper.LambdaTextMessage;
import messenger.logger.EventLogger;
import messenger.modules.*;

import java.util.Arrays;
import java.util.List;

public class MessagesReceiver {

    private List<Module> modules;
    private EventLogger eventLogger;
    private UserService userService;

    public MessagesReceiver(Messenger messenger,
                            EventLogger eventLogger,
                            UserService userService,
                            ObjectMapper mapper) {
        modules = Arrays.asList(
                new HelpModule(eventLogger, messenger, userService),
                new StartModule(eventLogger, messenger, userService),
                new SubscribeModule(eventLogger, messenger, userService, mapper),
                new SubscriptionsModule(eventLogger, messenger, userService)
        );
        this.eventLogger = eventLogger;
        this.userService = userService;
    }

    public void handle(LambdaMessage lambdaMessage) {
        String message;
        if (lambdaMessage instanceof LambdaTextMessage) {
            message = ((LambdaTextMessage) lambdaMessage).getMessage();
        } else {
            message = ((LambdaCallbackMessage) lambdaMessage).getCallback();
        }
        long chatId = lambdaMessage.getChatId();

        eventLogger.didAddUser(chatId);

        if (message.startsWith("/")) {
            eventLogger.clearPending(chatId);
        }

        DbUser user = userService.get(chatId);
        PendingBlock pendingBlock = user.getPendingBlock();

        if (pendingBlock.isPending()) {
            for (Module module : modules) {
                PendingBlock.Callback callback = pendingBlock.getCallback();

                if (module.canHandlePending(callback)) {
                    module.handlePending(lambdaMessage, callback);
                    break;
                }
            }
            return;
        }

        for (Module module : modules) {
            if (module.canHandle(message)) {
                module.handle(lambdaMessage);
                break;
            }
        }
    }
}
