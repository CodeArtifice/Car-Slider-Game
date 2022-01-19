package puzzles.jam.model;

import java.util.Objects;

/**
 * A class representing one of the cars in the Traffic Jam puzzle
 * */
public class JamCar {
    private String name;
    private int startRow;
    private int startCol;
    private int endRow;
    private int endCol;
    private boolean isHorizontal;

    /**
     * Creates a JamCar object. Takes in parameters for the name, starting coordinates, and end coordinates. Calculates
     * whether the car is horizontal or vertical based on the start and end rows.
     *
     * @param name The letter that will be displayed on the car
     * @param startRow The row of the starting coordinate
     * @param startCol The column of the starting coordinate
     * @param endRow The row of the ending coordinate
     * @param endCol The column of the ending coordinate
     * */
    public JamCar(String name, int startRow, int startCol, int endRow, int endCol) {
        this.name = name;
        this.startRow = startRow;
        this.startCol = startCol;
        this.endRow = endRow;
        this.endCol = endCol;
        this.isHorizontal = startRow == endRow;
    }

    /**
     * Copy constructor for JamCar.
     * */
    public JamCar(JamCar jamCar) {
        this.name = jamCar.name;
        this.startRow = jamCar.startRow;
        this.startCol = jamCar.startCol;
        this.endRow = jamCar.endRow;
        this.endCol = jamCar.endCol;
        this.isHorizontal = jamCar.isHorizontal;
    }

    /**
     * Returns a copy of this piece but moved up or down by the amount specified
     * @rit.pre ONLY to be called on vertical pieces
     * @param amount The amount to move the JamCar by. Positive if down, negative if up.
     * @return Copy of this JamCar but moved up or down by the amount specified
     * */
    public JamCar moveVertically(int amount) {
        JamCar output = new JamCar(this);
        output.startRow += amount;
        output.endRow += amount;
        return output;
    }

    /**
     * Returns a copy of this piece but moved left or right by the amount specified
     * @rit.pre ONLY to be called on horizontal pieces
     * @param amount The amount to move the JamCar by. Positive if right, negative if left.
     * @return Copy of this JamCar but moved left or right by the amount specified
     * */
    public JamCar moveHorizontally(int amount) {
        JamCar output = new JamCar(this);
        output.startCol += amount;
        output.endCol += amount;
        return output;
    }

    /**
     * Getter function for the isHorizontal attribute
     * @return The isHorizontal attribute
     * */
    public boolean getIsHorizontal() {
        return isHorizontal;
    }

    /**
     * Getter function for the name attribute
     * @return the name attribute
     * */
    public String getName() {
        return name;
    }

    /**
     * Getter function for the startRow attribute
     * @return the startRow attribute
     * */
    public int getStartRow() {
        return startRow;
    }

    /**
     * Getter function for the startCol attribute
     * @return the startCol attribute
     * */
    public int getStartCol() {
        return startCol;
    }

    /**
     * Getter function for the endRow attribute
     * @return the endRow attribute
     * */
    public int getEndRow() {
        return endRow;
    }

    /**
     * Getter function for the endCol attribute
     * @return the endCol attribute
     * */
    public int getEndCol() {
        return endCol;
    }

    /**
     * Checks whether two JamCars are equal
     * @return true if they are equal, false otherwise
     * */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JamCar jamCar = (JamCar) o;
        return startRow == jamCar.startRow && startCol == jamCar.startCol && endRow == jamCar.endRow && endCol == jamCar.endCol && isHorizontal == jamCar.isHorizontal && name.equals(jamCar.name);
    }

    /**
     * Returns a hashcode of the JamCar
     * @return a hashcode of the JamCar
     * */
    @Override
    public int hashCode() {
        return Objects.hash(name, startRow, startCol, endRow, endCol, isHorizontal);
    }
}
