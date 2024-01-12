package busAppCore;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Rough idea of how we may power the backend, primarily in terms of startup procedures and data management during runtime
 */
public class BackendEngine {
    // Specifies the number of milliseconds between data updates
    private static final long MS_BETWEEN_DATA_UPDATES = TimeUnit.MINUTES.toMillis(1);

    public static void main(String[] args) {
        // Automatically update Route and Bus data every MS_BETWEEN_DATA_UPDATES milliseconds
        Timer dataUpdateHandler = new Timer();
        TimerTask dataUpdater = new DataUpdater();

        dataUpdateHandler.scheduleAtFixedRate(dataUpdater, 0, MS_BETWEEN_DATA_UPDATES);
    }

    // Methods
    /**
     * Generates an array of {@code Route} objects that are active at the time of invocation
     *
     * @return an array of containing the {@code Route}s in {@code allRoutesById} that are currently active
     *
     * @see Route#determineActivity()
     */
    public static Route[] getActiveRoutes() {
        // Get every Route from the database
        Route[] allRoutes = DatabaseService.getAllRoutes();

        // Determine which Routes are active
        ArrayList<Route> activeRoutesList = new ArrayList<>();
        for (Route route : allRoutes) {
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
}