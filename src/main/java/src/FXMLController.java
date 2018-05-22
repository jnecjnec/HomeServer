package src;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;

public class FXMLController implements Initializable {

    @FXML
    private CheckBox sslCheckBox;

    @FXML
    private Button runButton;

    @FXML
    private Button stopButton;

    @FXML
    private Spinner clientPortSpinner;

    @FXML
    private Spinner devicesPortSpinner;

    ServerStartStopListener startStopListener;

    public void setServerStartStopListener(ServerStartStopListener startStopListener) {
        this.startStopListener = startStopListener;
    }

    public FXMLController() {
        this.startStopListener = null;
    }

    @FXML
    void handleStartServerAction(ActionEvent event) {
        if (startStopListener != null) {
            startStopListener.serverStart(8007, 8008, sslCheckBox.isIndeterminate());
        }
        runButton.setDisable(true);
        stopButton.setDisable(false);
        sslCheckBox.setDisable(true);
        clientPortSpinner.setDisable(true);
        devicesPortSpinner.setDisable(true);
    }

    @FXML
    void handleStopServerAction(ActionEvent event) {
        if (startStopListener != null) {
            startStopListener.serverStop();
        }
        runButton.setDisable(false);
        stopButton.setDisable(true);
        sslCheckBox.setDisable(false);
        clientPortSpinner.setDisable(false);
        devicesPortSpinner.setDisable(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stopButton.setDisable(true);
      //  clientPortSpinner.setv 
      //  devicesPortSpinner
    }
}
