package src;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import HomeServer.HandlerListener;

public class FXMLController implements Initializable, HandlerListener {

    @FXML
    private CheckBox sslCheckBox;

    @FXML
    private Button runButton;

    @FXML
    private Button stopButton;

    @FXML
    private Spinner<Integer> clientPortSpinner;

    @FXML
    private Spinner<Integer> devicesPortSpinner;

    SpinnerValueFactory<Integer> clientPortSpinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99999, 8007);
    SpinnerValueFactory<Integer> devicePortSpinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99999, 8008);

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
            startStopListener.serverStart(clientPortSpinnerValueFactory.getValue(), devicePortSpinnerValueFactory.getValue(), sslCheckBox.isIndeterminate());
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

        clientPortSpinner.setValueFactory(clientPortSpinnerValueFactory);
        devicesPortSpinner.setValueFactory(devicePortSpinnerValueFactory);
    }

    @Override
    public void onClientChenge(String ip, String name, EventType evtype) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onDeviceChange(String ip, String name, EventType evtype) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
