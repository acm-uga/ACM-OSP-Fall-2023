package baseClasses;

import busAppCore.DatabaseService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a single bus that travels along a route, stopping at each stop along the way
 * <p></p>
 * Contains the ID of the Stop being approached, the seconds till it arrives, and other relevant metadata
 *
 * @see Route
 */
public class Bus {
    // Fields that update with each batch of API data:
    // Derived directly from API data:
    private long busId; // VehicleID in API
    private long routeId; // RouteID in API
    private long nextStopId; // StopID in API of the stop currently being approached
    private List<Double> secondsTillArrival; // Seconds to arrival for each upcoming stop (index 0 is time to next stop)
    // Updated with each update:
    private LocalDateTime lastUpdated; // Record of when the bus was last updated locally

    //Constructors:
    public Bus(long busId, long routeId, long nextStopId, List<Double> secondsTillArrival) {
        this.busId = busId;
        this.routeId = routeId;
        this.nextStopId = nextStopId;
        this.secondsTillArrival = secondsTillArrival;
        this.lastUpdated = LocalDateTime.now(); //Set last updated to current time
    }

    //Methods:
    public long getBusId() {
        return busId;
    }

    public void setBusId(long busId) {
        this.busId = busId;
    }

    public long getRouteId() {
        return routeId;
    }

    public void setRouteId(long routeId) {
        this.routeId = routeId;
    }

    public long getNextStopId() {
        return nextStopId;
    }

    public void setNextStopId(long nextStopId) {
        this.nextStopId = nextStopId;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated() {
        this.lastUpdated = LocalDateTime.now();
    }

    public List<Double> getSecondsTillArrival() {
        return secondsTillArrival;
    }

    public void setSecondsTillArrival(List<Double> secondsTillArrival) {
        this.secondsTillArrival = secondsTillArrival;
    }

    @Override
    public String toString() {
        return ("Bus{" +
                "busId=" + busId +
                ", routeId=" + routeId +
                ", nextStopId=" + nextStopId +

                // Line below from: https://stackoverflow.com/a/23183963
                ", secondsTillArrival=" + secondsTillArrival.stream().map(Object::toString).collect(Collectors.joining(", ")) +

                ", lastUpdated=" + lastUpdated +
                '}'
        );
    }

    /**
     * Determines the number of {@code timeUnit}s that have elapsed since {@code this} {@code Bus} object had its data
     * updated and the time at invocation
     *
     * @param timeUnit the chronological time unit to measure in (such as {@code ChronoUnit.SECONDS})
     *
     * @return the number of {@code timeUnit}s that have elapsed between {@code this} {@code Bus}'s last data update and
     * the current time at invocation, exclusive
     *
     * @see ChronoUnit
     */
    public long timeSinceLastUpdate(ChronoUnit timeUnit) {
        return timeUnit.between(lastUpdated, LocalDateTime.now(ZoneId.of("UTC-5")));
    }

    /**
     * Determines the seconds until {@code this} {@code Bus} arrives at the {@code Stop} with id {@code stopId}
     *
     * @param stopId the id of the {@code Stop} to find seconds until arrival at
     *
     * @return <b>If the given {@code Stop} is along {@code this} {@code Bus}'s {@code Route} and the seconds till it
     * arrives there is known:</b> The seconds until {@code this} {@code Bus} arrives at the {@code Stop} with id
     * {@code stopId}
     * <br>
     * <b>Else:</b> {@code Double.NaN}
     */
    public double secondsTillArrivalAt(long stopId) {
        /*
        Context:
        REAL order of stops along this Bus's route:
        "Stop Ids": A B C D E F
        Indices:    0 1 2 3 4 5

        BUS' seconds till arrival order (index 0 is always the stop this Bus is approaching):
        C D E F A B
        0 1 2 3 4 5

        Approach:
        1. Index of secsToArrival to use = Real Index of desired STA stop - Real Index of nextStopId
        2. If the result of Step #1 negative add the length of Real to the result of Step #1

        Examples:
        Want STA to C, currently approaching F: 2 - 5 = -3 ... -3 + 6 = *3*
        Want STA to B, currently approaching C: 1 - 2 = -1 ... -1 + 6 = *5*
        Want STA to F, currently approaching C: 5 - 2 = *3*
        */

        long[] stopIdsAlongRoute = DatabaseService.getRoute(routeId).getStopIds();

        // Determine the real index of the desired stop
        // Employ a linear search since StopIds are sorted by the order they're served, not numerically
        int realIndexDesiredStop = 0;
        while (realIndexDesiredStop < stopIdsAlongRoute.length && stopIdsAlongRoute[realIndexDesiredStop] != stopId) {
            realIndexDesiredStop++;
        }

        // If the provided stop does not exist on this bus' route, stop here and return -1
        if (stopIdsAlongRoute[realIndexDesiredStop] != stopId) return -1;

        // Determine the real index of the stop currently being approached
        int realIndexNextStop = 0;
        while (realIndexNextStop < stopIdsAlongRoute.length && stopIdsAlongRoute[realIndexNextStop] != nextStopId) {
            realIndexNextStop++;
        }

        int indexOfSTA = realIndexDesiredStop - realIndexNextStop;
        if (indexOfSTA < 0) indexOfSTA += stopIdsAlongRoute.length;

        // If the STA to the desired stop for this bus is unknown, return Double.NaN
        if (indexOfSTA >= secondsTillArrival.size()) {
            return Double.NaN;
        } else {
            return accountForElapsedTime(secondsTillArrival.get(indexOfSTA));
        }
    }

    /**
     * Approximates the current seconds until something happens given the last recorded seconds until completion,
     * {@code ogSecondsToArrival}, the time that that was recorded, and the current time at invocation
     *
     * @param ogSecondsToArrival the last recorded seconds until something happens
     *
     * @return the approximate number of seconds until that same thing happens, elapsed time accounted for
     */
    public double accountForElapsedTime(double ogSecondsToArrival) {
        /* Accounting for the amount of time that's passed since STA was updated, STA at the moment of invocation
         * should equal (approximately) the originalSTA minus the time that's elapsed since that data was updated */
        return ogSecondsToArrival - (double)(timeSinceLastUpdate(ChronoUnit.SECONDS));
    }
}