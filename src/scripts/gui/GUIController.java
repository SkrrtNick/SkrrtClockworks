package scripts.gui;

import com.allatori.annotations.DoNotRename;
import com.sun.javafx.application.HostServicesDelegate;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import lombok.val;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.util.ScriptSettings;
import scripts.SkrrtClockWork;
import scripts.api.functions.Loggable;
import scripts.api.functions.Logger;
import scripts.data.BuyingOptions;
import scripts.data.GETeleports;
import scripts.data.Profile;
import scripts.data.SellingOptions;
import scripts.tasks.SellingClockworks;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@DoNotRename
public class GUIController extends AbstractGUIController {
    @DoNotRename
    @FXML
    private Button btnStart;
    @DoNotRename
    @FXML
    private CheckBox checkButler;
    @DoNotRename
    @FXML
    private CheckBox checkClockworks;
    @DoNotRename
    @FXML
    private CheckBox checkMouseSpeed;
    @DoNotRename
    @FXML
    private CheckBox checkReactions;
    @DoNotRename
    @FXML
    private CheckBox checkRestocking;
    @DoNotRename
    @FXML
    private CheckBox checkSkills;
    @DoNotRename
    @FXML
    private ComboBox<SellingOptions> comboSelling;
    @DoNotRename
    @FXML
    private ComboBox<BuyingOptions> comboBuying;
    @DoNotRename
    @FXML
    private ComboBox<GETeleports> comboTeleport;

    @DoNotRename
    @FXML
    private Slider sliderMouseSpeed;

    @DoNotRename
    @FXML
    private Slider sliderReactions;

    @DoNotRename
    @FXML
    private Spinner<Integer> spinConstruction;

    @DoNotRename
    @FXML
    private Spinner<Integer> spinCrafting;

    @DoNotRename
    @FXML
    private Text txtMouseSpeed;


    @DoNotRename
    @FXML
    private Text txtReactionTimes;
    @DoNotRename
    @FXML
    int mouseSpeed, reactions;
    @DoNotRename
    @FXML
    private SpinnerValueFactory<Integer> craftingFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(8, 14);
    @DoNotRename
    @FXML
    private SpinnerValueFactory<Integer> constructionFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(25, 50);
    @DoNotRename
    @FXML
    private Logger logger = new Logger().setHeader("GUI");

    @DoNotRename
    @FXML
    void profileClicked(ActionEvent event) throws IOException, URISyntaxException {
        getGUI().openBrowser("https://community.tribot.org/profile/300380-gigiwest123/");
    }

    @DoNotRename
    @FXML
    void threadClicked(ActionEvent event) throws IOException, URISyntaxException {
        getGUI().openBrowser("https://community.tribot.org/topic/83981-low-reqsmoney-making-skrrtclockworks/");
    }

    @DoNotRename
    @FXML
    void loadPressed(ActionEvent event) {
        load();
    }
    @DoNotRename
    @FXML
    private Hyperlink linkProfile;

    @DoNotRename
    @FXML
    private Hyperlink linkThread;

    @DoNotRename
    @FXML
    void savePressed(ActionEvent event) {
        save();
    }
    @DoNotRename
    @FXML
    private MenuItem mnuSave;
    @DoNotRename
    @FXML
    private MenuItem mnuLoad;

