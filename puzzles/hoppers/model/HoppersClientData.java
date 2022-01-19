package puzzles.hoppers.model;

/**
 * Utilized to update the model
 */
public class HoppersClientData {
    /** string value of current configuration */
    private final String currentConfig;
    /** string value of output message */
    private final String message;
    /** string value of current file */
    private final String file;

    /**
     * HoppersClientData Constructor
     * @param currentConfig string value of current configuration
     * @param message string value output string
     * @param file current file which is used
     */
    public HoppersClientData(String currentConfig,String message,String file){
        this.currentConfig = currentConfig; this.message = message; this.file = file;
    }

    /**
     * HoppersClientData Constructor (No use of file with this one)
     * @param currentConfig string value of current configuration
     * @param message string value output string
     */
    public HoppersClientData(String currentConfig,String message){
        this.currentConfig = currentConfig; this.message = message; file = null;
    }

    /**
     * Gets the current file
     * @return returns file
     */
    public String getFile() {return file;}

    /**
     * Gets the current configuration
     * @return returns configuration
     */
    public String getCurrentConfig() { return currentConfig; }

    /**
     * Gets the current output message
     * @return output message
     */
    public String getMessage() { return message; }
}