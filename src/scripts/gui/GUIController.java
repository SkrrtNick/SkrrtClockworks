package scripts.gui;

import com.allatori.annotations.DoNotRename;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.util.ScriptSettings;
import scripts.SkrrtClockWork;
import scripts.api.functions.Loggable;
import scripts.api.functions.Logger;
import scripts.data.BuyingOptions;
import scripts.data.GETeleports;
import scripts.data.Profile;
import scripts.data.SellingOptions;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GUIController extends AbstractGUIController {
    @DoNotRename
    @FXML
    private Button btnLoad;

    @DoNotRename
    @FXML
    private Button btnSave;
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
    private ComboBox<String> comboSelling;
    @DoNotRename
    @FXML
    private ComboBox<String> comboBuying;
    @DoNotRename
    @FXML
    private ComboBox<String> comboTeleport;

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
    private ComboBox<String> txtProfile;

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
    void btnLoadPressed(ActionEvent event) {
        String profileName = txtProfile.getEditor().getText();
        if (profileName == null || profileName.isBlank()) {
            logger
                    .setLoggable(Loggable.ERROR)
                    .setMessage("Please enter a profile name")
                    .print();
        } else {
            load(txtProfile.getValue());
        }
    }

    @DoNotRename
    @FXML
    void btnSavePressed(ActionEvent event) {
        save(txtProfile.getValue());
    }

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
        checkSkills.setDisable(true);
        spinCrafting.setDisable(true);
        spinConstruction.setDisable(true);
        updateSaveNames();
        load("last");
        comboTeleport.setItems(FXCollections.observableList(GETeleports.getList()));
        comboSelling.setItems(FXCollections.observableList(SellingOptions.getList()));
        comboBuying.setItems(FXCollections.observableList(BuyingOptions.getList()));
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
        updateSaveNames();
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
        updateSaveNames();
    }

    public void updateSaveNames() {
        ScriptSettings loadableProfiles = ScriptSettings.getDefault();
        List<String> profiles = loadableProfiles.getSaveNames();
        txtProfile.setItems(FXCollections.observableList(profiles));
    }

}
