package dataDisplay;

/**
 * Provides the context in which data fetched from the backend will be displayed on the frontend.
 */
public class UiContext {
    private double userLatitude;
    private double userLongitude;

    public UiContext(double userLat, double userLong) { // add additional info regarding Ui context here as params
        this.userLatitude = userLat;
        this.userLongitude = userLong;
    }

    // Context is always required to be passed to data retrieval methods. Sometimes, context may not be necessary.
    public UiContext() {

    }

    public double getUserLat() {
        return this.userLatitude;
    }

    public double getUserLong() {
        return this.userLongitude;
    }
}