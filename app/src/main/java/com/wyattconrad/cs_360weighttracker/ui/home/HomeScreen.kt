package com.wyattconrad.cs_360weighttracker.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wyattconrad.cs_360weighttracker.model.Weight
import com.wyattconrad.cs_360weighttracker.ui.theme.backgroundDark
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime
import java.time.ZoneOffset

@Composable
fun HomeScreen() {
    val weightEntries = remember { mutableStateListOf(
        Weight(141.0, LocalDateTime.now().minusDays(1).toEpochSecond(ZoneOffset.UTC)),
        Weight(142.0, LocalDateTime.now().minusDays(2).toEpochSecond(ZoneOffset.UTC)),
        Weight(143.0, LocalDateTime.now().minusDays(3).toEpochSecond(ZoneOffset.UTC)),
        Weight(144.0, LocalDateTime.now().minusDays(4).toEpochSecond(ZoneOffset.UTC)),
        Weight(145.0, LocalDateTime.now().minusDays(5).toEpochSecond(ZoneOffset.UTC))
    )}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Your Goal Weight Is: 135.0 lbs",
            fontSize = 18.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.Black)
                        .fillMaxWidth().padding(horizontal = 2.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Weight Lost:", color = Color.White)
                    Text("9.0", fontSize = 32.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    Text("lbs.", color = Color.White)
                }
            }

            Card(
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.Black)
                        .fillMaxWidth().padding(horizontal = 2.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Left To Goal:", color = Color.White)
                    Text("6.0", fontSize = 32.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    Text("lbs.", color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.fillMaxWidth().height(64.dp))

        Text(
            text = "Recorded Weights",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(weightEntries) { entry ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.Blue,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { /* edit action */ }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("${entry.weight} lbs.", modifier = Modifier.weight(1f))
                    Text(entry.dateTimeLogged.toKotlinLocalDateTime().toString(), modifier = Modifier.weight(2f))
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { /* delete action */ }
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}