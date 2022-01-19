package puzzles.hoppers.model;

import puzzles.common.solver.Configuration;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Configuration creator used to make the puzzle
 */
public class HoppersConfig implements Configuration {
    /** Dimensions of board */
    private final int row;
    private final int col;

    /** Where we are on board */
    private int cursorRow;
    private int cursorCol;

    /** 2D array of game*/
    private final char[][] board;

    /**
     * Configuration constructor for Hoppers puzzle
     * @param filename name of the current file
     * @throws IOException exception thrown if file is messed up
     */
    public HoppersConfig(String filename) throws IOException {
        cursorCol = 0; cursorRow = 0;
        BufferedReader in = new BufferedReader(new FileReader(filename));
        String [] fields = in.readLine().split("\\s+");

        // get the row and column values of board
        this.row = Integer.parseInt(fields[0]);
        this.col = Integer.parseInt(fields[1]);

        // create the board
        this.board = new char[row][col];
        for(int r=0;r<row;r++){
            String[] cells = in.readLine().split("\\s+");
            for(int c=0;c<col;c++){ board[r][c] = cells[c].charAt(0); }
        }
    }

    /**
     * Copy constructor used to create various configurations
     * @param other configuration which is being copied
     */
    private HoppersConfig(HoppersConfig other){
        this.row = other.row;
        this.col = other.col;

        this.cursorRow = other.cursorRow;
        this.cursorCol = other.cursorCol;

        this.board = new char[row][col];
        for(int i=0;i<row;i++){
            System.arraycopy(other.board[i],0,this.board[i],0,col);
        }
    }

    /**
     * gets the number of columns
     * @return returns column value
     */
    public int getCol() { return col; }

    /**
     * gets the number of rows
     * @return returns row value
     */
    public int getRow() { return row; }

    /**
     * gets the board configuration
     * @return returns board
     */
    public char[][] getBoard() { return board; }

    /**
     * Helper function which looks at the next available cell
     */
    private void nextCell(){
        // if we're on even row
        if(cursorRow % 2 == 0){
            // if not at the edge
            if(cursorCol!=col-1){ cursorCol = cursorCol + 2; }
            // if at edge
            else{ cursorRow++; cursorCol=1; }
        }

        // if we're on odd row
        else{
            // if not at the edge
            if(cursorCol!=col-2){ cursorCol = cursorCol + 2; }
            // if at edge
            else{ cursorRow++; cursorCol=0; }
        }
    }

    /**
     * Looks at current cell (R or G) and sees if there is a valid hop
     * @return configuration of valid hops from that cell
     */
    private Collection<Configuration> validHopHelper(){
        Collection<Configuration> configs = new ArrayList<>();
        char current = board[cursorRow][cursorCol];
        int r = cursorRow; int c = cursorCol;

        // Diagonal Bottom Right
        if(r+1<=row-1&&c+1<=col-1&&board[r+1][c+1]=='G'){
            if(r+2<=row-1&&c+2<=col-1&&board[r+2][c+2]=='.'){
                HoppersConfig config = new HoppersConfig(this);
                config.board[r][c] = '.';
                config.board[r+1][c+1] = '.';
                config.board[r+2][c+2] = current;
                configs.add(config);
            }
        }

        // Diagonal Bottom Left
        if(r+1<=row-1&&c-1>=0&&board[r+1][c-1]=='G'){
            if(r+2<=row-1&&c-2>=0&&board[r+2][c-2]=='.'){
                HoppersConfig config = new HoppersConfig(this);
                config.board[r][c] = '.';
                config.board[r+1][c-1] = '.';
                config.board[r+2][c-2] = current;
                configs.add(config);
            }
        }

        // Diagonal Top Right
        if(r-1>=0&&c+1<=col-1&&board[r-1][c+1]=='G'){
            if(r-2>=0&&c+2<=col-1&&board[r-2][c+2]=='.'){
                HoppersConfig config = new HoppersConfig(this);
                config.board[r][c] = '.';
                config.board[r-1][c+1] = '.';
                config.board[r-2][c+2] = current;
                configs.add(config);
            }
        }

        // Diagonal Top Left
        if(r-1>=0&&c-1>=0&&board[r-1][c-1]=='G'){
            if(r-2>=0&&c-2>=0&&board[r-2][c-2]=='.'){
                HoppersConfig config = new HoppersConfig(this);
                config.board[r][c] = '.';
                config.board[r-1][c-1] = '.';
                config.board[r-2][c-2] = current;
                configs.add(config);
            }
        }

        // If frog on even space:
        if(cursorRow % 2 == 0) {

            // Top
            if (r - 2 >= 0 && board[r - 2][c] == 'G') {
                if (r - 4 >= 0 && board[r - 4][c] == '.') {
                    HoppersConfig config = new HoppersConfig(this);
                    config.board[r][c] = '.';
                    config.board[r - 2][c] = '.';
                    config.board[r - 4][c] = current;
                    configs.add(config);
                }
            }

            // Bottom
            if (r + 2 <= row - 1 && board[r + 2][c] == 'G') {
                if (r + 4 <= row - 1 && board[r + 4][c] == '.') {
                    HoppersConfig config = new HoppersConfig(this);
                    config.board[r][c] = '.';
                    config.board[r + 2][c] = '.';
                    config.board[r + 4][c] = current;
                    configs.add(config);
                }
            }

            // Left
            if (c - 2 >= 0 && board[r][c - 2] == 'G') {
                if (c - 4 >= 0 && board[r][c - 4] == '.') {
                    HoppersConfig config = new HoppersConfig(this);
                    config.board[r][c] = '.';
                    config.board[r][c - 2] = '.';
                    config.board[r][c - 4] = current;
                    configs.add(config);
                }
            }

            // Right
            if (c + 2 <= col - 1 && board[r][c + 2] == 'G') {
                if (c + 4 <= col - 1 && board[r][c + 4] == '.') {
                    HoppersConfig config = new HoppersConfig(this);
                    config.board[r][c] = '.';
                    config.board[r][c + 2] = '.';
                    config.board[r][c + 4] = current;
                    configs.add(config);
                }
            }
        }

        return configs;
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        Collection<Configuration> configs = new ArrayList<>();
        cursorRow = 0; cursorCol = 0;
        while(cursorRow!=row-1 || cursorCol!=col-1){
            if(board[cursorRow][cursorCol]=='R'|| board[cursorRow][cursorCol]=='G'){
                configs.addAll(validHopHelper());
            }
            nextCell();
        }
        if(board[cursorRow][cursorCol]=='R'|| board[cursorRow][cursorCol]=='G'){
            configs.addAll(validHopHelper());
        }
        return configs;
    }

    @Override
    public boolean isGoal() {
        for(int r=0;r<row;r++){ for(int c=0;c<col;c++){
            if(board[r][c]=='G'){ return false; }
        }}
        return true;
    }

    @Override
    public String toString(){
        StringBuilder display = new StringBuilder();
        for(int r=0;r<row;r++){
            for(int c=0;c<col;c++){
                display.append(board[r][c]).append(" ");
            }
            if(r==row-1){break;}
            display.append('\n');
        }
        return display.toString();
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof Configuration){
            return other.hashCode()==this.hashCode();
        }
        return false;
    }

    @Override
    public int hashCode() {
        int sum = 0;
        for(int r=0;r<row;r++){
            for(int c=0;c<col;c++){
                char temp = board[r][c];
                int tmp = String.valueOf(temp).hashCode();
                sum += (tmp*(c+1));
            }
            sum = (sum * (r+1));
        }
        return sum + Arrays.deepHashCode(board);
    }
}
