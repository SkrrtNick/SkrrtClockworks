package scripts.data;

import org.tribot.api2007.Skills;

public enum Crafting {

    LEATHER_GLOVES(1059, "Leather gloves", 13);

    private int itemID;
    private int xpPerItem;
    private String itemName;

    Crafting(int itemID, String itemName, int xpPerItem) {
        this.itemID = itemID;
        this.xpPerItem = xpPerItem;
        this.itemName = itemName;
    }

    public int getItemID() {
        return itemID;
    }

    public int getXpPerItem() {
        return xpPerItem;
    }

    public String getItemName() {
        return itemName;
    }

    public int getRequiredNumber(int goal) {
        return (Skills.getXPToLevel(Skills.SKILLS.CRAFTING, goal)) / xpPerItem;
    }
}
