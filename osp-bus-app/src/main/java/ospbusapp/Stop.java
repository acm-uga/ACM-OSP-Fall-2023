package ospbusapp;

import java.util.List;

public class Stop {
    //Fields:
    private int stopId;
    private String name;
    private Double latitude;
    private double longitude;
    //List of route ids that this stop is connected to
    private List<Integer> servesRoutesIds;

    //Constructors:
    public Stop(int stopId, String name, Double latitude, double longitude, List<Integer> servesRoutesIds) {
        this.stopId = stopId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.servesRoutesIds = servesRoutesIds;
    }

    //Methods:
    public int getStopId() {
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

    public List<Integer> getServesRoutesIds() {
        return servesRoutesIds;
    }

    public void setServesRoutesIds(List<Integer> servesRoutesIds) {
        this.servesRoutesIds = servesRoutesIds;
    }

    @Override
    public boolean equals(Object stop) {
        //Check if same object
        if (this == stop) {
            return true;
        }

        //Verify object isn't null and that object is same class
        if (stop == null || getClass() != stop.getClass()) {
            return false;
        }

        int stopId = ((Stop) stop).stopId;

        return this.stopId == stopId;
    }

    @Override
    public String toString() {
        return ("Stop{" +
                "stopId=" + stopId +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", servesRoutesIds=" + servesRoutesIds +
                '}'
        );
    }
}
