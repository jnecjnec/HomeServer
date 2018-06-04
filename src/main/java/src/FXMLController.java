package src;

import HomeServer.Handlers.Device.Device;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import HomeServer.Handlers.ObservableListWrapper;
import HomeServer.Handlers.User.User;
import java.util.Optional;
import javafx.application.Platform;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class FXMLController implements Initializable {

    public FXMLController() {
        this._FXMLControllerListener = null;
    }

    private final SpinnerValueFactory<Integer> clientPortSpinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99999, 8007);
    private final SpinnerValueFactory<Integer> devicePortSpinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99999, 8008);

    private FXMLControllerListener _FXMLControllerListener;

    public void setFXMLControllerListener(FXMLControllerListener startStopListener) {
        this._FXMLControllerListener = startStopListener;
    }

    public void setObservableLists(ObservableListWrapper lists) {
        tableViewUserList.setItems(lists.getUsers());
        tableViewDeviceList.setItems(lists.getDevices());
    }

    @FXML
    private TableView<Device> tableViewDeviceList;
    @FXML
    private TableColumn<Device, String> deviceListIpAddress;
    @FXML
    private TableColumn<Device, String> deviceListName;
    @FXML
    private TableColumn<Device, String> deviceListNumber;

    @FXML
    private TableView<User> tableViewUserList;
    @FXML
    private TableColumn<User, String> userListName;
    @FXML
    private TableColumn<User, String> userListIp;

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
        if (_FXMLControllerListener != null) {
            _FXMLControllerListener.serverStart(clientPortSpinnerValueFactory.getValue(), devicePortSpinnerValueFactory.getValue(), sslCheckBox.isIndeterminate());
        }
        runButton.setDisable(true);
        stopButton.setDisable(false);
        sslCheckBox.setDisable(true);
        clientPortSpinner.setDisable(true);
        devicesPortSpinner.setDisable(true);
    }

    @FXML
    void handleStopServerAction(ActionEvent event) {
        if (_FXMLControllerListener != null) {
            _FXMLControllerListener.serverStop();
        }
        runButton.setDisable(false);
        stopButton.setDisable(true);
        sslCheckBox.setDisable(false);
        clientPortSpinner.setDisable(false);
        devicesPortSpinner.setDisable(false);
    }

    @FXML
    void handleAddUser(ActionEvent event) {

        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("HomeServer");
        dialog.setHeaderText("Add user");

         // Set the icon (must be included in the project).
         // dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Add user", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("User name");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        grid.add(new Label("User name:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);

        // Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> username.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(usernamePassword -> {

            if (_FXMLControllerListener != null) {
                if (!_FXMLControllerListener.addUser(usernamePassword.getKey(), usernamePassword.getValue())) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("HomeServer");
                    alert.setHeaderText("Error !!!");
                    alert.setContentText("User " + usernamePassword.getKey() + " already exists !");
                    alert.showAndWait();
                };
            }
        });
    }
    
    @FXML
    void handleDelUser(ActionEvent event) {
        tableViewUserList.getItems().removeAll(tableViewUserList.getSelectionModel().getSelectedItems());
    }
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stopButton.setDisable(true);

        clientPortSpinner.setValueFactory(clientPortSpinnerValueFactory);
        devicesPortSpinner.setValueFactory(devicePortSpinnerValueFactory);

        deviceListIpAddress.setCellValueFactory(new PropertyValueFactory<>("Ip"));
        deviceListName.setCellValueFactory(new PropertyValueFactory<>("DeviceName"));
        deviceListNumber.setCellValueFactory(new PropertyValueFactory<>("DeviceNumber"));
        
        userListName.setCellValueFactory(new PropertyValueFactory<>("name"));
        userListIp.setCellValueFactory(new PropertyValueFactory<>("Ip"));
    }
}
