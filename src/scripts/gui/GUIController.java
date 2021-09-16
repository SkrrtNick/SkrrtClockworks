package scripts.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        sliderMouseSpeed.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mouseSpeed = (int) sliderMouseSpeed.getValue();
                txtMouseSpeed.setText(Integer.toString(mouseSpeed));
            }
        });

    }
}
