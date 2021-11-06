package scripts.api.poh;

import org.tribot.api.General;
import org.tribot.api2007.Game;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSObject;
import org.tribot.script.sdk.GameTab;
import org.tribot.script.sdk.Interaction;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.antiban.PlayerPreferences;
import org.tribot.script.sdk.query.Query;


public class PoH {
    private static final int BUILD_MODE_SETTING = 780;
    private static final int HOUSE_BUTTON_MASTER = 116;
    private static final int HOUSE_BUTTON_CHILD = 72;
    private static final int HOUSE_OPTIONS_MASTER = 370;
    private static final int LEAVE_HOUSE_CHILD = 18;

    public static boolean leaveHouse() {
        if (!isInHouse()) {
            return true;
        }
        int preference = PlayerPreferences.preference("Leave house", g -> g.uniform(0, 100));
        int decision = General.randomSD(preference, 25);
        if (decision <= 50) {
            if (openHouseOptions()) {
                RSInterface leaveBtn = Interfaces.findWhereAction("Leave house");
                if (Interfaces.isInterfaceSubstantiated(leaveBtn)) {
                    if (leaveBtn.click()) {
                        Waiting.waitNormal(2000, 220);
                        return true;
                    }
                }
            }
        } else {
            if (Interaction.interactObj("Portal", "Enter")) {
                Waiting.waitNormal(2000, 220);
                return true;
            }
        }
        return false;
    }

    public static boolean isInHouse() {
        return Query.gameObjects()
                .nameContains("hotspot")
                .findFirst()
                .isPresent();
    }

    public static boolean hasWorkshop() {
        return Query.gameObjects()
                .nameContains("Workbench")
                .findFirst()
                .isPresent();
    }

    public static boolean enterPortal() {
        return Query.gameObjects()
                .nameEquals("Portal")
                .findFirst()
                .map(p -> p.interact("Build mode") && Waiting.waitUntil(PoH::isInHouse))
                .orElse(false);
    }

    public static boolean isNearPortal() {
        return Query.gameObjects()
                .nameEquals("Portal")
                .findFirst()
                .isPresent();
    }


    private static boolean openHouseOptions() {
        if (!GameTab.OPTIONS.isOpen()) {
            Waiting.waitUntil(GameTab.OPTIONS::open);
        }
        RSInterface buildBtn = Interfaces.findWhereAction("View House Options");
        if (Interfaces.isInterfaceSubstantiated(buildBtn)) {
            if (buildBtn.click()) {
                Waiting.waitNormal(600, 175);
                return Waiting.waitUntil(() -> Interfaces.isInterfaceSubstantiated(HOUSE_OPTIONS_MASTER, LEAVE_HOUSE_CHILD));
            }
        }
        return false;
    }

    public static boolean callButler() {
        int distance = PlayerPreferences.preference("distance",d->d.uniform(3,8));
        if(Query.npcs().nameContains("butler").maxDistance(distance).findFirst().isPresent()){
            return true;
        }
        if (openHouseOptions()) {
            RSInterface callServant = Interfaces.findWhereAction("Call Servant");
            if(Interfaces.isInterfaceSubstantiated(callServant)){
                if(callServant.click("Call Servant")){
                    Waiting.waitNormal(600, 175);
                    if(Query.npcs().nameContains("butler").maxDistance(3).findFirst().isPresent()){
                        return Query.widgets()
                                .inRoots(HOUSE_OPTIONS_MASTER)
                                .actionEquals("Close")
                                .findFirst()
                                .map(i -> i.click("Close"))
                                .orElse(false);
                    }
                }
            }
        } return false;
    }

}
