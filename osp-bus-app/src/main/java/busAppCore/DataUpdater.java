package busAppCore;

import dataSources.BusData;

import java.util.TimerTask;

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
    }
}