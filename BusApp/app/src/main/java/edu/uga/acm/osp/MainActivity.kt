package edu.uga.acm.osp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import edu.uga.acm.osp.nav.HomeScreen
import edu.uga.acm.osp.nav.Navigation
import edu.uga.acm.osp.ui.theme.BusAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            ThemeChangeDemo()
        }
    }
}

// Purely a demo of changing colors based on System Theme,
// delete later
@Composable
fun ThemeChangeDemo() {
    Column(){
        Surface(
            contentColor = BusAppTheme.colors.onBackgroundPrimary
        ) {
            Text(
                text = "Hello UGA light",
                style = BusAppTheme.typography.h3
            )
        }
        Surface(
            contentColor = BusAppTheme.colors.container
        ) {
            Text(
                text = "Hello UGA dark",
                style = BusAppTheme.typography.h1
            )
        }
        Surface(
            contentColor = BusAppTheme.colors.onBackgroundSecondary
        ) {
            Text(
                text = "Hello UGA idk",
                style = BusAppTheme.typography.body1
            )
    }
}