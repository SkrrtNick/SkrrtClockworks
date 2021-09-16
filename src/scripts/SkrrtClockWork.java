package scripts;

import org.checkerframework.checker.units.qual.C;
import org.tribot.api.General;
import org.tribot.api2007.GrandExchange;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.sdk.Login;
import org.tribot.script.sdk.walking.GlobalWalking;
import org.tribot.script.sdk.walking.adapter.DaxWalkerAdapter;
import org.tribot.util.Util;
import scripts.api.poh.PoH;
import scripts.gui.GUI;
import scripts.tasks.Clockworks;
import scripts.tasks.CraftingTraining;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Clock;

@ScriptManifest(name = "SkrrtClockworks", authors = { "SkrrtNick" }, category = "Money Making")
public class SkrrtClockWork extends Script implements Painting {
    DaxWalkerAdapter daxWalkerAdapter = new DaxWalkerAdapter("sub_JmRkbIB2XRYqmf","7227dd88-8182-4cd9-a3d9-00b8fa6ff56e");
    Clockworks clockworks = new Clockworks();
    private URL fxml;
    private GUI gui;
    @Override
    public void run() {
        try{
            fxml = new URL("https://raw.githubusercontent.com/SkrrtNick/SkrrtClockworks/master/src/scripts/gui/gui.fxml");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        gui = new GUI(fxml);
        gui.show();
        while(gui.isOpen()){
            sleep(500);
        }
        GlobalWalking.setEngine(daxWalkerAdapter);
        if(!Login.isLoggedIn()){
            Login.login();
        }
        while(true){
            if(clockworks.validate()){
                clockworks.execute();
            }
            General.sleep(20,40);
        }

    }

    @Override
    public void onPaint(Graphics graphics) {
        graphics.drawString("Clockworks: " + Clockworks.clockworks, 11,62);
    }
}
