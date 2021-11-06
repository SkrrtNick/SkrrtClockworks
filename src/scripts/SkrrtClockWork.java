package scripts;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.tribot.api.General;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.MessageListening07;
import org.tribot.script.sdk.Login;
import org.tribot.script.sdk.input.Mouse;
import org.tribot.script.sdk.painting.Painting;
import org.tribot.script.sdk.script.TribotScript;
import org.tribot.script.sdk.util.ScriptSettings;
import scripts.api.framework.Task;
import scripts.api.framework.TaskSet;
import scripts.api.functions.Logger;
import scripts.api.functions.Numbers;
import scripts.data.Prices;
import scripts.data.Profile;
import scripts.gui.GUI;
import scripts.tasks.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@ScriptManifest(name = "SkrrtClockworks", authors = {"SkrrtNick"}, category = "Money Making")
public class SkrrtClockWork implements MessageListening07, TribotScript {
    Logger logger = new Logger().setHeader("SkrrtScripts");
    private int elipses = 1;
    @Setter
    @Getter
    private static String status;
    @Setter
    @Getter
    private static Profile runningProfile = new Profile();
    @Setter
    @Getter
    private static boolean isRunning = true;
    @Getter
    private static final long START_TIME = System.currentTimeMillis();
    private URL fxml;
    private GUI gui;
    private double version = .07;

    @Setter
    @Getter
    private static int reactionModifier;

    private Image getImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    final Image paintBg = getImage("https://imgur.com/u7QHOC1.png");

    private final RenderingHints aa = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);

    Font myMainFont = new Font("Calibri", 1, 12);
    Font versionFont = new Font("Calibri", 1, 10);


    private String getEllipses() {
        return ".".repeat(elipses++);
    }


    @Override
    public void execute(String args) {
        Painting.addPaint(ui -> {
            ui.setFont(myMainFont);
            ui.setColor(new Color(1, 1, 1, 0.4f));
            ui.drawImage(paintBg, 275, 208, 240, 130, null);
            ui.setColor(Color.white);
            ui.drawString("Status: " + getStatus(), 290, 205);
            ui.setColor(Color.DARK_GRAY);
            ui.drawString(Numbers.getHumanisedRuntime(START_TIME), 334, 262);
            ui.drawString(MakingClockworks.getClockworks() + Numbers.getHourly(START_TIME, MakingClockworks.getClockworks()), 334, 292);
            ui.drawString(Prices.getProfitEstimation(MakingClockworks.getClockworks()) + Numbers.getHourly(START_TIME, Prices.getProfitEstimation(MakingClockworks.getClockworks())), 334, 320);
            ui.setColor(Color.red);
            ui.setFont(versionFont);
            ui.drawString("V.", 480, 205);
            ui.setColor(Color.ORANGE);
            ui.drawString(String.valueOf(version), 490, 205);
        });
        if (!handleArgs(args)) {
            runGui();
        }
        if (getRunningProfile().isUseCustomMouseSpeed()) {
            Mouse.setSpeed(runningProfile.getMouseSpeed());
        }
        if (getRunningProfile().isUseCustomReactionTimes()) {
            setReactionModifier(runningProfile.getReactionTimes());
        }
        logger.setHeader("Profile").setMessage(getRunningProfile().toString()).print();
//        GlobalWalking.setEngine(daxWalkerAdapter);
        if (!Login.isLoggedIn()) {
            Login.login();
        }
        while (isRunning) {
            setStatus("Determining next task...");
            TaskSet tasks = new TaskSet(new BankingTask(), new SellingClockworks(), new Restocking(), new MovingToHouse(), new MakingClockworks());
            for (Task task : tasks) {
                if (task.validate()) {
                    task.execute();
                }
            }
            General.sleep(20, 40);
        }
    }

    private boolean handleArgs(String args) {
        if (args == null) {
            return false;
        }
        val profile = ScriptSettings.getDefault()
                .load(args, Profile.class);
        profile.ifPresent(SkrrtClockWork::setRunningProfile);
        return profile.isPresent();
    }

    private void runGui() {
        try {
            fxml = new URL("https://raw.githubusercontent.com/SkrrtNick/SkrrtClockworks/master/src/scripts/gui/gui.fxml");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        gui = new GUI(fxml);
        gui.show();
        while (gui.isOpen()) {
            setStatus("Waiting on user input" + getEllipses());
            if (elipses == 4) {
                elipses = 0;
            }
            General.sleep(500);
        }
    }

}
