package ospbusapp;
import routeSchedule.RouteSchedule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a bus route: a series of stops and the buses actively driving them
 * <p></p>
 * Contains a list of stops, buses active on the route, relevant metadata, and data used in UI components on the frontend
 */
public class Route implements BasicUiDisplayable {
    // Fields derived from the database at instantiation:
    private long routeId; // Route ID as given by the Bus API. Derived from db
    private String name; // Full, official route name
    private String abbName; // Abbreviated route name, as displayed in current bus tracking tool
    private String displayColor; // Hex code of the color used when displaying this route in UI components
    private RouteSchedule schedule; // Contains all info pertaining to when the route is supposed to be operating
    private long[] stopIds; // Contains the IDs of the stops on this route in the order they are served

    // Fields that update with each batch of API data
    private boolean active; // True when the route is operating AND buses are appearing in the API. Otherwise false
    private List<Bus> activeBuses; // Contains all Bus objects currently active on this Route. Unsorted.

    //Constructors:
    // From DB calls
    public Route(long routeId, String name, String abbName, String displayColor, RouteSchedule schedule, long[] stopIds,
                 boolean active, List<Bus> activeBuses) {
        this.routeId = routeId;
        this.name = name;
        this.abbName = abbName;
        this.displayColor = displayColor;
        this.schedule = schedule;
        this.stopIds = stopIds;
        this.active = active;
        this.activeBuses = activeBuses;
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

    public List<Bus> getActiveBuses() {
        return activeBuses;
    }

    public void setActiveBuses(List<Bus> activeBuses) {
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

    // BasicUiDisplayable Implementations:
    /**
     * Provides the name of the invoking Route
     *
     * @return the name of {@code this} {@code Route}
     */
    @Override
    public String getHeader() {
        return this.name;
    }

    /**
     * Provides a brief overview of the stops served along the invoking Route
     *
     * @return the names of the first and last Stop along {@code this} {@code Route}
     */
    @Override
    public String getSubHeader() {
        Stop firstStop = DatabaseService.getStop(this.stopIds[0]);
        Stop lastStop = DatabaseService.getStop(this.stopIds[this.stopIds.length - 1]);

        return "From " + firstStop.getName() + " to " + lastStop.getName();
    }

    /**
     * Provides the current, primary schedule of the invoking Route
     *
     * @return the name of {@code this} {@code Route}
     */
    @Override
    public String getContext1() {
        return this.schedule.mainSchedule();
    }

    /**
     * Provides the current, alternate schedule of the invoking Route
     *
     * @return a {@code String} suitable for use as secondary context
     */
    @Override
    public String getContext2() {
        return this.schedule.altSchedule();
    }
}