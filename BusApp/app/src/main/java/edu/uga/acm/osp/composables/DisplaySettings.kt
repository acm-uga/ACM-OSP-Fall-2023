package edu.uga.acm.osp.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.uga.acm.osp.ui.theme.BusAppTheme
import edu.uga.acm.osp.ui.theme.BoydGray
import edu.uga.acm.osp.ui.theme.BulldogRed
import edu.uga.acm.osp.ui.theme.ChapelBellWhite

@Preview(showBackground = true)
@Composable
fun DisplayBox() {
    Card(
        modifier = Modifier
            .size(width = 500.dp, height = 400.dp)
            .padding(15.dp)
        //.paddingFromBaseline(50.dp, 50.dp)
    ) {
        Column(
            modifier = Modifier
                .height(400.dp)
                .background(color = BusAppTheme.colors.onBackgroundPrimary)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Row {
                Text(text = "Display", fontSize = 25.sp, fontWeight = FontWeight.Bold, modifier = Modifier
                    .offset(0.dp, -3.dp),
                    color = BusAppTheme.colors.onAccent
                )
            }
            Row (modifier = Modifier
                .height(30.dp)
            ){
                DefaultIcon(imageVector = Icons.Default.DarkMode, contentDescription = "Dark Mode")
                Text(text = "Dark Mode", fontSize = 15.sp, modifier = Modifier.offset(8.dp),
                    color = BusAppTheme.colors.onAccent)
                Spacer(modifier = Modifier.padding(90.dp))
                toggleButton()
            }
            Row (modifier = Modifier
                .height(30.dp)
            ) {
                DefaultIcon(imageVector = Icons.Default.Straighten, contentDescription = "Your Reminders")
                Text(text = "Distance Units", fontSize = 15.sp, modifier = Modifier.offset(8.dp),
                    color = BusAppTheme.colors.onAccent)
                Spacer(modifier = Modifier.padding(25.dp))
                DropdownGeneric(items = listOf("Miles/Feet", "Meters/Kilometers"))
            }

            Row (modifier = Modifier
                .height(30.dp)
            ) {
                DefaultIcon(imageVector = Icons.Default.AccessTime, contentDescription = "Your Reminders")
                Text(text = "Distance Units", fontSize = 15.sp, modifier = Modifier.offset(8.dp),
                    color = BusAppTheme.colors.onAccent)
                Spacer(modifier = Modifier.padding(25.dp))
                DropdownGeneric(items = listOf("12 Hour", "24 Hour"))
            }

            Row (modifier = Modifier
                .height(30.dp)
            ) {
                DefaultIcon(imageVector = Icons.Default.Language, contentDescription = "Your Reminders")
                Text(text = "Language", fontSize = 15.sp, modifier = Modifier.offset(8.dp),
                    color = BusAppTheme.colors.onAccent)
                Spacer(modifier = Modifier.padding(40.dp))
                DropdownGeneric(items = listOf("English"))
            }
        } // Col
    } // Card
} // NotifCom




@Composable
fun toggleButton() {
    var checked by remember { mutableStateOf(true) }

    Switch(
        checked = checked,
        onCheckedChange = {
            checked = it
        },
        colors = SwitchDefaults.colors(
            checkedThumbColor = BulldogRed,
            checkedTrackColor = ChapelBellWhite,
            uncheckedThumbColor = BulldogRed,
            uncheckedTrackColor = ChapelBellWhite
        )
    )
}
@Composable
fun DropdownGeneric(items: List<String>) {
    var expanded by remember { mutableStateOf(false) }
    val disabledValue = "B"
    var selectedIndex by remember { mutableStateOf(0) }
    Card(modifier = Modifier.size(width = 150.dp, height = 100.dp).padding(5.dp).fillMaxSize().wrapContentSize(Alignment.TopStart)) {
        Text(items[selectedIndex],modifier = Modifier.padding(10.dp, 0.dp).fillMaxWidth().clickable(onClick = { expanded = true }).background(
            BoydGray))
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.size(width = 150.dp, height = 120.dp).background(
                BoydGray)
        ) {
            items.forEachIndexed { index, s ->
                DropdownMenuItem(onClick = {
                    selectedIndex = index
                    expanded = false
                }) {
                    val disabledText = if (s == disabledValue) {
                        " (Disabled)"
                    } else {
                        ""
                    }
                    Text(text = s + disabledText)
                }
            }
        }
    }
}