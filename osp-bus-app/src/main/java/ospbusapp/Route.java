package ospbusapp;
import dataDisplay.ListItemData;
import dataDisplay.UiContext;
import routeSchedule.RouteSchedule;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;

/**
 * Represents a bus route: a series of stops and the buses actively driving them
 * <p></p>
 * Contains a list of stops, buses active on the route, relevant metadata, and data used in UI components on the frontend
 */
public class Route implements ListItemData {
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
    // From DB calls
    public Route(long routeId, String name, String abbName, String displayColor, RouteSchedule schedule, long[] stopIds) {
        // Populate these static fields using constructor args
        this.routeId = routeId;
        this.name = name;
        this.abbName = abbName;
        this.displayColor = displayColor;
        this.schedule = schedule;
        this.stopIds = stopIds;

        // Populate these dynamic fields with appropriate API calls
        this.activeBuses = ApiService.getActiveBuses(routeId);
        this.active = determineActivity();

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

    public List<Bus> getActiveBuses() {
        return activeBuses;
    }

    public void setActiveBuses(HashMap<Long, Bus[]> activeBuses) {
        //What to update?
        this.activeBuses = activeBuses;
    }

    @Override
    public String toString() {
        return ("Route{" +
                "routeId=" + routeId +
                ", name='" + name + '\'' +
                ", schedule=" + schedule +
                ", stopIds=" + stopIds +
                ", activeBuses=" + activeBuses +
                '}'
        );
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
        for (Bus bus : getActiveBuses()) {
            currentUnits = bus.timeSinceLastUpdate(timeUnit);
            leastUnits = (currentUnits < leastUnits) ? currentUnits : leastUnits;
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
    protected boolean determineActivity() {
        return (timeSinceMostRecentBusUpdate(ChronoUnit.MINUTES) <= 5) && (this.schedule.isOperatingNow());
    }

    // BasicUiDisplayable Implementations:
    /**
     * Provides the name of the invoking Route
     *
     * @return the name of {@code this} {@code Route}
     */
    @Override
    public String listItemHeader(UiContext ctx) {
        return getName();
    }

    /**
     * Provides an abbreviated view of the schedule currently in effect at the time of invocation
     *
     * @return the names of the first and last Stop along {@code this} {@code Route}
     *
     * @see routeSchedule.RouteSchedule#mainScheduleOn(LocalDateTime)
     */
    @Override
    public String listItemSubHeader(UiContext ctx) {
        return this.schedule.mainScheduleOn(LocalDateTime.now(ZoneId.of("UTC-5")));
    }

    // TODO change this to return distance to closest Stop along this Route
    /**
     * Provides the current, primary schedule of the invoking Route
     *
     * @return the name of {@code this} {@code Route}
     */
    @Override
    public String listItemContext1(UiContext ctx) {
        // Get all the stops on this route in order of proximity to the user
        Stop[] nearestStops = DatabaseService.getNearbyStops(ctx.getUserLat(), ctx.getUserLong(), this.stopIds.length);


        return this.getSchedule().mainSchedule();
    }

    // TODO change this to return number of buses currently on this Route
    /**
     * Provides the current, alternate schedule of the invoking Route
     *
     * @return a {@code String} suitable for use as secondary context
     */
    @Override
    public String listItemContext2(UiContext ctx) {
        return this.getSchedule().altSchedule();
    }
}