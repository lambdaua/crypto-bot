package messenger;

import configurations.ApiConfiguration;
import org.telegram.telegrambots.ApiContext;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;

public class AndrewTelegramBot extends DefaultAbsSender {

    private ApiConfiguration.TlBotConfiguration configuration;

    public AndrewTelegramBot(ApiConfiguration.TlBotConfiguration configuration) {
        super(ApiContext.getInstance(DefaultBotOptions.class));
        this.configuration = configuration;
    }

    @Override
    public String getBotToken() {
        return configuration.getBotToken();
    }
}