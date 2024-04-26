package edu.uga.acm.osp.data.sources;

import edu.uga.acm.osp.data.baseClasses.Bus;
import edu.uga.acm.osp.data.baseClasses.Route;
import edu.uga.acm.osp.data.baseClasses.Stop;
import com.google.gson.Gson;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for SpringBootService to ensure Gson serialization and deserialization work correctly.
 */
public class SpringBootServiceTest {

    private static final Gson gson = new Gson();

    @Test
    public void testBusSerializationAndDeserialization() {
        // Create a sample Bus object
        Bus bus = new Bus(1, 101, 201, null);  // Assuming constructor parameters match those in your Bus class
        String busJson = SpringBootService.createBusJson(bus);
        Bus deserializedBus = SpringBootService.getBusFromJson(busJson);

        assertEquals(bus.getBusId(), deserializedBus.getBusId());
    }

    @Test
    public void testRouteSerializationAndDeserialization() {
        // Create a sample Route object
        Route route = new Route(1, "Route Name", "Short Name", "#FFFFFF", null);  // Adjust parameters as needed
        String routeJson = SpringBootService.createRouteJson(route);
        Route deserializedRoute = SpringBootService.getRouteFromJson(routeJson);

        assertEquals(route.getRouteId(), deserializedRoute.getRouteId());
    }

    @Test
    public void testStopSerializationAndDeserialization() {
        // Create a sample Stop object
        Stop stop = new Stop(1, "Stop Name", null, 33.9604, -83.3779, null);  // Adjust parameters as needed
        String stopJson = SpringBootService.createStopJson(stop);
        Stop deserializedStop = SpringBootService.getStopFromJson(stopJson);

        assertEquals(stop.getStopId(), deserializedStop.getStopId());
    }
}
