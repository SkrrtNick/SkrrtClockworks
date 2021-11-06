package scripts.tasks;

import dax.walker_engine.interaction_handling.NPCInteraction;
import lombok.Getter;
import org.tribot.api2007.NPCChat;
import org.tribot.script.interfaces.MessageListening07;
import org.tribot.script.sdk.*;
import org.tribot.script.sdk.input.Keyboard;
import org.tribot.script.sdk.query.Query;
import scripts.SkrrtClockWork;
import scripts.api.framework.Priority;
import scripts.api.framework.Task;
import scripts.api.functions.Logger;
import scripts.api.functions.Numbers;
import scripts.api.poh.PoH;
import scripts.data.ItemID;
import scripts.data.Profile;

public class MakingClockworks implements Task, MessageListening07 {
    @Getter
    private static int clockworks = 0;
    private String[] chatResponses = {"Yes", "Clockwork mechanism", "Okay, here's 10,000 coins."};
    private final int AMOUNT_INTERFACE = 162;
    Logger logger = new Logger().setHeader("Clockworks");

    @Override
    public Priority priority() {
        return Priority.HIGH;
    }

    @Override
    public boolean validate() {
        return PoH.isInHouse() && conditionsMet() && !Restocking.isShouldRestock();
    }

    @Override
    public void execute() {
        SkrrtClockWork.setStatus("Making Clockworks");
        if (!PoH.hasWorkshop()) {
            logger.setMessage("Terminating script, we need a workshop!").print();
            SkrrtClockWork.setRunning(false);
        }
        handleButler();

        if (Inventory.contains(ItemID.STEEL_BAR) && Interaction.interactObj("Clockmaker's bench", "Craft")) {
            if (Waiting.waitUntil(() -> Query.widgets().textEquals("Clockwork mechanism").findFirst().isPresent())) {
                if (Numbers.getDecision("make clockworks", 75) > 75) {
                    ChatScreen.handle(chatResponses);
                } else {
                    NPCChat.selectOption("Clockwork mechanism", true);
                }
                clockworks++;
                Waiting.waitNormal(1800, 300);
            }
        }

    }

    private boolean conditionsMet() {
        return Skill.CRAFTING.getActualLevel() >= 8 && Skill.CONSTRUCTION.getActualLevel() >= 25 && hasRequiredItems();
    }

    public static boolean hasRequiredItems() {
        return Inventory.getCount(ItemID.COINS_995) > 367 && ((Inventory.getCount(ItemID.STEEL_BAR) > 0) || (getProfile().isUseButler() && Inventory.getCount(ItemID.STEEL_BAR + 1) > 0));
    }

    private static Profile getProfile() {
        return SkrrtClockWork.getRunningProfile();
    }

    private boolean shouldUnnote() {
        return !Inventory.contains(ItemID.STEEL_BAR) && Inventory.contains(ItemID.STEEL_BAR + 1) && !Inventory.isFull();
    }

    private boolean shouldSendClockworks() {
        return Inventory.contains(ItemID.CLOCKWORK) && !Inventory.contains(ItemID.STEEL_BAR);
    }


    private boolean handleButler() {
        int itemID = 0;
        int amount = 0;
        if (!getProfile().isUseButler() || (!shouldUnnote() && !shouldSendClockworks())) {
            return true;
        }
        if (shouldUnnote()) {
            itemID = ItemID.STEEL_BAR + 1;
            amount = 28 - Inventory.getAll().size();
            if (amount > Inventory.getCount(ItemID.STEEL_BAR + 1)) {
                amount = Inventory.getCount(ItemID.STEEL_BAR + 1);
            }
        }
        if (shouldSendClockworks()) {
            itemID = ItemID.CLOCKWORK;
            amount = Inventory.getCount(ItemID.CLOCKWORK);
        }
        if (PoH.callButler()) {
            if (useOnButler(itemID)) {
                Waiting.waitNormal(600, 175);
                Waiting.waitUntil(NPCInteraction::isConversationWindowUp);
            }
        }
        if (ChatScreen.isOpen()) {
            if (ChatScreen.handle(chatResponses)) {
                Waiting.waitNormal(600, 175);
            }
            var enterAmount = Query.widgets()
                    .inRoots(AMOUNT_INTERFACE)
                    .textContains("Enter amount:")
                    .isVisible()
                    .findFirst()
                    .isPresent();
            if (!Waiting.waitUntil(3000, () -> enterAmount)) return false;
            Waiting.waitNormal(600, 98);
            Keyboard.typeLine(String.valueOf(amount));
            Waiting.waitNormal(600,175);
        }
        // waiting for demon butler to return
        if (Waiting.waitUntil(8000, NPCInteraction::isConversationWindowUp)) {
            if (ChatScreen.handle(chatResponses)) {
                Waiting.waitNormal(600, 175);
            }
        }
        int finalItemID = itemID;
        if (Waiting.waitUntil(() -> Inventory.contains(finalItemID))) {
            logger.setMessage("Success, we've handled the butler.").print();
        }
        return true;
    }

    private boolean useOnButler(int itemID) {
        return Query.npcs()
                .nameEquals("Demon butler")
                .findFirst()
                .map(i ->
                        Query.inventory()
                                .idEquals(itemID)
                                .findFirst()
                                .map(s -> s.useOn(i))
                                .orElse(false))
                .orElse(false);
    }
}
