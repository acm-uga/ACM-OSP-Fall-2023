package edu.uga.acm.osp.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxColors
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontWeight
// Not used but could be applied for optimization
var titles = listOf("Notifications", "Status", "Remind")
val height = 225.sp

/**
 * Composes the entire Notification tab.
 */
@Preview(showBackground = false)
@Composable
fun NotificationComposable() {
    Card(
        modifier = Modifier
            .size(width = 500.dp, height = 220.dp)
            .padding(15.dp)
            //.paddingFromBaseline(50.dp, 50.dp)
    ) {
        Column(
            modifier = Modifier
                .height(400.dp)
                .background(color = Color.White)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Row {
                Text(text = "Notifications", fontSize = 25.sp, fontWeight = FontWeight.Bold, modifier = Modifier
                    .offset(0.dp, -3.dp)
                )
                //Icon(Icons.iconCategory.iconName
                Row (
                    modifier = Modifier
                        .offset(25.dp, 5.dp)
                ){
                    DefaultIcon(imageVector = Default.Phone, contentDescription = "SMS Notifications")
                    Spacer(modifier = Modifier.padding(17.dp))
                    DefaultIcon(imageVector = Default.Email, contentDescription = "Email Notifications")
                    Spacer(modifier = Modifier.padding(17.dp))
                    //Need a better icon for APP
                    DefaultIcon(imageVector = Default.Person, contentDescription = "App Notifications")
                }

            }
            Row (modifier = Modifier
                .height(30.dp)
            ){
                DefaultIcon(imageVector = Default.ArrowForward, contentDescription = "Status Updates")
                Text(text = "Status Updates", fontSize = 15.sp, modifier = Modifier.offset(8.dp))
                Spacer(modifier = Modifier.padding(15.dp))
                CheckboxLabelled()
                Spacer(modifier = Modifier.padding(5.dp))
                CheckboxLabelled()
                Spacer(modifier = Modifier.padding(5.dp))
                CheckboxLabelled()
            }
            Row (modifier = Modifier
                .height(30.dp)
            ) {
                DefaultIcon(imageVector = Default.Notifications, contentDescription = "Your Reminders")
                Text(text = "Your Reminders", fontSize = 15.sp, modifier = Modifier.offset(8.dp))
                Spacer(modifier = Modifier.padding(12.5.dp))
                CheckboxLabelled()
                Spacer(modifier = Modifier.padding(5.dp))
                CheckboxLabelled()
                Spacer(modifier = Modifier.padding(5.dp))
                CheckboxLabelled()
            }

        } // Col
    } // Card
} // NotifCom

/**
 * Creates one checkbox that can toggle b/w checked and unchecked. Not functional as of now.
 */
@Preview
@Composable
fun CheckboxLabelled() {
    val checked = remember { mutableStateOf(false) }
    //TODO("Be able to take in preset parameters concerning checkboxes(ie if user
    // enabled text notifications")
    Column (
        verticalArrangement = Arrangement.SpaceEvenly
    ){
        /*Text("Label for checkbox")
        Spacer(modifier = Modifier.padding(5.dp)) */
        Checkbox(
            checked = checked.value,
            onCheckedChange = { isChecked -> checked.value = isChecked },
            modifier = Modifier.offset(0.dp, -5.dp),
            colors = CheckboxDefaults.colors(Color.Red)
            // Can change color with: colors =
        )

    }
}

/**
 * Creates an icon based on inputs.
 *
 * @param imageVector refers to the imagine in the default package
 * @param contentDescription supplies the accessibility caption
 */
@Composable
fun DefaultIcon(imageVector: ImageVector, contentDescription: String) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        tint = Color.Black
    )
}
