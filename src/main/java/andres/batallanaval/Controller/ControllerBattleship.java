package andres.batallanaval.Controller;

import andres.batallanaval.Model.ModelBattleship;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.Node;
import javafx.scene.input.TransferMode;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.nio.file.Paths;

public class ControllerBattleship {
    private ModelBattleship model;

    @FXML
    private GridPane boardGrid;

    @FXML
    private HBox shipsContainer;

    @FXML
    private Button showInstructions;

    private static final int BOARD_SIZE = 10;
    private static final int CELL_SIZE = 60;

    // Imágenes para los diferentes estados y tipos de barcos
    private Image waterImage, hitImage, sunkImage;
    private Image portaavionesImage, submarinoImage, destructorImage, fragataImage;

    public void initialize() {
        model = new ModelBattleship();
        loadImages();
        createBoard();
        initializeShips();
    }

    // Carga las imágenes desde los recursos
    private void loadImages() {
        waterImage = new Image(Paths.get("D:/BatallaNaval/BatallaN/src/main/resources/Imagenes/agua.png").toUri().toString());
        hitImage = new Image(Paths.get("D:/BatallaNaval/BatallaN/src/main/resources/Imagenes/tocado.png").toUri().toString());
        sunkImage = new Image(Paths.get("D:/BatallaNaval/BatallaN/src/main/resources/Imagenes/hundido.png").toUri().toString());
        portaavionesImage = new Image(Paths.get("D:/BatallaNaval/BatallaN/src/main/resources/Imagenes/portaaviones.png").toUri().toString());
        submarinoImage = new Image(Paths.get("D:/BatallaNaval/BatallaN/src/main/resources/Imagenes/submarinos.png").toUri().toString());
        destructorImage = new Image(Paths.get("D:/BatallaNaval/BatallaN/src/main/resources/Imagenes/destructores.png").toUri().toString());
        fragataImage = new Image(Paths.get("D:/BatallaNaval/BatallaN/src/main/resources/Imagenes/fragatas.png").toUri().toString());
    }

    private void createBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Button cell = createCell(i, j);
                boardGrid.add(cell, j + 1, i + 1); // Agregar las celdas al tablero
            }
        }
    }

    private void initializeShips() {
        // Crea botones para cada barco y agrega funcionalidad de arrastrar
        createDraggableShip(portaavionesImage, 4);
        createDraggableShip(submarinoImage, 3);
        createDraggableShip(submarinoImage, 3);
        createDraggableShip(destructorImage, 2);
        createDraggableShip(destructorImage, 2);
        createDraggableShip(destructorImage, 2);
        createDraggableShip(fragataImage, 1);
        createDraggableShip(fragataImage, 1);
        createDraggableShip(fragataImage, 1);
        createDraggableShip(fragataImage, 1);
    }

    private void createDraggableShip(Image shipImage, int size) {
        Button shipButton = new Button();
        shipButton.setGraphic(new ImageView(shipImage));
        shipButton.setPrefSize(CELL_SIZE * size, CELL_SIZE);
        shipButton.setOnDragDetected(event -> {
            Dragboard db = shipButton.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(String.valueOf(size)); // Enviar el tamaño del barco
            db.setContent(content);
            event.consume();
        });

        shipsContainer.getChildren().add(shipButton); // Añadir cada barco al contenedor de barcos
    }

    private Button createCell(int x, int y) {
        Button cell = new Button();
        cell.setPrefSize(CELL_SIZE, CELL_SIZE);
        cell.setMaxSize(CELL_SIZE, CELL_SIZE);
        cell.setOnDragOver(event -> {
            if (event.getGestureSource() != cell && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        cell.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                int shipSize = Integer.parseInt(db.getString());
                boolean placed = model.placeShip(x, y, shipSize, true); // Colocación horizontal por defecto
                if (placed) {
                    updateBoard();
                    success = true;
                } else {
                    showError("No se puede colocar el barco aquí.");
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });

        return cell;
    }

    private void updateBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Button cell = (Button) getCellAt(i, j);
                ModelBattleship.CellStatus status = model.getCellStatus(i, j);
                updateCellStyle(cell, status);
            }
        }
    }

    private Node getCellAt(int x, int y) {
        return boardGrid.getChildren().stream()
                .filter(node -> GridPane.getColumnIndex(node) == (y + 1) && GridPane.getRowIndex(node) == (x + 1))
                .findFirst().orElse(null);
    }

    private void updateCellStyle(Button cell, ModelBattleship.CellStatus status) {
        switch (status) {
            case EMPTY:
                cell.setGraphic(new ImageView(waterImage));
                break;
            case SHIP:
                cell.setStyle("-fx-background-color: gray; -fx-font-weight: bold;"); // Usa imagen de barco aquí si deseas
                break;
            case HIT:
                cell.setGraphic(new ImageView(hitImage));
                break;
            case MISS:
                cell.setGraphic(new ImageView(waterImage));
                break;
            case SUNK:
                cell.setGraphic(new ImageView(sunkImage));
                break;
        }
    }

    @FXML
    private void startGame() {
        System.out.println("Juego iniciado");
        updateBoard();
    }

    @FXML
    private void resetGame() {
        model = new ModelBattleship();
        updateBoard();
        System.out.println("Juego reiniciado");
    }

    @FXML
    private void showInstructions() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Instrucciones del Juego");
        alert.setHeaderText("Reglas y cantidad de barcos:");

        String instructions =
                "Reglas del Juego:\n" +
                        "- Coloca los barcos en el tablero antes de iniciar.\n" +
                        "- Los barcos pueden colocarse horizontalmente por defecto.\n" +
                        "- Si un barco es tocado pero no hundido, mostrará una bomba.\n" +
                        "- Si un barco es hundido, mostrará un fuego en las celdas correspondientes.\n\n" +
                        "Cantidad de Barcos:\n" +
                        "1. Portaaviones (4 casillas) - Cantidad: 1\n" +
                        "2. Submarinos (3 casillas cada uno) - Cantidad: 2\n" +
                        "3. Destructores (2 casillas cada uno) - Cantidad: 3\n" +
                        "4. Fragatas (1 casilla cada una) - Cantidad: 4\n";

        alert.setContentText(instructions);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Colocación no válida");
        alert.setContentText(message);
        alert.showAndWait();
    }
}