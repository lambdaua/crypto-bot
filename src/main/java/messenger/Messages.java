package messenger;

import org.telegram.telegrambots.api.objects.Update;

/**
 * @author by AlexBlokh, 12/5/17 (aleksandrblokh@gmail.com)
 */
public class Messages {

    public static long chatId(Update update) {
        return update.hasMessage()
                ? update.getMessage().getChatId()
                : update.getCallbackQuery().getMessage().getChatId();

    }

    public static String message(Update update){
        return update.hasMessage()
                ? update.getMessage().getText()
                : update.getCallbackQuery().getData();
    }

    public static String oldMessage(Update update){
        return update.hasMessage()
                ? update.getMessage().getText()
                : update.getCallbackQuery().getMessage().getText();
    }

    public static int messageId(Update update){
        return update.hasMessage()
                ? update.getMessage().getMessageId()
                : update.getCallbackQuery().getMessage().getMessageId();
    }
}
