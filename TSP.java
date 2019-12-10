public class TSP {
    private static final int MAXIMUM = 99999999;

    public int[] bruteForce(int[][] matrix) {
        int length = matrix.length;

        int[] permutation = new int[length];

        for (int i = 0; i < length; i++)
            permutation[i] = i;

        int[] fastestTour = permutation.clone();
        int fastestTourCost = MAXIMUM;

        // try all tours.
        do {
            int tourCost = computeTourCost(permutation, matrix);

            if (tourCost < fastestTourCost) {
                fastestTourCost = tourCost;
                fastestTour = permutation.clone();
            }
        } while (nextPermutation(permutation));

        return fastestTour;
    }

    public int computeTourCost(int[] tour, int[][] matrix) {
        int cost = 0;

        // compute the cost of going everywhere else
        for (int i = 1; i < matrix.length; i++) {
            int from = tour[i - 1];
            int to = tour[i];
            cost += matrix[from][to];
        }

        // compute back to the starting node
        int last = tour[matrix.length - 1];
        int first = tour[0];
        return cost + matrix[last][first];
    }

    public boolean nextPermutation(int[] sequence) {
        int first = getFirst(sequence);
        if (first == -1)
            return false;
        int toSwap = sequence.length - 1;
        while (sequence[first] >= sequence[toSwap])
            --toSwap;
        swap(sequence, first++, toSwap);
        toSwap = sequence.length - 1;
        while (first < toSwap)
            swap(sequence, first++, toSwap--);
        return true;
    }

    private int getFirst(int[] sequence) {
        for (int i = sequence.length - 2; i >= 0; --i)
            if (sequence[i] < sequence[i + 1])
                return i;
        return -1;
    }

    private void swap(int[] sequence, int i, int j) {
        int tmp = sequence[i];
        sequence[i] = sequence[j];
        sequence[j] = tmp;
    }

}