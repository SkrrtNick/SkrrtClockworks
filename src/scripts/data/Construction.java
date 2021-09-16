package scripts.data;

import org.tribot.api2007.Skills;
import org.tribot.script.sdk.Skill;

public enum Construction {

    CRUDE_WOODEN_CHAIR(4, 2, 2, 58, ItemID.PLANK),
    WOODEN_BOOKCASE(9, 4, 4, 115, ItemID.PLANK),
    WOODEN_LARDER(15, 8, 8, 228, ItemID.PLANK),
    REPAIR_BENCH(16, 0, 2, 120, ItemID.OAK_PLANK),
    CRAFTING_TABLE_1(22, 0, 4, 240, ItemID.OAK_PLANK),
    OAK_DINING_TABLE(25, 0, 4, 240, ItemID.OAK_PLANK);

    private int stoppingLevel;
    private int requiredNails;
    private int requiredPlanks;
    private int xpPerItem;
    private int plankID;

    Construction(int stoppingLevel, int requiredNails, int requiredPlanks, int xpPerItem, int plankID) {
        this.stoppingLevel = stoppingLevel;
        this.requiredNails = requiredNails;
        this.requiredPlanks = requiredPlanks;
        this.xpPerItem = xpPerItem;
        this.plankID = plankID;
    }

    public int getStoppingLevel() {
        return stoppingLevel;
    }

    public int getRequiredNails() {
        return requiredNails;
    }

    public int getRequiredPlanks() {
        return requiredPlanks;
    }

    public int getPlankID() {
        return plankID;
    }

    public static int getRequiredPlank(int plankID) {
        int required = 0;
        for (Construction c : Construction.values()) {
            if (Skill.CONSTRUCTION.getActualLevel() > c.stoppingLevel || c.plankID != plankID) {
                continue;
            } else {
                required = required + ((Skills.getXPToLevel(Skills.SKILLS.CONSTRUCTION, c.stoppingLevel) / c.xpPerItem) * c.requiredPlanks);
            }
        } return required;
    }
}
