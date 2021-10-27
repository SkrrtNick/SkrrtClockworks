package scripts.api.banking;


import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.cache.BankCache;

import org.tribot.script.sdk.walking.GlobalWalking;


public class Bank extends org.tribot.script.sdk.Bank {

    public static boolean initialiseCache() {
        if(BankCache.isInitialized()){
            return true;
        }
        if (Bank.isNearby()) {
            if(open()){
                return Waiting.waitUntil(Bank::isOpen);
            }
        } else {
            if (GlobalWalking.walkToBank()) {
                Waiting.waitUntil(Bank::isNearby);
            }
        } return open();
    }
}