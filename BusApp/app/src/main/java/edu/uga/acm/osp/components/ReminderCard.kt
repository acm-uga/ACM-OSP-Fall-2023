package edu.uga.acm.osp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.uga.acm.osp.components.ButtonType
import edu.uga.acm.osp.components.IconListItem
import edu.uga.acm.osp.components.LabelListItem
import edu.uga.acm.osp.components.TextButton
import edu.uga.acm.osp.ui.theme.BoydGray
import edu.uga.acm.osp.ui.theme.ChapelBellWhite
import edu.uga.acm.osp.ui.theme.GloryGloryRed
import edu.uga.acm.osp.ui.theme.TripleGray

@Composable
fun ReminderCard(cardName: String, stop: List<String>, desc: List<String>, badge: List<String>, routes: List<Boolean>) {

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

                LazyColumn (
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(routes.size) { index ->
                        if (routes[index]) {
                            Row {
                                LabelListItem(
                                    header = stop[index],
                                    subheader = desc[index],
                                    badgeLabel = badge[index]
                                )
                            }
                        } else {
                            Row {
                                IconListItem(
                                    header = stop[index],
                                    subheader = desc[index],
                                    badgeIcon = Icons.Default.LocationOn,
                                    badgeIconDesc = "Location"
                                )
                            }
                        }
                    }

                    item {
                        Row {
                            Spacer(modifier = Modifier.padding(20.dp))
                            TextButton(
                                buttonText = "Import Trip",
                                buttonType = ButtonType.SECONDARY,
                                onClick = {}
                            )
                            TextButton(
                                buttonText = "Add Reminder",
                                buttonType = ButtonType.PRIMARY,
                                onClick = {}
                            )
                            Spacer(modifier = Modifier.padding(20.dp))
                        }
                    }
                }
            }
        }
    }
}