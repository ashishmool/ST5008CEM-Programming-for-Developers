package assignment.RecommendationSystem;

import java.util.ArrayList;
import java.util.List;

public class UserNode {
    private int userId;
    private String username;
    private List<UserNode> connections;
    private List<String> interests;
    private List<String> preferences;
    private List<Interaction> interactionHistory;

    // Constructor
    public UserNode(int userId, String username) {
        this.userId = userId;
        this.username = username;
        this.connections = new ArrayList<>();
        this.interests = new ArrayList<>();
        this.preferences = new ArrayList<>();
        this.interactionHistory = new ArrayList<>();
    }

    // Method to add a connection (friend/follower)
    public void addConnection(UserNode friend) {
        connections.add(friend);
    }

    // Method to add user interests
    public void addInterest(String interest) {
        interests.add(interest);
    }

    // Method to add user preferences
    public void addPreference(String preference) {
        preferences.add(preference);
    }

    // Method to record an interaction
    public void recordInteraction(UserNode otherUser, InteractionType type) {
        Interaction interaction = new Interaction(otherUser, type);
        interactionHistory.add(interaction);
    }

    // Inner class to represent interactions
    private static class Interaction {
        private UserNode otherUser;
        private InteractionType type;

        public Interaction(UserNode otherUser, InteractionType type) {
            this.otherUser = otherUser;
            this.type = type;
        }
    }

    // Enum to represent types of interactions (e.g., like, comment, share)
    public enum InteractionType {
        LIKE,
        COMMENT,
        SHARE
    }

}
