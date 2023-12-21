package ospbusapp;

import routeSchedule.RouteSchedule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Route {
    // Fields initialized at instantiation:
    private long routeId; // Route ID as given by the Bus API. Derived from db
    private String name; // Full, official route name
    private String abbName; // Abbreviated route name, as displayed in current bus tracking tool
    private String displayColor; // Hex code of the color used when displaying this route in UI components
    private RouteSchedule schedule; // Contains all info pertaining to when the route is supposed to be operating
    private Long[] stopIds; // Contains the IDs of the stops on this route in the order they are served

    // Fields that update with each API call
    private boolean active; // True when the route is operating AND buses are appearing in the API. Otherwise false

    private List<Bus> activeBuses;

    //Constructors:
    public Route(long routeId) {
        this.routeId = routeId;

        //Update this with DB calls
        //this.name = name;
        //this.schedule = RouteSchedule.decode(encodedScheduleInDb);
        //this.stopIds = stopIds;
        //this.activeBuses = activeBuses;
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

    public void setSchedule(String encodedRouteSchedule) {
        //
        this.schedule = new HashMap<>();

        this.schedule.put("weekend", "9:00AM - 9:00PM");
        this.schedule.put("weekday", "7:00AM - 7:00PM");
    }

    public List<Long> getStopIds() {
        return stopIds;
    }

    public void setStopIds(List<Long> stopIds) {
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
}
