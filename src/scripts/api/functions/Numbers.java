package scripts.api.functions;

import net.sourceforge.jdistlib.Normal;
import org.tribot.api.General;
import org.tribot.script.sdk.antiban.AntibanProperties;
import org.tribot.script.sdk.antiban.PlayerPreferences;

public class Numbers {

    public static int getDecision(String key, int skew){
        int preference = PlayerPreferences.preference(key, g -> g.normal(0,100,skew,25));
        return General.randomSD(preference,25);
    }

}
