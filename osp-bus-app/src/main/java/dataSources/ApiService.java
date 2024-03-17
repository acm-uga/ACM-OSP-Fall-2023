package dataSources;

import baseClasses.Bus;
import baseClasses.Stop;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

/**
 * Handles data retrieval from the API mirror
 */
public class ApiService extends BusDataSource {
    /**
     * Gets new data from the (now-defunct) UGA Bus System API mirror, parses it, and organizes
     * it for storage.
     *
     * @deprecated as of 3/13/24, with the privatization of UGA's Bus Data. To be replaced by {@link LiveBusModel}.
     */
    @Deprecated public void getNewData() {
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

        HashMap<Long, Integer> routes = new HashMap<>();

        //List where each index is a route
        //Map of each bus ID to its object in this route
        ArrayList<Map<Long, Bus>> busesOnRoute = new ArrayList<>();

        // Route ID (key) to value of map where stopid is key along this route and value is array of bus objs on this route
        // Instance field reference will be updated to this local variable at end of method
        HashMap<Long,HashMap<Long, Bus[]>> busDataByRouteId = new HashMap<>();

        //Parse data for each stop, iterating through stops
        for (long stopId : stopIds) {

            // List of bus objects for this stop (put in map and update this list with objects as we iterate through)
            List<Bus> stopBuses = new ArrayList<>();

            long first = -1;
            //Specify endpoint to be added to baseUrl for request
            String endpoint = "/Stop/" + stopId + "/Arrivals";

            //Create request to be sent
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + endpoint))
                    .build();

            long currRouteId = 0;
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
                for (int j = 0; j < arrivals.size(); j++) {
                    //Get each arrival object from within arrivals array
                    JSONObject arrival = (JSONObject) arrivals.get(i);

                    int posRouteId;

                    currRouteId = (long) arrival.get("RouteID");

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

                    // // adding to an existing bus object
                    // if (busesOnRoute.get(posRouteId).containsKey(vehicleId)) {
                    //     bus = busesOnRoute.get(posRouteId).get(vehicleId);
                    //     if (secondsToArrival < bus.getSecondsTillArrival().get(0)) {
                    //         bus.setNextStopId(stopId);
                    //         bus.getSecondsTillArrival().add(0, secondsToArrival);
                    //     } else {
                    //         // binary search possible (maybe in the future)
                    //         // for loop for now
                    //         boolean added = false;
                    //         for (int j = 0; j < bus.getSecondsTillArrival().size(); j++) {
                    //             if (bus.getSecondsTillArrival().get(j) > secondsToArrival) {
                    //                 bus.getSecondsTillArrival().add(j, secondsToArrival);
                    //                 added = true;
                    //                 break;
                    //             }
                    //         }
                    //         if (!added) {
                    //             bus.getSecondsTillArrival().add(secondsToArrival);
                    //         }
                    //     }
                    // }
                    // creating a new bus object
                    // else {

                    // For now, just creating new bus object -- maybe fix later to update existing ones
                    List<Double> listSecondsToArrival = new ArrayList<>();
                    listSecondsToArrival.add(secondsToArrival);
                    bus = new Bus(vehicleId, currRouteId, stopId, listSecondsToArrival);
                    // Add bus to list for this stop    
                    stopBuses.add(bus);
                    // }

                    //Debugging:
                    System.out.println("Route: " + currRouteId);
                    System.out.println("Vehicle " + vehicleId + " is arriving at stop " + stopId + " in " + secondsToArrival + " seconds.");
                }
            } catch (IOException | InterruptedException e) {
                System.err.println("Error with making HTTP request");
            } catch (ParseException e) {
                System.err.println("Error with parsing response");
            }

            // Get existing stop ids map for this route, then put this stop id with its buses as an entry in the map
            // Verify entry already exists in map for this route id or create new map
            HashMap<Long, Bus[]> stopIdsToBuses = busDataByRouteId.getOrDefault(currRouteId, new HashMap<>());
            stopIdsToBuses.put(stopId, (Bus[])stopBuses.toArray());

            // Update entry in map
            busDataByRouteId.put(currRouteId, stopIdsToBuses);

        }

        // Update reference
        this.busDataByRouteId = busDataByRouteId;
    }
}