package scripts.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import org.tribot.script.sdk.util.ScriptSettings;
import scripts.data.Profile;

import java.net.URL;
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
    private TextField txtProfile;

    @FXML
    private Text txtReactionTimes;

    int mouseSpeed, reactions;

    @FXML
    void btnLoadPressed(ActionEvent event) {

    }

    @FXML
    void btnSavePressed(ActionEvent event) {
        save();
    }

    @FXML
    void btnStartPressed(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        spinCrafting.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(8,15));
        spinConstruction.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(25,50));
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

    public void save() {
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
        saver.save(txtProfile.getText(), saver);
    }


}
