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
import scripts.api.entityselector.Entities;
import scripts.api.entityselector.finders.prefabs.ObjectEntity;

public class PoH {
    private static final int BUILD_MODE_SETTING = 780;
    private static final int HOUSE_BUTTON_MASTER = 116;
    private static final int HOUSE_BUTTON_CHILD = 72;
    private static final int HOUSE_OPTIONS_MASTER = 370;
    private static final int LEAVE_HOUSE_CHILD = 18;

    public static boolean inBuildMode() {
        return Game.getSetting(BUILD_MODE_SETTING) == 1;
    }

    public static boolean leaveHouse() {
        if (!isInHouse()) {
            return true;
        }
        int preference = PlayerPreferences.preference("Leave house", g -> g.uniform(0, 100));
        int decision = General.randomSD(preference, 25);
        if (decision <= 50) {
            if (!GameTab.OPTIONS.isOpen()) {
                Waiting.waitUntil(GameTab.OPTIONS::open);
            } else {
                RSInterface buildBtn = Interfaces.findWhereAction("View House Options");
                if (Interfaces.isInterfaceSubstantiated(buildBtn)) {
                    if (buildBtn.click()) {
                        Waiting.waitNormal(600, 175);
                        Waiting.waitUntil(() -> Interfaces.isInterfaceSubstantiated(HOUSE_OPTIONS_MASTER, LEAVE_HOUSE_CHILD));
                    }
                }
                RSInterface leaveBtn = Interfaces.findWhereAction("Leave house");
                if (Interfaces.isInterfaceSubstantiated(leaveBtn)) {
                    if (leaveBtn.click()) {
                        Waiting.waitNormal(1000, 150);
                        return true;
                    }
                }
            }
        } else {
            if (Interaction.interactObj("Portal", "Enter")) {
                Waiting.waitNormal(1000, 150);
                return true;
            }
        }
        return false;
    }

    public static boolean isInHouse() {
        RSObject[] hotspot = Entities.find(ObjectEntity::new)
                .nameContains("hotspot")
                .getResults();
        return hotspot.length > 0;
    }

    public static boolean hasWorkshop() {
        RSObject[] workbench = Entities.find(ObjectEntity::new)
                .nameContains("Workbench")
                .getResults();
        return workbench.length > 0;
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

    public static boolean hasDemonButler(){
        return Query.npcs()
                .nameEquals("Demon butler")
                .findFirst()
                .isPresent();
    }



}
