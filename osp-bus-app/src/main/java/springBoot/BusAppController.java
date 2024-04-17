package springBoot;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import baseClasses.Bus;
import baseClasses.Stop;
import baseClasses.Route;
import dataSources.DatabaseService;

@RestController
@RequestMapping("/api")
public class BusAppController {

    @GetMapping
    @RequestMapping(value = "/get/nearestStops")
    public Stop[] getNearestStops( 
        
        @RequestParam(value = "routeId", required = false) Long routeId, 
        @RequestParam(value = "n", defaultValue = "3") int n,
        @RequestParam(value = "longitude", defaultValue = "0") double longitude,
        @RequestParam(value = "latitude", defaultValue = "0") double latitude) {
            if (longitude == 0 || latitude == 0) {
                  throw new IllegalArgumentException("Url did not include latitude or longitude.");
            }
            if (routeId != null) { 
                Stop[] nearestStops = DatabaseService.getNearbyStops(latitude, longitude, DatabaseService.getAllRoutes().length);
                int i = 0;
                int addedRoutes = 0;
                ArrayList<Stop> nearestFilteredStops = new ArrayList<>();
                while(addedRoutes < n) {
                    if(nearestStops[i].containsRoute(routeId)) {
                        nearestFilteredStops.add(nearestStops[i]);
                        addedRoutes ++;
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

    @GetMapping
    @RequestMapping(value = "/get/stop")
    public Stop getStop(
        @RequestParam(value = "stopId", defaultValue = "0") long stopId) {
            if (stopId == 0) {
                throw new IllegalArgumentException("URL did not include a stopID.");
            }
            return DatabaseService.getStop(stopId);
    }


    @GetMapping
    @RequestMapping(value = "/get/route")
    public Route getRoute(
        @RequestParam(value = "routeID", defaultValue = "0") long routeId){
            if (routeId == 0) {
                throw new IllegalArgumentException("URL did not include RouteID.");
            }
            return DatabaseService.getRoute(routeId);
    }

    @GetMapping
    @RequestMapping(value = "/get/all/stops")
    public Stop[] getAllStops() {
        return DatabaseService.getAllStops();
    }

    @GetMapping
    @RequestMapping(value = "/get/all/routes")
    public Route[] getAllRoutes() {
        return DatabaseService.getAllRoutes();
    }

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