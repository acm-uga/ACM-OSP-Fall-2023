package ospbusapp;

import dataDisplay.ListItemData;

/**
 * Represents a bus stop: location that Buses stop at while operating on a Route.
 * <p></p>
 * Contains the location of the stop, its name, relevant metadata, and data used in UI components on the frontend
 */
public class Stop implements ListItemData {
    protected enum Type {UNKNOWN} // The primary "purpose"/type of location a stop might serve

    // Fields derived from the database at instantiation:
    private long stopId;
    private String name;
    private Type type;
    private double latitude;
    private double longitude;
    //List of route ids that this stop is connected to
    private long[] servesRouteIds;

    //Constructors:
    // From DB calls
    public Stop(long stopId, String name, Type type, double latitude, double longitude, long[] servesRouteIds) {
        this.stopId = stopId;
        this.name = name;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.servesRouteIds = servesRouteIds;
    }

    //Methods:
    public long getStopId() {
        return stopId;
    }

    public void setStopId(int stopId) {
        this.stopId = stopId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long[] getServesRoutesIds() {
        return servesRouteIds;
    }

    public void setServesRoutesIds(long[] servesRoutesIds) {
        this.servesRouteIds = servesRoutesIds;
    }

    @Override
    public boolean equals(Object stop) {
        //Check if same object
        if (this == stop) {
            return true;
        }

        //Verify object isn't null and that object is same class
        if (stop == null || getClass() != stop.getClass()) {
            return false;
        }

        long stopId = ((Stop) stop).stopId;

        return this.stopId == stopId;
    }

    @Override
    public String toString() {
        return ("Stop{" +
                "stopId=" + stopId +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", servesRoutesIds=" + servesRouteIds +
                '}'
        );
    }

    // Miscellaneous methods
    /**
     * Matches a {@code String} representation of {@code Stop.Type} to a valid {@code Stop.Type}
     *
     * @param typeString a {@code String} representing a valid {@code Stop.Type}
     *
     * @return the {@code Stop.Type} corresponding to the passed {@code String}
     *
     * @see #type
     */
    public static Stop.Type typeFromString(String typeString) {
        switch (typeString) {
            default:
                return Stop.Type.UNKNOWN;
        }
    }

    // BasicUiDisplayable Implementations:
    /**
     * Provides the name of the invoking Stop
     *
     * @return the name of {@code this} {@code Stop}
     */
    @Override
    public String listItemHeader() {
        return this.name;
    }

    /**
     * Provides an overview of the Routes served by this {@code Stop}
     *
     * @return the abbreviated names of all the Routes {@code this} {@code Stop} serves
     */
    @Override
    public String listItemSubHeader() {
        String serves = "Serves ";

        int itemNumber = 1;
        for (Route route : BackendEngine.activeRoutes()) {
            serves += route.getAbbName();
            if (itemNumber < BackendEngine.activeRoutes().length) serves += ", ";
        }

        return serves;
    }

    // TODO change this to return time to walk to this stop (#m walk) (3mph * distance to stop based on user's location)
    /**
     * Provides the current, primary schedule of the invoking Stop
     *
     * @return the name of {@code this} {@code Route}
     */
    @Override
    public String listItemContext1() {
        return this.schedule.mainSchedule();
    }

    // TODO change this to return time until next bus arrives (regardless of Route): (Arriving in #m)
    /**
     * Provides the current, alternate schedule of the invoking Stop
     *
     * @return a {@code String} suitable for use as secondary context
     */
    @Override
    public String listItemContext2() {
        return this.schedule.altSchedule();
    }
}