package edu.uga.acm.osp.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.uga.acm.osp.components.IconListItem
import edu.uga.acm.osp.components.LabelListItem
import edu.uga.acm.osp.ui.theme.BoydGray

@Composable
fun HomeCard(cardName: String, stop: List<String>, desc: List<String>, badge: List<String>, routes: List<Boolean>) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Card(
            modifier = Modifier
                .size(width = 500.dp, height = 340.dp)
                .padding(15.dp)
            //.paddingFromBaseline(50.dp, 50.dp)
        ) {
            Column(
                modifier = Modifier
                    .height(400.dp)
                    .background(color = BoydGray)
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Row {
                    Text(
                        text = cardName,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .offset(0.dp, -3.dp),
                    )
                }

                if (routes[0]) {
                    Row {
                        LabelListItem(
                            header = stop[0],
                            subheader = desc[0],
                            badgeLabel = badge[0]
                        )
                    }
                } else {
                    Row {
                        IconListItem(
                            header = stop[0],
                            subheader = desc[0],
                            badgeIcon = Icons.Default.LocationOn,
                            badgeIconDesc = "Location"
                        )
                    }
                }

                if (routes[1]) {
                    Row {
                        LabelListItem(
                            header = stop[1],
                            subheader = desc[1],
                            badgeLabel = badge[1])
                    }
                } else {
                    Row {
                        IconListItem(
                            header = stop[1],
                            subheader = desc[1],
                            badgeIcon = Icons.Default.LocationOn,
                            badgeIconDesc = "Location"
                        )
                    }
                }

                if (routes[2]) {
                    Row {
                        LabelListItem(
                            header = stop[2],
                            subheader = desc[2],
                            badgeLabel = badge[2])
                    }
                } else {
                    Row {
                        IconListItem(
                            header = stop[2],
                            subheader = desc[2],
                            badgeIcon = Icons.Default.LocationOn,
                            badgeIconDesc = "Location"
                        )
                    }
                }
            }
        }
    }
}