package configurations;

import io.dropwizard.lifecycle.Managed;
import messenger.LongPooling;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.generics.BotSession;

public class TelegramManaged implements Managed {

    private final TelegramBotsApi botsApi;
    private final LongPooling lPesaBot;
    private BotSession botSession;

    public TelegramManaged(TelegramBotsApi botsApi, LongPooling lPesaBot) {
        this.botsApi = botsApi;
        this.lPesaBot = lPesaBot;
    }

    @Override
    public void start() throws Exception {
        botSession = botsApi.registerBot(lPesaBot);
    }

    @Override
    public void stop() throws Exception {
        botSession.stop();
    }
}