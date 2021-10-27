package scripts.api.functions;

import org.tribot.script.sdk.Inventory;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.query.Query;

public class Interaction extends org.tribot.script.sdk.Interaction {

    public static boolean useOn(String itemName, String npc) {
        var selectItem = Query
                .inventory()
                .nameContains(itemName)
                .findFirst()
                .map(i -> i.click("Use"))
                .orElse(false);
        if ((Inventory.getSelected().isPresent() && Inventory.getSelected().get().getName().equals(itemName)) || selectItem) {
            Waiting.waitNormal(600, 98);
            return Query
                    .npcs()
                    .nameEquals(npc)
                    .maxDistance(10)
                    .findFirst()
                    .map(i -> i.click("Use")).orElse(false);
        }
        return false;
    }
}
