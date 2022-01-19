package puzzles.water;

import puzzles.common.solver.Configuration;

import java.util.ArrayList;
import java.util.Collection;

public class WaterConfig implements Configuration {
    /** amount of water needed */
    private final int amount;

    /** arraylist of bucket capacities */
    private final ArrayList<Integer> bucketSizes;

    /** Current array of buckets */
    private final ArrayList<Integer> current;

    /** Initial configuration of empty buckets*/
    private final ArrayList<Integer> start;

    /**
     * Initial Water Configuration
     *
     * @param amount desired amount of water
     * @param bucketSizes array list of buckets
     */
    public WaterConfig(int amount, ArrayList<Integer> bucketSizes){
        this.amount = amount;
        this.bucketSizes = bucketSizes;
        this.start = new ArrayList<>();
        for(int i = 0; i< bucketSizes.size(); i++){start.add(0);}
        this.current = start;
    }

    /**
     * Copy configuration
     *
     * @param other water configuration which is copied
     */
    private WaterConfig(WaterConfig other){
        this.bucketSizes = other.bucketSizes;
        this.amount = other.amount;
        this.start = other.start;
        this.current = new ArrayList<>(other.current);
    }

    /**
     * Gets the neighbors of the configuration
     *
     * @return collection of neighbors
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        Collection<Configuration> configs = new ArrayList<>();
        for(int i = 0; i< bucketSizes.size(); i++){

            // Dump bucket config
            WaterConfig configDump = new WaterConfig(this);
            configDump.current.set(i,0); configs.add(configDump);

            // Fill bucket config
            WaterConfig configFill = new WaterConfig(this);
            configFill.current.set(i, bucketSizes.get(i)); configs.add(configFill);

            // For buckets not the one above fill other buckets up
            for(int j = 0; j< bucketSizes.size(); j++){
                if(i!=j){
                    WaterConfig configPour = new WaterConfig(this);

                    // amount being poured in
                    int poured = current.get(j) + current.get(i);

                    // if amount poured can fit inside bucket
                    if(poured <= bucketSizes.get(j)){
                        configPour.current.set(j,poured);
                        configPour.current.set(i,0);
                    }

                    // if amount poured exceeds max capacity
                    else {
                        configPour.current.set(j, bucketSizes.get(j));
                        configPour.current.set(i, poured - bucketSizes.get(j));
                    }

                    // add the configuration to the list of configs
                    configs.add(configPour);
                }
            }
        }
        return configs;
    }

    /**
     * Does current configuration contain desired amount?
     *
     * @return returns true if yes, false if no to the above statement.
     */
    @Override
    public boolean isGoal() {
        for (Integer integer : current) {
            if (integer == amount) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gives string value of the current configuration
     *
     * @return string value of current configuration
     */
    @Override
    public String toString() {
        return String.valueOf(current);
    }

    /**
     * creates a unique hashcode specific to the current configuration
     *
     * @return integer associated to a configuration
     */
    @Override
    public int hashCode() {
        int sum = 0;
        for(int i =0; i< current.size();i++){
            sum = (current.get(i) * (i+1)) + sum;
        }
        return current.hashCode() + sum;
    }

    /**
     * utilizes hashcode to compare 2 configurations
     *
     * @param other configuration comparing current one to
     * @return true or false based on if configuration is the same
     */
    @Override
    public boolean equals(Object other) {
        if(other instanceof Configuration){
            return other.hashCode()==this.hashCode();
        }
        return false;
    }
}
