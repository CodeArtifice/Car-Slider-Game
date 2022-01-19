package puzzles.clock;

import puzzles.common.solver.Configuration;

import java.util.ArrayList;
import java.util.Collection;

public class ClockConfig implements Configuration {
    /** number of hours on custom clock*/
    private final int hours;

    /** the hour we start on*/
    private final int start;

    /** the end number we want*/
    private final int end;

    /** clock with a specified number of hours*/
    private final ArrayList<Integer> clock;

    /** current number we're at*/
    private int current;

    /**
     * Initial Clock Configuration
     *
     * @param hours number of hours on clock
     * @param start start hour
     * @param end end hour
     */
    public ClockConfig(int hours, int start, int end){
        this.start = start;
        this.end = end;
        this.hours = hours;
        this.clock = new ArrayList<>();
        for(int num=1;num<hours+1;num++){clock.add(num);}
        this.current = start;
    }

    /**
     * Copy configuration
     *
     * @param other clock configuration which is copied
     */
    private ClockConfig(ClockConfig other){
        this.start = other.start;
        this.current = other.current;
        this.hours = other.hours;
        this.clock = other.clock;
        this.end = other.end;
    }

    /**
     * Gets the neighbors of the current hour
     *
     * @return collection of neighbors
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        // Create a list of configurations
        Collection<Configuration> configs = new ArrayList<>();

        // Create 2 new configurations
        ClockConfig config1 = new ClockConfig(this);
        ClockConfig config2 = new ClockConfig(this);

        // Get two neighbors of the current hour
        int firstNeighbor; int secondNeighbor;

        // obtain the first neighbor (on "left" side of current)
        if(current==1){ firstNeighbor = clock.get(hours-1); }
        else{ firstNeighbor = clock.get(current-2); }

        // obtain the second neighbor (on "right" side of current)
        if(current==hours){secondNeighbor = clock.get(0);}
        else{secondNeighbor = clock.get(current);}

        // Add configurations to the collection of configurations
        config1.current = firstNeighbor; configs.add(config1);
        config2.current = secondNeighbor; configs.add(config2);

        return configs;
    }

    /**
     * Is the current hour the end hour?
     *
     * @return returns true if the end hour is reached, false if not
     */
    @Override
    public boolean isGoal() { return current==end; }

    /**
     * Gives string value of the current hour
     *
     * @return string value of current hour
     */
    @Override
    public String toString(){
        return String.valueOf(current);
    }

    /**
     * creates a unique hashcode specific to the hour number
     *
     * @return integer associated to a specific hour
     */
    @Override
    public int hashCode(){ return current +hours+start+end; }

    /**
     * utilizes hashcode to compare 2 configurations
     *
     * @param other configuration comparing current one to
     * @return true or false based on if configuration is the same
     */
    @Override
    public boolean equals(Object other){
        if(other instanceof Configuration){
            return other.hashCode()==this.hashCode();
        }
        return false;
    }
}
