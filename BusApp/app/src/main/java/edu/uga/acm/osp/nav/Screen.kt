package edu.uga.acm.osp.nav

sealed class Screen(val route: String) {
    object HomeScreen : Screen("home_screen");
    object SearchScreen : Screen("search_screen");
    object AlertScreen : Screen("alert_screen");
    object PlannerScreen: Screen("planner_screen");
    object SettingsScreen: Screen("settings_screen");
    object StopInfoScreen: Screen("stop_info_screen")
    object RouteInfoScreen: Screen("route_info_screen");
    object StopsScreen: Screen("stops_screen");
    object RoutesScreen: Screen("routes_screen");
    //more screens later


    fun withArgs(vararg args: String) : String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}