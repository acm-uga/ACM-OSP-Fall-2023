package edu.uga.acm.osp.data.sources;

import java.util.ArrayList;
import java.util.HashMap;

import edu.uga.acm.osp.data.baseClasses.Route;
import edu.uga.acm.osp.data.baseClasses.Stop;

public class StaticExampleData {
    private static final HashMap<Long, Route> routesById;
    private static final ArrayList<Long> routeIds;

    private static final HashMap<Long, Stop> stopsById;
    private static final ArrayList<Long> stopIds;

    static {
        ExampleGenerator examples = new ExampleGenerator();
        routesById = examples.getRoutesById();
        routeIds = examples.getRouteIds();
        stopsById = examples.getStopsById();
        stopIds = examples.getStopIds();
    }

    public static Route getRoute() {
        int randomIndex = (int)(Math.random() * (routeIds.size() - 1));
        return routesById.get(routeIds.get(0));
    }

    public static Stop getStop() {
        int randomIndex = (int)(Math.random() * (stopIds.size() - 1));
        return stopsById.get(stopIds.get(0));
    }

    /**
     * Gets the {@code Route} object with routeId {@code routeId}.
     *
     * @return a {@code Route} object with random, logical data
     *
     * @see Route
     */
    public static Route getRoute(long routeId) {
        return routesById.get(routeId);
    }

    /**
     * Gets the {@code Stop} object with stopId {@code stopId}.
     *
     * @return a {@code Stop} object with random, logical data
     *
     * @see Stop
     */
    public static Stop getStop(long stopId) {
        return stopsById.get(stopId);
    }

    /**
     * Gets all {@code Route} objects.
     *
     * @return an array of all {@code Route} objects
     */
    public static Route[] getRoutes() {
        return (Route[])routesById.values().toArray();
    }

    /**
     * Gets all {@code Stop} objects.
     *
     * @return an array of all {@code Stop} objects
     */
    public static Stop[] getStops() {
        return (Stop[])stopsById.values().toArray();
    }
}
