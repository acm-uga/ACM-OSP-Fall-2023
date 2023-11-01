package ospbusapp;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class ApiService {
    public static void getRoute(long routeId) {
        //Construct route object, which calls DB methods for getting about route data
        TestRoute testRoute = new TestRoute(routeId);

        //With this routeId, make API call for each stop in route
        List<Long> stopIds = testRoute.getStopIds();

        //Parse data for each stop
        for (Long stopId : stopIds) {
            String url = "<URL GOES HERE>";

            RestTemplate restTemplate = new RestTemplate();
            //Get each route object as object in an array
            String response = restTemplate.getForObject(url, String.class);

            try {
                //Get a parser object
                JSONParser parser = new JSONParser();
                //Get container array
                JSONArray container = (JSONArray) parser.parse(response);
                //Get single object from container
                JSONObject route = (JSONObject) container.get(0);
                //Get arrivals array from route object
                JSONArray arrivals = (JSONArray) route.get("Arrivals");

                for (int i = 0; i < arrivals.size(); i++) {
                    //Get each arrival object from within arrivals array
                    JSONObject arrival = (JSONObject) arrivals.get(i);

                    //Get specific attribute from arrival object (lookup by attribute name in API response)
                    long vehicleId = (long) arrival.get("VehicleID");
                    double secondsToArrival = (double) arrival.get("SecondsToArrival");

                    //Debugging:
                    System.out.println("Vehicle " + vehicleId + " is arriving at stop " + stopId + " in " + secondsToArrival + " seconds.");
                }
            } catch (ParseException e) {
                System.out.println("Error with parsing...");
            }
        }
    }

    public static void main(String[] args) {
        //Test on sample route data (Night Campus)
        getRoute(18724L);
    }

    //Sample: parse title from a fake JSON response
    public String getTitle() {
        String url = "https://jsonplaceholder.typicode.com/todos/1";

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        JsonParser jsonParser = JsonParserFactory.getJsonParser();
        //map of k:v pairs from JSON response
        var mappings = jsonParser.parseMap(response);

        //Get desired values using key from map
        return (String) mappings.get("title");
    }
}
