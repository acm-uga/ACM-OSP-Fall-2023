package baseClasses;

import busAppCore.BackendEngine;
import dataSources.DatabaseService;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Represents a bus stop: a location that buses stop at while operating on a route
 * <p></p>
 * Contains the location of the stop, its name, relevant metadata, and data used in UI components on the frontend
 *
 * @see Route
 */
public class Stop {
    /**
     * The primary "type" of location a {@code Stop} may serve, such as housing or parking
     */
    public enum StopType {UNKNOWN}

    // Fields derived from the database at instantiation:
    private long stopId;
    private String name;
    private StopType type;
    private double latitude;
    private double longitude;
    //List of route ids that this stop is connected to
    private long[] servesRouteIds;

    //Constructors:
    // From DB calls
    public Stop(long stopId, String name, StopType type, double latitude, double longitude, long[] servesRouteIds) {
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
    public String toString() {
        return ("Stop{" +
                "stopId=" + stopId +
                ", name=" + name +
                ", type=" + stringFromType(type) +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", servesRoutesIds=" + Arrays.toString(servesRouteIds) +
                '}'
        );
    }

    // Miscellaneous methods
    /**
     * Matches a {@code String} representation of {@code Stop.StopType} to a valid {@code Stop.Type}
     *
     * @param typeString a {@code String} representing a valid {@code Stop.Type}
     *
     * @return the {@code Stop.Type} corresponding to the passed {@code String}
     *
     * @see #type
     */
    public static Stop.StopType typeFromString(String typeString) {
        switch (typeString) {
            default:
                return Stop.StopType.UNKNOWN;
        }
    }

    /**
     * Matches a {@code StopType} to a {@code String} representing it
     *
     * @param stopType the {@code StopType} to textually represent
     *
     * @return <b>If {@code stopType} is a valid {@code StopType}:</b><br>
     * A textual representation fo {@code stopType}<br>
     * <b>Else:</b> Unknown
     *
     * @see StopType
     */
    public static String stringFromType(StopType stopType) {
        switch(stopType) {
            default:
                return "Unknown";
        }
    }

    /**
     * Determines the seconds until the nearest {@code Bus} arrives at {@code this} {@code Stop} along the {@code Route}
     * with ID {@code routeId}
     *
     * @param routeId the ID of the desired {@code Route} with which to find buses along
     *
     * @return the seconds until the nearest {@code Bus} on the {@code Route} with ID {@code routeID} arrives at
     * {@code this} {@code Stop}
     */
    public double secondsToArrivalOnRoute(long routeId) {
        Route route = DatabaseService.getRoute(routeId);
        double secondsTillArrival = Double.NaN;

        if (route != null) {
            secondsTillArrival = route.getActiveBuses().get(stopId)[0].secondsTillArrivalAt(stopId);
        }

        return secondsTillArrival;
    }

    /**
     * Finds the next {@code Bus} arriving at {@code this} {@code Stop}, regardless of what {@code Route} it's on
     *
     * @return the next bus arriving at {@code this} {@code Stop}
     */
    public Bus nextArrivingBus() {
        Bus lookingAtBus, nextBus = null;
        double leastSecondsTillArrival = Double.MAX_VALUE;

        for (Route route : BackendEngine.getActiveRoutes()) {
            lookingAtBus = route.getActiveBuses().get(stopId)[0];
            if (lookingAtBus.secondsTillArrivalAt(stopId) < leastSecondsTillArrival) {
                nextBus = lookingAtBus;
                leastSecondsTillArrival = nextBus.secondsTillArrivalAt(stopId);
            }
        }
        return nextBus;
    }
}