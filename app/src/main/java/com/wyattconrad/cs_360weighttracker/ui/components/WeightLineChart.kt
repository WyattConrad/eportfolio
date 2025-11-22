package com.wyattconrad.cs_360weighttracker.ui.components

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
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
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import java.time.format.DateTimeFormatter


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
    val maxPoints = 100
    val recentWeights = weights
        .take(maxPoints)

    // Get the weight values
    val weightValues = recentWeights.map { it.weight }
    val spacedWeights = listSpacer(recentWeights, 4)


    // Create a date formatter
    val dateFormatter = DateTimeFormatter.ofPattern("M/d") // day/month format

    // Create a reversed list of formatted day/month labels
    val dateLabels: List<String> = spacedWeights
        .map { weight ->
            weight.dateTimeLogged.format(dateFormatter)
        }

    // Create the Line Chart
    LineChart(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 0.dp, bottom = 16.dp, start = 8.dp, end = 8.dp ),
        data = remember {
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
                    dotProperties = DotProperties(
                        enabled = true,
                        color = SolidColor(Color.Blue),
                        radius = 1.dp,
                    )
                ),
                Line(
                    label = "Trend",
                    values = trendValues,
                    color = SolidColor(Color.Green),
                    strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                    gradientAnimationDelay = 1000,
                    drawStyle = DrawStyle.Stroke(width = 2.dp),
                    dotProperties = DotProperties(
                        enabled = true,
                        color = SolidColor(Color.Green),
                        radius = 1.dp,
                    )
                )
            )
        },
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
