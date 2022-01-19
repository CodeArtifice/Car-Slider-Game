package puzzles.hoppers.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Model used in PTUI and GUI
 */
public class HoppersModel {
    /** the collection of observers of this model */
    private final List<Observer<HoppersModel, HoppersClientData>> observers = new LinkedList<>();

    /** the current configuration */
    private HoppersConfig currentConfig;

    /** Space currently selected */
    private int currentRow;
    private int currentCol;

    /**
     * Model Constructor
     * @param filename name of current file in use
     * @throws IOException exception thrown if file is messed up
     */
    public HoppersModel(String filename) throws IOException {
        this.currentConfig = new HoppersConfig(filename);
        currentCol = -1; currentRow = -1;
    }

    /**
     * Gets the current configuration
     * @return returns the current configuration
     */
    public HoppersConfig getCurrentConfig() {return currentConfig;}

    /**
     * updated toString method that adds column and row numbers
     * @param config the current configuration which needs to be displayed
     * @return string version of the current configuration
     */
    public String display(HoppersConfig config){
        StringBuilder string = new StringBuilder();
        string.append("    ");
        for(int c=0; c< config.getCol();c++){
            string.append(c).append(" ");
        }
        string.append('\n');
        string.append("    ").append(String.valueOf('-').repeat(Math.max(0, config.getCol())*2-1)).append('\n');
        for(int r=0;r<config.getRow();r++){
            if(r<10){string.append(" ");}
            string.append(r).append("|").append(" ");
            for(int c=0;c<config.getCol();c++){
                string.append(config.getBoard()[r][c]).append(" ");
            }
            if(r==config.getRow()-1){break;}
            string.append('\n');
        }
        return String.valueOf(string);
    }

    /**
     * If the current configuration is solvable the hint method
     * takes the solution path and makes the current configuration
     * the next configuration within the solution path
     */
    public void hint(){
        Solver solver = new Solver();
        Collection<Configuration> solution = solver.solve(currentConfig);
        List<Configuration> last = (List<Configuration>) solution;
        if(currentConfig.isGoal()){
            alertObservers(new HoppersClientData(display(currentConfig),"Solution has been reached! Reset the board!" +"\n"));
        }
        else if(solution!=null){
            currentConfig = (HoppersConfig) last.get(1);
            alertObservers(new HoppersClientData(display(currentConfig),"Here's a Hint:" +"\n"));
        }
        else{
            alertObservers(new HoppersClientData(display(currentConfig),"No Solution from here, reset!"+"\n"));
        }
    }

    /**
     * Loads up a new puzzle if the file is correct
     * @param filename name of the new file
     * @throws IOException exception thrown if file is messed up
     */
    public void load(String filename) throws IOException {
        File file = new File(filename);
        if(file.exists()){
            currentConfig = new HoppersConfig(filename);
            alertObservers(new HoppersClientData(display(currentConfig),"Loaded: " + filename + '\n',filename));
        }
        else{
            alertObservers(new HoppersClientData(display(currentConfig),filename +" is an Invalid File"+ '\n'));
        }
    }

    /**
     * Resets the current configuration to a completely new one
     * @param filename name of the file
     * @throws IOException exception thrown if file is messed up
     */
    public void reset(String filename) throws IOException {
        this.currentConfig = new HoppersConfig(filename);
        alertObservers(new HoppersClientData(display(currentConfig),"Game has been reset!"+"\n"));
    }

    /**
     * Method which a user selects a frog and chooses where to move it
     * @param r initial position of the frog with respect to the row
     * @param c initial position of the frog with respect to the column
     */
    public void select(int r, int c){
        if(currentRow<0 && currentCol<0){if (r >= 0 && r < currentConfig.getRow()){if (c >= 0 && c < currentConfig.getCol()){
                if (currentConfig.getBoard()[r][c] == 'G' || currentConfig.getBoard()[r][c] == 'R') {
                    currentRow = r; currentCol = c;
                    alertObservers(new HoppersClientData(display(currentConfig), "Selected " + "(" + r + "," + c + ")" + '\n'));
                } else {
                    alertObservers(new HoppersClientData(display(currentConfig), "Invalid selection " + "(" + r + "," + c + ")" + '\n'));
                }
        }}}
        else{
            String temp = secondSelect(currentRow,currentCol,r,c);
            alertObservers(new HoppersClientData(display(currentConfig),temp));
            currentRow = -1; currentCol = -1;
        }
    }

