package scripts.tasks;

import dax.walker_engine.interaction_handling.NPCInteraction;
import lombok.Getter;
import org.tribot.api2007.NPCChat;
import org.tribot.script.interfaces.MessageListening07;
import org.tribot.script.sdk.*;
import org.tribot.script.sdk.input.Keyboard;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.Widget;
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
        Logger logger = new Logger().setHeader("Clockworks");
        if (!PoH.hasWorkshop()) {
            logger.setMessage("Terminating script, we need a workshop!").print();
            SkrrtClockWork.setRunning(false);
        }
        if (getProfile().isUseButler() && needsButler() && !PoH.callButler()) return;
        if (Inventory.isFull() && !Inventory.contains(ItemID.STEEL_BAR) && scripts.api.functions.Interaction.useOn("Clockwork", "Demon butler")) {
            if (Waiting.waitUntil(NPCInteraction::isConversationWindowUp)) {
                if (!ChatScreen.handle(chatResponses)) return;
                Waiting.waitNormal(600, 175);
                var enterAmount = Query.widgets()
                        .inRoots(AMOUNT_INTERFACE)
                        .textContains("Enter amount:")
                        .findFirst()
                        .map(Widget::isVisible)
                        .isPresent();
                Waiting.waitNormal(500, 75);
                if (!Waiting.waitUntil(3000, () -> enterAmount)) return;
                int amount = Inventory.getCount(ItemID.CLOCKWORK);
                logger.setMessage("We need to enter(1) " + amount).print();
                Keyboard.typeLine(String.valueOf(amount));
                if (Waiting.waitUntil(NPCInteraction::isConversationWindowUp)) {
                    if (ChatScreen.handle(chatResponses)) {
                        Waiting.waitNormal(600, 175);
                    }
                }
                if (Waiting.waitUntil(() -> !Inventory.contains(ItemID.CLOCKWORK))) {
                    logger.setMessage("Success, we've banked the clockworks").print();
                    Waiting.waitNormal(75, 15);
                    Waiting.waitUntil(8000, PoH::hasDemonButler);
                }
                Waiting.waitNormal(600, 98);
            }
        }

        if (!Inventory.contains(ItemID.STEEL_BAR) && !Inventory.isFull() && scripts.api.functions.Interaction.useOn("Steel bar", "Demon butler")) {
            if (Waiting.waitUntil(NPCInteraction::isConversationWindowUp)) {
                if (!ChatScreen.handle(chatResponses)) return;
                Waiting.waitNormal(600, 175);
                var enterAmount = Query.widgets()
                        .inRoots(AMOUNT_INTERFACE)
                        .textContains("Enter amount:")
                        .findFirst()
                        .map(Widget::isVisible)
                        .isPresent();
                Waiting.waitNormal(500, 75);
                if (!Waiting.waitUntil(3000, () -> enterAmount)) return;
                int amount = 28 - Inventory.getAll().size();
                if (amount > Inventory.getCount(ItemID.STEEL_BAR + 1))
                    amount = Inventory.getCount(ItemID.STEEL_BAR + 1);
                logger.setMessage("We need to enter(2) " + amount).print();
                Waiting.waitNormal(600, 98);
                Keyboard.typeLine(String.valueOf(amount));
                if (Waiting.waitUntil(8000, NPCInteraction::isConversationWindowUp)) {
                    ChatScreen.handle(chatResponses);
                    Waiting.waitNormal(600, 175);
                }
                if (Waiting.waitUntil(() -> Inventory.contains(ItemID.STEEL_BAR))) {
                    logger.setMessage("Success, we've un-noted steel bars.").print();
                }
            }
        }
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

    boolean conditionsMet() {
        return Skill.CRAFTING.getActualLevel() >= 8 && Skill.CONSTRUCTION.getActualLevel() >= 25 && hasRequiredItems();
    }

    public static boolean hasRequiredItems() {
        return Inventory.getCount(ItemID.COINS_995) > 367 && ((Inventory.getCount(ItemID.STEEL_BAR) > 0) || (getProfile().isUseButler() && Inventory.getCount(ItemID.STEEL_BAR + 1) > 0));
    }

    private static Profile getProfile() {
        return SkrrtClockWork.getRunningProfile();
    }

    boolean needsButler() {
        return (Inventory.contains(ItemID.STEEL_BAR + 1) && !Inventory.contains(ItemID.STEEL_BAR)) || (Inventory.isFull() && !Inventory.contains(ItemID.STEEL_BAR));
    }
}
