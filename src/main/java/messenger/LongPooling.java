package messenger;

import configurations.ApiConfiguration;
import mapper.LambdaMessage;
import mapper.TLMapper;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

public class LongPooling extends TelegramLongPollingBot {

    private MessagesReceiver messagesReceiver;
    private ApiConfiguration.TlBotConfiguration tlBotConfiguration;
    private final TLMapper tlMapper;

    public LongPooling(ApiConfiguration.TlBotConfiguration tlBotConfiguration) {
        this.tlBotConfiguration = tlBotConfiguration;
        tlMapper = new TLMapper();
    }

    public void setMessagesReceiver(MessagesReceiver messagesReceiver) {
        this.messagesReceiver = messagesReceiver;
    }

    @Override
    public void onUpdateReceived(Update update) {
        LambdaMessage lambdaMessage = tlMapper.map(update);
        messagesReceiver.handle(lambdaMessage);
    }

    @Override
    public String getBotUsername() {
        return tlBotConfiguration.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return tlBotConfiguration.getBotToken();
    }
}
