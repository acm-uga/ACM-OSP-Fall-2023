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
    // Static Gson instance for handling JSON operations
    private static final Gson gson = new Gson();

    /**
     * Retrieves a specific {@code Route} object from the backend.
     *
     * @param routeId the id of the desired {@code Route} to retrieve
     * @return the {@code Route} object for the route with id {@code routeId}
     */
    public static Route getRoute(long routeId) {
        return StaticExampleData.getRoute(routeId);
    }

    /**
     * Retrieves a specific {@code Stop} object from the backend.
     *
     * @param stopId the id of the desired {@code Stop} to retrieve
     *
     * @return the {@code Stop} object for the stop with id {@code stopId}
     */
    public static Stop getStop(long stopId) {
        return StaticExampleData.getStop(stopId);
    }

    // Gson JSON handling methods

    /**
     * Deserializes a JSON string to a Bus object.
     * @param json the JSON string representing a Bus
     * @return the deserialized Bus object
     */
    public static Bus getBusFromJson(String json) {
        return gson.fromJson(json, Bus.class);
    }

    /**
     * Serializes a Bus object to a JSON string.
     * @param bus the Bus object to serialize
     * @return the JSON string representing the serialized Bus
     */
    public static String createBusJson(Bus bus) {
        return gson.toJson(bus);
    }

    /**
     * Deserializes a JSON string to a Route object.
     * @param json the JSON string representing a Route
     * @return the deserialized Route object
     */
    public static Route getRouteFromJson(String json) {
        return gson.fromJson(json, Route.class);
    }

    /**
     * Serializes a Route object to a JSON string.
     * @param route the Route object to serialize
     * @return the JSON string representing the serialized Route
     */
    public static String createRouteJson(Route route) {
        return gson.toJson(route);
    }

    /**
     * Deserializes a JSON string to a Stop object.
     * @param json the JSON string representing a Stop
     * @return the deserialized Stop object
     */
    public static Stop getStopFromJson(String json) {
        return gson.fromJson(json, Stop.class);
    }

    /**
     * Serializes a Stop object to a JSON string.
     * @param stop the Stop object to serialize
     * @return the JSON string representing the serialized Stop
     */
    public static String createStopJson(Stop stop) {
        return gson.toJson(stop);
    }

    /**
     * Retrieves all stops, ordered by proximity to the user from the backend.
     *
     * @param latitude  the user's latitude coordinate
     * @param longitude the user's longitude coordinate
     * @return all {@code Stop}s in the UGA Bus system, in ascending order by proximity to the user
     */
    public static Stop[] getAllNearbyStops(double latitude, double longitude) {
        return StaticExampleData.getStops();
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
        return StaticExampleData.getStops();
    }

    /**
     * Retrieves the {@code Stop} object of every Stop in the UGA Bus System from the backend.
     *
     * @return the {@code Stop} object of every Stop in the UGA Bus System.
     */
    public static Stop[] getAllStops() {
        return StaticExampleData.getStops();
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
        return StaticExampleData.getRoutes();
    }

    /**
     * Retrieves the {@code Route} object of every Route in the UGA Bus System from the backend.
     *
     * @return the {@code Route} object of every Route in the UGA Bus System.
     */
    public static Route[] getAllRoutes() {
        return StaticExampleData.getRoutes();
    }
}
