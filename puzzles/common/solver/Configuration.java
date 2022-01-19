package puzzles.common.solver;

import java.util.Collection;

/**
 * representation of a single configuration
 */
public interface Configuration {
    /**
     * Gets a collection of the successors from current one.
     *
     * @return all successors
     */
    Collection<Configuration> getNeighbors();

    /**
     * Has the goal been reached?
     * @return true if goal is reached, false is not
     */
    boolean isGoal();
}
