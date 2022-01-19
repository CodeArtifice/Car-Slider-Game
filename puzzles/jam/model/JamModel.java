package puzzles.jam.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.jam.solver.Jam;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class JamModel {
    /** the collection of observers of this model */
    private final List<Observer<JamModel, JamClientData>> observers = new LinkedList<>();

    /** the current configuration */
    private JamConfig currentConfig;

    /** the coordinates of the current selection*/
    private int selectedRow = -1;
    private int selectedCol = -1;

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<JamModel, JamClientData> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(JamClientData data) {
        for (var observer : observers) {
            observer.update(this, data);
        }
    }

    /**
     * Loads a new file into this model
     * @param filename The name of the file to be loaded
     * */
    public void load(String filename) {
        try {
            JamConfig jamConfig = new JamConfig(filename);
            this.currentConfig = jamConfig;
            alertObservers(new JamClientData(currentConfig.toString(), "Loaded: " + filename, filename));
        } catch (IOException e) {
            alertObservers(new JamClientData(currentConfig.toString(), "Failed to load: " + filename));
        }
    }

    /**
     * Getter function for the board array of the current config
     * @return The board array of the current config
     * */
    public String[][] getBoardArray() {
        return this.currentConfig.getBoard();
    }

    /**
     * Loads the current file back into the model, resetting it. ONLY CALL WITH THE CURRENT FILE
     * Ignores any potential error because the file should have already been verified
     * @rit.pre The filename is the same as the file used to load this model already
     * @param filename The current file that this model is based on
     * */
    public void reset(String filename) {
        try {
            JamConfig jamConfig = new JamConfig(filename);
            this.currentConfig = jamConfig;
            alertObservers(new JamClientData(currentConfig.toString(), "Puzzle Reset"));
        } catch (IOException ignored) {}
    }

    /**
     * If no car is already selected, tries to select a car at the coordinates given.
     * If a car is already selected, tries to move the car in the direction of the coordinates
     * See JamConfig.moveCar
     * @param row The row selected
     * @param col The column selected
     * */
    public void select(int row, int col) {
        if (selectedCol < 0 && selectedRow < 0) {
            if (this.currentConfig.isACar(row, col).equals("yes")) {
                this.selectedRow = row;
                this.selectedCol = col;
                alertObservers(new JamClientData(currentConfig.toString(), "Selected (" + row + ", " + col + ")"));
            } else if (this.currentConfig.isACar(row, col).equals("no")) {
                alertObservers(new JamClientData(currentConfig.toString(), "Please select a car before a blank space"));
            } else {
                alertObservers(new JamClientData(currentConfig.toString(), "Please select a space within the array"));
            }
        } else {
            String indicator = this.currentConfig.moveCar(this.selectedRow, this.selectedCol, row, col);
            alertObservers(new JamClientData(currentConfig.toString(), indicator));
            this.selectedRow = -1;
            this.selectedCol = -1;
        }
    }

    /**
     * Moves the board one more step towards the solution if there is one
     * */
    public void hint() {
        Solver solver = new Solver();
        List<Configuration> path = (List<Configuration>) solver.solve(this.currentConfig);
        if (path == null) {
            alertObservers(new JamClientData(this.currentConfig.toString(), "Unsolvable"));
        } else if (path.size() >= 2) {
            this.currentConfig = (JamConfig) path.get(1);
            alertObservers(new JamClientData(this.currentConfig.toString(), "Next Step!"));
        } else if (path.size() == 1) {
            alertObservers(new JamClientData(this.currentConfig.toString(), "Already Solved!"));
        }

    }


    /**
     * Constructor for the JamModel class
     * @param jamConfig The current configuration of the puzzle
     * */
    public JamModel(JamConfig jamConfig) {
        this.currentConfig = jamConfig;
    }
}
