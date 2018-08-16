package messenger;

import com.github.messenger4j.Messenger;
import com.github.messenger4j.common.SupportedLocale;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.messengerprofile.MessengerSettings;
import com.github.messenger4j.messengerprofile.getstarted.StartButton;
import com.github.messenger4j.messengerprofile.greeting.Greeting;
import com.github.messenger4j.messengerprofile.greeting.LocalizedGreeting;
import com.github.messenger4j.messengerprofile.persistentmenu.LocalizedPersistentMenu;
import com.github.messenger4j.messengerprofile.persistentmenu.PersistentMenu;
import com.github.messenger4j.messengerprofile.persistentmenu.action.PostbackCallToAction;
import com.github.messenger4j.send.MessagePayload;
import com.github.messenger4j.send.message.TextMessage;
import com.github.messenger4j.send.message.quickreply.QuickReply;
import com.github.messenger4j.send.message.quickreply.TextQuickReply;
import com.github.messenger4j.send.recipient.IdRecipient;
import messenger.entities.Message;
import messenger.entities.UserMessage;
import messenger.entities.UserMessageWithKeyboard;
import messenger.entities.UserMessageWithReplyKeyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Optional.empty;
import static java.util.Optional.of;

public class FbMessenger implements messenger.Messenger {

    private Messenger fbMessenger;

    public FbMessenger(Messenger fbMessenger) {
        this.fbMessenger = fbMessenger;
    }

    @Override
    public void editMessage(long chatId, Message message) {
        final TextMessage textMessage = TextMessage.create(message.getMessage());
        final MessagePayload messagePayload = MessagePayload.create(String.valueOf(chatId), textMessage);
        try {
            fbMessenger.send(messagePayload);
        } catch (MessengerApiException | MessengerIOException e) {
        }
    }

    @Override
    public void deleteMessage(long chatId, Message message) {

    }

    @Override
    public void sendMessage(long chatId, Message message) {
        if (message instanceof UserMessage) {
            sendUserMessage(chatId, (UserMessage) message);
        }
        if (message instanceof UserMessageWithKeyboard) {
            sendUserMessageWithKeyboard(chatId, (UserMessageWithKeyboard) message);
        }
        if (message instanceof UserMessageWithReplyKeyboard) {
            sendUserMessageWithReplyKeyboard(chatId, (UserMessageWithReplyKeyboard) message);
        }
    }

    private void sendUserMessage(long chatId, UserMessage message) {
        final TextMessage textMessage = TextMessage.create(message.getMessage());
        final MessagePayload messagePayload = MessagePayload.create(String.valueOf(chatId), textMessage);
        try {
            fbMessenger.send(messagePayload);
        } catch (MessengerApiException | MessengerIOException e) {
        }
    }

    private void sendUserMessageWithKeyboard(long chatId, UserMessageWithKeyboard message) {
        final IdRecipient recipient = IdRecipient.create(String.valueOf(chatId));

        List<QuickReply> quickReplies = null;

        List<String> callbacks = message.getCallbacks();
        if (callbacks.size() == 4) {
            TextQuickReply fivePercent = TextQuickReply.create(callbacks.get(0), callbacks.get(0));
            TextQuickReply tenPercent = TextQuickReply.create(callbacks.get(1), callbacks.get(1));
            TextQuickReply twentyPercent = TextQuickReply.create(callbacks.get(2), callbacks.get(2));
            TextQuickReply fiftyPercent = TextQuickReply.create(callbacks.get(3), callbacks.get(3));

            quickReplies = Arrays.asList(fivePercent, tenPercent, twentyPercent, fiftyPercent);
        }
        if (callbacks.size() == 2) {
            TextQuickReply yes = TextQuickReply.create("Yes", callbacks.get(0));
            TextQuickReply no = TextQuickReply.create("No", callbacks.get(1));

            quickReplies = Arrays.asList(yes, no);
        }

        final TextMessage textMessage = TextMessage.create(message.getMessage(), of(quickReplies), empty());
        final MessagePayload payload = MessagePayload.create(recipient, textMessage, empty());

        try {
            fbMessenger.send(payload);
        } catch (MessengerApiException | MessengerIOException e) {
            e.printStackTrace();
        }
    }

    private void sendUserMessageWithReplyKeyboard(long chatId, UserMessageWithReplyKeyboard message) {
        final IdRecipient recipient = IdRecipient.create(String.valueOf(chatId));
        String text = message.getMessage();
        List<String> names = message.getNames();
        List<QuickReply> quickReplies = new ArrayList<>();

        for (int i = 0; i < names.size(); i++) {
            TextQuickReply subscription = TextQuickReply.create(names.get(i), names.get(i));
            quickReplies.add(subscription);
        }

        final TextMessage textMessage = TextMessage.create(text, of(quickReplies), empty());
        final MessagePayload payload = MessagePayload.create(recipient, textMessage, empty());

        try {
            fbMessenger.send(payload);
        } catch (MessengerApiException | MessengerIOException e) {
            e.printStackTrace();
        }
    }
}
