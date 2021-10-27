package scripts.api.functions;

import org.tribot.script.sdk.Waiting;

public class Keyboard extends org.tribot.script.sdk.input.Keyboard {

    public static void typeKeys(String keys){
        char[] chars = keys.toCharArray();
        for(char c: chars){
            org.tribot.api.input.Keyboard.typeKeys(c);
            Waiting.waitNormal(100,45);
        }
        org.tribot.api.input.Keyboard.pressEnter();
    }

}
