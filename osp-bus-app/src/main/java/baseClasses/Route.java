package baseClasses;

import dataSources.DatabaseService;
import routeSchedule.RouteSchedule;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Represents a bus route: a series of stops and the buses actively driving through them in order
 * <p></p>
 * Contains a list of stops, buses active on the route, relevant metadata, and data used in UI components on the frontend
 *
 * @see Stop
 * @see Bus
 */
public class Route {
    // Fields derived from the database at instantiation:
    private long routeId; // Route ID as given by the Bus API. Derived from db
    private String name; // Full, official route name
    private String abbName; // Abbreviated route name, as displayed in current bus tracking tool
    private String displayColor; // Hex code of the color used when displaying this route in UI components
    private RouteSchedule schedule; // Contains all info pertaining to when the route is supposed to be operating
    private long[] stopIds; // Contains the IDs of the stops on this route in the order they are served

    // Fields that update with each batch of API data
    private boolean active; // True when the route is operating AND buses are appearing in the API. Otherwise false
    private HashMap<Long, Bus[]> activeBuses; // Contains all Bus objects currently active on this Route and the id of the Stop they're approaching. Sorted in Stop order.

    //Constructors:
    public Route(
            long routeId,
            String name,
            String abbName,
            String displayColor,
            RouteSchedule schedule,
            long[] stopIds,
            HashMap<Long, Bus[]> activeBuses
    ) {
        this.routeId = routeId;
        this.name = name;
        this.abbName = abbName;
        this.displayColor = displayColor;
        this.schedule = schedule;
        this.stopIds = stopIds;
        this.activeBuses =activeBuses;
        this.active = this.determineActivity();
    }

    //Methods:
    public long getRouteId() {
        return routeId;
    }

    public void setRouteId(long routeId) {
        this.routeId = routeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbName() {
        return abbName;
    }

    public void setAbbName(String abbName) {
        this.abbName = abbName;
    }

    public String getDisplayColor() {
        return displayColor;
    }

    public void setDisplayColor(String displayColor){
        this.displayColor = displayColor;
    }

    public RouteSchedule getSchedule() {
        return schedule;
    }

    public void setSchedule(RouteSchedule routeSchedule) {
        this.schedule = routeSchedule;
    }

    public long[] getStopIds() {
        return stopIds;
    }

    public void setStopIds(long[] stopIds) {
        this.stopIds = stopIds;
    }

    public boolean isActive(){
        return this.active;
    }

    public void setActive(boolean isActive) {
        this.active = isActive;
    }

    public HashMap<Long, Bus[]> getActiveBuses() {
        return activeBuses;
    }

    public void setActiveBuses(HashMap<Long, Bus[]> activeBuses) {
        this.activeBuses = activeBuses;
    }

    @Override
    public String toString() {
        return ("Route{" +
                "routeId=" + routeId +
                ", name=" + name +
                ", abbName=" + abbName +
                ", displayColor=" + displayColor +
                ", schedule=\n" + schedule.toString() +
                "\n, stopIds=" + Arrays.toString(stopIds) +
                " active=" + active +
                ", activeBuses=" + activeBuses.toString() +
                '}'
        );
    }

    /**
     * Parses a string of {@code long} stop IDs into an array of {@code long}s, provided it is in the "id-id-id" format
     * used in the database.
     *
     * @param stopIdsString a String in "stopId-stopId-stopId" format
     *
     * @return an array of {@code long}s, with each element derived from the '-'-delimited {@code stopIdsString}
     */
    public static long[] parseStopIdsString(String stopIdsString) {
        String[] stopIdStrings = stopIdsString.split("-");
        long[] stopIds = new long[stopIdStrings.length];
        int i = 0;
        for (String stopIdString : stopIdStrings) {
            stopIds[i] = Long.getLong(stopIdString);
            i++;
        }
        return stopIds;
    }

    /**
     * Determines the number of {@code timeUnit}s that have elapsed between the most recent update of a {@code Bus}'s
     * data on {@code this} {@code Route} and the current time at invocation
     *
     * @param timeUnit the chronological time unit to measure in (such as {@code ChronoUnit.SECONDS})
     *
     * @return the number of {@code timeUnit}s between the last update of a {@code Bus}'s data on {@code this}
     * {@code Route}'s data and the time at invocation, exclusive
     *
     * @see ChronoUnit
     */
    private long timeSinceMostRecentBusUpdate(ChronoUnit timeUnit) {
        long leastUnits = 0;
        long currentUnits;

        /* Iterate through all of this Route's Bus objects and determine the number of time units since the most recent
        * update of a Bus object's data */
        for (Bus[] listOfApproachingBuses : activeBuses.values()) {
            for (Bus bus : listOfApproachingBuses) {
                currentUnits = bus.timeSinceLastUpdate(timeUnit);
                leastUnits = Math.min(currentUnits, leastUnits);
            }
        }

        return leastUnits;
    }

    /**
     * Determines whether {@code this} {@code Route} is active based on whether it is currently supposed to be operating
     * according to its schedule and when the most recent update of a {@code Bus} in {@code activeBuses}' data occurred
     *
     * @return {@code true} if {@code this} {@code Route} is supposed to be operating at the time of invocation according
     * to it's {@code schedule} and it has been less than five minutes since the last
     */
    public boolean determineActivity() {
        return (this.timeSinceMostRecentBusUpdate(ChronoUnit.MINUTES) <= 5) && (this.schedule.isOperatingNow());
    }

    /**
     * Finds the nearest {@code Stop} to the provided coordinates along {@code this} {@code Route}
     *
     * @param latitude the latitude with which to base proximity
     * @param longitude the longitude with which to base proximity
     *
     * @return the nearest {@code Stop} to the given location that is served by {@code this} {@code Route}
     */
    public Stop getNearestStop(double latitude, double longitude) {
        Stop[] nearestStops = DatabaseService.getNearbyStops(latitude, longitude, DatabaseService.stopCount());

        // Determine the first stop on nearestStops that serves this Route
        Stop nearestStopOnRoute = null;
        int i = 0;
        while (nearestStopOnRoute == null && i < nearestStops.length) {
            if (Arrays.stream(nearestStops[i].getServesRoutesIds()).anyMatch(id -> id == this.routeId)) {
                nearestStopOnRoute = nearestStops[i];
            }
            i++;
        }

        return nearestStopOnRoute;
    }

    /**
     * Counts the number of unique buses that are active on {@code this} {@code Route} at invocation
     *
     * @return the number of unique busIds active on {@code this} {@code Route} at invocation
     */
    public int totalUniqueBuses() {
        ArrayList<Long> idsSeen = new ArrayList<Long>();
        long currentId;
        for (Bus[] arrayOfBusesApproachingStop: activeBuses.values()) {
            for (Bus busApproachingStop : arrayOfBusesApproachingStop) {
                currentId = busApproachingStop.getBusId();
                if (!idsSeen.contains(currentId)) {
                    idsSeen.add(currentId);
                }
            }
        }

        return idsSeen.size();
    }
}