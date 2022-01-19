package puzzles.hoppers.ptui;

import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersClientData;
import puzzles.hoppers.model.HoppersModel;

import java.io.IOException;
import java.util.Scanner;

/**
 * Plain Text implementation of Hoppers Puzzle
 */
public class HoppersPTUI implements Observer<HoppersModel, HoppersClientData> {
    /** Model used throughout the Plain Text UI implementation */
    private final HoppersModel model;
    /** The current file in which the model is using */
    private String file;

    /**
     * Constructor for the Plain Text UI
     * @param filename name of the file
     * @throws IOException exception thrown if file is messed up
     */
    public HoppersPTUI(String filename) throws IOException {
        this.file = filename;
        this.model = new HoppersModel(filename);
        model.addObserver(this);
    }

    /**
     * Displays the help screen along with a fresh copy of the board
     */
    private void displayHelp(){
        System.out.println("                           Help Menu:                            ");
        System.out.println("-----------------------------------------------------------------");
        System.out.println("s(elect) r c       -- select cell at row r and column c" );
        System.out.println("l(oad) filename*   -- load a new puzzle" );
        System.out.println("q(uit)             -- quit the game" );
        System.out.println("r(eset)            -- start a new game" );
        System.out.println("h(int)             -- shows next move" + "\n");
        System.out.println("filename* = file format should be: data/hoppers/hoppers-4.txt");
        System.out.println("  the number can be replaced with any number between 0 to 9  ");
        System.out.println("-----------------------------------------------------------------");
    }

    /**
     * method that runs the Plain Text UI
     * @throws IOException exception thrown if file is messed up
     */
    public void run() throws IOException {
        System.out.println(("-").repeat(34));
        System.out.println("Loaded: " + file);
        System.out.println(("-").repeat(34) + '\n');
        System.out.println(model.display(model.getCurrentConfig()));
        Scanner in = new Scanner(System.in);
        for(;;){
            System.out.println();System.out.print("Game Command: "+"\n"+"> ");
            String line = in.nextLine();
            String[] words = line.split("\s");
            System.out.print("\n");
            if(words.length>0){
                if(words[0].startsWith("q")){
                    break;
                }
                else if(words[0].startsWith("h")){
                    model.hint();
                }
                else if(words[0].startsWith("l")){
                    if(words.length==2){model.load(words[1]);}
                    else{displayHelp();}
                }
                else if(words[0].startsWith("r")){
                    model.reset(file);
                }
                else if(words[0].startsWith("s")){
                    if(words.length==3) {
                        int row = Integer.parseInt(words[1]); int col = Integer.parseInt(words[2]);
                        model.select(row, col);
                    }
                    else{displayHelp();}
                }
                else {displayHelp();}
            }
        }
    }
    
    @Override
    public void update(HoppersModel model, HoppersClientData data) {
        assert data.getMessage() != null;
            System.out.println(data.getMessage());
            if(data.getFile()!=null){this.file = data.getFile();}
            System.out.println(data.getCurrentConfig());
    }

    /**
     * Main method which Java calls
     * @param args command line arguments
     * @throws IOException exception thrown if file is messed up
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        }
        else{
            HoppersPTUI ptui = new HoppersPTUI(args[0]);
            ptui.run();
        }
    }
}
