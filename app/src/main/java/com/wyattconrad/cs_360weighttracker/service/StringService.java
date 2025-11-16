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
package com.wyattconrad.cs_360weighttracker.service;

public class StringService {

    /**
     * Converts a string to proper case.
     * @param input The string to convert
     * @return String in proper case format
     */
    public static String toProperCase(String input) {
        // Handle null or empty input
        if (input == null || input.isEmpty()) {
            return input;
        }

        // Split the input string into words if necessary
        String[] words = input.toLowerCase().split(" ");
        StringBuilder sb = new StringBuilder();

        // Convert each word to proper case
        for (String word : words) {
            // Capitalize the first letter of each word
            if (!word.isEmpty()) {
                sb.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }

        return sb.toString().trim();
    }
}
