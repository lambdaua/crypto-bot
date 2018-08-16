import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.mongodb.client.MongoDatabase;
import configurations.ApiConfiguration;
import configurations.ManagedMongoClient;
import configurations.TelegramManaged;
import controllers.TelegramController;
import cron.PriceNotifier;
import cron.PriceNotifierJob;
import data.coinmarketcap.CoinMarketCapRestService;
import database.UserService;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.jersey.jackson.JacksonMessageBodyProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import messenger.*;
import messenger.logger.EventLogger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static org.quartz.DateBuilder.futureDate;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

public class ApiApplication extends Application<ApiConfiguration> {
    public static void main(String[] args) throws Exception {
        new ApiApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<ApiConfiguration> bootstrap) {
        super.initialize(bootstrap);

        bootstrap.addBundle(new AssetsBundle("/assets", "/files"));

        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false))
        );

        bootstrap.getObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }

    @Override
    public void run(ApiConfiguration configuration, Environment environment) throws Exception {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            ManagedMongoClient mongoClient = configuration.getMongo().build();
            environment.lifecycle().manage(mongoClient);
            MongoDatabase database = mongoClient.getDatabase("db");

            ApiContextInitializer.init();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(CoinMarketCapRestService.baseUrl)
                    .addConverterFactory(JacksonConverterFactory.create(mapper))
                    .build();

            CoinMarketCapRestService coinMarketCapRestService = retrofit.create(CoinMarketCapRestService.class);

            UserService userService = new UserService(database, coinMarketCapRestService);
            EventLogger eventLogger = new EventLogger(userService);

            AndrewTelegramBot andrewTelegramBot = new AndrewTelegramBot(configuration.getTlBotConfiguration());

            TelegramMessenger telegramMessenger = new TelegramMessenger(andrewTelegramBot);
            MessagesReceiver tlMessagesReceiver = new MessagesReceiver(telegramMessenger, eventLogger, userService, mapper);

            PriceNotifier priceNotifier = new PriceNotifier(andrewTelegramBot, userService, coinMarketCapRestService);

            /*
             LongPooling for testing
             */
            LongPooling longPooling = new LongPooling(configuration.getTlBotConfiguration());
            longPooling.setMessagesReceiver(tlMessagesReceiver);
            TelegramBotsApi botsApi = new TelegramBotsApi();
            TelegramManaged telegramManaged = new TelegramManaged(botsApi, longPooling);
            environment.lifecycle().manage(telegramManaged);

            JobDetail cryptoJob = JobBuilder.newJob(PriceNotifierJob.class)
                    .withIdentity("cryptoJob", "group")
                    .build();

            Trigger currencyTrigger = TriggerBuilder.newTrigger()
                    .withIdentity("currencyTrigger", "group")
                    .withSchedule(simpleSchedule()
                            .withIntervalInMinutes(1)
                            .repeatForever())
                    .startAt(futureDate(5, DateBuilder.IntervalUnit.MINUTE))
                    .build();

            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.getContext().put(PriceNotifierJob.PRICE_NOTIFIER, priceNotifier);
            scheduler.start();
            scheduler.scheduleJob(cryptoJob, currencyTrigger);

            environment.jersey().register(new JacksonMessageBodyProvider(mapper));
            environment.jersey().register(new TelegramController(tlMessagesReceiver, andrewTelegramBot));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
