package edu.uga.acm.osp.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.uga.acm.osp.ui.theme.BulldogRed

@Preview
@Composable
fun Header() {
   Row(modifier = Modifier
       .requiredHeight(50.dp)
       .fillMaxWidth()
       .background(color = BulldogRed, shape = RectangleShape)
   ) {
    Text(text = "Cason", modifier = Modifier.padding(15.dp))
   }
}