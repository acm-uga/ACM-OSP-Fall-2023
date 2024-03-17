package dataSources;

import baseClasses.Bus;

import java.util.HashMap;

/**
 * Provides external bus data (the only "dynamic" data in this application) from a specified source.
 */
public class BusData {
    /**
     * The data source to pull bus data from.
     */
    private static final BusDataSource SOURCE = new InstantBusModel();

    /**
     * Updates the {@code Bus} data stored at runtime with new data from the {@link BusDataSource} specified in
     * {@link #SOURCE}.
     */
    public static void updateBusData() {
        SOURCE.getNewData();
    }

    /**
     * Gets the entire mapping of Route IDs to their Stop ID-Approaching Bus Array mappings, which is constantly
     * updated with new data from the API mirror
     *
     * @return a {@code HashMap} mapping the ID of every {@code Route} to its own {@code HashMap} mapping the ID of every
     * {@code Stop} along that {@code Route} to its own array of {@code Bus}es approaching it, in order of seconds to
     * arrival
     */
    public static HashMap<Long, HashMap<Long, Bus[]>> getAllBusData() {
        return SOURCE.busDataByRouteId;
    }

    /**
     * Gets a mapping of Stop IDs to an array of {@code Bus}es approaching each, which is constantly updated with new
     * data from the API mirror
     *
     * @param routeId the ID of the {@code Route} whose {@code Bus} data is desired
     *
     * @return a mapping of the ID of every {@code Stop} along the {@code Route} with ID {@code routeId} to its own array
     * of {@code Bus}es approaching it, in ascending order of seconds to arrival
     *
     */
    public static HashMap<Long, Bus[]> getBusDataFromId(long routeId) {
        return SOURCE.busDataByRouteId.get(routeId);
    }
}