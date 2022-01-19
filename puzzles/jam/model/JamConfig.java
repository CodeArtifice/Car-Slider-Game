package puzzles.jam.model;

// TODO: implement your JamConfig for the common solver

import puzzles.common.solver.Configuration;

import java.io.*;
import java.util.*;

/**
 * A configuration of the Traffic Jam game
 * */
public class JamConfig implements Configuration {
    // 2-D representation of the game. Uses letters for cars and periods for blank spaces
    private String[][] board;
    // HashMap of JamCars. Useful for moving them and generating the board.
    private HashMap<String,JamCar> jamCarMap;

    /**
     * Constructor for the initial configuration
     * @param filename The filename of the initial puzzle configuration
     */
    public JamConfig(String filename) throws IOException {
        prepareByFile(filename);
    }

    /**
     * Copy constructor for JamConfig
     * @param jamConfig The JamConfig to be copied
     * */
    public JamConfig(JamConfig jamConfig) {
        this.board = new String[jamConfig.board.length][jamConfig.board[0].length];
        for (int i = 0; i < this.board.length; ++i) {
            this.board[i] = jamConfig.board[i].clone();
        }

        this.jamCarMap = new HashMap<>();
        for (JamCar jamCar : jamConfig.jamCarMap.values()) {
            this.jamCarMap.put(jamCar.getName(), new JamCar(jamCar));
        }
    }

    /**
     * Creates an initial state for the board and jamCarMap using a starting file
     * @rit.pre The file has a valid starting configuration
     * @param filename The name of the file with the starting information
     * */
    public void prepareByFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        jamCarMap = new HashMap<>();

        // The first line of the file has the dimensions of the board
        String[] splitLine = reader.readLine().split(" ");
        this.board = new String[Integer.parseInt(splitLine[0])][Integer.parseInt(splitLine[1])];

