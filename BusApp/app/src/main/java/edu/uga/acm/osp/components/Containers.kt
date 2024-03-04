package edu.uga.acm.osp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.uga.acm.osp.ui.theme.BusAppTheme

@Preview
@Composable
fun BasicContainerPreview() {
    BasicContainer(
        containerHeader = "Header Text",
        context =
        {
            ContextInfo(
                contextText = "Context Name",
                contextIcon = Icons.Default.DirectionsBus,
                contextDesc ="Context icon description.",
                modifier = Modifier
            )
        }
    ) {
        TextButton(buttonText = "Example Content", buttonType = ButtonType.PRIMARY, onClick = {})
    }
}

@Preview
@Composable
fun InvisibleContainerPreview() {
    InvisibleContainer(containerHeader = "Header Text") {
        TextButton(buttonText = "Example Content", buttonType = ButtonType.PRIMARY, onClick = {})
    }
}

/**
 * Basic container that accepts header text, a context module, and content.
 *
 * @param containerHeader text to display as the container's header
 * @param context optional context module
 * @param content content to display in the container
 */
@Composable
fun BasicContainer(
    containerHeader: String, // Container header
    context: @Composable() (Modifier) -> Unit = {}, // Optional view all button, info, etc.
    content: @Composable() (Modifier) -> Unit // Container content
) {
    // Container shape
    Box(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, top = 15.dp, bottom = 15.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .background(BusAppTheme.colors.container)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(10.dp)
        ) {
            // Header and context
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "$containerHeader:",
                    style = BusAppTheme.typography.h2,
                    color = BusAppTheme.colors.onContainerPrimary,
                    textAlign = TextAlign.Left,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .graphicsLayer(translationY = -10f)
                        .weight(weight = 1f, fill = true)
                )

                // Contains the header "context"
                Box(
                    modifier = Modifier
                        .wrapContentWidth()
                        .weight(weight = .75f, fill = true)
                ) {
                    context(Modifier)
                }
            }
            // Content
            Column(
                modifier = Modifier.padding(top = 5.dp)
            ){
                content(Modifier.weight(weight = 1f, fill = true))
            }
        }
    }
}

/**
 * Invisible container for content groupings directly on page backgrounds.
 *
 * @param containerHeader text to display as the container's header
 * @param content the content to display inside the containers
 */
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
                    text = "$containerHeader:",
                    style = BusAppTheme.typography.h2,
                    color = BusAppTheme.colors.onContainerPrimary,
                    textAlign = TextAlign.Left,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            // Content
            Column(
                modifier = Modifier.padding(top = 5.dp)
            ){
                content(Modifier.weight(weight = 1f, fill = true))
            }
        }
    }
}