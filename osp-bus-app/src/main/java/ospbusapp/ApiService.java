package ospbusapp;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ApiService {
    public static void getRoute(long routeId) {
        //Construct route object, which calls DB methods for getting route data
        TestRoute testRoute = new TestRoute(routeId);

        //With this routeId, make an API call for each stop in route
        List<Long> stopIds = testRoute.getStopIds();

        //Base URL of the API
        String baseUrl = "https://routes.uga.edu";

        //Construct new HTTP client for making API request and storing data in String
        HttpClient client = HttpClient.newHttpClient();

        //Use Google's JSON-simple library for parsing
        //Get a parser object to parse each response's body
        JSONParser parser = new JSONParser();

        //Parse data for each stop
        for (long stopId : stopIds) {
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

                for (int i = 0; i < arrivals.size(); i++) {
                    //Get each arrival object from within arrivals array
                    JSONObject arrival = (JSONObject) arrivals.get(i);

                    //Get specific attribute from arrival object (lookup by attribute name in response)
                    long vehicleId = (long) arrival.get("VehicleID");
                    double secondsToArrival = (double) arrival.get("SecondsToArrival");

                    //Debugging:
                    System.out.println("Vehicle " + vehicleId + " is arriving at stop " + stopId + " in " + secondsToArrival + " seconds.");
                }
            } catch (IOException | InterruptedException e) {
                System.err.println("Error with making HTTP request");
            } catch (ParseException e) {
                System.err.println("Error with parsing response");
            }
        }
    }
}
