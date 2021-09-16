package scripts.tasks;

import dax.walker_engine.interaction_handling.NPCInteraction;
import org.tribot.api.General;
import org.tribot.script.sdk.*;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.walking.GlobalWalking;
import scripts.api.framework.Priority;
import scripts.api.framework.Task;
import scripts.api.functions.Numbers;
import scripts.api.poh.PoH;
import scripts.data.ItemID;

public class Clockworks implements Task {
    public static int clockworks = 0;
    static boolean useButler = true;

    @Override
    public Priority priority() {
        return Priority.HIGH;
    }

    @Override
    public boolean validate() {
        return conditionsMet();
    }

    @Override
    public void execute() {
        if (hasRequiredItems() && !PoH.isInHouse() && !PoH.isNearPortal()) {
            Interfaces.closeAll();
            var teleHome = Query.inventory()
                    .idEquals(ItemID.TELEPORT_TO_HOUSE)
                    .findFirst()
                    .map(i -> i.click("Break"))
                    .orElse(false);
            if (teleHome) {
                Waiting.waitUntil(() -> PoH.isInHouse() || PoH.isNearPortal());
            }
        } else if (hasRequiredItems() && PoH.isNearPortal() && !PoH.isInHouse()) {
            if (PoH.enterPortal()) {
                if (Waiting.waitUntil(PoH::isInHouse)) {
                    Waiting.waitNormal(400, 175);
                    General.println("We have entered the house");
                }
            }
        }
        if (hasRequiredItems() && PoH.isInHouse()) {
            if (!PoH.hasWorkshop()) {
                General.println("We need a workshop!");
            } else {
                if (Interaction.interactObj("Clockmaker's bench", "Craft")) {
                    if (Waiting.waitUntil(NPCInteraction::isConversationWindowUp)) {
                        if (Numbers.getDecision("make clockworks", 75) > 75) {
                            NPCInteraction.handleConversation("Clockwork mechanism");
                        } else {
                            NpcChat.getOptions()
                                    .stream()
                                    .filter(i -> i.contains("Clockwork mechanism"))
                                    .findFirst()
                                    .map(NpcChat::selectOption);
                        }
                        clockworks++;
                        Waiting.waitNormal(1800, 300);
                    }
                }
            }
        }
        if (!hasRequiredItems()) {
                if (Bank.isNearby()) {
                    if (Bank.openBank()) {
                        if (Bank.depositAll(ItemID.CLOCKWORK)) {
                            Waiting.waitNormal(600, 98);
                        }
                        if (Bank.withdrawAll(ItemID.STEEL_BAR)) {
                            Waiting.waitNormal(600, 98);
                        } else {
                            General.println("Did we fail to withdraw all?");
                        }
                    }
                } else {
                    if (GlobalWalking.walkToBank()) {
                        Waiting.waitUntil(Bank::isNearby);
                    } else {
                        if (PoH.leaveHouse()) {
                            Waiting.waitNormal(1200, 350);
                        }
                    }
                }
            }
    }

    boolean conditionsMet() {
        return Skill.CRAFTING.getActualLevel() >= 8 && Skill.CONSTRUCTION.getActualLevel() >= 25;
    }

    boolean hasRequiredItems() {
        return Inventory.getCount(ItemID.COINS_995) > 367 && Inventory.getCount(ItemID.STEEL_BAR) > 0;
    }

}
