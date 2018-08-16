package cron;

import com.vdurmont.emoji.EmojiParser;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class PriceNotifierTest {

    private static final String MESSAGE_SENT = "%s %s $%s\n%s%.03f%% (%s$%.01f)";

    @Test
    public void shouldNotifyNowTest() throws Exception {
        long now = System.currentTimeMillis();
        long min20 = TimeUnit.MINUTES.toMillis(20);
        long min29 = TimeUnit.MINUTES.toMillis(29);
        long min31 = TimeUnit.MINUTES.toMillis(31);
        long min40 = TimeUnit.MINUTES.toMillis(40);

        assertTrue(PriceNotifier.shouldNotifyNow(now - min40));
        assertTrue(PriceNotifier.shouldNotifyNow(now - min31));
        assertFalse(PriceNotifier.shouldNotifyNow(now - min29));
        assertFalse(PriceNotifier.shouldNotifyNow(now - min20));
    }

    @Test
    public void getPercentDifferenceTest() throws Exception {
        assertEquals(5.0, PriceNotifier.getPercentDifference(10000.0, 10500.0), 0.0);
        assertEquals(-5.0, PriceNotifier.getPercentDifference(10000.0, 9500.0), 0.0);
        assertEquals(1.0, PriceNotifier.getPercentDifference(12000.0, 12120.0), 0.0);
        assertEquals(-1.0, PriceNotifier.getPercentDifference(12000.0, 11880.0), 0.0);
        assertEquals(10.0, PriceNotifier.getPercentDifference(14000.0, 15400.0), 0.0);
        assertEquals(-10.0, PriceNotifier.getPercentDifference(14000.0, 12600.0), 0.0);

        assertNotEquals(5.0, PriceNotifier.getPercentDifference(10000.0, 9500.0), 0.0);
        assertNotEquals(-5.0, PriceNotifier.getPercentDifference(10000.0, 10500.0), 0.0);
    }

    @Test
    public void isPercentDifferenceHigherTest() throws Exception {
        assertFalse(PriceNotifier.isPercentDifferenceHigher(10000.0,10500.0,5.01));
        assertTrue(PriceNotifier.isPercentDifferenceHigher(10000.0,9500.0,5.00));
        assertTrue(PriceNotifier.isPercentDifferenceHigher(14000.0,15400.0,9.01));
        assertTrue(PriceNotifier.isPercentDifferenceHigher(14000.0,12600.0,8.01));
    }

    @Test
    public void getMessageTextTest() throws Exception {
        double positivePercentDifference = PriceNotifier.getPercentDifference(10000.0, 10500.0);
        double negativePercentDifference = PriceNotifier.getPercentDifference(10000.0, 9500.0);

        String actualPositiveMessage = PriceNotifier.getMessageText("btc", "10500.0",
                positivePercentDifference, 500.0);
        String actualNegativeMessage = PriceNotifier.getMessageText("btc", "9500.0",
                negativePercentDifference, 500.0);

        String expectedPositiveMessage = String.format(MESSAGE_SENT, "BTC", ":point_up:", "10500.0",
                "+", positivePercentDifference, "+", 500.0);
        String expectedNegativeMessage = String.format(MESSAGE_SENT, "BTC", ":point_down:", "9500.0",
                "-", positivePercentDifference, "-", 500.0);

        assertEquals(EmojiParser.parseToUnicode(expectedPositiveMessage), actualPositiveMessage);
        assertEquals(EmojiParser.parseToUnicode(expectedNegativeMessage), actualNegativeMessage);
    }
}

