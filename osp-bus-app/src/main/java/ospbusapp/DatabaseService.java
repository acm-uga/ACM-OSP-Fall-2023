package ospbusapp;

import java.sql.*;

public class DatabaseService {
    //Add to properties file for security
    private static final String url = "jdbc:mysql://localhost:3306/OSPDB";
    private static final String username = "root";
    private static final String password = "Password123";

    public static String getStopInfo(long stopID) {
        String stopName = "ERROR";

        //Use try-with-resources to make code more concise
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connected!");

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

    public static String getRouteInfo(long routeID) {
        String stopName = "ERROR";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT * FROM routeinfo WHERE routeid = " + routeID;
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                stopName = rs.getString("routename");
                stopName = stopName + ";" + rs.getString("routenameabrv");
                stopName = stopName + ";" + rs.getString("stopidsordered");
                stopName = stopName + ";" + rs.getString("hexcolor");

            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error: ", ex);
        }

        return stopName;
    }

    //Add method to call Calculate procedure on SQL...
}