    /**
     * Helper method which helps select method
     * @param r initial position of the frog with respect to the row
     * @param c initial position of the frog with respect to the column
     * @param r2 final position of the frog with respect to the row if move is valid
     * @param c2 final position of the frog with respect to the column if move is valid
     * @return string output on whether or not the jump was successful or not
     */
    public String secondSelect(int r, int c, int r2, int c2){
        // check if the destination coordinates are within bounds and is free space
        if(r2>=0&&r2<currentConfig.getRow()){if(c2>=0&&c2<currentConfig.getCol()){
        if(currentConfig.getBoard()[r2][c2] == '.'){if(validMove(r,c,r2,c2)){
            char temp = currentConfig.getBoard()[r][c];
            currentConfig.getBoard()[r][c] = '.';
            currentConfig.getBoard()[r2][c2] = temp;
            return "Hopped from "+"("+r+","+c+")"+" to "+"("+r2+","+c2+")";
        }}}}
        return "Can't jump from "+"("+r+","+c+")"+" to "+"("+r2+","+c2+")";
    }

    /**
     * Helper method which helps secondSelect method
     * @param r initial position of the frog with respect to the row
     * @param c initial position of the frog with respect to the column
     * @param r2 final position of the frog with respect to the row if move is valid
     * @param c2 final position of the frog with respect to the column if move is valid
     * @return boolean stating whether or not the move is legal or not
     */
    private boolean validMove(int r, int c, int r2, int c2){
        int r_temp = r2 - r;
        int c_temp = c2 - c;
        if(r_temp<0){
            // up and left
            if(c_temp<0){
                if(currentConfig.getBoard()[r-1][c-1]=='G'&&r-r2==2&&c-c2==2){
                    currentConfig.getBoard()[r-1][c-1]= '.';
                    return true;
                }
                return false;
            }
            // straight up
            else if(r%2==0&&c_temp==0){
                if(currentConfig.getBoard()[r-2][c]=='G'&&r-r2==4){
                    currentConfig.getBoard()[r-2][c]= '.';
                    return true;
                }
                return false;
            }
            // up and right
            else{
                if(currentConfig.getBoard()[r-1][c+1]=='G'&&r-r2==2&&c2-c==2){
                    currentConfig.getBoard()[r-1][c+1]='.';
                    return true;
                }
                return false;
            }
        }
        else if(r_temp>0){
            // down and left
            if(c_temp<0){
                if(currentConfig.getBoard()[r+1][c-1]=='G'&&r2-r==2&&c-c2==2){
                    currentConfig.getBoard()[r+1][c-1]= '.';
                    return true;
                }
                return false;
            }
            // straight down
            else if(r%2==0&&c_temp==0){
                if(currentConfig.getBoard()[r+2][c]=='G'&&r2-r==4){
                    currentConfig.getBoard()[r+2][c]= '.';
                    return true;
                }
                return false;
            }
            // down and right
            else{
                if(currentConfig.getBoard()[r+1][c+1]=='G'&&r2-r==2&&c2-c==2){
                    currentConfig.getBoard()[r+1][c+1]='.';
                    return true;
                }
                return false;
            }
        }
        else{
            // straight left
            if(r%2==0&&c_temp<0){if(currentConfig.getBoard()[r][c-2]=='G'&&c-c2==4){
                    currentConfig.getBoard()[r][c-2]='.';
                    return true;
            }}
            // straight right
            else{if(r%2==0){if(currentConfig.getBoard()[r][c+2]=='G'&&c2-c==4){
                    currentConfig.getBoard()[r][c+2]='.';
                    return true;
            }}}
            return false;
        }
    }

    /**
     * The view calls this to add itself as an observer.
     * @param observer the view
     */
    public void addObserver(Observer<HoppersModel, HoppersClientData> observer) {this.observers.add(observer);}

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(HoppersClientData data){for(var observer : observers){observer.update(this,data);}}
}
