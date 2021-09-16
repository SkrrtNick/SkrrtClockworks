# Entity-Selector

A entity selection system for TRiBot. Allows for easy selection of NPCs, Inventory items, Ground items, Bank items, Objects, Players and Interfaces through method chaining.
It wraps around TRiBot's filters for best performance and compatibility.

### How to use
The following section gives a brief overview how the entity selection can be used and will hopefully demonstrate just how powerful it can be. I tried to use TRiBot's filter names wherever i could.

Lets start with a simple example, we search for a Rat. As with the equivalent tribot filters, this is all case insensitive.

    // Find a rat!
    RSNPC rat = Entities.find(NpcEntity::new)
           .nameEquals("rat")
           .getFirstResult();
           
    if (rat != null) {
        // We found a rat!
    }
    
You can keep chaining selectors before calling #getFirstResult which will return a single result (or null if not found)
or #getResults which will return an array, empty if nothing was found.

Lets try something a little more advanced.

    RSArea cowArea = new RSArea(...);

    // Find the closest attackable NPC to the player's position, inside the cow area.
    RSNPC cow = Entities.find(NpcEntity::new)
            .actionsEquals("Attack")
            .inArea(cowArea)
            .sortByDistance()
            .getFirstResult();
            
    if (cow != null) {
        // We found a attackable npc!
    }

The above snippet would return the closest NPC which has an 'Attack' option, relative to the player, inside the cow area.
As you can see, selectors for the action and area are chained, we sort the results by distance to the player, and then we retrieve the first result.

Now if we want to find all type of arrows of the ground, but only if its a stack of at least 20 arrows.

    // Get all arrows with a stacksize of over 20.
    RSGroundItem[] arrows = Entities.find(GroundItemEntity::new)
            .nameContains("arrow")
            .custom((item) -> item.getStack() >= 20)
            .getResults();
            
    if (arrows.length > 0) {
        // We found an arrow stack!
        // ...
    }
    
As you might notice, some interesting stuff start happening. First of, we use #getResults() instead of #getFirstResult(), which is why we get an array.
Then we also use the 'custom' selector. This is an extremely useful method which will allow you to specify a selector as a lambda. Note that this works on any entity, not just ground items!

Finally we will select an interface.
Since TRiBot does not supply any filters for interfaces in it's API. All these methods are custom. I've followed the same syntax and it should be easy to understand once you see them.

    // Retrieve the 'Add friend' button.
    RSInterface friendButton = Entities.find(InterfaceEntity::new)
            .textEquals("add friend")
            .getFirstResult();
            
    if (friendButton != null) {
        // Found the button!
    }
    
Note that you can use #inMaster(int) and #inMasterAndChild(int, int) methods to reduce the heap of interfaces to search for, speeding it up significantly.


#### NPCs
Npcs can be selected by using

    RSNPC npc = Entities.find(NpcEntity::new)
            ...

#### Inventory items

Inventory items can be selected by using

    RSItem item = Entities.find(ItemEntity::new)
            ...
			
Note that inventory items do not have positions, and thus do not have a #sortByDistance() method.

#### Ground items

Ground items can be selected by using

    RSGroundItem item = Entities.find(GroundItemEntity::new)
            ...

#### Objects

Objects can be selected by using

    RSObject object = Entities.find(ObjectEntity::new)
            ...
			
Note that when using the Object selector, a method #setDistance is available. You can use it to set the max search distance to find objects.
If you do not set the value, a default value of 20 will be used.

#### Players

Players can be selected by using

    RSPlayer player = Entities.find(PlayerEntity::new)
            ...
            
#### Interfaces

Interfaces can be selected by using

    RSInterface rsInterface = Entities.find(InterfaceEntity::new)
            ...
			
Note that interfaces do not have positions, and thus do not have a #sortByDistance() method.

#### Bank items

Bank items can be selected by using

    RSItem item = Entities.find(BankItemEntity::new)
            ...
			
Note that bank items do not have positions, and thus do not have a #sortByDistance() method.
You can only use this when the bank screen is already open. Otherwise it will always return an empty array (getResults) or null (getFirstResult).
It waits for bank items to be loaded if it has to, so you can use this right after Banking#openBank


=== 

If you have any questions, feel free to contact me.

Javadocs:
https://laniax.github.io/Entity-Selector/
