package edu.uga.acm.osp.data.baseClasses;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import edu.uga.acm.osp.data.display.UiContext;
import edu.uga.acm.osp.data.display.ParentContainer;
import edu.uga.acm.osp.data.sources.SpringBootService;
import edu.uga.acm.osp.data.display.DisplayableObject;
import edu.uga.acm.osp.data.display.ListItemData;
import edu.uga.acm.osp.data.display.MeasurementSystem;

/**
 * Represents a bus stop: a location that buses stop at while operating on a route
 * <p></p>
 * Contains the location of the stop, its name, relevant metadata, and data used in UI components on the frontend
 *
 * @see Route
 */
public class Stop implements DisplayableObject, ListItemData {
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

    public StopType getType() {
        return type;
    }

    public void setType(StopType type) {
        this.type = type;
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
    public static StopType typeFromString(String typeString) {
        switch (typeString) {
            default:
                return StopType.UNKNOWN;
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
     * Calculates the approximate distance to {@code this} {@code Stop} using a perfect sphere model from the provided
     * coordinates
     *
     * @param latitude the latitude with which to calculate vertical distance
     * @param longitude the longitude with which to calculate horizontal distance
     * @param measurementSystem the measurement system, {@code IMPERIAL} or {@code METRIC}, determining whether the
     * response is in miles or kilometers
     *
     * @return the approximate distance from the provided coordinates to {@code this} {@code Stop}, measured in miles or
     * kilometers
     */
    public double distanceToStop(double latitude, double longitude, MeasurementSystem measurementSystem) {
        int earthRadius = (measurementSystem == MeasurementSystem.IMPERIAL) ? 3959 : 6371;

        latitude = Math.toRadians(latitude);
        longitude = Math.toRadians(longitude);
        double stopLat = Math.toRadians(this.latitude);
        double stopLong = Math.toRadians(this.longitude);
        return Math.acos(Math.sin(latitude)*Math.sin(stopLat)+Math.cos(latitude)*Math.cos(stopLat)*Math.cos(stopLong-longitude)) * earthRadius;
    }

    /**
     * Calculates the time required to walk a certain {@code distance}, measured in {@code units} units, in
     * {@code timeUnits} units
     * <br><br>
     * The average walking speed is considered to be 3.2mph (5.1kph).
     *
     * @param distance the distance to calculate approximate walk time with
     * @param units the units {@code distance} is in ({@code MeasurementSystem.IMPERIAL} is miles,
     * {@code MeasurementSystem.METRIC} is kilometers)
     * @param timeUnits the units of time to calculate walk time in
     *
     * @return the time required to walk {@code distance} {@code units}, measured in {@code timeUnits}
     */
    public static int walkTimeOf(double distance, MeasurementSystem units, TimeUnit timeUnits) {
        // Mph for imperial, kph for metric
        final double AVG_WALK_SPEED = (units == MeasurementSystem.IMPERIAL) ? 3.2 : 5.1;

        // Adjust AVG_WALK_SPEED to match the specified timeUnits
        double adjWalkSpeed = AVG_WALK_SPEED / timeUnits.convert(1, TimeUnit.HOURS);
        return (int)Math.round(distance / adjWalkSpeed);
    }

    /**
     * Condenses a {@code timeQuantity} in seconds to a {@code String} representing the same duration but using the
     * most appropriate time unit, indicated by 's' for seconds, 'm' for minutes, and 'h' for hours
     * <br><br>
     * Resulting durations are approximate due to rounding "half-up."
     *
     * @param timeQuantity the number of seconds to textually represent in the most appropriate unit
     *
     * @return a {@code String} representing the {@code timeQuantity} in the most appropriate units
     *
     * @see <a href="https://en.wikipedia.org/wiki/Rounding#Rounding_half_up">Wikipedia</a>
     */
    public static String condenseSecsToString(double timeQuantity) {
        String finalString = "Unknown";
        if (!Double.isNaN(timeQuantity)) {
            finalString = "<1m";
            // If the number of seconds exceeds one minute...
            if (timeQuantity >= 60.0) {
                // Round minutes (<30s round down, >=30s round up)
                if (timeQuantity % 60.0 < 30.0) {
                    timeQuantity = (timeQuantity - (timeQuantity % 60.0)) / 60.0;
                } else {
                    timeQuantity = ((timeQuantity - (timeQuantity % 60.0)) / 60.0) + 1;
                }
                finalString = String.format("%.0fm", timeQuantity);

                // If the number of minutes exceeds an hour...
                if (timeQuantity >= 60.0) {
                    // Round minutes (<30m round down, >=30m round up)
                    if (timeQuantity % 60.0 < 30.0) {
                        timeQuantity = (timeQuantity - (timeQuantity % 60.0)) / 60.0;
                    } else {
                        timeQuantity = ((timeQuantity - (timeQuantity % 60.0)) / 60.0) + 1;
                    }
                    finalString = String.format("%.0fh", timeQuantity);
                }
            }
        }
        return finalString;
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
        Route route = SpringBootService.getRoute(routeId);
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

        for (Route route : SpringBootService.getActiveRoutes()) {
            lookingAtBus = route.getActiveBuses().get(stopId)[0];
            if (lookingAtBus.secondsTillArrivalAt(stopId) < leastSecondsTillArrival) {
                nextBus = lookingAtBus;
                leastSecondsTillArrival = nextBus.secondsTillArrivalAt(stopId);
            }
        }
        return nextBus;
    }

    // ListItemData Implementations:
    /**
     * Provides the name of the invoking Stop
     *
     * @param ctx the context in which this data is being displayed in the UI
     *
     * @return the name of {@code this} {@code Stop}
     */
    public String listItemHeader(UiContext ctx) {
        return name;
    }

    /**
     * Provides an overview of the Routes served by this {@code Stop} in a general context or the time until the next
     * bus along the context's Route arrives
     *
     * @param ctx the context in which this data is being displayed in the UI
     *
     * @return <u><b>If the context of this subheader is General...</b></u><br>
     * <b>If the number of currently active {@code Route}s is >= 1:</b>
     * <p>The abbreviated names of all the currently active Routes {@code this} {@code Stop} serves</p>
     * <b>Else:</b> The abbreviated names of all the {@code Route}s {@code this} {@code Stop} serves<br><br>
     * <u><b>If the context of this subheader is beneath a {@code Route}...</b></u><br>
     * <b>If the seconds till the next {@code Bus} arrives is > 0:</b>
     * <p>The seconds until that {@code Bus} arrives</p>
     * <b>Else:</b> "Arrived""<br><br>
     * <u><b>Else:</b></u> "Unknown"
     *
     * @see SpringBootService#getActiveRoutes()
     */
    public String listItemSubHeader(UiContext ctx) {
        String finalString = "Unknown";

        if (ctx.getDisplayedUnder() == ParentContainer.GENERAL) {
            finalString = "Serves ";
            Route[] activeRoutes = SpringBootService.getActiveRoutes();

            if (activeRoutes.length >= 1) {
                int itemNumber = 1;
                for (Route route : activeRoutes) {
                    finalString += route.getAbbName();
                    if (itemNumber <
                            activeRoutes.length) finalString += ", ";
                }
            } else {
                Route[] allRoutes = SpringBootService.getAllRoutes();
                int itemNumber = 1;
                for (Route route : allRoutes) {
                    finalString += route.getAbbName();
                    if (itemNumber < allRoutes.length) finalString += ", ";
                }
            }
        } else if (ctx.getDisplayedUnder() instanceof Route) {
            // Determine the closest Bus' STA
            double secondsTillNextArrival = secondsToArrivalOnRoute(((Route) ctx.getDisplayedUnder()).getRouteId());

            // Determine whether the Bus has already arrived
            if (secondsTillNextArrival > 0) {
                finalString = String.format("Next arrival in %.0f", secondsTillNextArrival);
            } else if (secondsTillNextArrival <= 0) {
                finalString = "Bus at stop";
            }
        }

        return finalString;
    }

    /**
     * Provides the time to walk from the user's location to {@code this} {@code Stop}
     *
     * @param ctx the context in which this data is being displayed in the UI
     *
     * @return the walking time from the user's location to {@code this} {@code Stop}
     *
     * @see #distanceToStop
     */
    public String listItemContext1(UiContext ctx) {
        double distanceToStop = this.distanceToStop(ctx.getUserLat(), ctx.getUserLat(), ctx.getMeasurementSystem());
        double walkTimeToStop = walkTimeOf(distanceToStop, ctx.getMeasurementSystem(), TimeUnit.SECONDS);
        return condenseSecsToString(walkTimeToStop) + " walk";
    }

    /**
     * Provides the seconds until the next {@code Bus} arrives (regardless of {@code Route}) in a general context or the
     * distance to {@code this} {@code Stop}'s location from the user's current location in the user's preferred units
     *
     * @param ctx the context in which this data is being displayed in the UI
     *
     * @return <b>If the context of this context info is General:</b><br>
     * <p>The time until the next {@code Bus} arrives at {@code this} {@code Stop}, regardless of {@code Route} and the
     * abbreviated named of the {@code Route} it's on</p>
     * <b>Else if the context of this context info is beneath a {@code Route}:</b>
     * <p>The distance to {@code this} {@code Stop}'s location from the user's current location in the user's
     * units</p>
     * <b>Else (or if a calculation is indeterminate):</b> Unknown
     * @see SpringBootService#getActiveRoutes()
     */
    public String listItemContext2(UiContext ctx) {
        String finalString = "Unknown";

        if (ctx.getDisplayedUnder() == ParentContainer.GENERAL) {
            Bus nextArrivingBus = nextArrivingBus();
            if (nextArrivingBus != null){
                String timeString = condenseSecsToString(nextArrivingBus.secondsTillArrivalAt(stopId));
                String abbRouteName = SpringBootService.getRoute(nextArrivingBus.getRouteId()).getAbbName();
                finalString = timeString + " away (" + abbRouteName + ")";
            }
        } else if (ctx.getDisplayedUnder() instanceof Route) {
            finalString = distanceToStop(ctx.getUserLat(), ctx.getUserLong(), ctx.getMeasurementSystem()) + " away";
        }

        return finalString;
    }
}