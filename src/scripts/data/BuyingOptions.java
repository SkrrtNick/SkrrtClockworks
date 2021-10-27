package scripts.data;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum BuyingOptions {
    OSRS("OSRS prices",0),
    OSRS_5("OSRS prices + 5%",1),
    OSRS_10("OSRS prices + 10%",2),
    OSRS_15("OSRS prices + 15%",3);
    private String name;
    private int priceModifier;

    BuyingOptions(String name, int priceModifier) {
        this.name = name;
        this.priceModifier = priceModifier;
    }

    @Override
    public String toString(){
        return name;
    }

    public int getPriceModifier() {
        return priceModifier;
    }
}