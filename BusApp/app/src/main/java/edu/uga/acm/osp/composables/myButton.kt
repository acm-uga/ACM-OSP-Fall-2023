package edu.uga.acm.osp.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import edu.uga.acm.osp.nav.Screen
import edu.uga.acm.osp.ui.theme.*

//How to use: myButton(text = "Cason", int = 5, navController = navController)
//myButton(text = "Cason", int = 5, navController = navController,color = Color.Red,route = Screen.SearchScreen.route)
//myButton(text = "Kelsey", int = 2, navController = navController,color = Color.Cyan, route = Screen.SettingScreen.route)
//
/**
 * Custom myButton which takes in multiple customization parameters, displays how
 * to use parameters to change multiple instances of the same composable
 * @param text The text of the button
 * @param int The number on button
 * @param navController The navController
 * @param color The background color
 * @param route route to Screen
 */
@Composable
fun myButton(text: String,int: Int,navController: NavController,color: Color,route:String) {
    Box(modifier = Modifier.fillMaxWidth().background(color = color),
        contentAlignment = Alignment.Center,
        ) {
        Button(onClick = {
            navController.navigate(route)
        },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(20.dp)
                .border(width = 3.dp, color = Color.Black)
                .background(color = Color.Gray)
        ) {
            Text(text = "Hi, $text $int", color = Purple200, fontSize = 20.sp)
        }
    }

}