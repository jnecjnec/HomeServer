package src;

import HomeServer.Handlers.Client.ClientHandler;
import HomeServer.Handlers.Device.DeviceHandler;
import HomeServer.Handlers.ObservableListWrapper;
import HomeServer.HomeServer;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.net.ssl.SSLException;

public class Main extends Application implements FXMLControllerListener {

    private HomeServer homeServerClients = null;
    private HomeServer homeServerDevices = null;
    private FXMLController controller = null;   
    private final ObservableListWrapper observableLists = new ObservableListWrapper();
    
       
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        homeServerClients = new HomeServer();
        homeServerDevices = new HomeServer();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Scene.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        controller = loader.getController();
        controller.setFXMLControllerListener(this);

        controller.setObservableLists(observableLists);

        stage.setScene(scene);
        stage.setTitle("HomeServer");

        stage.show();
    }

    @Override
    public void serverStart(int clientPort, int devicePort, boolean ssl) {
        try {
            homeServerClients.runServer(clientPort, ssl, new ClientHandler(observableLists));
            homeServerDevices.runServer(devicePort, false, new DeviceHandler(observableLists));
        } catch (InterruptedException | CertificateException | SSLException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
            homeServerClients.stopServer();
            homeServerDevices.stopServer();
        }
    }

    @Override
    public void serverStop() {
        homeServerClients.stopServer();
        homeServerDevices.stopServer();
    }

    @Override
    public boolean addUser(String name, String password) {
        return observableLists.addUser(name, password);
       
    }

    @Override
    public boolean deleteUser(String name) {
        return observableLists.delUser(name);
    }
}
