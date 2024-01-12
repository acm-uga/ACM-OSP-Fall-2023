package busAppCore;
import java.util.TimerTask;

/**
 * Specifies what happens during each data update
 */
public class DataUpdater extends TimerTask {

    /**
     * Updates all {@link Bus} data
     */
    @Override
    public void run() {
        ApiService.updateBusData();
    }
}