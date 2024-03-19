package edu.uga.acm.osp.data.display;

/**
 * Provides the context in which data fetched from the backend will be displayed on the frontend, allowing returned data
 * to be context-aware and adapt accordingly
 */
public class UiContext {
    private MeasurementSystem measurementSystem;
    private double userLatitude;
    private double userLongitude;

    private DisplayableObject displayedUnder;

    public UiContext(double userLat, double userLong, MeasurementSystem measurementSystem, DisplayableObject displayedUnder) { // add additional info regarding Ui context here as params
        this.measurementSystem = measurementSystem;
        this.userLatitude = userLat;
        this.userLongitude = userLong;
        this.displayedUnder = displayedUnder;
    }

    // Context is always required to be passed to data retrieval methods. Sometimes, context may not be necessary.
    public UiContext() {}
    
    public MeasurementSystem getMeasurementSystem() {
        return this.measurementSystem;
    }

    public double getUserLat() {
        return this.userLatitude;
    }

    public double getUserLong() {
        return this.userLongitude;
    }

    public DisplayableObject getDisplayedUnder() {
        return this.displayedUnder;
    }
}