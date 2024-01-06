package busAppCore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Rough idea of how we may power the backend, primarily in terms of startup procedures and data management during runtime
 */
public class BackendEngine {
    // Necessary to instantiate all Route objects through database queries
    private static final long[] ALL_ROUTE_IDS = new long[]{
            22472, 22473, 19367, 20003, 19376, 19369, 20032, 20051, 20002, 14736, 22473, 22698, 20056, 20233, 18724,
            20156, 18689, 20049, 13439
    };

    // Specifies the number of milliseconds between data updates
    private static final long MS_BETWEEN_DATA_UPDATES = TimeUnit.MINUTES.toMillis(1);

    // Contains every Route, regardless of whether it's currently active
    private static HashMap<Long, Route> allRoutesById;

    public static void main(String[] args) {
        // STARTUP PROCEDURES
        // Instantiate every route and place it in allRoutesById
        for (long routeId : ALL_ROUTE_IDS) {
            allRoutesById.put(routeId, DatabaseService.getRoute(routeId));
        }

        // UPKEEP
        // Automatically update Route and Bus data every MS_BETWEEN_DATA_UPDATES milliseconds
        Timer dataUpdateHandler = new Timer();
        TimerTask dataUpdater = new DataUpdater();

        dataUpdateHandler.scheduleAtFixedRate(dataUpdater, 0, MS_BETWEEN_DATA_UPDATES);
    }


    // GETTERS
    public static HashMap<Long, Route> getAllRoutesById() {
        return allRoutesById;
    }

    // Methods
    /**
     * Generates an array of {@code Route} objects that are active at the time of invocation
     *
     * @return an array of containing the {@code Route}s in {@code allRoutesById} that are currently active
     *
     * @see Route#determineActivity()
     */
    public static Route[] activeRoutes() {
        // Determine which Routes are active
        ArrayList<Route> activeRoutesList = new ArrayList<>();
        for (Route route : allRoutesById.values()) {
            if (route.isActive()) activeRoutesList.add(route);
        }

        // Now that the number of active Route's is known, convert the ArrayList to an Array
        Route[] activeRoutesArray = new Route[activeRoutesList.size()];
        int i = 0;
        for (Route activeRoute : activeRoutesList) {
            activeRoutesArray[i] = activeRoute;
            i++;
        }

        return activeRoutesArray;
    }

    /**
     * Get the {@code Route} with the corresponding {@code routeId} from the program's memory, as opposed to the database.
     *
     * @param routeId the ID of the desired {@code Route}
     *
     * @return the {@code Route} object that corresponds to {@code routeId}
     */
    public static Route getRoute(long routeId) {
        return allRoutesById.get(routeId);
    }
}