package andres.batallanaval.Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

public class MainMenuController {

    @FXML
    private Button jugarButton;

    @FXML
    private Button salirButton;

    @FXML
    private void onJugarButtonClick(ActionEvent event) {
        try {
            // Cargar la vista del tablero
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ViewBattleship.fxml"));
            Parent root = loader.load();

            // Configurar el controlador del tablero
            ControllerBattleship controller = loader.getController();
            controller.initialize();

            // Configurar la escena y el stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 750, 820);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onSalirButtonClick() {
        Stage stage = (Stage) salirButton.getScene().getWindow();
        stage.close();
    }
}