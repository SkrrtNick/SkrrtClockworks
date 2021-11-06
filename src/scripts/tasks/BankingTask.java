package scripts.tasks;

import lombok.val;
import org.tribot.script.sdk.*;
import org.tribot.script.sdk.tasks.Amount;
import org.tribot.script.sdk.tasks.BankTask;
import org.tribot.script.sdk.tasks.EquipmentReq;
import org.tribot.script.sdk.tasks.ItemReq;
import org.tribot.script.sdk.walking.GlobalWalking;
import scripts.SkrrtClockWork;
import scripts.api.framework.Priority;
import scripts.api.framework.Task;
import scripts.api.functions.Logger;
import scripts.api.poh.PoH;
import scripts.data.ItemID;
import scripts.data.Profile;

public class BankingTask implements Task {
    private Logger logger = new Logger();

    @Override
    public Priority priority() {
        return Priority.MEDIUM;
    }

    @Override
    public boolean validate() {
        return !Restocking.isShouldRestock() && (((PoH.isInHouse() && !getProfile().isUseButler()) && !MakingClockworks.hasRequiredItems()) ||
                (getProfile().isUseButler() && (!Inventory.contains(ItemID.STEEL_BAR + 1) && !Inventory.contains(ItemID.STEEL_BAR)) || (!PoH.isInHouse() && !PoH.isNearPortal() && !Inventory.contains(ItemID.STEEL_BAR))));
    }

    @Override
    public void execute() {
        SkrrtClockWork.setStatus("Banking");
        logger.setHeader("Banking");
        if (PoH.isInHouse() && PoH.leaveHouse()) {
            Waiting.waitNormal(1200, 350);
        }
        if (!Bank.isNearby() && GlobalWalking.walkToBank()) {
            Waiting.waitUntil(Bank::isNearby);
        }
        val bankTask = BankTask.builder()
                .addInvItem(ItemID.COINS_995, Amount.range(55000, 5000000))
                .addInvItem(ItemID.STEEL_BAR, Amount.of(25))
                .addNotedInvItem(ItemID.STEEL_BAR + 1, Amount.range(1, 5000))
                .addInvItem(ItemID.TELEPORT_TO_HOUSE, Amount.of(1))
                .addEquipmentItem(() -> {
                    if (getProfile().getGeTeleport() == null || !getProfile().getGeTeleport().isEquippable()) {
                        return EquipmentReq.slot(Equipment.Slot.HANDS).none();
                    } else {
                        return EquipmentReq.slot(getProfile().getGeTeleport().getSlot()).chargedItem(getProfile().getGeTeleport().getName(), 1);
                    }
                })
                .addInvItem(() -> {
                    if (getProfile().getGeTeleport() == null || getProfile().getGeTeleport().isEquippable()) {
                        return new ItemReq(0, Amount.of(0));
                    } else {
                        return new ItemReq(getProfile().getGeTeleport().getTradableID(), Amount.range(1, 10));
                    }
                })
                .build();
        if (!bankTask.isSatisfied()) {
            if (Inventory.contains(ItemID.STEEL_BAR + 1) && !Inventory.contains(ItemID.STEEL_BAR)) {
                if (!Bank.isOpen()) {
                    Bank.open();
                }
                if (Bank.depositAll(ItemID.STEEL_BAR + 1)) {
                    Waiting.waitNormal(600, 175);
                    Waiting.waitUntil(() -> Bank.contains(ItemID.STEEL_BAR));
                }

            }
            bankTask.execute()
                    .ifPresent(n -> {
                        logger.setMessage(n.toString()).print();
                        if (!getProfile().isRestocking()) {
                            logger.setMessage("Restocking is disabled and we don't have the required items, exiting script").print();
                            SkrrtClockWork.setRunning(false);
                        } else {
                            if (n.toString().contains("995")) {
                                logger.setMessage("We don't have enough coins, terminating script").print();
                                SkrrtClockWork.setRunning(false);
                                return;
                            }
                            logger.setMessage("Restocking has been enabled").print();
                            if (Inventory.isFull()) {
                                Bank.depositInventory();
                            }
                            if (Bank.contains(ItemID.CLOCKWORK) && getProfile().isSellClockworks()) {
                                if (!BankSettings.isNoteEnabled()) {
                                    BankSettings.setNoteEnabled(true);
                                }
                                if (Bank.withdrawAll(ItemID.CLOCKWORK)) {
                                    Waiting.waitUntil(() -> Inventory.contains(ItemID.CLOCKWORK + 1));
                                }
                            }
                            if (Inventory.contains(ItemID.COINS_995) || Bank.withdraw(ItemID.COINS_995, Bank.getCount(ItemID.COINS_995))) {
                                if (Waiting.waitUntil(() -> Inventory.contains(ItemID.COINS_995))) {
                                    Bank.close();
                                }
                            }
                            Restocking.setShouldRestock(true);
                        }
                    });
        }
    }

    Profile getProfile() {
        return SkrrtClockWork.getRunningProfile();
    }

}
