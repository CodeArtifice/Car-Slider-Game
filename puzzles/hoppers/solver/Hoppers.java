package puzzles.hoppers.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.hoppers.model.HoppersConfig;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Puzzle Implementation to check if BFS solves correctly
 */
public class Hoppers {

    /**
     * Main method of Hoppers Solver which Java calls
     * @param args command line arguments
     * @throws IOException exception thrown if file is messed up
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java Hoppers filename");
        }
        else{
            // construct the initial configuration
            Configuration init = new HoppersConfig(args[0]);
            Solver solver = new Solver();

            // Print out starting message:
            System.out.println("File: "+args[0]);
            System.out.println(init);

            // Attempt to solve puzzle
            Collection<Configuration> solution = solver.solve(init);
            List<Configuration> last = (List<Configuration>) solution;

            // Output total number of configurations and unique configurations
            System.out.println("Total configurations: " + solver.getTotalConfigs());
            System.out.println("Unique configurations: " + solver.getUniqueConfigs());

            // Output for solution depending if it's present
            if(solution!=null){
                for(int i=0;i<solution.size();i++){
                    System.out.println("Step "+i+": ");
                    System.out.println(last.get(i) + "\n");
                }
            }
            else{ System.out.println("No Solution!"); }
        }
    }
}
