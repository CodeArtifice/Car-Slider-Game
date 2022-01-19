package puzzles.common.solver;

import java.util.*;

public class Solver {
    /** total number of configs generated */
    private int totalConfigs;

    /** Map of configurations */
    private HashMap<Configuration, Configuration> predecessorMap;

    public Solver(){
        this.totalConfigs = 0;
        this.predecessorMap = new HashMap<>();
    }

    /**
     * gets total number of configurations
     * @return returns total number of configs
     */
    public int getTotalConfigs() {return this.totalConfigs;}

    /**
     * gets number of unique configurations
     * @return returns total number of unique configs
     */
    public int getUniqueConfigs(){return this.predecessorMap.size();}

    /**
     * Attempts to solve the puzzle
     *
     * @param start starting configuration
     * @return A solution configuration, or null if no solution
     */
    public Collection<Configuration> solve(Configuration start){
        //Creates Hashmap of Configurations
        HashMap<Configuration, Configuration> predecessorMap = new HashMap<>();
        this.predecessorMap = predecessorMap;

        // starts with map putting start config inside
        predecessorMap.put(start,null); totalConfigs++;
        Queue<Configuration> queue = new LinkedList<>();
        queue.add(start);
        Configuration solution = start;

        // loops through until end is found or until queue is empty
        while(!queue.isEmpty()){
            Configuration current = queue.remove();
            if(!current.isGoal()){
                current.getNeighbors().forEach(neighbor ->{
                    totalConfigs++;
                    if(!predecessorMap.containsKey(neighbor)){
                        queue.add(neighbor);
                        predecessorMap.put(neighbor,current);
                    }
                });
            }
            else{
                solution = current;
                break;
            }
        }
        if(queue.isEmpty()){
            if(solution.isGoal()){
                Collection<Configuration> path = new LinkedList<>();
                HashMap<Integer, Configuration> tmp = new HashMap<>();
                int num = 1;
                Configuration config = predecessorMap.get(solution);
                while(config!=null){
                    tmp.put(num,config);
                    config = predecessorMap.get(config);
                    num++;
                }
                for(int i=num-1;i>0;i--){
                    path.add(tmp.get(i));
                }
                path.add(solution);

                return path;
            }
            return null;
        }
        else{
            Collection<Configuration> path = new LinkedList<>();
            HashMap<Integer, Configuration> tmp = new HashMap<>();
            int num = 1;
            Configuration config = predecessorMap.get(solution);
            while(config!=null){
                tmp.put(num,config);
                config = predecessorMap.get(config);
                num++;
            }
            for(int i=num-1;i>0;i--){
                path.add(tmp.get(i));
            }
            path.add(solution);

            return path;
        }
    }
}
