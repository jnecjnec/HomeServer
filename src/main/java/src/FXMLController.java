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
import static HomeServer.HandlerListener.EventType.evtAdd;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class FXMLController implements Initializable, HandlerListener {

    private class ViewData {

        private final StringProperty ipAddress;
        private final StringProperty name;
        private final StringProperty number;

        public ViewData(StringProperty ipAddress, StringProperty name, StringProperty number) {
            this.ipAddress = ipAddress;
            this.name = name;
            this.number = number;
        }

        public StringProperty getIpAddress() {
            return ipAddress;
        }

        public StringProperty getName() {
            return name;
        }
        
         public StringProperty getNumber() {
            return number;
        }
    }

    private final SpinnerValueFactory<Integer> clientPortSpinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99999, 8007);
    private final SpinnerValueFactory<Integer> devicePortSpinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99999, 8008);

    private ServerStartStopListener startStopListener;

    public void setServerStartStopListener(ServerStartStopListener startStopListener) {
        this.startStopListener = startStopListener;
    }

    public FXMLController() {
        this.startStopListener = null;
    }

    @FXML
    private TableView<ViewData> tableViewDeviceList;
    @FXML
    private TableColumn<ViewData, String> deviceListIpAddress;
    @FXML
    private TableColumn<ViewData, String> deviceListName;
    @FXML
    private TableColumn<ViewData, String> deviceListNumber;
    
    @FXML
    private TableView<ViewData> tableViewClientList;
    @FXML
    private TableColumn<ViewData, String> clientListIpAddress;

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

        deviceListIpAddress.setCellValueFactory(cellData -> cellData.getValue().getIpAddress());
        deviceListName.setCellValueFactory(cellData -> cellData.getValue().getName());
        deviceListNumber.setCellValueFactory(cellData -> cellData.getValue().getNumber());
        
        clientListIpAddress.setCellValueFactory(cellData -> cellData.getValue().getIpAddress());
    }

    @Override
    public void onClientChange(String ip, EventType evtype) {
        ViewData data = new ViewData(new SimpleStringProperty(ip), new SimpleStringProperty(""), new SimpleStringProperty(""));

        switch (evtype) {
            case evtAdd:
                tableViewClientList.getItems().add(data);
                break;
            case evtRemove: {
                ViewData d = null;

                for (ViewData item : tableViewClientList.getItems()) {
                    if (item.getIpAddress().getValue().equals(data.getIpAddress().getValue())) {
                        d = item;
                    }
                }
                if (d != null) {
                    tableViewClientList.getItems().remove(d);
                }
                break;
            }
        
        }
    }

    @Override
    public void onDeviceChange(String ip, String name, String number, EventType evtype) {
        ViewData data = new ViewData(new SimpleStringProperty(ip), new SimpleStringProperty(name), new SimpleStringProperty(number));

        switch (evtype) {
            case evtAdd:
                tableViewDeviceList.getItems().add(data);
                break;
            case evtRemove: {
                ViewData d = null;

                for (ViewData item : tableViewDeviceList.getItems()) {
                    if (item.getIpAddress().getValue().equals(data.getIpAddress().getValue())) {
                        d = item;
                    }
                }
                if (d != null) {
                    tableViewDeviceList.getItems().remove(d);
                }
                break;
            }
        
        }
    }
}
