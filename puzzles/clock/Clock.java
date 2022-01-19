package puzzles.clock;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.Collection;
import java.util.List;

public class Clock {
    /**
     *  Main method
     * @param args command line arguments (hours,start,end)
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java Clock hours start stop");
        }
        else {
            int hours = Integer.parseInt(args[0]);
            int start = Integer.parseInt(args[1]);
            int end = Integer.parseInt(args[2]);

            // construct the initial configuration
            Configuration init = new ClockConfig(hours,start,end);
            Solver solver = new Solver();

            // Print out starting message
            System.out.println("Hours: "+hours+" Start: "+start+" End: "+end);

            // Attempt to solve puzzle
            Collection<Configuration> solution = solver.solve(init);
            List<Configuration> last = (List<Configuration>) solution;

            // Output total number of configurations and unique configurations
            System.out.println("Total configurations: " + solver.getTotalConfigs());
            System.out.println("Unique configurations: " + solver.getUniqueConfigs());

            // Output for solution depending if it's present
            if(solution!=null){
                for(int i=0;i<solution.size();i++){
                    System.out.println("Step "+i+": "+ last.get(i));
                }
            }

            else{ System.out.println("No Solution!"); }
        }
    }
}