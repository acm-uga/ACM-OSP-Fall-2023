package edu.uga.acm.osp.data.sources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.uga.acm.osp.data.baseClasses.Bus;
import edu.uga.acm.osp.data.baseClasses.Route;
import edu.uga.acm.osp.data.baseClasses.Stop;
import edu.uga.acm.osp.data.routeSchedule.RouteSchedule;

/**
 * Contains methods that generate example objects with random, but valid data for UI testing.
 */
public class ExampleGenerator {
    // Temporary data (for smaller-scale testing)
    private HashMap<Long, Route> routesById = new HashMap<>();

    private ArrayList<Long> routeIds = new ArrayList<>();
    private HashMap<Long, Stop> stopsById = new HashMap<>();
    private ArrayList<Long> stopIds = new ArrayList<>();

    public ExampleGenerator() {
        generateRandomRoutes(3, 15);
    }

    // BASE GENERATORS
    /**
     * Generates a random number of unique {@code Route}s between {@code minNum} and {@code maxNum}
     * (inclusive) with logical, valid data.
     *
     * @param minNum the minimum number of routes to generate
     * @param maxNum the maximum number of routes to generate
     *
     * @return a mapping of unique {@code Route} instances (each with logical, valid data) mapped
     * to their route IDs, with the number of entries between (@code minNum} and {@code maxNum}
     * (inclusive)
     */
    private void generateRandomRoutes(int minNum, int maxNum) {
        Route newRoute;
        long routeId;
        String[] namePair;
        String name, abbName, displayColor;
        RouteSchedule schedule;
        long[] stopIds;
        HashMap<Long, Bus[]> activeBuses;

        // Generate a random number of Routes
        int numOfRoutesToGenerate = minNum + (int) (Math.random() * (maxNum - minNum));
        for (int i = 1; i <= numOfRoutesToGenerate; i++) {
            // Generate random (but logical) fields for the new Route
            routeId = randomRouteId();
            this.routeIds.add(routeId);
            namePair = randomRouteNamePair();
            name = namePair[0];
            abbName = namePair[1];
            displayColor = randomRouteColor();
            schedule = randomSchedule();
            stopIds = generateRandomStopsOnRoute(routeId,  0.75,5, 20);
            activeBuses = generateRandomBusesOnRoute(routeId, stopIds, 0.25, 0, 3);

            newRoute = new Route(routeId, name, abbName, displayColor, schedule, stopIds, activeBuses);
            this.routesById.put(newRoute.getRouteId(), newRoute);
        }
    }

    /**
     * Generates a random number of unique {@code Stop}s between {@code minNum} and {@code maxNum}
     * (inclusive) with logical, valid data. The resultant mapping is an aggregation of brand new
     * {@code Stop} instances and existing {@code Stop}s in {@code stopsById} associated with
     * the provided {@code routeId}.
     *
     * @param routeId the routeId to associate new {@code Stop}s with
     * @param percentExistingStops the bias towards using existing {@code Stop}s in {@code stopsById}
     *      as opposed to creating new ones
     * @param minNumOfStops the minimum number of {@code Route}s to generate
     * @param maxNumOfStops the maximum number of {@code Route}sto generate
     *
     * @return a mapping of unique {@code Stop} instances (each with logical, valid data) mapped
     * to their stop IDs, with the number of entries between {@code minNum} and {@code maxNum}
     * (inclusive)
     */
    private long[] generateRandomStopsOnRoute(
            long routeId,
            double percentExistingStops,
            int minNumOfStops,
            int maxNumOfStops) {
        // The number of brand new and existing Stops to randomly aggregate
        int numOfStopsToGenerate = minNumOfStops + (int)(Math.random() * (maxNumOfStops - minNumOfStops));

        // Aggregate a combination of new and existing Stops
        long[] stopIds = new long[numOfStopsToGenerate];
        Stop newStop, existingStop;
        double selection;
        int existingStopIndex;
        long existingStopId;
        long[] oldServesRouteIds;

        long stopId;
        String name;
        Stop.StopType type;
        double[] coordinates;
        double latitude;
        double longitude;
        long[] servesRouteIds;
        for (int i = 0; i < numOfStopsToGenerate; i++) {
            selection = Math.random();

            // If an existing Stop is chosen to be collected, associate it with this Route
            if (selection <= percentExistingStops && !this.stopsById.isEmpty()) {
                existingStopIndex = (int) (Math.random() * (stopsById.size() - 1));
                existingStopId = (long) this.stopsById.keySet().toArray()[existingStopIndex];
                existingStop = this.stopsById.get(existingStopId);

                stopId = existingStopId;
                name = existingStop.getName();
                type = existingStop.getType();
                latitude = existingStop.getLatitude();
                longitude = existingStop.getLongitude();
                oldServesRouteIds = existingStop.getServesRoutesIds();
                servesRouteIds = new long[oldServesRouteIds.length + 1];

                for (int j = 0; j < servesRouteIds.length; j++) {
                    servesRouteIds[j] = (j < (servesRouteIds.length - 1)) ? oldServesRouteIds[j] : routeId;
                }

                this.stopsById.remove(stopId);
            } else { // Else, create a brand new Stop
                stopId = randomStopId();
                name = randomStopName();
                type = randomStopType();
                coordinates = randomCoordinates();
                latitude = coordinates[0];
                longitude = coordinates[1];
                servesRouteIds = new long[]{routeId};
            }

            this.stopIds.add(stopId);
            newStop = new Stop(stopId, name, type, latitude, longitude, servesRouteIds);

            this.stopsById.put(newStop.getStopId(), newStop);
            stopIds[i] = newStop.getStopId();
        }

        return stopIds;
    }

