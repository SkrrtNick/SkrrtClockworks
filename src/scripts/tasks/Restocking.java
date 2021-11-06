package scripts.tasks;

import lombok.Getter;
import lombok.Setter;
import org.tribot.api2007.Inventory;
import org.tribot.script.sdk.Equipment;
import org.tribot.script.sdk.GrandExchange;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.Widgets;
import org.tribot.script.sdk.antiban.PlayerPreferences;
import org.tribot.script.sdk.cache.BankCache;
import org.tribot.script.sdk.pricing.Pricing;
import scripts.SkrrtClockWork;
import scripts.api.banking.Bank;
import scripts.api.framework.Priority;
import scripts.api.framework.Task;
import scripts.api.functions.Logger;
import scripts.api.grandexchange.GrandExchangeItem;
import scripts.data.ItemID;
import scripts.data.Profile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Restocking implements Task {
    @Setter
    @Getter
    private static boolean shouldRestock;
    private Logger logger = new Logger().setHeader("Restocking");

    private int coinsCount, steelBarsCount, houseTelesCount, geTelesCount, buyableSteelBars, buyableHouseTeles, buyableGETeles;
    private double priceModifier;
    private int minimumAmountGETeleport = PlayerPreferences.preference("minimum-ge-tp", g -> g.normal(1, 3, 2, 1));
    private int minimumAmountHouseTeleport = PlayerPreferences.preference("minimum-house-tp", g -> g.normal(1, 10, 4, 2));
    private int minimumSteelBars = PlayerPreferences.preference("minimum-steel-bars", g -> g.normal(26, 100, 70, 48));

    @Override
    public Priority priority() {
        return Priority.LOW;
    }

    @Override
    public boolean validate() {
        return getProfile().isRestocking() && shouldRestock;
    }

    @Override
    public void execute() {
        if(Inventory.isFull()){
            setShouldRestock(false);
            return;
        }
        if(requiresNoItems()){
            Widgets.closeAll();
            setShouldRestock(false);
            return;
        }
        SkrrtClockWork.setStatus("Restocking");
        coinsCount = Inventory.getCount(ItemID.COINS_995);
        steelBarsCount = getTotalCount(ItemID.STEEL_BAR);
        houseTelesCount = getTotalCount(ItemID.TELEPORT_TO_HOUSE);
        priceModifier = getProfile().getBuyingOption().getPriceModifier();
        List<GrandExchangeItem> grandExchangeItems = new ArrayList<>() {{
            add(new GrandExchangeItem(ItemID.STEEL_BAR, getBuyableSteelBars()));
            add(new GrandExchangeItem(ItemID.TELEPORT_TO_HOUSE, getRequiredHouseTeleports()));
            add(new GrandExchangeItem(getProfile().getGeTeleport().getTradableID(), getRequiredGETeleports()));
        }};
        Collections.shuffle(grandExchangeItems);
        logger.setMessage("Coins Count: " + coinsCount).print();
        logger.setMessage("Steel bar Count: " + steelBarsCount).print();
        logger.setMessage("House tele Count: " + houseTelesCount).print();
        logger.setMessage("Buyable Steel bars: " + getBuyableSteelBars()).print();
        logger.setMessage("Total Steel bars: " + getTotalCount(ItemID.STEEL_BAR)).print();
        logger.setMessage("Required Steel bars: " + getRequiredSteelBars()).print();
        logger.setMessage("Minimum House teleport: " + minimumAmountHouseTeleport).print();
        logger.setMessage("Minimum GE teleport: " + minimumAmountGETeleport).print();
        logger.setMessage("Required GE teleports: " + getRequiredGETeleports()).print();
        logger.setMessage("Required Home teleports: " + getRequiredHouseTeleports()).print();
        if (scripts.api.grandexchange.GrandExchange.moveToGE()) {
            logger.setMessage("We are at the GE").print();
        }
        if (org.tribot.script.sdk.GrandExchange.isOpen() || org.tribot.script.sdk.GrandExchange.open()) {
            if (GrandExchange.collectAll()) {
                Waiting.waitNormal(600, 98);
            }
            if (!scripts.api.grandexchange.GrandExchange.hasAvailableSlot()) {
                logger.setMessage("We don't have any available slots, killing script.").print();
                SkrrtClockWork.setRunning(false);
                return;
            }
            for (GrandExchangeItem grandExchangeItem : grandExchangeItems) {
                if(requiresNoItems()){
                    break;
                }
                if (grandExchangeItem.getQuantity() <= 0) {
                    continue;
                }
                if (GrandExchange.placeOffer(getOfferConfig(grandExchangeItem))) {
                    Waiting.waitNormal(1200, 350);
                }
            }
            if (!requiresNoItems()) {
                if (scripts.api.grandexchange.GrandExchange.clickCollect(true)) {
                    Waiting.waitNormal(600, 98);
                }
                if (scripts.api.grandexchange.GrandExchange.close()) {
                    Waiting.waitNormal(600, 98);
                    if(Bank.open() && Waiting.waitUntil(Bank::isOpen)){
                        Bank.depositInventory();
                    }
                    if(org.tribot.script.sdk.Inventory.isEmpty()){
                        setShouldRestock(false);
                    }
                }
            }
        }

    }

    private Profile getProfile() {
        return SkrrtClockWork.getRunningProfile();
    }

    private int getTotalCount(int... itemIDs) {
        int count = 0;
        for (int itemID : itemIDs) {
            count += BankCache.getStack(itemID) + Inventory.getCount(itemID) + Inventory.getCount(itemID + 1) + Equipment.getCount(itemID);
        }
        return count;
    }

    private boolean requiresNoItems() {
        return getRequiredSteelBars() <= 0 && getRequiredHouseTeleports() <= 0 && getRequiredGETeleports() <= 0;
    }

    private int getBuyableSteelBars() {
        int price = (getItemPrice(ItemID.STEEL_BAR)) * (int) (1 + (priceModifier * 5) / 100);
        int maximumBars = ((coinsCount - getPriceForGETeleports() - getPriceForHouseTeleports()) / price);
        return buyableSteelBars = ((coinsCount - getPriceForGETeleports() - getPriceForHouseTeleports() - (maximumBars * 52)) / price);
    }

    private int getRequiredHouseTeleports() {
        return minimumAmountHouseTeleport - getTotalCount(ItemID.TELEPORT_TO_HOUSE);
    }

    private int getRequiredSteelBars() {
        return minimumSteelBars - getTotalCount(ItemID.STEEL_BAR);
    }

    private int getRequiredGETeleports() {
        return minimumAmountGETeleport - getTotalCount(getProfile().getGeTeleport().getItemIDs());
    }

    private int getPriceForHouseTeleports() {
        if (getRequiredHouseTeleports() > 0) {
            return (int) (getRequiredHouseTeleports() * (getItemPrice(getProfile().getGeTeleport().getTradableID()) * priceModifier));
        }
        return 0;
    }

    private int getPriceForGETeleports() {
        if (getRequiredGETeleports() > 0) {
            return (int) (getRequiredGETeleports() * (getItemPrice(getProfile().getGeTeleport().getTradableID()) * priceModifier));
        }
        return 0;
    }

    private GrandExchange.CreateOfferConfig getOfferConfig(GrandExchangeItem grandExchangeItem) {
        return GrandExchange.CreateOfferConfig.builder()
                .itemId(grandExchangeItem.getItemID())
                .quantity(grandExchangeItem.getQuantity())
                .priceAdjustment(getProfile().getBuyingOption().getPriceModifier())
                .build();
    }

    private int getItemPrice(int itemID) {
        return Pricing.lookupPrice(itemID).orElse(-1);
    }

    //
//    logger.setMessage("We weren't able to fetch a price, terminating script.");
//            SkrrtClockWork.setRunning(false);
}
