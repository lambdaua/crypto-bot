package database;

import com.mongodb.client.MongoDatabase;
import data.coinmarketcap.CoinMarketCapRestService;
import data.coinmarketcap.Currency;

import java.io.IOException;
import java.util.List;

public class UserService {
    private UserRepository repository;
    private CoinMarketCapRestService service;

    public UserService(MongoDatabase database, CoinMarketCapRestService service) {
        this.repository = new UserRepository(database);
        this.service = service;
    }

    public void addUser(long chatId) {
        if (repository.get(chatId) == null)
            repository.insert(chatId);
    }

    public void setPending(long chatId, boolean pendingStatus, String block) {
        repository.setPending(chatId, pendingStatus, block);
    }

    public void addSubscribe(long chatId, String diff) {
        String rate = "";

        DbUser dbUser = get(chatId);
        String currency = dbUser.getMeta().getCurrency();

        try {
            List<Currency> all = service.listTickers().execute().body();
            if (all.stream().anyMatch(name -> name.getSymbol().toUpperCase().equals(currency.toUpperCase())))
                rate = all.stream().filter(name -> name.getSymbol().toUpperCase().equals(currency.toUpperCase())).findFirst().get().getPriceUsd();

            repository.addSubscribe(chatId, rate, diff);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCurrency(long chatId, String currency) {
        repository.setCurrency(chatId, currency);
    }

    public void setRate(long chatId, String currency, String rate) {
        repository.setRate(chatId, currency, rate);
    }

    public void didUnsubscribeCurrency(long chatId, String currency, String diffToDelete) {
        repository.deleteCurrencySubscribe(chatId, currency, diffToDelete);
    }

    public DbUser get(long chatId) {
        return repository.get(chatId);
    }

    public List<DbUser> getAll() {
        return repository.getAll();
    }
}
