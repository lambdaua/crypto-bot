package messenger.intents;

import java.util.List;

/**
 * @author by AlexBlokh, 12/7/17 (aleksandrblokh@gmail.com)
 */
public class Currency {
    private String key;
    private String symbol;
    private String name;
    private List<String> dictionary;

    public Currency() {
    }

    public Currency(String key, String symbol, String name, List<String> dictionary) {
        this.key = key;
        this.symbol = symbol;
        this.name = name;
        this.dictionary = dictionary;
    }

    public List<String> getDictionary() {
        return dictionary;
    }

    public String getKey() {
        return key;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }
}