        // The second line tells the number of cars to follow
        int numberOfCars = Integer.parseInt(reader.readLine());
        for (int i = 0; i < numberOfCars; ++i) {
            // Creates a new JamCar based on the file's description, then adds it to the map.
            splitLine = reader.readLine().split(" ");
            JamCar jamCar = new JamCar(splitLine[0], Integer.parseInt(splitLine[1]), Integer.parseInt(splitLine[2]), Integer.parseInt(splitLine[3]), Integer.parseInt(splitLine[4]));
            jamCarMap.put(jamCar.getName(), jamCar);
        }
        setBoardFromMap();

    }

    /**
     * Updates the board so that it matches the jamCarMap
     * */
    public void setBoardFromMap() {
        // Fills the board with periods to be used as blank spaces
        for (String[] strings : board) {
            Arrays.fill(strings, ".");
        }
        // Replaces the empty spaces with cars
        for (JamCar jamCar : jamCarMap.values()) {
            board[jamCar.getStartRow()][jamCar.getStartCol()] = jamCar.getName();
            if (jamCar.getIsHorizontal()) {
                for (int i = jamCar.getStartCol() + 1; i <= jamCar.getEndCol(); ++i) {
                    board[jamCar.getStartRow()][i] = jamCar.getName();
                }
            } else {
                for (int i = jamCar.getStartRow() + 1; i <= jamCar.getEndRow(); ++i) {
                    board[i][jamCar.getStartCol()] = jamCar.getName();
                }
            }
        }
    }

    /*
    * Checks whether two JamConfigs are equal
    * @return true if they're equal, false otherwise
    * */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JamConfig jamConfig = (JamConfig) o;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (!board[i][j].equals(jamConfig.board[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        String output = "   ";
        for (int i = 0; i < board[0].length; ++i) {
            output += i + " ";
        }
        output += "\n ";
        for (int i = 0; i < 2.5 * board[0].length; ++i) {
            output += "-";
        }
        output += "\n";
        for (int i = 0; i < board.length; ++i) {
            output += i + "| ";
            for (int col = 0; col < board[i].length; ++col) {
                output += board[i][col] + " ";
            }
            output += "|\n";
        }
        output += " ";
        for (int i = 0; i < 2.5 * board[0].length; ++i) {
            output += "-";
        }
        return output;
    }

    /*
    * Returns a hashcode of this jamConfig
    * @return A hashcode of this jamConfig
    * */
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    /**
     * Gets a collection of the successors from current one.
     *
     * @return all successors
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        Collection<Configuration> neighbors = new ArrayList<>();
        for (JamCar jamCar : jamCarMap.values()) {
            if (jamCar.getIsHorizontal()) {
                // Config where this piece is moved left
                if (jamCar.getStartCol() != 0 && board[jamCar.getStartRow()][jamCar.getStartCol() - 1].equals(".")) {
                    JamConfig neighbor = new JamConfig(this);
                    neighbor.jamCarMap.replace(jamCar.getName(), jamCar.moveHorizontally(-1));
                    neighbor.setBoardFromMap();
                    neighbors.add(neighbor);
                }
                // Config where this piece is moved right
                if (jamCar.getEndCol() != board[jamCar.getStartRow()].length - 1 && board[jamCar.getStartRow()][jamCar.getEndCol() + 1].equals(".")) {
                    JamConfig neighbor = new JamConfig(this);
                    neighbor.jamCarMap.replace(jamCar.getName(), jamCar.moveHorizontally(1));
                    neighbor.setBoardFromMap();
                    neighbors.add(neighbor);
                }
            } else {
                // Config where this piece is moved up
                if (jamCar.getStartRow() != 0 && board[jamCar.getStartRow() - 1][jamCar.getStartCol()].equals(".")) {
                    JamConfig neighbor = new JamConfig(this);
                    neighbor.jamCarMap.replace(jamCar.getName(), jamCar.moveVertically(-1));
                    neighbor.setBoardFromMap();
                    neighbors.add(neighbor);
                }
                // Config where this piece is moved down
                if (jamCar.getEndRow() != board.length - 1 && board[jamCar.getEndRow() + 1][jamCar.getStartCol()].equals(".")) {
                    JamConfig neighbor = new JamConfig(this);
                    neighbor.jamCarMap.replace(jamCar.getName(), jamCar.moveVertically(1));
                    neighbor.setBoardFromMap();
                    neighbors.add(neighbor);
                }
            }
        }
        return neighbors;
    }

    /**
     * Has the goal been reached?
     *
     * @return true if the red car is in the furthest-right column, false otherwise
     */
    @Override
    public boolean isGoal() {
        JamCar redCar = jamCarMap.get("X");
        return redCar.getEndCol() == board[redCar.getStartRow()].length - 1;
    }

    /**
     * Is the space at the given row and col taken up by a car?
     * */
    public String isACar(int row, int col) {
        try {
            if (jamCarMap.containsKey(board[row][col])) {
                return "yes";
            } else {
                return "no";
            }
        } catch (IndexOutOfBoundsException e) {
            return "IndexOutOfBounds";
        }
    }

    /**
     * Getter for the board array
     * @return the board array
     * */
    public String[][] getBoard() {
        return board;
    }

    /**
     * Moves a car in the specified row and column by 1 in the direction of the endRow and endCol if they're valid
     * @param carRow The row of the car
     * @param carCol the column of the car
     * @param endRow The destination row of the car
     * @param endCol The destination column of the car
     * @return indicator string for the PTUI
     * */
    public String moveCar(int carRow, int carCol, int endRow, int endCol) {
        try {
            if (!jamCarMap.containsKey(board[carRow][carCol])) {
                return "Starting position doesn't have a corresponding car";
            }
        } catch (IndexOutOfBoundsException e) {
            return "Please select a car within the bounds of the board";
        }
        JamCar movingCar = jamCarMap.get(board[carRow][carCol]);
        int headRow = movingCar.getStartRow();
        int headCol = movingCar.getStartCol();
        int rearRow = movingCar.getEndRow();
        int rearCol = movingCar.getEndCol();
        try {
            if (movingCar.getIsHorizontal()) {
                if (headRow == endRow) {
                    if (endCol > rearCol) {
                        if (board[headRow][rearCol + 1].equals(".")) {
                            jamCarMap.replace(board[carRow][carCol], movingCar.moveHorizontally(1));
                            this.setBoardFromMap();
                            return "Moved " + movingCar.getName() + " to the right 1 space";
                        } else {
                            return "Can't move " + movingCar.getName() + " to the right 1 space";
                        }
                    } else if (endCol < headCol) {
                        if (board[headRow][headCol - 1].equals(".")) {
                            jamCarMap.replace(board[carRow][carCol], movingCar.moveHorizontally(-1));
                            this.setBoardFromMap();
                            return "Moved " + movingCar.getName() + " to the left 1 space";
                        } else {
                            return "Can't move " + movingCar.getName() + " to the left 1 space";
                        }
                    } else {
                        return "To move a horizontal piece, select a space to the right or left of it";
                    }
                } else {
                    return "Can't move a horizontal piece vertically";
                }
            } else {
                if (headCol == endCol) {
                    if (endRow > rearRow) {
                        if (board[rearRow + 1][carCol].equals(".")) {
                            jamCarMap.replace(board[carRow][carCol], movingCar.moveVertically(1));
                            this.setBoardFromMap();
                            return "Moved " + movingCar.getName() + " down 1 space";
                        } else {
                            return "Can't move " + movingCar.getName() + " down 1 space";
                        }
                    } else if (endRow < headRow) {
                        if (board[headRow - 1][carCol].equals(".")) {
                            jamCarMap.replace(board[carRow][carCol], movingCar.moveVertically(-1));
                            this.setBoardFromMap();
                            return "Moved " + movingCar.getName() + " up 1 space";
                        } else {
                            return "Can't move " + movingCar.getName() + " up 1 space";
                        }
                    } else {
                        return "To move a vertical piece, select a space above or below your current selection";
                    }
                } else {
                    return "Can't move a vertical piece horizontally";
                }
            }
        } catch (IndexOutOfBoundsException e) {
            return "Please select a location within the board";
        }
    }
}
