package scripts.data;

import org.tribot.script.sdk.pricing.Pricing;

public class Prices {
    private static int clockworkPrice, steelbarPrice;

    public static int getProfitEstimation(int clockworkAmount){
        return (getClockworkPrice() - getSteelbarPrice()) * clockworkAmount;
    }

    private static int getClockworkPrice() {
        return Pricing.lookupPrice(ItemID.CLOCKWORK).orElse(-1);
    }

    private static int getSteelbarPrice() {
        return Pricing.lookupPrice(ItemID.STEEL_BAR).orElse(-1);
    }
}
