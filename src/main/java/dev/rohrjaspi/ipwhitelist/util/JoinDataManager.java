package dev.rohrjaspi.ipwhitelist.util;

import java.util.HashMap;
import java.util.Map;

public class JoinDataManager {
    private final Map<String, JoinData> joinDataMap;

    // Constructor to initialize the map
    public JoinDataManager() {
        this.joinDataMap = new HashMap<>();
    }

    // Get or create join data for a specific IP address
    public JoinData getOrCreateJoinData(String ipAddress) {
        return joinDataMap.computeIfAbsent(ipAddress, k -> new JoinData());
    }

    // Inner class to manage join data for each IP address
    public static class JoinData {
        private int joinCount; // Count of how many times the player has joined
        private long firstJoinTime; // Timestamp of the first join within the tracking period

        // Constructor to initialize join data
        public JoinData() {
            this.joinCount = 0;
            this.firstJoinTime = System.currentTimeMillis(); // Set the current time as the first join time
        }

        // Update the join count and manage the time limit
        public void updateJoin() {
            long currentTime = System.currentTimeMillis();
            if (currentTime - firstJoinTime > 5 * 60 * 1000) { // If more than 5 minutes have passed since the first join
                // Reset the join count and update the first join time
                joinCount = 1;
                firstJoinTime = currentTime;
            } else {
                // Increment the join count if within the 5-minute window
                joinCount++;
            }
        }

        // Get the current join count
        public int getJoinCount() {
            return joinCount;
        }

        // Check if the join attempts are within the specified time limit
        public boolean isWithinLimit(long limit) {
            return System.currentTimeMillis() - firstJoinTime <= limit;
        }
    }
}