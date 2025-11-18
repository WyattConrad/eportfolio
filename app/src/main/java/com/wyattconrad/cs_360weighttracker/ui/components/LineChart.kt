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
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Composable
fun WeightLineChart(
    weights: List<Weight>
) {

    val now = LocalDateTime.now()
    val currentMonth = now.monthValue  // 1 = January, 12 = December
    val currentYear = now.year

    val currentMonthWeights = weights.filter { weight ->
        val dt = weight.dateTimeLogged
        dt.monthValue == currentMonth && dt.year == currentYear
    }

    // Get the weight values and reverse them (since they are in descending order by date logged)
    val weightValues = currentMonthWeights.reversed().map { it.weight }


    val dateFormatter = DateTimeFormatter.ofPattern("M/d") // day/month format

    // Create a reversed list of formatted day/month labels
    val dateLabels: List<String> = currentMonthWeights
        .reversed() // reverse to match reversed weightValues
        .map { weight ->
            weight.dateTimeLogged.format(dateFormatter)
        }

    // Create the Line Chart
    LineChart(
        modifier = Modifier.fillMaxSize().padding(horizontal = 22.dp),
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
                        color = SolidColor(Color.White),
                        strokeWidth = 1.dp,
                        radius = 1.dp,
                        strokeColor = SolidColor(Color.Black),
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
                mode = LabelProperties.Rotation.Mode.Force,
                degree = -45f
            ),
        )
    )
}
