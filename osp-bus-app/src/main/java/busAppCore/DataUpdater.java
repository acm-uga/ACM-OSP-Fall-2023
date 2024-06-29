package busAppCore;

import dataSources.BusData;
import dataSources.DatabaseService;

import java.util.TimerTask;

import baseClasses.Route;
import baseClasses.Stop;

/**
 * Specifies what happens during each data update.
 */
public class DataUpdater extends TimerTask {

    /**
     * Updates all {@link baseClasses.Bus} data.
     */
    @Override
    public void run() {
        BusData.updateBusData();
        // Validate updates are occurring
        System.out.println("LATEST SNAPSHOT OF DATA BELOW:");
        System.out.println("ROUTES:-----------------------");
        Route[] allRoutes = DatabaseService.getAllRoutes();
        for (Route route : allRoutes) {
            System.out.println(route.toString());
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        }

        System.out.println("STOPS:------------------------");
        Stop[] allStops = DatabaseService.getAllStops();
        for (Stop stop : allStops) {
            System.out.println(stop.toString());
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        }
    }
}