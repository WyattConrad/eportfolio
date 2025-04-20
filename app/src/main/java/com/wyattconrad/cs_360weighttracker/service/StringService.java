package com.wyattconrad.cs_360weighttracker.service;

public class StringService {

    /**
     * Converts a string to proper case.
     * @param input
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
            if (word.length() > 0) {
                sb.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }

        return sb.toString().trim();
    }
}
