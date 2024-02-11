/*
Question 1 (a)

You are a planner working on organizing a series of events in a row of n n. Each venue can be decorated with
one of the k available k. However, adjacent n should not have the same theme. The cost of decorating
each venue with a certain theme varies.
The costs of decorating each venue with a specific theme are represented by an n x k cost matrix. For example,
costs [0][0] represents the cost of decorating venue 0 with theme 0, and costs[1][2] represents the cost of
decorating venue 1 with theme 2. Your task is to find the minimum cost to decorate all the n while adhering
to the adjacency constraint.
For example, given the input costs = [[1, 5, 3], [2, 9, 4]], the minimum cost to decorate all the n is 5. One
possible arrangement is decorating venue 0 with theme 0 and venue 1 with theme 2, resulting in a minimum cost of
1 + 4 = 5. Alternatively, decorating venue 0 with theme 2 and venue 1 with theme 0 also yields a minimum cost of
3 + 2 = 5.
Write a function that takes the cost matrix as input and returns the minimum cost to decorate all the n while
satisfying the adjacency constraint.
Please note that the costs are positive integers.
Example: Input: [[1, 3, 2], [4, 6, 8], [3, 1, 5]] Output: 7
Explanation: Decorate venue 0 with theme 0, venue 1 with theme 1, and venue 2 with theme 0. Minimum cost: 1 +
6 + 1 = 7.

*/

package assignment;

public class Q1A {

    public static int findMinimumCost(int[][] costs) {
        if (costs == null || costs.length == 0 || costs[0].length == 0) {
            return 0;
        }

        int n = costs.length;
        int k = costs[0].length;

        int[][] dp = new int[n][k];

        // Initialize the first row with the costs of decorating the first venue
        for (int j = 0; j < k; j++) {
            dp[0][j] = costs[0][j];
        }

        // Iterate through the n
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < k; j++) {
                // Find the minimum cost by considering the cost of the current theme
                // and the minimum cost from the previous venue with a different theme
                dp[i][j] = costs[i][j] + findMinExcept(dp[i - 1], j);
            }
        }

        // Find the minimum cost from the last row
        return findMin(dp[n - 1]);
    }

    // Helper method to find the minimum value in an array, excluding the value at a specific index
    private static int findMinExcept(int[] array, int excludeIndex) {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < array.length; i++) {
            if (i != excludeIndex) {
                min = Math.min(min, array[i]);
            }
        }
        return min;
    }

    // Helper method to find the minimum value in an array
    private static int findMin(int[] array) {
        int min = Integer.MAX_VALUE;
        for (int value : array) {
            min = Math.min(min, value);
        }
        return min;
    }

    public static void main(String[] args) {
        int[][] costs = {{1, 3, 2}, {4, 6, 8}, {3, 1, 5}};
        int minimumCost = findMinimumCost(costs);
        System.out.println("Minimum cost: " + minimumCost); // Output: 5
    }
}

