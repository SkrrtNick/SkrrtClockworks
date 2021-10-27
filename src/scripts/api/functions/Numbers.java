package scripts.api.functions;

import org.tribot.api.General;
import org.tribot.script.sdk.antiban.PlayerPreferences;

import java.util.concurrent.TimeUnit;

public class Numbers {

    public static int getDecision(String key, int skew) {
        int preference = PlayerPreferences.preference(key, g -> g.normal(0, 100, skew, 25));
        return General.randomSD(preference, 25);
    }

    public static String getHourly(long startTime, int total) {

        return " (" + (Math.round(total / ((System.currentTimeMillis() - startTime) / 3600000.0D))) + ")";

    }

    public static String getHumanisedRuntime(long startTime) {
        long runtime = System.currentTimeMillis() - startTime;
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(runtime),
                TimeUnit.MILLISECONDS.toMinutes(runtime) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(runtime) % TimeUnit.MINUTES.toSeconds(1));
    }
}
