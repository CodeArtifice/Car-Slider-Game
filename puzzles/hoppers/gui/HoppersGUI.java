package puzzles.hoppers.gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersClientData;
import puzzles.hoppers.model.HoppersConfig;
import puzzles.hoppers.model.HoppersModel;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
/**
 * Game implementation of Hoppers Puzzle
 */
public class HoppersGUI extends Application implements Observer<HoppersModel, HoppersClientData> {
    /** Style related stuff */
    private final Font font = new Font("Comic Sans MS",15);
    private final Background sky = new Background(new BackgroundFill(Color.rgb(94, 109, 252),null,null));
    private final Background ground = new Background(new BackgroundFill(Color.rgb(101, 67, 33),null,null));
    private final Background btnBG = new Background(new BackgroundFill(Color.rgb(88, 161, 77),null,null));
    private final Border border = new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID,null,BorderStroke.THIN));
    private final Image redFrog = new Image(Objects.requireNonNull(getClass().getResourceAsStream( "resources/red_frog.png")));
    private final Image greenFrog = new Image(Objects.requireNonNull(getClass().getResourceAsStream("resources/green_frog.png")));
    private final Image lilyPad = new Image(Objects.requireNonNull(getClass().getResourceAsStream("resources/lily_pad.png")));
    private final Image water = new Image(Objects.requireNonNull(getClass().getResourceAsStream("resources/water.png")));

    /** GUI related things to help with implementation */
    private HoppersModel model;
    private String filename;
    private BorderPane layout;
    private Label title;
    private Stage stage;

    /**
     * GUI initializer
     * @throws IOException exception thrown if file is messed up
     */
    public void init() throws IOException {
        this.filename = getParameters().getRaw().get(0);
        this.model = new HoppersModel(filename);
        model.addObserver(this);
    }

    /**
     * GridPane creator
     * @param config current configuration
     * @return created GridPane
     */
    private GridPane makeGridPane(HoppersConfig config){
        GridPane grid = new GridPane();
        int row = model.getCurrentConfig().getRow();
        int col = model.getCurrentConfig().getCol();
        for(int r=0;r<row;r++){for(int c=0;c<col;c++){
            Button button = new Button();
            button.setMinSize(75,75); button.setMaxSize(75,75);
            if(r==row-1){button.setMaxSize(74,74);button.setMinSize(74,74);}
            char temp = config.getBoard()[r][c];
            if(temp=='.'){
                button.setGraphic(new ImageView(lilyPad));
            }
            else if(temp=='G'){
                button.setGraphic(new ImageView(greenFrog));
            }
            else if(temp=='R'){
                button.setGraphic(new ImageView(redFrog));
            }
            else{
                button.setGraphic(new ImageView(water));
            }
            int finalR = r; int finalC = c;
            button.setOnAction(e-> model.select(finalR,finalC));
            grid.add(button,c,r);
        }}
        grid.setGridLinesVisible(false);
        grid.setBorder(border);
        return grid;
    }

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane layout = new BorderPane();
        this.layout = layout; this.stage = stage;

        // Top of Borderpane:
        HBox top = new HBox(); top.setAlignment(Pos.CENTER);
        Label title = new Label(); title.setFont(font);
        title.setText("Loaded: " + filename); this.title = title;
        top.getChildren().add(title);
        top.setBackground(sky); top.setBorder(border);
        layout.setTop(top);

        // Center of Borderpane:
        GridPane grid = makeGridPane(model.getCurrentConfig()); grid.setAlignment(Pos.CENTER);
        layout.setCenter(grid);

        // Bottom of Borderpane:
        HBox bottom = new HBox(); bottom.setAlignment(Pos.CENTER);

            // Load Button:
            Button load = new Button(); load.setBackground(btnBG);
            load.setFont(font);load.setText("Load"); load.setBorder(border);
            FileChooser fileChooser = new FileChooser();
            load.setOnAction(e->{
                File file = fileChooser.showOpenDialog(stage);
                if(file!=null){
                    try {model.load(file.getPath()); this.filename=file.getPath();}
                    catch (IOException ioException) {ioException.printStackTrace();}
                }
            });

            // Hint Button:
            Button hint = new Button(); hint.setBackground(btnBG);
            hint.setFont(font); hint.setText("Hint"); hint.setBorder(border);
            hint.setOnAction(e->model.hint());

            // Reset Button:
            Button reset = new Button(); reset.setBackground(btnBG);
            reset.setFont(font); reset.setText("Reset"); reset.setBorder(border);
            reset.setOnAction(e-> {
                try { model.reset(filename);}
                catch (IOException ioException) {ioException.printStackTrace();}
            });

        bottom.getChildren().addAll(load,hint,reset);
        bottom.setBackground(ground); bottom.setBorder(border);
        layout.setBottom(bottom);

        // Set Scene and Stage
        Scene scene = new Scene(layout);
        stage.setTitle("Hoppers GUI");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    @Override
    public void update(HoppersModel hoppersModel, HoppersClientData data) {
        this.model = hoppersModel;
        GridPane newGrid = makeGridPane(model.getCurrentConfig());
        newGrid.setAlignment(Pos.CENTER); this.layout.setCenter(newGrid);
        this.title.setText(data.getMessage());
        stage.sizeToScene();
    }

    /**
     * Main method of HoppersGUI which Java calls
     * @param args command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 1) {System.out.println("Usage: java HoppersPTUI filename");}
        else {Application.launch(args);}
    }
}
