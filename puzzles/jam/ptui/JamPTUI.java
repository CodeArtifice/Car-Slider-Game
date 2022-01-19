package puzzles.jam.ptui;

import puzzles.common.Observer;
import puzzles.common.solver.Solver;
import puzzles.jam.model.JamClientData;
import puzzles.jam.model.JamConfig;
import puzzles.jam.model.JamModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class JamPTUI implements Observer<JamModel, JamClientData> {
    private JamModel model;
    private String currentFile;

    /**
     * Constructor for the JamPTUI
     * @param jamModel The model for the PTUI
     * */
    public JamPTUI(JamModel jamModel, String currentFile) {
        this.model = jamModel;
        this.model.addObserver(this);
        this.currentFile = currentFile;
    }

    /**
     * Displays a help message with the available commands
     * */
    public void help() {
        System.out.println("""
                h(int)              -- hint next move
                l(oad) filename     -- load new puzzle file
                s(elect) r c        -- select cell at r, c
                q(uit)              -- quit the game
                r(eset)             -- reset the current game""");
    }

    @Override
    public void update(JamModel jamModel, JamClientData jamClientData) {
        System.out.println(jamClientData.getBoardString());
        if (jamClientData.getIndicator() != null) {
            System.out.println(jamClientData.getIndicator());
        }
        if (jamClientData.getCurrentFile() != null) {
            this.currentFile = jamClientData.getCurrentFile();
        }
    }
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java JamPTUI filename");
        }
        // Starts up a ptui
        JamPTUI ptui = null;
        try {
            JamConfig config = new JamConfig(args[0]);
            System.out.println(config.toString());
            ptui = new JamPTUI(new JamModel(config), args[0]);
        } catch (IOException e) {
            System.out.println("Invalid file. Please try again");
            System.exit(-1);
        }
        ptui.help();

        int startCol = -1;
        int startRow = -1;

        Scanner in = new Scanner(System.in);
        String userInput = in.nextLine();
        // Main loop of the program
        while (!userInput.equals("q")) {
            String[] splitLine = userInput.split(" ");
            switch (splitLine[0]) {
                case "h":
                    ptui.model.hint();
                    break;
                case "s":
                    // Either selects a cell to be moved or selects an empty space to move it to.
                    if (splitLine.length == 3) {
                        ptui.model.select(Integer.parseInt(splitLine[1]), Integer.parseInt(splitLine[2]));
                    } else {
                        System.out.println("Incorrect number of arguments for command 's'");
                    }
                    break;
                case "r":
                    // Resets the board. The error is ignored because the current file should already be error-checked.
                    ptui.model.reset(ptui.currentFile);
                    break;
                case "l":
                    // Loads a new file
                    if (splitLine.length == 2) {
                            ptui.model.load(splitLine[1]);
                    } else {
                        System.out.println("Incorrect number of arguments for command 'l'");
                    }
                    break;
                case "q":
                    break;
                default:
                    System.out.println("Invalid command");
                    ptui.help();
            }
            userInput = in.nextLine();
        }

    }
}
