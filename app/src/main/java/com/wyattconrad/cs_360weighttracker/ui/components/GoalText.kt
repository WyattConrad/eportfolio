/*
 * Copyright (C) 2025 Wyatt Conrad. All rights reserved.
 *
 * This file is part of the CS-360 Weight Tracker project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wyattconrad.cs_360weighttracker.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GoalText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = MaterialTheme.colorScheme.onSurface, // default
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    maxFontSize: TextUnit = 24.sp,  // largest font size
    minFontSize: TextUnit = 14.sp   // smallest font size
){
    // Track the width of the text
    var widthPx by remember { mutableStateOf(0) }

    // Convert px → dp → sp math using local density
    val density = LocalDensity.current
    // Calculate the font size based on the width of the text
    val fontSize = remember(widthPx) {

        if (widthPx > 0) {
            // convert px to dp
            with(density) {
                val widthDp = widthPx.toDp().value
                (widthDp / 20).coerceIn(minFontSize.value, maxFontSize.value).sp
            }
        } else {
            maxFontSize
        }
    }

    // Display the text with the calculated font size
    Text(
        text = text,
        fontSize = fontSize,
        color = color,
        style = style,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .onSizeChanged { widthPx = it.width },
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold
    )
}

@Preview(showBackground = true)
@Composable
fun GoalTextPreview() {

    GoalText(modifier = Modifier,
        "Your Goal Weight Is: 135.0 lbs")
}