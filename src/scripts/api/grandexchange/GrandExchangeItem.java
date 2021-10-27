package scripts.api.grandexchange;

import lombok.Data;

@Data
public class GrandExchangeItem {
    private int quantity;
    private int itemID;
    public GrandExchangeItem(int itemID, int quantity) {
        this.itemID = itemID;
        this.quantity = quantity;
    }
}
