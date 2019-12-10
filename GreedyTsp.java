import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GreedyTsp {
    private static final int MAXIMUM = 99999999;

    private int nodes;
    private Stack<Integer> stack;

    private List<Integer> bestTour;

    public GreedyTsp() {
        stack = new Stack<Integer>();
        bestTour = new ArrayList<Integer>();
    }

    public int[] greedyTsp(int[][] matrix) {
        nodes = matrix[1].length - 1;
        int[] visited = new int[nodes + 1];
        visited[1] = 1;
        stack.push(1);
        int el, dst = 0, i;
        int min = MAXIMUM;
        boolean isMin = false;
        bestTour.add(0);

        while (!stack.isEmpty()) {
            el = stack.peek();
            i = 1;
            min = MAXIMUM;
            while (i <= nodes) {
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

}