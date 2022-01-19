package puzzles.water;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Water {
    /**
     *  Main method
     * @param args command line arguments (amount,bucket(s))
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(("Usage: java Water amount bucket1 bucket2 ..."));
        }
        else {
            ArrayList<Integer> buckets = new ArrayList<>();
            for (String arg : args) {
                int tmp = Integer.parseInt(arg);
                buckets.add(tmp);
            }
            int amount = buckets.get(0); buckets.remove(0);

            // construct the initial configuration
            Configuration init = new WaterConfig(amount,buckets);
            Solver solver = new Solver();

            // Print out starting message
            System.out.println("Amount: "+amount+", Buckets: "+buckets);

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

