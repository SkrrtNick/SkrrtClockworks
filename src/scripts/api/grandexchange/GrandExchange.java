package scripts.api.grandexchange;

import org.tribot.script.sdk.MyPlayer;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.Area;
import org.tribot.script.sdk.types.WorldTile;
import org.tribot.script.sdk.walking.GlobalWalking;

public class GrandExchange extends org.tribot.api2007.GrandExchange {

    private static final Area GRAND_EXCHANGE = Area.fromPolygon(
            new WorldTile[]{ new WorldTile(3156, 3489, 0),
                    new WorldTile(3159, 3484, 0),
                    new WorldTile(3171, 3484, 0),
                    new WorldTile(3173, 3489, 0)});

    public static boolean open() {
        return Query.npcs().nameEquals("Grand Exchange Clerk").findBestInteractable().map(g -> g.interact("Exchange")).orElse(false);
    }

    public static boolean moveToGE() {
        if (GRAND_EXCHANGE.containsMyPlayer()){
            return true;
        } else {
            if(GlobalWalking.walkTo(GRAND_EXCHANGE.getRandomTile())){
                return Waiting.waitUntilInArea(GRAND_EXCHANGE, 20000);
            }
        } return false;
    }



}
