import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GreedyTsp {
    private static final int MAXIMUM = 99999999;

    private int nodes;
    private Stack<Integer> stack;

    private List<Integer> bestTour;

    private int cost;

    public GreedyTsp() {
        stack = new Stack<Integer>();
        bestTour = new ArrayList<Integer>();
    }

    public int[] greedyTsp(int[][] matrix) {
        nodes = matrix[1].length;
        int[] visited = new int[nodes];
        visited[0] = 1;
        stack.push(1);
        int el, dst = 0, i;
        int min = MAXIMUM;
        boolean isMin = false;
        // bestTour.add(0);

        while (!stack.isEmpty()) {
            el = stack.peek();
            i = 0;
            min = MAXIMUM;
            while (i < nodes) {
                if (matrix[el][i] > 1 && visited[i] == 0) {
                    if (min > matrix[el][i]) {
                        min = matrix[el][i];
                        dst = i;
                        isMin = true;
                    }
                }
                i++;
            }
            if (isMin) {
                visited[dst] = 1;
                stack.push(dst);
                bestTour.add(dst - 1);
                isMin = false;
                continue;
            }
            stack.pop();
        }

        int[] arr = new int[bestTour.size()];

        for (int x = 0; x < bestTour.size(); x++) {
            if (bestTour.get(x) != null) {
                arr[x] = bestTour.get(x);
            }
        }

        return arr;
    }

    public int computeTourCost(int[] tour, int[][] matrix) {
        int cost = 0;

        // compute the cost of going everywhere else
        for (int i = 1; i < tour.length; i++) {
            int from = tour[i - 1];
            int to = tour[i];
            cost += matrix[from][to];
        }

        // compute back to the starting node
        int temp = 0;
        if (tour.length >= 1) {
            temp = tour.length - 1;
        }
        int last = tour[temp];
        int first = tour[0];
        return cost + matrix[last][first];
    }

}