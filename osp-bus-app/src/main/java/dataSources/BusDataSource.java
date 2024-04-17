package dataSources;

import baseClasses.Bus;

import java.util.HashMap;

/**
 * Specifies fields and methods of an external data source, such as the (now-defunct)
 * {@code ApiService} or {@link InstantBusModel}.
 */
public abstract class BusDataSource  {
    /**
     * Maps Route IDs to a mapping of Stop IDs to the Buses approaching them, updated with each batch of data from the
     * API mirror. For example:
     * <br><br>
     * <b>First Key is any RouteID. Second Key is a StopID along that Route.</b>
     * <ul>
     *     <li> Route ID 123456: <ul>
     *         <li> Stop ID 000001: <ul>
     *             <li>Next approaching Bus,</li>
     *             <li>Second-next approaching Bus,</li>
     *             <li><i>Any other Buses approaching this stop on this Route that we receive from the API, ordered.</i></li>
     *         </ul></li>
     *         <li> Stop ID 000002: <ul>
     *             <li>Next approaching Bus,</li>
     *             <li>Second-next approaching Bus,</li>
     *             <li><i>Any other Buses approaching this stop on this Route that we receive from the API, ordered.</i></li>
     *         </ul></li>
     *         <li><i>Every other Stop on this Route, unordered.</i></li>
     *     </ul></li>
     *     <li> Route ID 789101 <ul>
     *         <li> Stop ID 000010 <ul>
     *             <li>Next approaching Bus</li>
     *             <li>Second-next approaching Bus</li>
     *             <li><i>Any other Buses approaching this stop on this Route that we receive from the API, ordered.</i></li>
     *         </ul></li>
     *         <li> Stop ID 000020 <ul>
     *             <li>Next approaching Bus</li>
     *             <li>Second-next approaching Bus</li>
     *             <li><i>Any other Buses approaching this stop on this Route that we receive from the API, ordered.</i></li>
     *         </ul></li>
     *         <li><i>Every other Stop on this Route, unordered.</i></li>
     *     </ul></li>
     *     <li><i>Every other Route, unordered.</i></li>
     * </ul>
     */
    static HashMap<Long, HashMap<Long, Bus[]>> busDataByRouteId = new HashMap<>();

    /**
     * Gets and stores new data from the child's source.
     */
    protected abstract void getNewData();
}