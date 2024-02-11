package assignment.RecommendationSystem;

public class Main {
    public static void main(String[] args) {
        SocialGraph socialGraph = new SocialGraph();


        //Creation of a simple social graph with users and connections using Adjacency List

        // Adding users to the graph
        socialGraph.addUser(1, "Ashish Mool");
        socialGraph.addUser(2, "Rohan Manandhar");
        socialGraph.addUser(3, "Sushmita Bishwakarma");

        // Adding connections (edges)
        socialGraph.addConnection(1, 2); // Ashish Mool and Rohan Manandhar are connected
        socialGraph.addConnection(2, 3); // Rohan Manandhar and Sushmita Bishwakarma are connected

    }
}

