package database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class UserRepository {

    private static final String USERS = "users";
    static final String PENDING = "pending";
    static final String IS_PENDING = "is_pending";
    static final String BLOCK = "block";
    static final String USER_ID = "user_id";
    static final String SUBSCRIBES = "subscribes";
    static final String CURRENCY = "currency";
    static final String DIFF = "diff";
    static final String LAST_RATE = "last_rate";
    static final String LAST_UPDATE_TIME = "last_update_time";
    static final String META = "meta";

    private MongoCollection<Document> users;

    public UserRepository(MongoDatabase database) {
        users = database.getCollection(USERS);
    }

    public void insert(long chatId) {
        Document document = new Document();

        Document pendingDoc = new Document().append(IS_PENDING, false)
            .append(BLOCK, PENDING + "." + BLOCK);

        Document metaDoc = new Document()
            .append(CURRENCY, "");

        document.append(USER_ID, chatId)
            .append(PENDING, pendingDoc)
            .append(META, metaDoc);

        users.insertOne(document);
    }

    public void setPending(long chatId, boolean pendingStatus, String block) {
        Bson filter = Filters.eq(USER_ID, chatId);
        Bson updateStatus = Updates.set(PENDING + "." + IS_PENDING, pendingStatus);
        Bson updateBlock = Updates.set(PENDING + "." + BLOCK, block);

        Bson update = Updates.combine(Arrays.asList(updateStatus, updateBlock));
        users.updateOne(filter, update);
    }

    public DbUser get(long chatId) {
        Bson filter = Filters.eq(USER_ID, chatId);

        Document document = users.find(filter).first();

        return DbUser.from(document);
    }

    public List<DbUser> getAll() {
        return users.find()
            .map(DbUser::from)
            .into(new ArrayList<>());
    }

    public void addSubscribe(long chatId, String rate, String diff) {
        Bson filter = Filters.eq(USER_ID, chatId);
        Bson duplicatedFilter = Filters.and(Filters.eq(USER_ID, chatId), Filters.eq(LAST_RATE, rate), Filters.eq(DIFF, diff));
        Date date = new Date();

        DbUser dbUser = get(chatId);
        String currency = dbUser.getMeta().getCurrency();

        Document duplicatedFind = users.find(duplicatedFilter).first();
        if (duplicatedFind != null && !duplicatedFind.isEmpty()) {
            return;
        }

        Document append = new Document().append(CURRENCY, currency)
            .append(DIFF, diff.replaceAll("\\D+", ""))
            .append(LAST_RATE, rate)
            .append(LAST_UPDATE_TIME, date);

        Bson update = Updates.push(SUBSCRIBES, append);
        users.updateOne(filter, update);
    }

    public void setCurrency(long chatId, String currency) {
        Bson filter = Filters.eq(USER_ID, chatId);

        Bson update = Updates.set(META + "." + CURRENCY, currency);
        users.updateOne(filter, update);
    }

    public void setRate(long chatId, String currency, String rate) {
        Date date = new Date();

        Bson filter = Filters.eq(USER_ID, chatId);

        List<Document> subscribes = (ArrayList<Document>) users.find(filter).first().get(SUBSCRIBES);

        for (Document subscribe : subscribes) {
            if (subscribe.get(CURRENCY).toString().equals(currency)) {
                subscribe.put(LAST_RATE, rate);
                subscribe.put(LAST_UPDATE_TIME, date);
            }
        }

        Bson update = Updates.set(SUBSCRIBES, subscribes);
        users.updateOne(filter, update);
    }

    public void deleteCurrencySubscribe(long chatId, String currency, String diffToDelete) {
        Bson filter = Filters.eq(USER_ID, chatId);

        List<Document> subscribes = (ArrayList<Document>) users.find(filter).first().get(SUBSCRIBES);

        for (int i = 0; i < subscribes.size(); i++) {
            if (subscribes.get(i).get(CURRENCY).toString().equals(currency) &&
                subscribes.get(i).get(DIFF).toString().equals(diffToDelete.replaceAll("\\D+", ""))) {
                subscribes.remove(i);
            }
        }
        Bson update = Updates.set(SUBSCRIBES, subscribes);
        users.updateOne(filter, update);
    }

    public void MigrateFromFullNameToSymbol() {
        ArrayList<Document> allUsers = users.find().into(new ArrayList<>());

        Document metaDoc = new Document()
            .append(CURRENCY, "");

        for (Document user : allUsers) {
            Bson filter = Filters.eq(USER_ID, user.get(USER_ID));
            List<Document> subscribes = (ArrayList<Document>) user.get(SUBSCRIBES);

            for (Document subscribe : subscribes) {
                subscribe.replace(CURRENCY, "bitcoin", "BTC");
                subscribe.replace(CURRENCY, "ethereum", "ETH");
            }

            Document pendingChange = (Document) user.get(PENDING);
            pendingChange.replace(BLOCK, "no.pending", "");
            user.append(META, metaDoc);
            users.replaceOne(filter, user);
        }
    }

}

