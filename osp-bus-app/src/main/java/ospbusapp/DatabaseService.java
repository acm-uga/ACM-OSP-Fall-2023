package ospbusapp;

import routeSchedule.RouteSchedule;

import java.sql.*;
import java.util.*;

public class DatabaseService {
    //Add to properties file for security
    private static final String url = "jdbc:mysql://localhost:3306/OSPDB";
    private static final String username = "root";
    private static final String password = "Password123";

    // TODO deprecate this in lieu of below
    public static String getStopInfo(long stopID) {
        String stopName = "ERROR";

        //Use try-with-resources to make code more concise
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connected!"); //DEBUG

            String query = "SELECT * FROM stopinfo WHERE stopid = " + stopID;
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                stopName = rs.getString("stopname");
                stopName = stopName + " " + rs.getString("type_info");
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error: ", ex);
        }

        return stopName;
    }

    public static Stop getStop(long stopId) {
        // Retrieve all Stop fields from database by querying with stopId
        // TODO add db query for corresponding Stop record and parse response to replace placeholder values below
        String name = "Placeholder Name";
        Stop.Type type = Stop.Type.TEST_TYPE;
        Double latitude = 123.45;
        Double longitude = 678.90;
        long[] servesRouteIds = {12, 34, 56, 78, 90};

        return new Stop(stopId, name, type, latitude, longitude, servesRouteIds);
    }

    // TODO deprecate this in lieu of below
    public static List<String> getRouteInfo(long routeID) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            List<String> stops = new ArrayList<>();

            //Need to parameterize query to prevent possible SQL injection?
            String query = "SELECT * FROM routeinfo WHERE routeid = " + routeID;
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                String stop = rs.getString("routename");
                stop += ";" + rs.getString("routenameabrv");
                stop += ";" + rs.getString("stopidsordered");
                stop += ";" + rs.getString("hexcolor");

                stops.add(stop);
            }

            return stops;
        } catch (SQLException ex) {
            throw new RuntimeException("Error: ", ex);
        }
    }

    public static Route getRoute(long routeId) {
        // Retrieve all Route fields from database by querying with routeId
        // TODO add db query for corresponding Route record and parse response to replace placeholder values below
        String name = "Placeholder Name";
        String abbName = "PN";
        String displayColor = "0xFFFFFF";
        RouteSchedule schedule = RouteSchedule.decode("f;s{M-F:0630-1945;}|u{M-F:0700-1900;}|i{M-F:0630-1945;}");
        long[] stopIds = {12, 34, 56, 78, 90};

        return new Route(routeId, name, abbName, displayColor, schedule, stopIds);
    }

    //Calls the FinalCalculate stored procedure to get nearby stops for the specified location
    //Question: how do we want to return this data? Just as a list of strings?
    public static List<String> getNearbyStops(double latitude, double longitude, int numOfStops) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            List<String> nearbyStops = new ArrayList<>();

            //Need to parameterize query to prevent possible SQL injection?
            String query = "CALL FinalCalculate(" + latitude + ", " + longitude + ", " + numOfStops + ")";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                String stop = rs.getString("stopid");
                stop += ";" + rs.getString("latitude");
                stop += ";" + rs.getString("longitude");
                stop += ";" + rs.getString("distance");

                nearbyStops.add(stop);
            }

            return nearbyStops;
        } catch (SQLException ex) {
            throw new RuntimeException("Error: ", ex);
        }
    }
}