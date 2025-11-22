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
package com.wyattconrad.cs_360weighttracker.service

import kotlin.math.roundToInt

// Function to select N evenly spaced items from a list
fun <T> listSpacer(list: List<T>, count: Int): List<T> {

    // If the list is short, return the whole thing
    if (list.size <= count) {
        return list
    }

    // Calculate the interval between each item
    val selectedItems = mutableListOf<T>()
    val interval = (list.size - 1).toFloat() / (count - 1)

    // Loop through the list and add the selected items to the list
    for (i in 0 until count) {
        val index = (i * interval).roundToInt()
        selectedItems.add(list[index])
    }

    // Return the list of selected items
    return selectedItems
}