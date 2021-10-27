package scripts.tasks;

import org.tribot.script.sdk.Inventory;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.pricing.Pricing;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.GrandExchangeOffer;
import scripts.SkrrtClockWork;
import scripts.api.framework.Priority;
import scripts.api.framework.Task;
import scripts.api.functions.Logger;
import scripts.api.grandexchange.GrandExchange;
import scripts.data.ItemID;
import scripts.data.Profile;
import scripts.data.SellingOptions;

import java.util.Optional;

public class SellingClockworks implements Task {

    @Override
    public Priority priority() {
        return Priority.MEDIUM;
    }

    @Override
    public boolean validate() {
        return getProfile().isRestocking() && getProfile().isSellClockworks() && hasClockworks();
    }

    @Override
    public void execute() {
        Logger logger = new Logger().setHeader("GE");
        SkrrtClockWork.setStatus("Selling Clockworks");
        if (GrandExchange.moveToGE()) {
            logger.setMessage("We are at the GE").print();
        }
        logger.setMessage("Grand Exchange is Open:" + org.tribot.script.sdk.GrandExchange.isOpen()).print();
        logger.setMessage("Grand Exchange open: " + org.tribot.script.sdk.GrandExchange.open()).print();
        if(org.tribot.script.sdk.GrandExchange.isOpen() || org.tribot.script.sdk.GrandExchange.open()){
            if(Waiting.waitUntil(()->org.tribot.script.sdk.GrandExchange.placeOffer(offerConfig()))){
                org.tribot.script.sdk.GrandExchange.collectAll();
            }
        }

    }

    Profile getProfile() {
        return SkrrtClockWork.getRunningProfile();
    }

    boolean hasClockworks() {
        return Inventory.contains(ItemID.CLOCKWORK + 1) || Query.grandExchangeOffers().itemIdEquals(ItemID.CLOCKWORK).findFirst().isPresent();
    }
    private org.tribot.script.sdk.GrandExchange.CreateOfferConfig offerConfig(){
        if(getProfile().getSellingOption().getName().equals("1gp")){
            return org.tribot.script.sdk.GrandExchange.CreateOfferConfig.builder()
                    .price(1)
                    .quantity(Inventory.getCount(ItemID.CLOCKWORK + 1))
                    .type(GrandExchangeOffer.Type.SELL)
                    .itemName("Clockwork")
                    .build();
        } else {
            return org.tribot.script.sdk.GrandExchange.CreateOfferConfig.builder()
                    .priceAdjustment(getProfile().getSellingOption().getPriceModifier())
                    .quantity(Inventory.getCount(ItemID.CLOCKWORK + 1))
                    .type(GrandExchangeOffer.Type.SELL)
                    .itemName("Clockwork")
                    .build();
        }
    }
}
