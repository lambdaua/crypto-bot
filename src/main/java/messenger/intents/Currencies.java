package messenger.intents;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Currencies {

    private Map<Currency, List<String>> dictionary;

    public Currencies(ObjectMapper mapper) {
        try {
            List<Currency> currencies = Arrays.asList(mapper.readValue(new File("src/main/resources/supported_currencies.json"), Currency[].class));
            dictionary = new HashMap<>(currencies.size());

            for (Currency currency : currencies) {
                dictionary.put(currency, currency.getDictionary());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String currencyFor(String message) {
        final String in = message.toLowerCase();

        for (Map.Entry<Currency, List<String>> intent : dictionary.entrySet()) {
            String key = intent.getKey().getSymbol();
            List<String> values = intent.getValue();
            if (values.stream().anyMatch(m -> in.equals(m.toLowerCase()))) {
                return key;
            }
        }
        return "";
    }

/*    private static List<String> bitcoinCash = Arrays.asList("bch","bitcoin-cash","bitcoincash","bitkoin-cash","bitkoincash");
    private static List<String> ripple = Arrays.asList("xpr","ripple","riple","rippl","ripl");
    private static List<String> iota = Arrays.asList("miota","iota");
    private static List<String> dash = Arrays.asList("dash","dash");
    private static List<String> litecoin = Arrays.asList("ltc","litecoin","lite","litekoin");
    private static List<String> bitcoinGold = Arrays.asList("btg","bitcoingold","bitcoin-gold","bitkoin-gold","bitkoingold");
    private static List<String> cardano = Arrays.asList("ada","cardano");
    private static List<String> monero = Arrays.asList("xmr","monero","manero");
    private static List<String> ethereumClassic = Arrays.asList(
            "etc","ethereum-classic","ethereumclassic",
            "etheriumclassic","ethiriumclassic",
            "etereumclassic","eteriumclassic",
            "etherium-classic","ethirium-classic",
            "etereum-classic","eterium-classic");
    private static List<String> neo = Arrays.asList("neo","neo");
    private static List<String> nem = Arrays.asList("xem","nem");
    private static List<String> eos = Arrays.asList("eos","eos");
    private static List<String> stellar = Arrays.asList("xlm","stellar","stelar");
    private static List<String> lisk = Arrays.asList("lsk","lisk");
    private static List<String> bitconnect = Arrays.asList("bcc","bitconnect","bit-connect","bitconect","bit-conect");
    private static List<String> omisego = Arrays.asList("omg","omisego");
    private static List<String> qtum = Arrays.asList("qtum","qtum");
    private static List<String> zcash = Arrays.asList("zec","zcash","zkash");
    private static List<String> tether = Arrays.asList("usdt","tether","teter","tethr");
    private static List<String> stratis = Arrays.asList("strat","stratis");
    private static List<String> waves = Arrays.asList("waves","vaves","wavs","vavs");
    private static List<String> hshare = Arrays.asList("hsr","hshare");
    private static List<String> ardor = Arrays.asList("ardor","ardor");
    private static List<String> populous = Arrays.asList("populous","populos");
    private static List<String> monacoin = Arrays.asList("mona","monacoin","mona-coin","monakoin","mona-koin");
    private static List<String> bitshares = Arrays.asList("bts","bitshares","bitshare","bit-shares","bit-share");
    private static List<String> nxt = Arrays.asList("nxt","nxt");
    private static List<String> bytecoin = Arrays.asList("bcn","bytecoin","byte-coin","bytekoin");
    private static List<String> ark = Arrays.asList("ark","arc");
    private static List<String> vertcoin = Arrays.asList("vtc","vertcoin","vertkoin");
    private static List<String> augur = Arrays.asList("rep","augur","agur","augr");
    private static List<String> decred = Arrays.asList("dcr","decred","decret","dekret");
    private static List<String> steem = Arrays.asList("steem","stem");
    private static List<String> komodo = Arrays.asList("kmd","komodo","comodo");
    private static List<String> salt = Arrays.asList("salt","solt");
    private static List<String> qash = Arrays.asList("qash","qash");
    private static List<String> golemNetworkTokens = Arrays.asList(
            "gnt","golem-network-tokens","golemnetworktoken",
            "golemnetworktokens","golem-network-token",
            "golemnetwork","golemtoken","golemtokens");
    private static List<String> pivx = Arrays.asList("pivx","pivx");
    private static List<String> dogecoin = Arrays.asList("doge","dogecoin","dogcoin","doge-coin","dog-coin");
    private static List<String> siacoin = Arrays.asList("sc","siacoin","sia");
    private static List<String> tenx = Arrays.asList("pay","tenx");
    private static List<String> maidsafecoin = Arrays.asList("maid","midsafecoin","midsafe","midsavecoin","midsave");
    private static List<String> binanceCoin = Arrays.asList("bnb","binancecoin");
    private static List<String> status = Arrays.asList("snt","status");
    private static List<String> walton = Arrays.asList("wtc","walton");
    private static List<String> powerLedger = Arrays.asList("powr","powerledger","powerleder");
    private static List<String> digixdao = Arrays.asList("dgd","digixdao");
    private static List<String> factom = Arrays.asList("fct","factom");*/


    /*public static final Map<String, List<String>> intents;

    static {
        intents = new HashMap<>();
        intents.put("BTC", bitcoin);
        intents.put("ETH", ethereum);*/

       /* intents.put("bitcoin-cash",bitcoinCash);
        intents.put("ripple",ripple);
        intents.put("iota",iota);
        intents.put("dash",dash);
        intents.put("litecoin",litecoin);
        intents.put("bitcoin-gold",bitcoinGold);
        intents.put("cardano",cardano);
        intents.put("monero",monero);
        intents.put("ethereum-classic",ethereumClassic);
        intents.put("neo",neo);
        intents.put("nem",nem);
        intents.put("eos",eos);
        intents.put("stellar",stellar);
        intents.put("lisk",lisk);
        intents.put("bitconnect",bitconnect);
        intents.put("omisego",omisego);
        intents.put("qtum",qtum);
        intents.put("zcash",zcash);
        intents.put("tether",tether);
        intents.put("stratis",stratis);
        intents.put("waves",waves);
        intents.put("hshare",hshare);
        intents.put("ardor",ardor);
        intents.put("populous",populous);
        intents.put("monacoin",monacoin);
        intents.put("bitshares",bitshares);
        intents.put("nxt",nxt);
        intents.put("bytecoin-bcn",bytecoin);
        intents.put("ark",ark);
        intents.put("vertcoin",vertcoin);
        intents.put("augur",augur);
        intents.put("decred",decred);
        intents.put("steem",steem);
        intents.put("komodo",komodo);
        intents.put("salt",salt);
        intents.put("qash",qash);
        intents.put("golem-network-tokens",golemNetworkTokens);
        intents.put("pivx",pivx);
        intents.put("dogecoin",dogecoin);
        intents.put("siacoin",siacoin);
        intents.put("tenx",tenx);
        intents.put("maidsafecoin",maidsafecoin);
        intents.put("binance-coin",binanceCoin);
        intents.put("status",status);
        intents.put("walton",walton);
        intents.put("power-ledger",powerLedger);
        intents.put("digixdao",digixdao);
        intents.put("factom",factom);*/
}
//}
