package edu.uga.acm.osp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Switch
import androidx.compose.material.Text
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


@Preview(showBackground = true)
@Composable
fun DisplayBox() {
    Card {
        Row (modifier = Modifier.padding(24.dp)) {
            Column(
                modifier = Modifier
                    .padding(bottom = 48.dp)
                    .padding(bottom = 48.dp)

            ) {
                Row {
                    Column{
                        Text(text = "Display Settings", fontWeight = FontWeight.ExtraBold)
                    }
                }
                Row {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Dark Mode: ")
                    }
                    Column{
                        toggleButton()
                    }
                }
                Row {
                    Column{
                        Text(text = "Distance Units: ")
                    }
                    Column{
                        DropdownDistance()
                    }

                }
                Row{
                    Column{
                        Text(text = "Time Format: ")
                    }
                    Column{
                        DropdownTime()
                    }
                }
                Row {
                    Column{
                        Text(text = "Language: ")
                    }
                    Column{
                        DropdownLanguage()
                    }
                }
            }
        }
    }
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

@Composable
fun DropdownDistance() {
    var expanded by remember { mutableStateOf(false) }
    val items = listOf("Miles/Feet", "Meters/Kilometers")
    val disabledValue = "B"
    var selectedIndex by remember { mutableStateOf(0) }
    Box(modifier = Modifier
        .wrapContentSize(Alignment.TopStart)) {
        Text(items[selectedIndex],modifier = Modifier
            .clickable(onClick = { expanded = true })
            .background(
                Color.LightGray
            ))
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
    }
}

@Composable
fun DropdownTime() {
    var expanded by remember { mutableStateOf(false) }
    val items = listOf("12-Hour Format", "24-Hour Format")
    val disabledValue = "B"
    var selectedIndex by remember { mutableStateOf(0) }
    Box(modifier = Modifier
        .wrapContentSize(Alignment.TopStart)) {
        Text(items[selectedIndex],modifier = Modifier
            .clickable(onClick = { expanded = true })
            .background(
                Color.LightGray
            ))
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
    }
}

@Composable
fun DropdownLanguage() {
    var expanded by remember { mutableStateOf(false) }
    val items = listOf("English")
    val disabledValue = "B"
    var selectedIndex by remember { mutableStateOf(0) }
    Box(modifier = Modifier
        .wrapContentSize(Alignment.TopStart)) {
        Text(items[selectedIndex],modifier = Modifier
            .clickable(onClick = { expanded = true })
            .background(
                Color.LightGray
            ))
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
    }
}