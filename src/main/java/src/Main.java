package src;

import HomeServer.ClientHandler;
import HomeServer.DeviceHandler;
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

public class Main extends Application implements ServerStartStopListener {

    HomeServer homeServerClients = null;
    HomeServer homeServerDevices = null;
    FXMLController controller = null;

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
        controller.setServerStartStopListener(this);

        stage.setScene(scene);
        stage.setTitle("HomeServer");

        stage.show();
    }

    @Override
    public void serverStart(int clientPort, int devicePort, boolean ssl) {
        try {
            homeServerClients.runServer(clientPort, ssl, new ClientHandler(controller));
            homeServerDevices.runServer(devicePort, false, new DeviceHandler(controller));
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
}
