package edu.uga.acm.osp.data.sources;

import edu.uga.acm.osp.data.baseClasses.Route;
import edu.uga.acm.osp.data.baseClasses.Stop;

/**
 * Retrieves UGA Bus data from the backend using the SpringBoot API. To be implemented.
 * <br><br>
 * Behind-the-scenes, the SpringBoot response is an aggregation of UGA Bus system API data,
 * cached data in the backend, and static database data.
 * TODO
 */
public class SpringBootService {
    /**
     * Retrieves a specific {@code Route} object from the backend.
     *
     * @param routeId the id of the desired {@code Route} to retrieve
     *
     * @return the {@code Route} object for the route with id {@code routeId}
     */
    public static Route getRoute(long routeId) {
        return ExampleGenerator.getRoute();
    }

    /**
     * Retrieves a specific {@code Stop} object from the backend.
     *
     * @param stopId the id of the desired {@code Stop} to retrieve
     *
     * @return the {@code Stop} object for the stop with id {@code stopId}
     */
    public static Stop getStop(long stopId) {
        return ExampleGenerator.getStop();
    }

    /**
     * Retrieves all stops, ordered by proximity to the user from the backend.
     *
     * @param latitude the user's latitude coordinate
     * @param longitude the user's longitude coordinate
     *
     * @return all {@code Stop}s in the UGA Bus system, in ascending order by proximity to the user
     */
    public static Stop[] getAllNearbyStops(double latitude, double longitude) {
        return ExampleGenerator.getStops();
    }

    /**
     * Retrieves {@code count} many stops, ordered by proximity to the user from the backend.
     *
     * @param latitude the user's latitude coordinate
     * @param longitude the user's longitude coordinate
     * @param count the number of stops to retrieve
     *
     * @return {@code count}-many {@code Stop}s in ascending order by proximity to the user
     */
    public static Stop[] getNearbyStops(double latitude, double longitude, int count) {
        return ExampleGenerator.getStops();
    }

    /**
     * Retrieves the {@code Stop} object of every Stop in the UGA Bus System from the backend.
     *
     * @return the {@code Stop} object of every Stop in the UGA Bus System.
     */
    public static Stop[] getAllStops() {
        return ExampleGenerator.getStops();
    }

    /**
     * Retrieves {@code Route} objects of every "active" Route in the UGA Bus System from the
     * backend.
     *
     * @return the {@code Route} objects of all Routes active in the UGA Bus System at the time of
     * invocation
     *
     * @see Route#determineActivity()
     */
    public static Route[] getActiveRoutes() {
        return ExampleGenerator.getRoutes();
    }

    /**
     * Retrieves the {@code Route} object of every Route in the UGA Bus System from the backend.
     *
     * @return the {@code Route} object of every Route in the UGA Bus System.
     */
    public static Route[] getAllRoutes() {
        return ExampleGenerator.getRoutes();
    }
}
