package scripts.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class GUIController extends AbstractGUIController{

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
    private ComboBox<?> comboPricing;

    @FXML
    private ComboBox<?> comboTeleport;

    @FXML
    private Slider sliderMouseSpeed;

    @FXML
    private Slider sliderReactions;

    @FXML
    private Spinner<?> spinConstruction;

    @FXML
    private Spinner<?> spinCrafting;

    @FXML
    private Text txtMouseSpeed;

    @FXML
    private TextField txtProfile;

    @FXML
    private Text txtReactionTimes;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
