package ospbusapp;

import routeSchedule.RouteSchedule;

import java.sql.*;
import java.util.*;

public class DatabaseService {
    //Add to properties file for security
    private static final String url = "jdbc:mysql://localhost:3306/OSPDB";
    private static final String username = "root";
    private static final String password = "Password123";

    /**
     * Retrieves the data for the {@code Stop} with ID {@code stopId} from the database and returns an instantiated
     * {@code Stop} using it
     * <br>
     * Preferred over {@link #getStop(String)} when possible.
     *
     * @param stopId the ID of the {@code Stop} to instantiate
     *
     * @return the {@code Stop} object of ID {@code stopId}, constructed from database data
     */
    public static Stop getStop(long stopId) {
        //Use try-with-resources to make code more concise
        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            String query = "SELECT * FROM stopinfo WHERE stopid = " + stopId;
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            return stopFromResultSet(rs);
        } catch (SQLException ex) {
            throw new RuntimeException("Error: ", ex);
        }
    }

    /**
     * Retrieves the data for the {@code Stop} with ID {@code stopId} from the database and returns an instantiated
     * {@code Stop} using it
     * <br>
     * Alternative to {@link #getStop(long)}} (which is preferred when possible).
     *
     * @param stopId the ID of the {@code Stop} to instantiate
     *
     * @return the {@code Stop} object of ID {@code stopId}, constructed from database data
     */
    public static Stop getStop(String stopName) {
        //Use try-with-resources to make code more concise
        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            String query = "SELECT * FROM stopinfo WHERE stopname = " + stopName;
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            return stopFromResultSet(rs);
        } catch (SQLException ex) {
            throw new RuntimeException("Error: ", ex);
        }
    }

    /**
     * Constructs a {@code Stop} object given the {@code ResultSet} from a database query
     *
     * @param rs the {@code ResultSet} with which to derive data from
     *
     * @return a {@code Stop} object whose fields match the data contained in {@code rs}
     */
    private static Stop stopFromResultSet(ResultSet rs) {
        try {
            String name = rs.getString("stopname");
            long stopId = rs.getLong("stopid");
            Stop.Type type = Stop.typeFromString(rs.getString("type_info"));
            double stopLatitude = rs.getDouble("latitude");
            double stopLongitude = rs.getDouble("longitude");

            // Determine the Routes served by this Stop
            ArrayList<Long> servesRouteIdsList = new ArrayList<Long>();
            for (Route route : BackendEngine.getAllRoutesById().values()) {
                if (Arrays.stream(route.getStopIds()).anyMatch(id -> id == stopId)) {
                    servesRouteIdsList.add(route.getRouteId());
                }
            }

            int i = 0;
            long[] servesRouteIds = new long[servesRouteIdsList.size()];
            for (long routeId : servesRouteIdsList) {
                servesRouteIds[i] = routeId;
                i++;
            }

            return new Stop(stopId, name, type, stopLatitude, stopLongitude, servesRouteIds);
        } catch (SQLException ex) {
            throw new RuntimeException("Error: ", ex);
        }
    }

    /**
     * Retrieves the data for the {@code Route} with ID {@code routeId} from the database and returns an instantiated
     * {@code Route} using it
     * <br>
     * Preferred over {@link #getRoute(String)} when possible.
     *
     * @param routeId the ID of the {@code Route} to instantiate
     *
     * @return the {@code Route} object of ID {@code routeId}, constructed from database data
     */
    public static Route getRoute(long routeId) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            //Need to parameterize query to prevent possible SQL injection?
            String query = "SELECT * FROM routeinfo WHERE routeid = " + routeId;
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            return routeFromResultSet(rs);
        } catch (SQLException ex) {
            throw new RuntimeException("Error: ", ex);
        }
    }

    /**
     * Retrieves the data for the {@code Route} of name {@code routeName} from the database and returns an instantiated
     * {@code Route} using it
     * <br>
     * Alternative to {@link #getRoute(long)}} (which is preferred when possible).
     *
     * @param routeName the name of the {@code Route} to instantiate
     *
     * @return the {@code Route} object of ID {@code routeId}, constructed from database data
     */
    public static Route getRoute(String routeName) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            //Need to parameterize query to prevent possible SQL injection?
            String query = "SELECT * FROM routeinfo WHERE routename = " + routeName;
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            return routeFromResultSet(rs);
        } catch (SQLException ex) {
            throw new RuntimeException("Error: ", ex);
        }
    }

    /**
     * Constructs a {@code Route} object given the {@code ResultSet} from a database query
     *
     * @param rs the {@code ResultSet} with which to derive data from
     *
     * @return a {@code Route} object whose fields match the data contained in {@code rs}
     */
    private static Route routeFromResultSet(ResultSet rs) {
        try {
            // Holds the fields of the current Stop being instantiated from db fields below
            long routeId = rs.getLong("routeid");
            String name = rs.getString("routename");
            String abbName = rs.getString("routenameabrv");
            String displayColor = rs.getString("hexcolor");
            RouteSchedule schedule = RouteSchedule.decode(rs.getString("schedule")); // TODO edit to reflect actual field name when determined

            // Convert the stopIds String into an array of Longs
            String stopIdsString = rs.getString("stopidsordered");
            String[] stopIdStrings = stopIdsString.split("-");
            long[] stopIds = new long[stopIdStrings.length];
            int i = 0;
            for (String stopIdString : stopIdStrings) {
                stopIds[i] = Long.getLong(stopIdString);
                i++;
            }

            return new Route(routeId, name, abbName, displayColor, schedule, stopIds);
        } catch (SQLException ex) {
            throw new RuntimeException("Error: ", ex);
        }
    }

    /**
     * Invokes the {@code FinalCalculate} stored procedure in the database to obtain a list of the {@code numOfStops}
     * nearest stops to the provided {@code latitude} and {@code longitude} coordinates
     *
     * @param latitude the latitude of the location in which to base proximity
     * @param longitude the longitude of the location in which to base proximity
     * @param numOfStops the desired number of closest stops to determine
     *
     * @return <b>If {@code numOfStops <=} the total number of stops on campus:</b>
     * <p>An array of {@code Stop}s sorted in order of proximity to the provided coordinates (closest i=0) </p>
     * <b>Else:</b> "Every stop on campus, sorted in order of proximity to the provided coordinates (closest i=0)"
     */
    public static Stop[] getNearbyStops(double latitude, double longitude, int numOfStops) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            /* The final number of elements is unknown as numOfStops may exceed the number of stops present on a given
            * Route. For this reason, use an ArrayList */
            List<Stop> nearbyStopsList = new ArrayList<>();

            //Need to parameterize query to prevent possible SQL injection?
            String query = "CALL FinalCalculate(" + latitude + ", " + longitude + ", " + numOfStops + ")";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            // Holds the fields of the current Stop being instantiated from db fields below
            String name;
            long stopId, stopLatitude, stopLongitude;
            long[] servesRouteIds;
            Stop.Type type;

            while (rs.next()) {
                nearbyStopsList.add(stopFromResultSet(rs));
            }

            int i = 0;
            Stop[] nearbyStops = new Stop[nearbyStopsList.size()];
            for (Stop nearbyStop : nearbyStopsList) {
                nearbyStops[i] = nearbyStop;
                i++;
            }

            return nearbyStops;
        } catch (SQLException ex) {
            throw new RuntimeException("Error: ", ex);
        }
    }
}