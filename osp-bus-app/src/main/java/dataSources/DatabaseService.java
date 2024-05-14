package dataSources;

import baseClasses.Bus;
import baseClasses.Route;
import baseClasses.Stop;
import routeSchedule.RouteSchedule;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Handles data retrieval from the database
 */
@Component
public class DatabaseService {
    //Add to properties file for security
    // private static final String url = "jdbc:mysql://ospdb-instance.c3cfroxqfhpq.us-east-1.rds.amazonaws.com:3306/OSPDB";
    // private static final String username = "yushus_komarlu";
    // private static final String password = "Incntat3m";

    // retrieves information with this name from app.props
    @Value("${spring.datasource.url}")
    private static String url = "jdbc:mysql://acm-osp-db.cwj19omwwmxs.us-east-2.rds.amazonaws.com/OSPDB";
    @Value("${spring.datasource.username}")
    private static String username = "yushus_komarlu";
    @Value("${spring.datasource.password}")
    private static String password = "Inc@ntat3m";

    // STOP DATA
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
     * Retrieves the data for the {@code Stop} with name {@code stopName} from the database and returns an instantiated
     * {@code Stop} using it
     * <br>
     * Alternative to {@link #getStop(long)}} (which is preferred when possible).
     *
     * @param stopName the name of the {@code Stop} to instantiate
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
     * Retrieves the data for every {@code Stop} in the database and returns an array of their instantiated objects
     *
     * @return an array containing every {@code Stop} stored in the database
     */
    public static Stop[] getAllStops() {
        //Use try-with-resources to make code more concise
        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            String query = "SELECT * FROM stopinfo";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            Stop[] allStops = new Stop[stopCount()];
            int i = 0;
            while (rs.next()) {
                allStops[i] = stopFromResultSet(rs);
            }

            return allStops;
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
            if (rs != null) {
                String name = rs.getString("stopname");
                long stopId = rs.getLong("stopid");
                Stop.StopType type = Stop.typeFromString(rs.getString("type_info"));
                double stopLatitude = rs.getDouble("latitude");
                double stopLongitude = rs.getDouble("longitude");

                // Determine the Routes served by this Stop
                ArrayList<Long> servesRouteIdsList = new ArrayList<Long>();
                for (Route route : DatabaseService.getAllRoutes()) {
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
            } else {
                return null;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error: ", ex);
        }
    }

    /**
     * Counts the number of {@code Stop} records stored in the database
     *
     * @return the number of {@code Stop} records in the {@code stopinfo} database table
     */
    public static int stopCount() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            //Need to parameterize query to prevent possible SQL injection?
            String query = "SELECT COUNT(*) as stopCount FROM stopinfo" +
                    "\n";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            rs.next();
            return rs.getInt("stopCount");
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
            //Need to parameterize query to prevent possible SQL injection?
            String query = "CALL FinalCalculate(" + latitude + ", " + longitude + ", " + numOfStops + ")";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            int actualNumOfStops = Math.min(numOfStops, stopCount());
            Stop[] nearbyStops = new Stop[actualNumOfStops];
            int i = 0;
            while (rs.next()) {
                nearbyStops[i] = stopFromResultSet(rs);
            }

            return nearbyStops;
        } catch (SQLException ex) {
            throw new RuntimeException("Error: ", ex);
        }
    }

    // ROUTE DATA
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
     * Counts the number of {@code Route} records stored in the database
     *
     * @return the number of {@code Route} records in the {@code routeinfo} database table
     */
    public static int routeCount() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            //Need to parameterize query to prevent possible SQL injection?
            String query = "SELECT COUNT(*) FROM routeinfo";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
                if (rs.next()) {
                    return rs.getInt(1);
                }else {
                    return 0;
                }
            } catch (SQLException ex) {
            throw new RuntimeException("Error: ", ex);
        }
    }

    /**
     * Retrieves the data for every {@code Route} in the database and returns an array of their instantiated objects
     *
     * @return an array containing every {@code Route} stored in the database
     */
    public static Route[] getAllRoutes() {
        //Use try-with-resources to make code more concise
        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            String query = "SELECT * FROM routeinfo";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            // Questions I should ask myself: Is it giving 

            Route[] allRoutes = new Route[routeCount()];
            int i = 0;
            while (rs.next()) {
                allRoutes[i] = routeFromResultSet(rs);
            }

            return allRoutes;
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
            if (rs.next()) {
            // Holds the fields of the current Route being instantiated from db fields below
            long routeId = rs.getLong("routeid");
            String name = rs.getString("routename");
            String abbName = rs.getString("routenameabrv");
            String displayColor = rs.getString("hexcolor");
            System.out.println("routeid: " + routeId + " name: " + name);
            RouteSchedule schedule = RouteSchedule.decode(rs.getString("encodedschedule")); // TODO edit to reflect actual field name when determined
            int[] stopIds = Route.parseStopIdsString(rs.getString("stopidsordered"));

            HashMap<Long, Bus[]> activeBuses = BusData.getBusDataFromId(routeId);

            return new Route(routeId, name, abbName, displayColor, schedule, stopIds, activeBuses);
            }
            else {
                return null;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error: ", ex);
        }
    }
}