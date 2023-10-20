package edu.uga.acm.osp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import edu.uga.acm.osp.ui.theme.BusAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            SurfaceDemo()
        }
    }
}

@Composable
fun SurfaceDemo() {
    Column{
        Surface(
            contentColor = BusAppTheme.colors.onBackgroundPrimary
        ) {
            Text("Hello World")
        }
        Surface(
            contentColor = BusAppTheme.colors.onPrimaryAction
        ) {
            Text("Hello World")
        }
    }
}