package scripts.tasks;

import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.Widgets;
import org.tribot.script.sdk.query.Query;
import scripts.SkrrtClockWork;
import scripts.api.framework.Priority;
import scripts.api.framework.Task;
import scripts.api.functions.Logger;
import scripts.api.poh.PoH;
import scripts.data.ItemID;

public class MovingToHouse implements Task {
    private Logger logger = new Logger().setHeader("POH");

    @Override
    public Priority priority() {
        return Priority.HIGH;
    }

    @Override
    public boolean validate() {
        return !Restocking.isShouldRestock() && !PoH.isInHouse() && MakingClockworks.hasRequiredItems();
    }

    @Override
    public void execute() {
        SkrrtClockWork.setStatus("Moving to POH");
        Widgets.closeAll();
        if (!PoH.isNearPortal()) {
            var teleHome = Query.inventory()
                    .idEquals(ItemID.TELEPORT_TO_HOUSE)
                    .findFirst()
                    .map(i -> i.click("Break"))
                    .orElse(false);
            if (teleHome) {
                Waiting.waitUntil(() -> PoH.isInHouse() || PoH.isNearPortal());
            }
        } else {
            if (PoH.enterPortal() && Waiting.waitUntil(PoH::isInHouse)) {
                Waiting.waitNormal(800, 175);
                logger.setMessage("We have entered the house").print();
            }
        }
    }
}

