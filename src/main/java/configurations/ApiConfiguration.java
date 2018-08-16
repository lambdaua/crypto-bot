package configurations;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ApiConfiguration extends Configuration {

    @Valid
    @NotNull
    @JsonProperty
    private MongoClientFactory mongo;

    @JsonProperty
    private TlBotConfiguration tlBotConfiguration;

    public TlBotConfiguration getTlBotConfiguration() {
        return tlBotConfiguration;
    }

    public MongoClientFactory getMongo() {
        return mongo;
    }

    public static class TlBotConfiguration{
        @JsonProperty
        private String botUsername;

        @JsonProperty
        private String botToken;

        public String getBotUsername() {
            return botUsername;
        }

        public String getBotToken() {
            return botToken;
        }
    }
}
