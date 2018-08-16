package cron;

import com.vdurmont.emoji.EmojiParser;
import data.coinmarketcap.CoinMarketCapRestService;
import data.coinmarketcap.Currency;
import database.DbUser;
import database.UserService;
import database.models.Subscription;
import messenger.AndrewTelegramBot;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import utils.AndrewComparator;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PriceNotifier {

    private static final String MESSAGE = "%s %s $%s\n%s%.03f%% (%s$%.01f)";

    private AndrewTelegramBot bot;
    private UserService userService;
    private CoinMarketCapRestService service;

    public PriceNotifier(AndrewTelegramBot bot,
                         UserService userService,
                         CoinMarketCapRestService service) {
        this.bot = bot;
        this.userService = userService;
        this.service = service;
    }

    public void run() {
        try {
            List<Currency> currencies = service.listTickers().execute().body();
            List<DbUser> users = userService.getAll();

            for (DbUser user : users) {
                if (user.getPendingBlock().isPending()) continue;

                List<Subscription> userSubscribes = user.getSubscriptions();
                if (userSubscribes.size() == 0) continue;

                for (Subscription subscription : userSubscribes) {

                    for (Currency currency : currencies) {
                        String userCurrency = subscription.getCurrency();
                        if (!currency.getSymbol().toLowerCase().equals(userCurrency)) continue;

                        Date lastUpdateTime = subscription.getLastUpdateTime();

                        String newPrice = currency.getPriceUsd();

                        double lastRate = subscription.getLastRate();
                        double newRate = Double.parseDouble(newPrice);

                        double percentDifference = getPercentDifference(lastRate, newRate);
                        double absRateDifference = Math.abs(newRate - lastRate);

                        double userDiff = Double.parseDouble(subscription.getDiff());

                        if (shouldNotifyNow(lastUpdateTime.getTime()) && isPercentDifferenceHigher(lastRate, newRate, userDiff)) {

                            String text = getMessageText(userCurrency, newPrice,
                                    percentDifference, absRateDifference);

                            SendMessage sendMessage = new SendMessage()
                                    .setChatId(user.getChatId())
                                    .setText(text);

                            try{
                                bot.execute(sendMessage);
                            }catch (TelegramApiRequestException e){
                                //ToDo set to user block flag
                            }
                            userService.setRate(user.getChatId(), userCurrency, newPrice);
                        }
                    }
                }
            }
        } catch (TelegramApiException | IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean shouldNotifyNow(long lastUpdateTime) {
        return AndrewComparator.compareTimes(lastUpdateTime, System.currentTimeMillis(), TimeUnit.MINUTES.toMillis(30));
    }

    public static double getPercentDifference(double lastRate, double newRate) {
        return (lastRate - newRate) / -lastRate * 100f;
    }

    public static boolean isPercentDifferenceHigher(double lastRate, double newRate, double userDiff) {
        return Math.abs(getPercentDifference(lastRate, newRate)) >= userDiff;
    }

    public static String getMessageText(String userCurrency, String newPrice,
                                        double percentDifference, double absRateDifference) {

        boolean differencePositive = percentDifference >= 0.0;

        String sign = differencePositive ? "+" : "-";
        String emoji = differencePositive ? ":point_up:" : ":point_down:";

        String text = String.format(MESSAGE, userCurrency.toUpperCase(), emoji, newPrice,
                sign, Math.abs(percentDifference), sign, absRateDifference);

        return EmojiParser.parseToUnicode(text);
    }
}