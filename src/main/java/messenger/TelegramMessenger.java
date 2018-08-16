package messenger;

import messenger.entities.*;
import messenger.keyboards.InlineKeyBoard;
import messenger.keyboards.ReplyKeyBoard;
import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.api.methods.ParseMode;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResultVenue;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.List;

public class TelegramMessenger implements Messenger {

    private AndrewTelegramBot bot;

    public TelegramMessenger(AndrewTelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public void sendMessage(long chatId, Message message){
        if(message instanceof UserMessage){
            sendUserMessage(chatId,(UserMessage) message);
        }
        if(message instanceof UserMessageWithKeyboard){
            sendUserMessageWithKeyboard(chatId, (UserMessageWithKeyboard) message);
        }
        if(message instanceof UserMessageWithReplyKeyboard){
            sendUserMessageWithReplyKeyboard(chatId, (UserMessageWithReplyKeyboard) message);
        }
    }

    @Override
    public void editMessage(long chatId, Message message) {
        if(message instanceof MessageToEdit){
            MessageToEdit messageToEdit = (MessageToEdit) message;
            int messageId = messageToEdit.getMessageId();

            if(messageId != 0){
                EditMessageText editMessageText = new EditMessageText().setChatId(chatId)
                        .setMessageId(messageId)
                        .setText(messageToEdit.getMessage())
                        .setParseMode(ParseMode.MARKDOWN);
                try {
                    bot.execute(editMessageText);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
        if(message instanceof UserMessageWithReplyKeyboard){
            UserMessageWithReplyKeyboard messageToEdit = (UserMessageWithReplyKeyboard) message;
            SendMessage editMessage = new SendMessage().setChatId(chatId)
                                                       .setText(messageToEdit.getMessage())
                                                       .setReplyMarkup(new ReplyKeyboardRemove());

            try {
                bot.execute(editMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deleteMessage(long chatId, Message message) {
        if(message instanceof MessageToDelete){
            MessageToDelete messageToDelete = (MessageToDelete) message;
            int messageId = messageToDelete.getMessageId();

            DeleteMessage deleteMessage = new DeleteMessage().setChatId(Long.toString(chatId)).setMessageId(messageId);

            try {
                bot.execute(deleteMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendUserMessage(long chatId, UserMessage message){
        String text = message.getMessage();
        SendMessage sendMessage = new SendMessage().setChatId(chatId).setText(text).setParseMode(ParseMode.MARKDOWN);
        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendUserMessageWithKeyboard(long chatId, UserMessageWithKeyboard message){
        String text = message.getMessage();
        List<String> callbacks = message.getCallbacks();
        SendMessage sendMessage = new SendMessage().setChatId(chatId).setText(text).setParseMode(ParseMode.MARKDOWN);
        if(callbacks.size() == 4)
            sendMessage.setReplyMarkup(new InlineKeyBoard().diffChoice(callbacks));
        else if(callbacks.size() == 2){
            sendMessage.setReplyMarkup(new InlineKeyBoard().unsubscribeChoice(callbacks));
        }
        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendUserMessageWithReplyKeyboard(long chatId, UserMessageWithReplyKeyboard message){
        String text = message.getMessage();
        List<String> names = message.getNames();

        SendMessage sendMessage = new SendMessage().setChatId(chatId).setText(text).setParseMode(ParseMode.MARKDOWN);
        sendMessage.setReplyMarkup(new ReplyKeyBoard().subscribeChoice(names));

        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
