package busAppCore;

import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

/**
 * Specifies the process through which {@link Route} and {@link Bus} data gets updated by fresh API data
 */
public class DataUpdater extends TimerTask {
    /**
     * Update all {@link Route} and {@link Bus} data with fresh API data by fetching that data from the API mirror and
     * passing it to {@link Route#update}
     */
    @Override
    public void run() {
        HashMap<Long, HashMap<Long, Bus[]>> apiData = ApiService.getAllActiveBuses();
        HashMap<Long, Route> existingData = BackendEngine.getAllRoutesById();

        for (Map.Entry<Long, HashMap<Long, Bus[]>> newData : apiData.entrySet()) {
            // Update each the Buses stored in each Route stored in BackendEngine
            existingData.get(newData.getKey()).update(newData.getValue());
        }
    }
}