package ospbusapp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Route {
    //Fields:
    private long routeId;
    private String name;
    //K: either weekend or weekday, V: start time - end time
    private Map<String, String> schedule;
    private List<Long> stopIds;
    private List<Bus> activeBuses;

    //Constructors:
    public Route(long routeId) {
        this.routeId = routeId;

        //Update this with DB calls
//        this.name = name;
//        this.schedule = schedule;
//        this.stopIds = stopIds;
//        this.activeBuses = activeBuses;
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

    public Map<String, String> getSchedule() {
        return schedule;
    }

    public void setSchedule() {
        //Populate new map with times from DB -- temporary values for now
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
