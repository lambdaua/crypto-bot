package mapper;

import com.github.messenger4j.webhook.Event;
import com.github.messenger4j.webhook.event.PostbackEvent;
import com.github.messenger4j.webhook.event.QuickReplyMessageEvent;
import com.github.messenger4j.webhook.event.TextMessageEvent;

public class FbMapper {
    public static final String FACEBOOK = "facebook";

    public LambdaMessage map(Event event) {
        LambdaMessage lambdaMessage = null;
        if (event.isQuickReplyMessageEvent()) {
            QuickReplyMessageEvent callbackQuery = event.asQuickReplyMessageEvent();
            String oldMessage = callbackQuery.text();
            lambdaMessage = new LambdaCallbackMessage(
                    Long.parseLong(event.senderId()),
                    FACEBOOK,
                    0,
                    callbackQuery.payload(),
                    oldMessage
            );
        } else if (event.isTextMessageEvent()) {
            TextMessageEvent message = event.asTextMessageEvent();
            lambdaMessage = new LambdaTextMessage(
                    Long.parseLong(event.senderId()),
                    FACEBOOK,
                    message.text()
            );
        }
        else if(event.isPostbackEvent()){
            PostbackEvent message = event.asPostbackEvent();
            lambdaMessage = new LambdaTextMessage(
                    Long.parseLong(event.senderId()),
                    FACEBOOK,
                    message.payload().get()
            );
        }
        return lambdaMessage;
    }
}
