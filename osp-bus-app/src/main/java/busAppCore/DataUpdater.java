package busAppCore;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

/**
 * Specifies the process through which {@link Route} and {@link Bus} data gets updated by fresh API data
 */
public class DataUpdater extends TimerTask {
    LocalDate lastUpdateDate;

    /**
     * Update all {@link Route} and {@link Bus} data with fresh database and API data by fetching that data from the API
     * mirror and passing it to {@link Route#updateBuses} and {@link Route#refreshWithDbData}
     */
    @Override
    public void run() {
        HashMap<Long, HashMap<Long, Bus[]>> apiData = ApiService.getAllActiveBuses();
        HashMap<Long, Route> existingData = BackendEngine.getAllRoutesById();

        /* This is responsible for refreshing instantiated Route objects post-startup. Data refreshes are daily, at the
        first execution of this TimeTask after midnight. Because all of our static data is stored in the database, this
        allows us to change data in the backend without downtime. */
        boolean notUpdatedToday = lastUpdateDate == null || lastUpdateDate.isBefore(LocalDate.now(ZoneId.of("UTC-5")));
        if (notUpdatedToday) {
            lastUpdateDate = LocalDate.now(ZoneId.of("UTC-5"));
            for (long routeId : BackendEngine.ALL_ROUTE_IDS) {
                BackendEngine.getAllRoutesById().put(routeId, DatabaseService.getRoute(routeId));
            }
        }

        for (Map.Entry<Long, HashMap<Long, Bus[]>> newData : apiData.entrySet()) {
            // Update each the Buses stored in each Route stored in BackendEngine
            existingData.get(newData.getKey()).updateBuses(newData.getValue());
        }
    }
}