import java.util.Arrays;
import java.util.Random;

class Runner {

    public static void main(String[] args) {

        TSP tsp = new TSP();

        Random rand = new Random();

        int n = 10;
        int[][] matrix = new int[n][n];
        for (int[] row : matrix)
            java.util.Arrays.fill(row, 100);

        // Make this be random for a better check
        // Construct an optimal tour
        int edgeCost = 5;
        int[] optimalTour = { 2, 7, 6, 1, 9, 8, 5, 3, 4, 0, 2 };
        for (int i = 1; i < optimalTour.length; i++)
            matrix[optimalTour[i - 1]][optimalTour[i]] = edgeCost;

        int[] bestTour = tsp.bruteForce(matrix);
        System.out.println(java.util.Arrays.toString(bestTour));

        double tourCost = tsp.computeTourCost(bestTour, matrix);
        System.out.println("Tour cost: " + tourCost);

        GreedyTsp nearestGreedyTsp = new GreedyTsp();

        int[][] greedyMatrix = new int[n + 1][n + 1];

        for (int[] row : greedyMatrix)
            java.util.Arrays.fill(row, 100);

        // fill greedy matrix with values up to random.
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                int temp = rand.nextInt(50);
                greedyMatrix[i][j] = temp;
                greedyMatrix[j][i] = temp;
            }
        }

        System.out.println("Greedy best path...");
        int[] greedyBest = nearestGreedyTsp.greedyTsp(greedyMatrix);

        System.out.println(java.util.Arrays.toString(greedyBest));
    }
}