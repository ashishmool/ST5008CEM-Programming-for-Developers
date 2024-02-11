/*
Question 3 (a)

You are developing a student score tracking system that keeps track of scores from different assignments. The
ScoreTracker class will be used to calculate the median score from the stream of assignment scores. The class
should have the following methods:
         ScoreTracker() initializes a new ScoreTracker object.
         void addScore(double score) adds a new assignment score to the data stream.
         double getMedianScore() returns the median of all the assignment scores in the data stream. If the number
        of scores is even, the median should be the average of the two middle scores.

    Input:
    ScoreTracker scoreTracker = new ScoreTracker();
    scoreTracker.addScore(85.5); // Stream: [85.5]
    scoreTracker.addScore(92.3); // Stream: [85.5, 92.3]
    scoreTracker.addScore(77.8); // Stream: [85.5, 92.3, 77.8]
    scoreTracker.addScore(90.1); // Stream: [85.5, 92.3, 77.8, 90.1]
    double median1 = scoreTracker.getMedianScore(); // Output: 88.9 (average of 90.1 and 85.5)
    scoreTracker.addScore(81.2); // Stream: [85.5, 92.3, 77.8, 90.1, 81.2]
    scoreTracker.addScore(88.7); // Stream: [85.5, 92.3, 77.8, 90.1, 81.2, 88.7]
    double median2 = scoreTracker.getMedianScore(); // Output: 86.95 (average of 88.7 and 85.5)

*/

package assignment;

import java.util.TreeSet;

public class Q3A {

    private final TreeSet<Double> scores;

    public Q3A() {
        scores = new TreeSet<>();
    }

    public void addScore(double score) {
        scores.add(score);
    }

    public double getMedianScore() {
        if (scores.isEmpty()) {
            throw new IllegalStateException("No scores available.");
        }

        int size = scores.size();
        int middle = size / 2;

        if (size % 2 == 0) {
            // Average of two middle scores for even number of scores
            double[] midScores = scores.stream()
                    .skip(middle - 1)
                    .limit(2)
                    .mapToDouble(Double::doubleValue)
                    .toArray();

            return (midScores[0] + midScores[1]) / 2.0;
        } else {
            // Middle score for odd number of scores
            return scores.stream().skip(middle).findFirst().orElseThrow();
        }
    }

    public static void main(String[] args) {
        Q3A q3A = new Q3A();
        q3A.addScore(85.5);
        q3A.addScore(92.3);
        q3A.addScore(77.8);
        q3A.addScore(90.1);
        double median1 = q3A.getMedianScore();
        System.out.println("Median 1: " + median1);

        q3A.addScore(81.2);
        q3A.addScore(88.7);
        double median2 = q3A.getMedianScore();
        System.out.println("Median 2: " + median2);
    }
}