    /**
     * Generates a random mapping of stop IDs included in {@code stopIds} to an array of
     * randomly generated {@code Bus} instances in ascending order of seconds to arrival.
     *
     * @param routeId the routeId to associate new {@code Bus}es
     * @param percentWithApproachingBuses the percent (0-1.0) of Stop IDs which should have
     *       {@code Bus}es "actively" approaching them
     * @param minNumOfBuses the minimum number of {@code Bus}es to generate approaching
     *      {@code percentWithApproachingBuses} percent of Stop IDs
     * @param maxNumOfBuses the maximum number of {@code Bus}es to generate approaching
     *      {@code percentWithApproachingBuses} percent of Stop IDs
     *
     * @return a mapping of unique {@code Stop} instances (each with logical, valid data) mapped
     * to their stop IDs, with the number of entries between {@code minNum} and {@code maxNum}
     * (inclusive)
     */
    private HashMap<Long, Bus[]> generateRandomBusesOnRoute(
            long routeId,
            long[] stopIds,
            double percentWithApproachingBuses,
            int minNumOfBuses,
            int maxNumOfBuses
    ) {
        // The number of brand new Buses to generate
        int numOfBusesToGenerate;
        double selection;

        // Values constant throughout iterations
        HashMap<Long, Bus[]> newOverview = new HashMap<>();
        int numOfStops = stopIds.length;

        // Holds the new fields generated by each iteration of below
        Bus[] newBusArray;
        Bus newBus;
        List<Double> newSTAs;
        double lowestSTA;

        // Add buses to each Stop along the Route
        for (long stopId : stopIds) {
            selection = Math.random();
            if (selection <= percentWithApproachingBuses) {
                numOfBusesToGenerate = minNumOfBuses + (int)(Math.random() * (maxNumOfBuses - minNumOfBuses));
                newBusArray = new Bus[numOfBusesToGenerate];
                lowestSTA = -10.0; // Buses currently at a stop go negative (-10 = 10 seconds at stop)

                for (int i = 0; i < numOfBusesToGenerate; i++) {
                    newSTAs = randomSTAs(numOfStops, lowestSTA);
                    lowestSTA = newSTAs.get(0);
                    newBus = new Bus(
                            randomBusId(),
                            routeId,
                            stopId,
                            newSTAs
                    );

                    newBusArray[i] = newBus;
                }

                newOverview.put(stopId, newBusArray);
            } else {
                newOverview.put(stopId, new Bus[]{});
            }
        }

        return newOverview;
    }

