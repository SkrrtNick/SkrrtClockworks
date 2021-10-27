package scripts.data;

import lombok.Getter;
import org.tribot.script.sdk.Equipment;

@Getter
public enum GETeleports {
    VARROCK_TELEPORT("Varrock teleport tab", false, ItemID.VARROCK_TELEPORT, ItemID.VARROCK_TELEPORT),
    AMULET_OF_GLORY("Amulet of glory", true, Equipment.Slot.NECK, ItemID.AMULET_OF_GLORY6, ItemID.AMULET_OF_GLORY6, ItemID.AMULET_OF_GLORY5, ItemID.AMULET_OF_GLORY4, ItemID.AMULET_OF_GLORY3, ItemID.AMULET_OF_GLORY2, ItemID.AMULET_OF_GLORY1),
    RING_OF_WEALTH("Ring of wealth", true, Equipment.Slot.RING, ItemID.RING_OF_WEALTH_5, ItemID.RING_OF_WEALTH_5, ItemID.RING_OF_WEALTH_4, ItemID.RING_OF_WEALTH_3, ItemID.RING_OF_WEALTH_2, ItemID.RING_OF_WEALTH_1);
    private String name;
    private int tradableID;
    private int[] itemIDs;
    private boolean equippable;
    private Equipment.Slot slot;

    GETeleports(String name, boolean isEquippable, Equipment.Slot slot, int tradableID, int... itemIDs) {
        this.name = name;
        this.slot = slot;
        this.tradableID = tradableID;
        this.itemIDs = itemIDs;
        this.equippable = isEquippable;
    }

    GETeleports(String name, boolean isEquippable, int tradableID, int... itemIDs) {
        this.name = name;
        this.tradableID = tradableID;
        this.itemIDs = itemIDs;
        this.equippable = isEquippable;
    }


    @Override
    public String toString() {
        return name;
    }

}
