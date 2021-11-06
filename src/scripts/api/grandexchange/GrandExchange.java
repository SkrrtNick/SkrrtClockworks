package scripts.api.grandexchange;

import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterface;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.Area;
import org.tribot.script.sdk.types.GrandExchangeOffer;
import org.tribot.script.sdk.types.WorldTile;
import org.tribot.script.sdk.walking.GlobalWalking;

public class GrandExchange extends org.tribot.api2007.GrandExchange {
    private static final int GRAND_EXCHANGE_MASTER = 465;
    private static final int COLLECT_BUTTON_CHILD = 6;
    private static final Area GRAND_EXCHANGE = Area.fromPolygon(
            new WorldTile[]{new WorldTile(3156, 3489, 0),
                    new WorldTile(3159, 3484, 0),
                    new WorldTile(3171, 3484, 0),
                    new WorldTile(3173, 3489, 0)});

    public static boolean moveToGE() {
        if (GRAND_EXCHANGE.containsMyPlayer()) {
            return true;
        } else {
            if (GlobalWalking.walkTo(GRAND_EXCHANGE.getRandomTile())) {
                return Waiting.waitUntilInArea(GRAND_EXCHANGE, 20000);
            }
        }
        return false;
    }

    public static boolean hasAvailableSlot() {
        return Query.grandExchangeOffers()
                .statusEquals(GrandExchangeOffer.Status.EMPTY)
                .isAny();
    }

    public static boolean clickCollect(boolean inventory) {
        String action = "Collect to bank";
        if (inventory) {
            action = "Collect to inventory";
        }
        RSInterface collectButton = Interfaces.findWhereAction(action,GRAND_EXCHANGE_MASTER);
        if (Interfaces.isInterfaceSubstantiated(collectButton)) {
            Waiting.waitNormal(100, 27);
            return collectButton.click(action);
        }
        return false;
    }

}