    // GETTERS
    public Route getRoute() {
        int randomIndex = (int)(Math.random() * (routeIds.size() - 1));
        return routesById.get(routeIds.get(0));
    }

    public Stop getStop() {
        int randomIndex = (int)(Math.random() * (stopIds.size() - 1));
        return stopsById.get(stopIds.get(0));
    }

    /**
     * Gets the {@code Route} object with routeId {@code routeId}.
     *
     * @return a {@code Route} object with random, logical data
     *
     * @see Route
     */
    public Route getRoute(long routeId) {
        return routesById.get(routeId);
    }

    /**
     * Gets the {@code Stop} object with stopId {@code stopId}.
     *
     * @return a {@code Stop} object with random, logical data
     *
     * @see Stop
     */
    public Stop getStop(long stopId) {
        return stopsById.get(stopId);
    }

    /**
     * Gets all {@code Route} objects.
     *
     * @return an array of all {@code Route} objects
     */
    public Route[] getRoutes() {
        return (Route[])routesById.values().toArray();
    }

    /**
     * Gets all {@code Route} ids.
     *
     * @return an {@code ArrayList} of all {@code Route} ids
     */
    public ArrayList<Long> getRouteIds() {
        return this.routeIds;
    }

    /**
     * Gets the mapping of {@code Route} objects to their ids.
     *
     * @return a {@code HashMap} of Route ids ({@code Long}) to their respective {@code Route}s
     */
    public HashMap<Long, Route> getRoutesById() {
        return this.routesById;
    }

    /**
     * Gets all {@code Stop} objects.
     *
     * @return an array of all {@code Stop} objects
     */
    public Stop[] getStops() {
        return (Stop[])stopsById.values().toArray();
    }

    /**
     * Gets all {@code Stop} ids.
     *
     * @return an {@code ArrayList} of all {@code Stop} ids
     */
    public ArrayList<Long> getStopIds() {
        return this.stopIds;
    }

    /**
     * Gets the mapping of {@code Stop} objects to their ids.
     *
     * @return a {@code HashMap} of Stop ids ({@code Long}) to their respective {@code Stop}s
     */
    public HashMap<Long, Stop> getStopsById() {
        return this.stopsById;
    }

    // HELPERS

    /**
     * Generates a {@code double} array of length 0-{@code maxToGenerate} containing random
     * "seconds to arrival" values in ascending order.
     *
     * @param maxToGenerate the maximum number of random seconds to arrival values to include in the
     *      array
     *
     * @return a {@code List<Double>} containing random "seconds to arrival" values in ascending order, the size
     * of which anywhere from 0-{@code maxToGenerate}
     */
    private static List<Double> randomSTAs(int maxToGenerate, double minSTA) {
        int numOfSTAsToGenerate = 1 + (int)(Math.random() * (maxToGenerate - 1));
        ArrayList<Double> newSTAs = new ArrayList<Double>();

        double currentMin = minSTA;
        double newSTA, maxSTA = 1800; // Max STA is 30 minutes

        for (int i = 1; i <= numOfSTAsToGenerate; i++) {
            newSTA = currentMin + (Math.random() * (maxSTA - currentMin));
            newSTAs.add(newSTA);
        }

        return newSTAs;
    }

    /**
     * Generates a random 4-digit number between 1,000 and 9,999.
     *
     * @return a random 4-digit number between 1,000 and 9,999 not used by any other buses previously
     * generated by this class
     */
    private long randomBusId() {
        long newId;

        // While EXTREMELY unlikely, ensure no duplicate IDs are generated
        do {
            newId = 1000L + (long) (Math.random() * (9999L));
        } while (busIdAlreadyExists(newId));

        return newId;
    }

