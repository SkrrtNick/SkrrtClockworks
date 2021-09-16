package scripts.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import org.tribot.script.sdk.util.ScriptSettings;
import scripts.api.functions.Loggable;
import scripts.api.functions.Logger;
import scripts.data.Profile;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GUIController extends AbstractGUIController {

    @FXML
    private Button btnLoad;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnStart;

    @FXML
    private CheckBox checkButler;

    @FXML
    private CheckBox checkClockworks;

    @FXML
    private CheckBox checkMouseSpeed;

    @FXML
    private CheckBox checkReactions;

    @FXML
    private CheckBox checkRestocking;

    @FXML
    private CheckBox checkSkills;

    @FXML
    private ComboBox<String> comboPricing;

    @FXML
    private ComboBox<String> comboTeleport;

    @FXML
    private Slider sliderMouseSpeed;

    @FXML
    private Slider sliderReactions;

    @FXML
    private Spinner<Integer> spinConstruction;

    @FXML
    private Spinner<Integer> spinCrafting;

    @FXML
    private Text txtMouseSpeed;

    @FXML
    private ComboBox<String> txtProfile;

    @FXML
    private Text txtReactionTimes;

    int mouseSpeed, reactions;

    private SpinnerValueFactory<Integer> craftingFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(8, 14);
    private SpinnerValueFactory<Integer> constructionFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(25, 50);
    private Logger logger = new Logger().setHeader("GUI");

    @FXML
    void btnLoadPressed(ActionEvent event) {
        String profileName = txtProfile.getValue();
        if(profileName.equals("")){
            logger.setLoggable(Loggable.ERROR)
                    .setMessage("Please enter a profile name")
                    .print();
        }
        else {
            load(txtProfile.getValue());
        }
    }

    @FXML
    void btnSavePressed(ActionEvent event) {
        save(txtProfile.getValue());
    }

    @FXML
    void btnStartPressed(ActionEvent event) {
        save("last");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateSaveNames();
        load("last");
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
                txtReactionTimes.setText(Integer.toString(reactions));
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
        saver.save(name, profile);
        updateSaveNames();
    }

    public void updateSaveNames() {
        ScriptSettings loadableProfiles = ScriptSettings.getDefault();
        List<String> profiles = loadableProfiles.getSaveNames();
        txtProfile.setItems(FXCollections.observableList(profiles));
    }
}
