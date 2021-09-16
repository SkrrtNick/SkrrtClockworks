package scripts.api.banking;

import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.cache.BankCache;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.walking.GlobalWalking;

import java.util.ArrayList;
import java.util.Collections;

public class Bank extends org.tribot.script.sdk.Bank {

    public static boolean initialiseCache() {
        if(BankCache.isInitialized()){
            return true;
        }
        if (Bank.isNearby()) {
            if(openBank()){
                return Waiting.waitUntil(Bank::isOpen);
            }
        } else {
            if (GlobalWalking.walkToBank()) {
                Waiting.waitUntil(Bank::isNearby);
            }
        } return openBank();
    }
    public static boolean setInventory(ArrayList<BankItem> bankItems){
        Collections.shuffle(bankItems);
        for(BankItem bankItem:bankItems){
            if(Query.inventory().idEquals(bankItem.getID()).count() >= bankItem.getQuantity()){
                continue;
            }
            if(Bank.isOpen()){
                if(Bank.getCount(bankItem.getID())==0){
                    return false;
                }
                if(bankItem.getQuantity() == 0){
                    if(Bank.withdrawAll(bankItem.getID())){
                        Waiting.waitNormal(75,15);
                    }
                } else {
                    if(Bank.withdraw(bankItem.getID(), bankItem.getQuantity())){
                        Waiting.waitNormal(75,15);
                    }
                }
            } else {
                if(Bank.initialiseCache()){
                    return false;
                }
            }
        }
        Bank.close();
        return true;
    }
}