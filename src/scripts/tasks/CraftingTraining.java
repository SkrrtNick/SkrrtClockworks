package scripts.tasks;

import dax.shared.jsonSimple.ItemList;
import net.sourceforge.jdistlib.Normal;
import org.tribot.script.sdk.Inventory;
import org.tribot.script.sdk.MakeScreen;
import org.tribot.script.sdk.Skill;

import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.antiban.AntibanProperties;
import org.tribot.script.sdk.interfaces.Item;
import org.tribot.script.sdk.query.Query;
import scripts.api.banking.Bank;
import scripts.api.banking.BankItem;
import scripts.api.framework.Priority;
import scripts.api.framework.Task;
import scripts.api.functions.Numbers;
import scripts.api.functions.Timer;
import scripts.data.Crafting;
import scripts.data.ItemID;

import java.util.ArrayList;


public class CraftingTraining implements Task{

    @Override
    public Priority priority() {
        return null;
    }

    @Override
    public boolean validate() {
        return Skill.CRAFTING.getActualLevel() < 8;
    }

    @Override
    public void execute() {

    }

}
