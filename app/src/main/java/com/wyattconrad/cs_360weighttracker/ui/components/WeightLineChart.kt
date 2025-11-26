package com.wyattconrad.cs_360weighttracker.ui.components

import android.webkit.ConsoleMessage
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wyattconrad.cs_360weighttracker.model.Weight
import com.wyattconrad.cs_360weighttracker.service.listSpacer
import com.wyattconrad.cs_360weighttracker.service.spacedIndexes
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import java.time.format.DateTimeFormatter
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight


@Composable
fun WeightLineChart(
    weights: List<Weight>,
    trendValues: List<Double>,
    modifier: Modifier = Modifier
) {

    /*val now = LocalDateTime.now()
    val currentMonth = now.monthValue  // 1 = January, 12 = December
    val currentYear = now.year

    val recentWeights = weights.filter { weight ->
        val dt = weight.dateTimeLogged
        dt.monthValue == currentMonth && dt.year == currentYear
    }*/
    val MAXPOINTS = 30

    val orderedWeights = remember(weights) { weights.sortedBy { it.dateTimeLogged } }

    val recentWeights = remember(orderedWeights){
        if (orderedWeights.size <= MAXPOINTS) orderedWeights else orderedWeights.takeLast(MAXPOINTS)
    }

    // Get the weight values
    val weightValues = remember(recentWeights) { recentWeights.map { it.weight } }
    // align trend values to the same window
    val alignedTrendValues = remember(trendValues, recentWeights) {
        // if trendValues is empty or shorter, pad with current values or zeros
        if (trendValues.isEmpty()) {
            List(weightValues.size) { 0.0 }
        } else {
            // takeLast to align with the recent window; if trend is larger, trim; if smaller, left-pad with zeros
            val tv = if (trendValues.size >= recentWeights.size) {
                trendValues.takeLast(recentWeights.size)
            } else {
                // pad on the left with zeros so lengths match
                List(recentWeights.size - trendValues.size) { 0.0 } + trendValues
            }
            tv
        }
    }


    // Create a date formatter
    val dateFormatter = DateTimeFormatter.ofPattern("M/d") // day/month format

    // Create ALL labels (one per point)
    val fullDateLabels = recentWeights.map { it.dateTimeLogged.format(dateFormatter) }

    // Determine which indices we want to label
    val labelIndexes = spacedIndexes(fullDateLabels.size, 5)  // 5 labels total

    // Build sparse label list (full length, empty where unused)
    val dateLabels = fullDateLabels.mapIndexed { index, label ->
        if (index in labelIndexes) label else " "  // empty = no label drawn
    }
    Log.d("WeightLineChart", "dateLabels: $dateLabels")
    // Create the Line list and remember it keyed on the inputs so it updates correctly
    val lines = remember(weightValues, alignedTrendValues) {
        listOf(
            Line(
                label = "Weight",
                values = weightValues,
                color = SolidColor(Color(0xFF0075C4)),
                firstGradientFillColor = Color(0xFF0075C4).copy(alpha = .5f),
                secondGradientFillColor = Color.Transparent,
                strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                gradientAnimationDelay = 1000,
                drawStyle = DrawStyle.Stroke(width = 2.dp),
                dotProperties = DotProperties(enabled = true, color = SolidColor(Color.Blue), radius = 1.dp)
            ),
            Line(
                label = "Trend",
                values = alignedTrendValues,
                color = SolidColor(Color.Green),
                strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                gradientAnimationDelay = 1000,
                drawStyle = DrawStyle.Stroke(width = 2.dp),
                dotProperties = DotProperties(enabled = true, color = SolidColor(Color.Green), radius = 1.dp)
            )
        )
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Header for the recorded weights list
        Text(
            text = "Recorded Weights",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Create the Line Chart
        LineChart(
            modifier = modifier
                .fillMaxSize()
                .padding(top = 0.dp, bottom = 16.dp, start = 8.dp, end = 8.dp),
            data = lines,
            animationMode = AnimationMode.Together(delayBuilder = {
                it * 500L
            }),
            labelProperties = LabelProperties(
                enabled = true,
                labels = dateLabels,
                textStyle = TextStyle(
                    fontSize = 11.sp,
                    color = Color.Black
                ),
                rotation = LabelProperties.Rotation(
                    mode = LabelProperties.Rotation.Mode.IfNecessary,
                    degree = -45f
                ),
            )
        )
    }
}
