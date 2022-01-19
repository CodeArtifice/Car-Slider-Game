package puzzles.jam.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.jam.model.JamConfig;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class Jam {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Jam filename");
            System.exit(-1);
        } else {
            Solver solver = new Solver();
            // Tries to construct the initial config. Exits if there is an invalid file
            JamConfig startConfig = null;
            try {
                startConfig = new JamConfig(args[0]);
            } catch (IOException e) {
                System.out.println("Invalid file. Please try again");
                System.exit(-1);
            }

            // Print out starting message
            System.out.println("File: " + args[0]);
            System.out.println(startConfig.toString());

            // Attempt to solve puzzle
            List<Configuration> solution = (List<Configuration>) solver.solve(startConfig);

            // Output the total number of configurations and unique configuraitons
            System.out.println("Total configurations: " + solver.getTotalConfigs());
            System.out.println("Unique configurations: " + solver.getUniqueConfigs());

            // Output for solution if it's present and "No solution" otherwise
            if (solution != null) {
                for (int i = 0; i < solution.size(); ++i) {
                    System.out.println("Step " + i + ": \n" + solution.get(i).toString());
                }
            }
            else { System.out.println("No Solution!"); }

        }
    }
}