    @DoNotRename
    @FXML
    void btnStartPressed(ActionEvent event) {
        save("last");
        getGUI().close();
        if (Waiting.waitUntil(() -> !getGUI().isOpen())) {
            logger.setLoggable(Loggable.MESSAGE)
                    .setMessage("Welcome to SkrrtClockworks, starting script!")
                    .print();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        checkReactions.setDisable(true);
        sliderReactions.setDisable(true);
        checkSkills.setDisable(true);
        spinCrafting.setDisable(true);
        spinConstruction.setDisable(true);
        load("last");
        comboTeleport.getItems().setAll(GETeleports.values());
        comboSelling.getItems().setAll(SellingOptions.values());
        comboBuying.getItems().setAll(BuyingOptions.values());
        spinCrafting.setValueFactory(craftingFactory);
        spinConstruction.setValueFactory(constructionFactory);
        sliderMouseSpeed.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mouseSpeed = (int) sliderMouseSpeed.getValue();
                txtMouseSpeed.setText(Integer.toString(mouseSpeed));
            }
        });
        sliderReactions.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                reactions = (int) sliderReactions.getValue();
                txtReactionTimes.setText(reactions + "%");
            }
        });
    }
    void load() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Profile");
        fileChooser.setInitialDirectory(ScriptSettings.getDefault().getDirectory());
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Profile", "*.json"));
        File selectedFile = fileChooser.showOpenDialog(getGUI().getStage());
        if (selectedFile != null) {
            logger.setMessage("Selected File: " + selectedFile.getName()).print();
            load(selectedFile.getName());
        }
    }
    public void load(String name) {
        ScriptSettings loader = ScriptSettings.getDefault();
        loader.load(name, Profile.class)
                .ifPresent(s -> {
                    logger.setLoggable(Loggable.MESSAGE).setMessage("Successfully loaded the profile")
                            .print();
                    sliderMouseSpeed.setValue(s.getMouseSpeed());
                    craftingFactory.setValue(s.getCraftingGoal());
                    constructionFactory.setValue(s.getConstructionGoal());
                    checkButler.setSelected(s.isUseButler());
                    checkReactions.setSelected(s.isUseCustomReactionTimes());
                    checkMouseSpeed.setSelected(s.isUseCustomMouseSpeed());
                    checkSkills.setSelected(s.isTrainSkills());
                    checkClockworks.setSelected(s.isSellClockworks());
                    checkRestocking.setSelected(s.isRestocking());
                    checkRestocking.setSelected(s.isRestocking());
                    sliderReactions.setValue(s.getReactionTimes());
                    txtReactionTimes.setText(Integer.toString(s.getReactionTimes()));
                    txtMouseSpeed.setText(Integer.toString(s.getMouseSpeed()));
                    comboTeleport.setValue(s.getGeTeleport());
                    comboSelling.setValue(s.getSellingOption());
                    comboBuying.setValue(s.getBuyingOption());
                });
    }
    void save() {
        val profile = new Profile();
        profile.setCraftingGoal(spinCrafting.getValue());
        profile.setConstructionGoal(spinConstruction.getValue());
        profile.setUseButler(checkButler.isSelected());
        profile.setReactionTimes((int) sliderReactions.getValue());
        profile.setMouseSpeed((int) sliderMouseSpeed.getValue());
        profile.setRestocking(checkRestocking.isSelected());
        profile.setSellClockworks(checkClockworks.isSelected());
        profile.setTrainSkills(checkSkills.isSelected());
        profile.setUseCustomMouseSpeed(checkMouseSpeed.isSelected());
        profile.setUseCustomReactionTimes(checkReactions.isSelected());
        profile.setGeTeleport(comboTeleport.getValue());
        profile.setSellingOption(comboSelling.getValue());
        profile.setBuyingOption(comboBuying.getValue());
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Profile");
        fileChooser.setInitialDirectory(ScriptSettings.getDefault().getDirectory());
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Profile", "*.json"));
        File selectedFile = fileChooser.showSaveDialog(getGUI().getStage());
        if (selectedFile != null) {
            logger.setMessage("Saved File: " + selectedFile.getName()).print();
            ScriptSettings.getDefault().save(selectedFile.getName(), profile);
        }
    }

    public void save(String name) {
        ScriptSettings saver = ScriptSettings.getDefault();
        Profile profile = new Profile();
        profile.setCraftingGoal(spinCrafting.getValue());
        profile.setConstructionGoal(spinConstruction.getValue());
        profile.setUseButler(checkButler.isSelected());
        profile.setReactionTimes((int) sliderReactions.getValue());
        profile.setMouseSpeed((int) sliderMouseSpeed.getValue());
        profile.setRestocking(checkRestocking.isSelected());
        profile.setSellClockworks(checkClockworks.isSelected());
        profile.setTrainSkills(checkSkills.isSelected());
        profile.setUseCustomMouseSpeed(checkMouseSpeed.isSelected());
        profile.setUseCustomReactionTimes(checkReactions.isSelected());
        profile.setGeTeleport(comboTeleport.getValue());
        profile.setSellingOption(comboSelling.getValue());
        profile.setBuyingOption(comboBuying.getValue());
        saver.save(name, profile);
        logger.setLoggable(Loggable.MESSAGE).setMessage("Successfully saved " + name)
                .print();
        SkrrtClockWork.setRunningProfile(profile);
    }

}
