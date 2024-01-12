package busAppCore;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Handles data retrieval from the API mirror
 */
public class ApiService {
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
    private static HashMap<Long,HashMap<Long, Bus[]>> busDataByRouteId;

    /* This method is responsible for getting all the Bus data from the API mirror, organizing it, and changing
     * the busDataByRouteId field that provides bus data for the rest of this program.
     *
     * TODO: change the method below to create a a mapping of Route ID keys to values of Stop ID-Bus array HashMaps.
     * Then, set busDataByRouteId to this new data (SEE ABOVE). Add JavaDoc to this method when complete :) */
    public static void updateBusData() {
        // Make an API call for every known stop ID. First, determine those IDs
        Stop[] allStops = DatabaseService.getAllStops();
        long[] stopIds = new long[allStops.length];
        int i = 0;
        for (Stop stop : allStops) {
            stopIds[i] = stop.getStopId();
            i++;
        }

        //Base URL of the API
        String baseUrl = "";

        //Construct new HTTP client for making API request and storing data in String
        HttpClient client = HttpClient.newHttpClient();

        //Use Google's JSON-simple library for parsing
        //Get a parser object to parse each response's body
        JSONParser parser = new JSONParser();

        Map<Long, Integer> routes = new HashMap<>();
        List<Map<Long, Bus>> busesOnRoute = new ArrayList<>();

        //Parse data for each stop
        for (long stopId : stopIds) {


            long first = -1;
            //Specify endpoint to be added to baseUrl for request
            String endpoint = "/Stop/" + stopId + "/Arrivals";

            //Create request to be sent
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + endpoint))
                    .build();

            try {
                //Send request and store response body in String
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                String body = response.body();

                //Get container array
                JSONArray container = (JSONArray) parser.parse(body);
                //Get single route object from container
                JSONObject route = (JSONObject) container.get(0);
                //Get arrivals array from inside route object
                JSONArray arrivals = (JSONArray) route.get("Arrivals");
                Set<Long> vehicleIds = new HashSet<>();
                for (int i = 0; i < arrivals.size(); i++) {
                    //Get each arrival object from within arrivals array
                    JSONObject arrival = (JSONObject) arrivals.get(i);

                    int posRouteId;

                    long currRouteId = (long) arrival.get("RouteID");

                    if (routes.containsKey(currRouteId)) {
                        posRouteId = routes.get(currRouteId);
                    } else {
                        posRouteId = routes.size();
                        routes.put(currRouteId, routes.size());
                        busesOnRoute.add(new HashMap<>());
                    }

                    //Get specific attribute from arrival object (lookup by attribute name in response)
                    long vehicleId = (long) arrival.get("VehicleID");
                    if (vehicleIds.contains(vehicleId)) continue;
                    vehicleIds.add(vehicleId);
                    double secondsToArrival = (double) arrival.get("SecondsToArrival");
                    // stopID, routeID already in for loop
                    Bus bus;
                    // adding to an existing bus object
                    if (busesOnRoute.get(posRouteId).containsKey(vehicleId)) {
                        bus = busesOnRoute.get(posRouteId).get(vehicleId);
                        if (secondsToArrival < bus.getSecondsTillArrival().get(0)) {
                            bus.setNextStopId(stopId);
                            bus.getSecondsTillArrival().add(0, secondsToArrival);
                        } else {
                            // binary search possible (maybe in the future)
                            // for loop for now
                            boolean added = false;
                            for (int j = 0; j < bus.getSecondsTillArrival().size(); j++) {
                                if (bus.getSecondsTillArrival().get(j) > secondsToArrival) {
                                    bus.getSecondsTillArrival().add(j, secondsToArrival);
                                    added = true;
                                    break;
                                }
                            }
                            if (!added) {
                                bus.getSecondsTillArrival().add(secondsToArrival);
                            }
                        }
                    }
                    // creating a new bus object
                    else {
                        List<Double> listSecondsToArrival = new ArrayList<>();
                        listSecondsToArrival.add(secondsToArrival);
                        bus = new Bus(vehicleId, currRouteId, stopId, listSecondsToArrival);
                        busesOnRoute.get(posRouteId).put(vehicleId, bus);
                    }

                    //Debugging:
                    System.out.println("Route: " + currRouteId);
                    System.out.println("Vehicle " + vehicleId + " is arriving at stop " + stopId + " in " + secondsToArrival + " seconds.");
                }
            } catch (IOException | InterruptedException e) {
                System.err.println("Error with making HTTP request");
            } catch (ParseException e) {
                System.err.println("Error with parsing response");
            }
        }

//        for (Long routeId: routes.keySet()) {
//            //Position in list
//            int routePos = routes.get(routeId);
//
//            Map<Long, Bus> buses = busesOnRoute.get(routePos);
//
//            System.out.println("For Route: " + routeId);
//
//        }

        return busesOnRoute;
    }

    /**
     * Gets the entire mapping of Route IDs to their Stop ID-Approaching Bus Array mappings, which is constantly
     * updated with new data from the API mirror
     *
     * @return a {@code HashMap} mapping the ID of every {@code Route} to its own {@code HashMap} mapping the ID of every
     * {@code Stop} along that {@code Route} to its own array of {@code Bus}es approaching it, in order of seconds to
     * arrival
     */
    public static HashMap<Long,HashMap<Long, Bus[]>> getAllBusData() {
        return busDataByRouteId;
    }

    /**
     * Gets a mapping of Stop IDs to an array of {@code Bus}es approaching each, which is constantly updated with new
     * data from the API mirror
     *
     * @param routeId the ID of the {@code Route} whose {@code Bus} data is desired
     *
     * @return a mapping of the ID of every {@code Stop} along the {@code Route} with ID {@code routeId} to its own array
     * of {@code Bus}es approaching it, in ascending order of seconds to arrival
     *
     */
    public static HashMap<Long, Bus[]> getBusDataFromId(long routeId) {
        return busDataByRouteId.get(routeId);
    }
}