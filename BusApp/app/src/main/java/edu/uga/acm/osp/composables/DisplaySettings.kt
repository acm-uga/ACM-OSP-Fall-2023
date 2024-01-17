package edu.uga.acm.osp.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.uga.acm.osp.ui.theme.BusAppTheme
import edu.uga.acm.osp.composables.DefaultIcon


@Preview(showBackground = true)
@Composable
fun DisplayBox() {
    Card(modifier = Modifier
        .size(width = 500.dp, height = 300.dp)
        .padding(15.dp),
        backgroundColor = BusAppTheme.colors.onBackgroundPrimary)
    {
        Row (modifier = Modifier.padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Column(){
                Text(text = "Display", fontSize = 25.sp, fontWeight = FontWeight.Bold, modifier = Modifier
                    .offset(0.dp, -3.dp),
                    color = BusAppTheme.colors.onAccent
                )
                Row {
                    DefaultIcon(imageVector = Icons.Default.DarkMode, contentDescription = "Update dark mode scheme")
                    Text(text = "Dark Mode: ")
                }
                
                Text(text = "Distance Units: ")
                Text(text = "Time Format: ")
                Text(text = "Language: ")
            } // First Column
            Column(modifier = Modifier
                .padding(30.dp)
            ){
                toggleButton()
                DropdownGeneric(items = listOf("Miles/Feet", "Meters/Kilometers"))
                DropdownGeneric(items = listOf("12-Hour Format", "24-Hour Format"))
                DropdownGeneric(items = listOf("English"))
            } // Second column
        } // main Col
    } // Card
}
@Composable
fun toggleButton() {
    var checked by remember { mutableStateOf(true) }

    Switch(
        checked = checked,
        onCheckedChange = {
            checked = it
        }
    )
}
// Parameterized
@Composable
fun DropdownGeneric(items: List<String>) {
    var expanded by remember { mutableStateOf(false) }
    val disabledValue = "B"
    var selectedIndex by remember { mutableStateOf(0) }
    Box(modifier = Modifier
        .wrapContentSize(Alignment.TopStart))
    {
        Text(items[selectedIndex],modifier = Modifier
            .clickable(onClick = { expanded = true })
            .background(BusAppTheme.colors.onContainerSecondary)
            )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(
                    Color.LightGray
                )
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
    } // Card
}