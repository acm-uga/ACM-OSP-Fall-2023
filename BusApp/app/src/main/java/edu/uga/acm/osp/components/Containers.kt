package edu.uga.acm.osp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import edu.uga.acm.osp.ui.theme.BusAppTheme

// Basic container that accepts a header, content, and context composable
@Composable
fun BasicContainer(
    containerHeader: String, // Container header
    context: @Composable (Modifier) -> Unit = {}, // Optional view all button, info, etc.
    contentModifier: Modifier = Modifier, // Optional Modifier
    content: @Composable (Modifier) -> Unit, // Container content
) {
    // Container shape
    Box(
        modifier = Modifier
            .padding(5.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .background(BusAppTheme.colors.container)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(bottom = 10.dp)
        ) {
            // Header and context
            Row() {
                Text(
                    text = containerHeader,
                    style = BusAppTheme.typography.h2,
                    color = BusAppTheme.colors.onContainerPrimary,
                    textAlign = TextAlign.Left,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(start = 10.dp, top = 10.dp)
                        .graphicsLayer(translationY = -10f)
                )
                context
            }
            // Content
            Column(verticalArrangement = Arrangement.spacedBy(15.dp), modifier = Modifier
                .padding(top = 10.dp)
            ){
                content(contentModifier.weight(weight = 1f, fill = true))
            }

            //content(contentModifier.weight(weight = 1f, fill = true))
        }
    }
}

@Composable
fun InvisibleContainer(
    containerHeader: String,
    content: @Composable (Modifier) -> Unit) {
    Box(modifier = Modifier
        .padding(5.dp)
        .background(Color(0x00FFFFFF))
        .fillMaxWidth()
        .wrapContentHeight()
    )
    {
        Column(){
            Row(){
                Text(
                    text = containerHeader,
                    style = BusAppTheme.typography.h2,
                    color = BusAppTheme.colors.onContainerPrimary,
                    textAlign = TextAlign.Left,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis)
            }
            // Content
            content(Modifier
                    .weight(weight = 1f, fill = true)
                    .padding(bottom = 10.dp))
        }
    }
}