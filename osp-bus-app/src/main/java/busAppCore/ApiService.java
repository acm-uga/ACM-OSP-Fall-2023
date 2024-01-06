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

public class ApiService {
    // TODO make this return a HashMap of Route Ids to StopId-Bus Object array HashMaps
    public static HashMap<Long,HashMap<Long, Bus[]>> getAllActiveBuses() {
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
}