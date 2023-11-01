package ospbusapp;

import java.time.LocalDateTime;
import java.util.List;

public class Bus {
    //Fields:
    //VehicleID in API
    private long busId;
    //RouteID in API
    private long routeId;
    //Next upcoming stop and time to arrival till this stop (seconds)
    private long nextStopId;
    //Keep track of time bus was last updated locally on server (not retrieved from API)
    private LocalDateTime lastUpdated;
    //Keep list of seconds to arrival for each upcoming stop (index 0 is time till next stop)
    private List<Double> secondsTillArrival;

    //Constructors:
    public Bus(long busId, long routeId, long nextStopId, List<Double> secondsTillArrival) {
        this.busId = busId;
        this.routeId = routeId;
        this.nextStopId = nextStopId;
        //Set last updated to current time
        this.lastUpdated = LocalDateTime.now();
        this.secondsTillArrival = secondsTillArrival;
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

    //Same bus vs same instance?
    @Override
    public boolean equals(Object bus) {
        //Check if same object
        if (this == bus) {
            return true;
        }

        //Verify object isn't null and that object is same class
        if (bus == null || getClass() != bus.getClass()) {
            return false;
        }

        long busId = ((Bus) bus).busId;

        return this.busId == busId;
    }

    @Override
    public String toString() {
        return ("Bus{" +
                "busId=" + busId +
                ", routeId=" + routeId +
                ", nextStopId=" + nextStopId +
                ", lastUpdated=" + lastUpdated +
                ", secondsTillArrival=" + secondsTillArrival +
                '}'
        );
    }
}
