package assignment.RecommendationSystem;

import java.util.HashMap;
import java.util.Map;

public class SocialGraph {
    private Map<Integer, UserNode> userMap;

    // Constructor
    public SocialGraph() {
        this.userMap = new HashMap<>();
    }

    // Method to add a user to the graph
    public void addUser(int userId, String username) {
        if (!userMap.containsKey(userId)) {
            UserNode newUser = new UserNode(userId, username);
            userMap.put(userId, newUser);
        }
    }

    // Method to add a connection between two users
    public void addConnection(int userId1, int userId2) {
        UserNode user1 = userMap.get(userId1);
        UserNode user2 = userMap.get(userId2);

        if (user1 != null && user2 != null) {
            user1.addConnection(user2);
            user2.addConnection(user1);
        }
    }

    // Additional methods for updating user profiles, preferences, etc.
}
