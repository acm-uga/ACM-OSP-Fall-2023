package springBoot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import java.util.List;

import baseClasses.Bus;
import baseClasses.Stop;
import baseClasses.Route;
import dataSources.DatabaseService;

@RestController
//@RequestMapping("/api")
public class BusAppController {

    // works!!
    @GetMapping("/hello")
    public String hello() {
        return "Hello There";
    }

    @GetMapping
    @RequestMapping(value = "/get/nearestStops")
    public Stop[] getNearestStops( 
        @RequestParam(value = "routeId", required = false) Long routeId, 
        @RequestParam(value = "n", defaultValue = "3") int n,
        @RequestParam(value = "longitude", defaultValue = "0") double longitude,
        @RequestParam(value = "latitude", defaultValue = "0") double latitude) {
            if (routeId != null) { 
                System.out.println(DatabaseService.getAllRoutes().length);
                Stop[] nearestStops = DatabaseService.getNearbyStops(latitude, longitude, DatabaseService.getAllRoutes().length);
                Stream<Stop> StopStream = Arrays.stream(nearestStops);
                StopStream.forEach(stop -> System.out.println("StopName: " + stop.getName()));
                int i = 0;
                int addedRoutes = 0;
                ArrayList<Stop> nearestFilteredStops = new ArrayList<>();
                while(addedRoutes < n) {
                    if(nearestStops[i].containsRoute(routeId)) {
                        nearestFilteredStops.add(nearestStops[i]);
                        addedRoutes++;
                    }
                    i++;
                }
                return nearestFilteredStops.toArray(new Stop[0]);
                // not done, need to filter stops by Route.

            } else {
                Stop[] nearestStops = DatabaseService.getNearbyStops(latitude, longitude, n);
                return nearestStops;
            }
        }

        // works!!
    @GetMapping
    @RequestMapping(value = "/get/stop/")
    public Stop getStop(
        @RequestParam(value = "stopId", defaultValue = "2737327") long stopId) {
            return DatabaseService.getStop(stopId);
    }


    // works!!
    @GetMapping
    @RequestMapping(value = "/get/route")
    public Route getRoute(
        @RequestParam(value = "routeID", defaultValue = "0") long routeId){
            return DatabaseService.getRoute(routeId);
    }
    
    // works!!
    @GetMapping
    @RequestMapping(value = "/get/all/stops")
    public Stop[] getAllStops() {
        return DatabaseService.getAllStops();
    }

    //works!!
    @GetMapping
    @RequestMapping(value = "/get/all/routes")
    public Route[] getAllRoutes() {
        return DatabaseService.getAllRoutes();
        
    }

    // works!
    @GetMapping
    @RequestMapping(value = "/get/all/active/routes")
    public Route[] getAllActiveRoutes() {
        Route[] allRoutes = DatabaseService.getAllRoutes();
        ArrayList<Route> activeRoutes = new ArrayList<>();
        for (int i = 0; i < allRoutes.length; i++) {
            if(allRoutes[i].isActive()) {
                activeRoutes.add(allRoutes[i]);
            }
        }
        Route[] array = activeRoutes.toArray(new Route[0]);
        return array;
        
    }

}
//accept user commands and return JSON