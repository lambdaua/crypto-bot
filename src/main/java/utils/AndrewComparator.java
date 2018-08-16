package utils;

public class AndrewComparator {

    public static boolean compareTimes(long t1, long t2, long diff) {
        return Math.abs(t1 - t2) >= diff;
    }
}
