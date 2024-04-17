package edu.uga.acm.osp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import edu.uga.acm.osp.R
import edu.uga.acm.osp.ui.theme.BusAppTheme
import edu.uga.acm.osp.ui.theme.ChapelBellWhite

/**
 * Header dislpayed on every screen.
 *
 * @param screenName the name of the current screen
 */
@Composable
fun Header(screenName: String) {
   Box(
       modifier = Modifier
           .requiredHeight(55.dp)
           .height(55.dp)
           .fillMaxWidth()
           .background(color = BusAppTheme.colors.accent, shape = RectangleShape)
   ) {
       Row(
           modifier = Modifier
               .padding(10.dp)
               .fillMaxWidth()
               .height(50.dp)
       ) {
           Image(
               painter = painterResource(id = R.drawable.acmatuga),
               contentDescription = null,
               modifier = Modifier.width(50.dp)
           )
           Text(
               text = screenName, color = ChapelBellWhite,
               modifier = Modifier
                   .height(23.dp)
                   .padding(horizontal = 5.dp)
                   .offset(y = 8.dp),
               style = TextStyle(fontFamily = FontFamily.SansSerif, textAlign = TextAlign.Center, fontWeight = FontWeight.ExtraBold)
           )
       }
   }
}