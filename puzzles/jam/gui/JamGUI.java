package puzzles.jam.gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import puzzles.common.Observer;
import puzzles.jam.model.JamClientData;
import puzzles.jam.model.JamConfig;
import puzzles.jam.model.JamModel;

import java.io.File;
import java.io.IOException;

public class JamGUI extends Application  implements Observer<JamModel, JamClientData>  {
    // for demonstration purposes
    private final static String X_CAR_COLOR = "#DF0101";
    private final static int BUTTON_FONT_SIZE = 20;
    private final static int ICON_SIZE = 75;

    private JamModel model;
    private String currentFile;
    private BorderPane border;
    private String[][] boardArray;
    private Label indicator;
    private Stage stage;

    /**
     * Initializes the starting config, boardArray, and model.
     * */
    public void init() {
        this.currentFile = getParameters().getRaw().get(0);
        try {
            JamConfig config = new JamConfig(currentFile);
            this.boardArray = config.getBoard();
            this.model = new JamModel(config);
            this.model.addObserver(this);
        } catch (IOException e) {
            System.out.println("Please input a valid file");
        }
    }

    /**
     * Sets up a borderpane with the indicator on the top, grid in the middle, and load, reset, and hint buttons on the bottom
     * */
    @Override
    public void start(Stage stage) throws Exception {
        // Indicator at the top
        this.stage = stage;
        border = new BorderPane();
        stage.setTitle("Traffic Jam GUI");
        HBox box = new HBox(); box.setAlignment(Pos.CENTER);
        indicator = new Label();
        indicator.setText("Welcome to Traffic Jam!");
        box.getChildren().add(indicator);
        border.setTop(box);

        // Buttons at the bottom
        HBox hBox = new HBox();
        final FileChooser fileChooser = new FileChooser();
        Button load = new Button("Load");
        load.setOnAction(
                actionEvent -> {
                    File file = fileChooser.showOpenDialog(stage);
                    if (file != null) {
                        openFile(file);
                    }
                }
        );
        Button reset = new Button("Reset");
        reset.setOnAction((event) -> this.model.reset(currentFile));
        Button hint = new Button("Hint");
        hint.setOnAction((event) -> this.model.hint());
        hBox.getChildren().addAll(load, reset, hint);
        hBox.setAlignment(Pos.CENTER);
        border.setBottom(hBox);

        // The Center Grid
        GridPane grid = new GridPane();
        setGridByArray(grid);
        border.setCenter(grid);


        Scene scene = new Scene(border);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    public void openFile(File file) {
        String filename = file.getPath();
        this.model.load(filename);
    }

    /**
     * Updates the grid and the indicator
     * */
    @Override
    public void update(JamModel jamModel, JamClientData jamClientData) {
        this.boardArray = jamModel.getBoardArray();
        GridPane grid = new GridPane();
        setGridByArray(grid);
        this.border.setCenter(grid);
        this.indicator.setText(jamClientData.getIndicator());
        stage.sizeToScene();
    }

    /**
     * Destructively updates a grid to match the boardArray. Also changes the size of the window
     * */
    private void setGridByArray(GridPane grid) {
        for (int row = 0; row < boardArray.length; ++row) {
            for (int col = 0; col < boardArray[row].length; ++col) {
                Button button = new Button(boardArray[row][col]);
                button.setMinSize(ICON_SIZE, ICON_SIZE);
                button.setMaxSize(ICON_SIZE, ICON_SIZE);
                String style = "-fx-font-size: " + BUTTON_FONT_SIZE + ";" + "-fx-font-weight: bold;";
                if (boardArray[row][col].equals("X")) {
                    style += "-fx-background-color: " + X_CAR_COLOR + ";";
                } else if (boardArray[row][col].equals(".")) {
                    style += "-fx-background-color: gray;";
                } else {
                    int hash = boardArray[row][col].hashCode();
                    style += "-fx-background-color: rgb(" + (hash + 31) % 360 + "," + (hash * 100000 + 31) % 360 + "," + (hash * 300000 - 31) % 360 + ")"  + ";";
                }
                button.setStyle(style);
                int finalRow = row;
                int finalCol = col;
                button.setOnAction((event) -> this.model.select(finalRow, finalCol));
                grid.add(button, col, row);
            }
        }
        grid.setAlignment(Pos.CENTER);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
