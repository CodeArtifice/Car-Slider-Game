package puzzles.jam.model;

public class JamClientData {
    private String indicator;
    private String boardString;
    private String currentFile;

    /**
     * Getter for the currentFile
     * @return the currentfile
     * */
    public String getCurrentFile() {
        return currentFile;
    }

    /**
     * Constructor for the JamClientData class with a boardstring, indicator, and currentfile
     * @param boardString A string representing the board
     * @param indicator A message to be displayed to the user
     * @param currentFile The filename of the current file being used for the puzzle
     * */
    public JamClientData(String boardString, String indicator, String currentFile) {
        this.boardString = boardString;
        this.indicator = indicator;
        this.currentFile = currentFile;
    }

    /**
     * Constructor for the JamClientData class with both a boardString and indicator
     * @param boardString A string representing the board
     * @param indicator A message to be displayed to the user
     * */
    public JamClientData(String boardString, String indicator) {
        this.indicator = indicator;
        this.boardString = boardString;
    }

    /**
     * Getter for the indicator
     * @return the indicator
     * */
    public String getIndicator() {
        return indicator;
    }

    /**
     * Getter for the boardString
     * @return the boardString
     * */
    public String getBoardString() {
        return boardString;
    }
}
