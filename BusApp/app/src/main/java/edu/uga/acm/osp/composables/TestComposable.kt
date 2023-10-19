package edu.uga.acm.osp.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.uga.acm.osp.R

@Composable
fun TestComposable() {

    val description = "I am a test description, with the only purpose of being discarded after a" +
            " better version is implemented :("


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
            .background(color = Color.Gray)
    ) {
        Text(
            text = "This is a Test Title!",
            color = Color.Red,
            modifier = Modifier
                .fillMaxWidth()
                .padding(50.dp)
                .height(50.dp)
                .border(shape = RectangleShape, color = Color.Black, width = 2.dp)
                .padding(8.dp)
        )
        Box(modifier = Modifier
            .padding(3.dp),
            contentAlignment = Alignment.BottomCenter) {
            Text(text = description, style = TextStyle(color = Color.White, fontFamily = FontFamily.SansSerif, fontSize = 16.sp),
                modifier = Modifier.padding(horizontal = 10.dp),
                textAlign = TextAlign.Center)
        }
        Row(modifier = Modifier
            .padding(10.dp)
            .width(width = 120.dp)
            .border(width = 1.dp, shape = CircleShape, color = Color.Black)
        ) {
            val painter = painterResource(id = R.drawable.baseline_copyright_24)
            Image(painter = painter, contentDescription = "Copyright")
            Column(modifier = Modifier.padding(2.dp)) {
                Text(text = "Cason Pittman", style = TextStyle(color = Color.White, fontSize = 12.sp))
                Text(text = "Dat boi", style = TextStyle(color = Color.Magenta, fontSize = 8.sp, textDecoration = TextDecoration.Underline),
                    textAlign = TextAlign.End,
                    modifier = Modifier.rotate(degrees = 180f)
                )
            }
        }
    }
}