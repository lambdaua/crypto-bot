package messenger.modules;

import database.models.PendingBlock;
import mapper.LambdaMessage;
import org.telegram.telegrambots.api.objects.Update;

public interface Module {

    boolean canHandle(String message);

    boolean canHandlePending(PendingBlock.Callback callback);

    void handle(LambdaMessage lambdaMessage);

    void handlePending(LambdaMessage lambdaMessage, PendingBlock.Callback callback);
}
