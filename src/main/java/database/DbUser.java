package database;

import database.models.*;
import org.bson.Document;

import java.util.*;

public class DbUser {
    private final long chatId;
    private final PendingBlock pendingBlock;
    private final List<Subscription> subscribes;
    private final Meta meta;

    public DbUser(long chatId, PendingBlock pendingBlock, List<Subscription> subscribes, Meta meta) {
        this.chatId = chatId;
        this.pendingBlock = pendingBlock;
        this.subscribes = subscribes;
        this.meta = meta;
    }

    public Meta getMeta() {
        return meta;
    }

    public long getChatId() {
        return chatId;
    }

    public PendingBlock getPendingBlock() {
        return pendingBlock;
    }

    public List<Subscription> getSubscriptions() {
        return subscribes;
    }

    public static DbUser from(Document document) {
        if (document == null) return null;
        long chatId = (long) document.get(UserRepository.USER_ID);

        Document pendingDoc = (Document) document.get(UserRepository.PENDING);
        boolean isPending = (boolean) pendingDoc.get(UserRepository.IS_PENDING);
        String callback = pendingDoc.get(UserRepository.BLOCK, String.class);
        PendingBlock pendingBlock = new PendingBlock(isPending, callback);

        ArrayList<Document> currencyDoc = (ArrayList<Document>) document.get(UserRepository.SUBSCRIBES);

        List<Subscription> subscribes = new ArrayList<>();
        if (currencyDoc != null) {
            for (Document subscribesDocument : currencyDoc) {
                String currency = (String) subscribesDocument.get(UserRepository.CURRENCY);
                String diff = (String) subscribesDocument.get(UserRepository.DIFF);
                double lastRate = Double.parseDouble(subscribesDocument.get(UserRepository.LAST_RATE,String.class));
                Date lastUpdateTime = (Date) subscribesDocument.get(UserRepository.LAST_UPDATE_TIME);
                subscribes.add(new Subscription(currency, diff, lastRate, lastUpdateTime));
            }
        }

        Document metaDoc = (Document) document.get(UserRepository.META);
        String currency = (String) metaDoc.get(UserRepository.CURRENCY);

        Meta meta = new Meta(currency);

        return new DbUser(chatId, pendingBlock, subscribes, meta);
    }
}
