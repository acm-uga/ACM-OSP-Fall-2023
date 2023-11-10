package edu.uga.acm.osp.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.uga.acm.osp.ui.theme.BusAppTheme

/**
 * Creates one checkbox default unchecked. Not functional yet with preset parameters.
 *
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
            //.background(BusAppTheme.colors.onBackgroundSecondary),
            colors = CheckboxDefaults.colors(checkedColor = BusAppTheme.colors.accent,
                uncheckedColor = BusAppTheme.colors.onBackgroundSecondary,
                checkmarkColor = BusAppTheme.colors.onBackgroundPrimary
            )
        )

    }
}