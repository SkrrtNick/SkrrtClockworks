package scripts.data;

import lombok.Data;

@Data
public class Profile {

    private boolean useButler, restocking, sellClockworks, trainSkills, useCustomMouseSpeed, useCustomReactionTimes;
    private int craftingGoal, constructionGoal, mouseSpeed, ReactionTimes;
    private GETeleports geTeleport;
    private SellingOptions sellingOption;
    private BuyingOptions buyingOption;

}
