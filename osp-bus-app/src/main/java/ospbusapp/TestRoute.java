package ospbusapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Test class for working with similar data to what would be retrieved via DB methods
//Hardcoded with some sample values from BH route
public class TestRoute {
    //Fields:
    private final long routeId;
    private String name;
    //K: either weekend/weekday, V: start time - end time
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
        name = "Bulldog Housing";
    }

    public Map<String, String> getSchedule() {
        return schedule;
    }

    public void setSchedule() {
        schedule = new HashMap<>();

        schedule.put("weekend", "9:00AM - 9:00PM");
        schedule.put("weekday", "7:00AM - 7:00PM");
    }

    public List<Long> getStopIds() {
        return stopIds;
    }

    public void setStopIds() {
        stopIds = new ArrayList<>();

        //Add sample stopIds for BH
        stopIds.add(2737326L);
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

class Driver {
    public static void main(String[] args) {
        //Test getRoute() method
        ApiService.getRoute(19369L);
    }
}