    /**
     * Determines if the provided {@code busId} already exists in the example batch
     * {@code routesById}.
     *
     * @param busId the ID to check
     *
     * @return {@code true} if {@code busId} already exists in {@code routesById},
     * else {@code false}
     */
    private boolean busIdAlreadyExists(long busId) {
        if (routesById.isEmpty()) {
            return false;
        }

        HashMap<Long, Bus[]> activeBuses;
        for (Route route : routesById.values()) {
            activeBuses = route.getActiveBuses();
            for (Bus[] busList : activeBuses.values()) {
                for (Bus bus : busList) {
                    if (bus.getBusId() == busId) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Generates a random global coordinate pair.
     *
     * @return a random pair of valid global coordinates.
     */
    private static double[] randomCoordinates() {
        double minLat = -90, maxLat = 90, minLong = -180, maxLong = 180;

        double latitude = (double) (Math.random() * (maxLat - minLat));
        double longitude = (double) (Math.random() * (maxLong - minLong));

        return new double[] {latitude, longitude};
    }

    /**
     * Chooses a random {@code Stop.StopType}.
     *
     * @return a random {@code Stop.StopType}
     */
    private static Stop.StopType randomStopType() {
        Stop.StopType[] stopTypes = Stop.StopType.values();
        int index = (int) (Math.random() * (stopTypes.length - 1));

        return stopTypes[index];
    }

    /**
     * Generates a random, unique name for a {@code Stop}.
     *
     * @return a random stop name
     */
    private String randomStopName() {
        String[] prefixes = new String[]{
                "Big", "Lecture Hall", "Dr. Professor", "Student", "UGA", "Bulldog", "The", "Tiny",
                "Westside", "Eastside", "Northside", "Southside", "Central", "Harry Dawg", "UGA", "Old", "New"};
        String[] suffixes = new String[]{
                "Center", "Park", "Parking Lot", "Dining Hall", "Stadium", "Dormitory", "Building",
                "Clinic", "Mall", "Apartments", "Commons", "Expansion", "Campus", "House", "Fields", "Woods"};

        String fullName;
        int prefixIndex, suffixIndex;

        do {
            prefixIndex = (int) (Math.random() * (prefixes.length - 1));
            suffixIndex = (int) (Math.random() * (suffixes.length - 1));
            fullName = prefixes[prefixIndex] + " " + suffixes[suffixIndex];
        } while (stopNameAlreadyExists(fullName));

        return fullName;
    }

    /**
     * Determines if the provided {@code stopName} already exists in the example batch
     * {@code stopsById}.
     *
     * @param stopName the name to check
     *
     * @return {@code true} if {@code stopName} already exists in {@code stopsById},
     * else {@code false}
     */
    private boolean stopNameAlreadyExists(String stopName) {
        if (stopsById.isEmpty()) {
            return false;
        }

        for (Stop stop : stopsById.values()) {
            if (stop.getName().equals(stopName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Generates a random, unique 6-digit {@code long} number between 100,000 and 999,999.
     *
     * @return a random, 6-digit {@code long} number not already used by a {@code Route} in
     * {@code routesById} or other {@code Stop}s in {@code stopsById}
     */
    private long randomStopId() {
        long newId;

        // While EXTREMELY unlikely, ensure no duplicate IDs are generated
        do {
            newId = 100000L + (long) (Math.random() * (899999L));
        } while (routeIdAlreadyExists(newId) && stopIdAlreadyExists(newId));

        return newId;
    }

    /**
     * Determines if the provided {@code stopId} already exists in the example batch
     * {@code stopsById}.
     *
     * @param stopId the ID to check
     *
     * @return {@code true} if {@code stopId} already exists in {@code stopsById},
     * else {@code false}
     */
    private boolean stopIdAlreadyExists(long stopId) {
        if (stopsById.isEmpty()) {
            return false;
        }

        for (Stop stop : stopsById.values()) {
            if (stop.getStopId() == stopId) {
                return true;
            }
        }

        return false;
    }

    /**
     * Generates a random, unique 6-digit {@code long} number between 100,000 and 999,999.
     *
     * @return a random, 6-digit {@code long} number not already used by another {@code Route} in
     * {@code routesById}
     */
    private long randomRouteId() {
        long newId;

        // While EXTREMELY unlikely, ensure no duplicate IDs are generated
        do {
            newId = 100000L + (long) (Math.random() * (899999L));
        } while (routeIdAlreadyExists(newId));

        return newId;
    }

    /**
     * Determines if the provided {@code routeId} already exists in the example batch
     * {@code routesById}.
     *
     * @param routeId the ID to check
     *
     * @return {@code true} if {@code routeId} already exists in {@code routesById},
     * else {@code false}
     */
    private boolean routeIdAlreadyExists(long routeId) {
        if (routesById.isEmpty()) {
            return false;
        }

        for (Route route : routesById.values()) {
            if (route.getRouteId() == routeId) {
                return true;
            }
        }

        return false;
    }

    /**
     * Generates a random, unique name for a {@code Route} (plus a matching abbreviated name).
     *
     * @return a random route name (index 0) and abbreviated name (index 1)
     */
    private String[] randomRouteNamePair() {
        String[] prefixes = new String[]{
                "Cross-Campus", "Northern", "Eastern", "Western", "Southern", "Bulldog", "Parking",
                "Lecture Hall", "Dormitory", "Downtown", "Weekend", "Weekday",
                "UGA", "Night", "Main"};
        String[] suffixes = new String[]{
                "Connector", "Bypass", "Shuttle", "Transporter", "Loop"};

        String fullName;
        String abbName;
        int prefixIndex, suffixIndex;

        do {
            prefixIndex = (int) (Math.random() * (prefixes.length - 1));
            suffixIndex = (int) (Math.random() * (suffixes.length - 1));
            fullName = prefixes[prefixIndex] + " " + suffixes[suffixIndex];
            abbName = prefixes[prefixIndex].substring(0, 1) + suffixes[suffixIndex].substring(0, 1);
        } while (routeNameAlreadyExists(fullName));

        return new String[] {fullName, abbName};
    }

    /**
     * Determines if the provided {@code routeName} already exists in the example batch
     * {@code routesById}.
     *
     * @param routeName the name to check
     *
     * @return {@code true} if {@code routeName} already exists in {@code routesById},
     * else {@code false}
     */
    private boolean routeNameAlreadyExists(String routeName) {
        if (routesById.isEmpty()) {
            return false;
        }

        for (Route route : routesById.values()) {
            if (route.getName().equals(routeName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Generates a random, unique color.
     *
     * @return a random, unique color not already used in {@code routesById}
     */
    private String randomRouteColor() {
        String color;
        do {
            int[] characterValues = new int[6];

            // Generate random values 0-16
            for (int i = 0; i < 6; i++) {
                characterValues[i] = (int) (Math.random() * 16);
            }

            // Convert values >= 10 to A-F
            char[] characters = new char[6];
            for (int i = 0; i < 6; i++) {
                int characterValue = characterValues[i];
                if (characterValue >= 10) {
                    characters[i] = (char) (characterValue + 55);
                } else {
                    characters[i] = (char) (characterValue + 48);
                }
            }

            // Create a color string valid for frontend use
            color = "0xFF" + String.valueOf(characters);
        } while (colorAlreadyExists(color));

        return color;
    }

    /**
     * Determines if the provided {@code color} already exists in the example batch
     * {@code routesById}.
     *
     * @param color the color (hex string) to check
     *
     * @return {@code true} if {@code color} already exists in {@code routesById},
     * else {@code false}
     */
    private boolean colorAlreadyExists(String color) {
        if (routesById.isEmpty()) {
            return false;
        }

        for (Route route : routesById.values()) {
            if (route.getDisplayColor().equals(color)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Picks a random, valid schedule from a predefined list.
     *
     * @return a random, unique {@code RouteSchedule} not already used in {@code routesById}
     */
    private static RouteSchedule randomSchedule() {
        String[] presetSchedules = new String[] {
                "-121225{U:-2030;M-F:0830-2230;S:0830-;}",
                "012524-{U:1725-;M:-0230,0830-1230,1330-1400,1740-;TW:-;R:-0830;}",
                "-012325,012524-022724,030124,030324-{U:1725-;M:-0230,0830-1230,1330-1400,1740-;TW:-;R:-0830;}",
                "121223{0830-2230}",
                "{M:0830-1230;}",
                "121224;s{M:0830-1230;}"
        };

        int randomIndex = (int) (Math.random() * (presetSchedules.length - 1));

        return RouteSchedule.decode(presetSchedules[randomIndex]);
    }
}