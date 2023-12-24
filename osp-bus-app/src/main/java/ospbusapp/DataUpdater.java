package ospbusapp;

import java.util.TimerTask;

/**
 * Specifies the process through which {@link Route} and {@link Bus} data gets updated by fresh API data
 */
public class DataUpdater extends TimerTask {
    /**
     * Update all {@link Route} and {@link Bus} data with fresh API data by fetching new data from the mirror server
     * and updating these existing objects' fields with to reflect it
     */
    @Override
    public void run() {
        // TODO employ ApiService functions to obtain new data
        // TODO use data from above to adjust Route and Bus fields, instantiate Routes and Buses, and destroy them
    }
}