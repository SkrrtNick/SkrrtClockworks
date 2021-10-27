package scripts.data;

import lombok.Getter;
import org.tribot.api.General;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum SellingOptions {
    OSRS("OSRS prices", 0),
    OSRS_5("OSRS prices - 5%", -1),
    OSRS_10("OSRS prices - 10%", -2),
    OSRS_15("OSRS prices - 15%", -3),
    ONE_GOLD_PIECE("1gp", -1);
    private String name;
    private int priceModifier;

    SellingOptions(String name, int priceModifier) {
        this.name = name;
        this.priceModifier = priceModifier;
    }


    public static double getPriceModifier(String name) {
        var modifier = Arrays.stream(SellingOptions.values())
                .filter(sellingOptions -> sellingOptions.name.equals(name))
                .map(SellingOptions::getPriceModifier)
                .findFirst();
        if (modifier.isEmpty()) {
            return -1.0;
        } else {
            return modifier.get();
        }
    }
    @Override
    public String toString(){
        return name;
    }
}