package dataSources;

/**
 * Generates and maintains {@code Bus} data that uses existing {@code Stop} and {@code Route} data while attempting to
 * replicate the movement of buses over time, like reality. Buses will be consistent across invocations of
 * {@link #getNewData()}, making this a great model of the bus system over time. For a less-realistic model ideal for
 * testing, see {@link InstantBusModel}.
 */
public class LiveBusModel extends BusDataSource {
    protected void getNewData() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
