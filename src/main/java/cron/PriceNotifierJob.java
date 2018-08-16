package cron;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

public class PriceNotifierJob implements Job {

    public static final String PRICE_NOTIFIER = "priceNotifier";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        PriceNotifier priceNotifier;
        try {
            priceNotifier = (PriceNotifier) context.getScheduler().getContext().get(PRICE_NOTIFIER);
            priceNotifier.run();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
