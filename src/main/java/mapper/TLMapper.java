package mapper;

import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;

public class TLMapper {
    public static final String TELEGRAM = "telegram";

    public LambdaMessage map(Update update) {
        LambdaMessage lambdaMessage = null;
        if (update.hasMessage()) {
            Message message = update.getMessage();
            lambdaMessage = new LambdaTextMessage(
                    message.getChatId(),
                    TELEGRAM,
                    message.getText()
            );
        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            Message oldMessage = callbackQuery.getMessage();
            lambdaMessage = new LambdaCallbackMessage(
                    oldMessage.getChatId(),
                    TELEGRAM,
                    oldMessage.getMessageId(),
                    callbackQuery.getData(),
                    oldMessage.getText()
            );
        }
        return lambdaMessage;
    }

}
