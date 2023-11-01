package ospbusapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Test class for working with similar data to what would be retrieved via DB methods
public class TestRoute {
    //Fields:
    private final long routeId;
    private String name;
    //K: either weekend or weekday, V: start time : end time
    private Map<String, String> schedule;
    private List<Long> stopIds;
    private List<Bus> activeBuses;

    //Constructors:
    public TestRoute(long routeId) {
        this.routeId = routeId;

        //Set static data from DB calls
        setName();
        setSchedule();
        setStopIds();
    }

    //Methods:
    public long getRouteId() {
        return routeId;
    }

    public String getName() {
        return name;
    }

    public void setName() {
        this.name = "Night Campus";
    }

    public Map<String, String> getSchedule() {
        return schedule;
    }

    public void setSchedule() {
        this.schedule = new HashMap<>();

        this.schedule.put("weekend", "9:00AM - 9:00PM");
        this.schedule.put("weekday", "7:00AM - 7:00PM");
    }

    public List<Long> getStopIds() {
        return stopIds;
    }

    public void setStopIds() {
        this.stopIds = new ArrayList<>();

        this.stopIds.add(2732990L);
        this.stopIds.add(2737326L);
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
