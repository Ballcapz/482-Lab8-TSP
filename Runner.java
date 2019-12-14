import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Collection;
import java.util.Collections;
import java.util.*;
import java.util.Random;

import genome.GeneticTSP;
import genome.Salesman;

class Runner {

    static ThreadMXBean bean = ManagementFactory.getThreadMXBean();

    /* define constants */
    static long MAXVALUE = 2000000000;
    static long MINVALUE = -2000000000;
    private static int numberOfTrials = 50;
    private static int MAXINPUTSIZE = 12;
    // (int) Math.pow(2, 14);
    private static int MININPUTSIZE = 2;
    private static int SIZEINCREMENT = 1;

    private static String ResultsFolderPath = "C:\\Users\\Zach\\Documents\\School\\Fall 2019\\482 Algorithms\\lab8_logs\\";

    private static FileWriter resultsFile;
    private static PrintWriter resultsWriter;

    static Random rand = new Random();

    public static void main(String[] args) {
        // runFullExperiment("bruteForce-1.txt");
        // runFullExperiment("bruteForce-2.txt");
        // runFullExperiment("bruteForce-3.txt");

        int[][] mat = generateCircular(6, 10);

        PrintMatrixNicely(mat);

    }

    private static void runFullExperiment(String resultsFileName) {
        try {
            resultsFile = new FileWriter(ResultsFolderPath + resultsFileName);
            resultsWriter = new PrintWriter(resultsFile);

        } catch (Exception e) {

            System.out.println(
                    "*****!!!!!  Had a problem opening the results file " + ResultsFolderPath + resultsFileName);
            return; // not very foolproof... but we do expect to be able to create/open the file...

        }

        // set up the class to be used
        TSP tsp = new TSP();
        ThreadCpuStopWatch TrialStopwatch = new ThreadCpuStopWatch(); // for timing an individual trial

        resultsWriter.println("#InputSize    AverageTime        DoublingRatio"); // # marks a comment in gnuplot data

        resultsWriter.flush();

        /*
         * for each size of input we want to test: in this case starting small and
         * doubling the size each time
         */
        // double[] timeRatios;
        double previousTime = 0;

        for (int inputSize = MININPUTSIZE; inputSize <= MAXINPUTSIZE; inputSize += SIZEINCREMENT) {

            // progress message...

            System.out.println("Running test for input size " + inputSize + " ... ");

            /* repeat for desired number of trials (for a specific size of input)... */

            long batchElapsedTime = 0;

            /*
             * force garbage collection before each batch of trials run so it is not
             * included in the time
             */

            System.out.println("Collecting the trash...");
            System.gc();

            // instead of timing each individual trial, we will time the entire set of
            // trials (for a given input size)

            // and divide by the number of trials -- this reduces the impact of the amount
            // of time it takes to call the

            // stopwatch methods themselves

            // BatchStopwatch.start(); // comment this line if timing trials individually

            int[] results;

            // run the trials
            System.out.println("Timing Each sort individually wo gc every time forced...");
            System.out.print("    Starting trials for input size " + inputSize + " ... ");
            for (long trial = 0; trial < numberOfTrials; trial++) {
                // Set up the matrix to be used
                int[][] matrix = generateRandomMatrix(inputSize);
                TrialStopwatch.start(); // *** uncomment this line if timing trials individually

                /* run the function we're testing on the trial input */

                ///////////////////////////////////////////
                /* DO BIDNESS */
                /////////////////////////////////////////
                results = tsp.bruteForce(matrix);
                ///////////////////////////////////////////
                /* END DO BIDNESS */
                /////////////////////////////////////////

                batchElapsedTime = batchElapsedTime + TrialStopwatch.elapsedTime(); // *** uncomment this line if timing
                                                                                    // trials individually

            }

            // batchElapsedTime = BatchStopwatch.elapsedTime(); // *** comment this line if
            // timing trials individually

            double averageTimePerTrialInBatch = (double) batchElapsedTime / (double) numberOfTrials;
            double doublingRatio = 0;
            if (previousTime > 0) {
                doublingRatio = averageTimePerTrialInBatch / previousTime;
            }

            previousTime = averageTimePerTrialInBatch;
            /* print data for this size of input */

            resultsWriter.printf("%12d  %18.2f %18.1f\n", inputSize, averageTimePerTrialInBatch, doublingRatio);

            resultsWriter.flush();

            System.out.println(" ....done.");

        }
    }

    private static int[][] generateRandomMatrix(int n) {

        int[][] matrix = new int[n][n];

        for (int i = 1; i < n; i++) {
            for (int j = 1; j <= i; j++) {
                if (i == j) {
                    matrix[i][j] = 0;
                } else {
                    int temp = rand.nextInt(50);
                    matrix[i][j] = temp;
                    matrix[j][i] = temp;
                }
            }
        }

        return matrix;
    }

    public static int[][] generateRandEuclid(int vert, int n) {

        VertexPoint[] vertices = new VertexPoint[vert];

        for (int i = 0; i < vert; i++) {
            vertices[i] = new VertexPoint(rand.nextInt(n - 1), rand.nextInt(n - 1), i);
        }

        int[][] matrix = new int[vert][vert];
        // fill the matrix up with maxes
        for (int[] row : matrix)
            java.util.Arrays.fill(row, 99);
        // populate x,y coords with the proper distances
        for (int i = 0; i < vert; i++) {
            for (int j = 0; j <= i; j++) {
                if (i == j) {
                    matrix[i][j] = 0;
                } else {
                    matrix[i][j] = vertices[i].getDistance(vertices[j]);
                    matrix[j][i] = vertices[j].getDistance(vertices[i]);
                }
            }
        }

        return matrix;
    }

    public static int[][] generateCircular(int vert, int radius) {
        VertexPoint[] vertices = new VertexPoint[vert];
        List<VertexPoint> shuf = new ArrayList<VertexPoint>();

        int x = 0, y = radius;
        double stepAngle = (2 * Math.PI) / vert;
        for (int i = 0; i < vert; i++) {
            shuf.add(new VertexPoint(x, y, i));
            x = (int) (radius * Math.sin((i + 1) * stepAngle));
            y = (int) (radius * Math.cos((i + 1) * stepAngle));
        }

        // mix up the order to the elements
        Collections.shuffle(shuf);
        vertices = shuf.toArray(vertices);

        int[][] matrix = new int[vert][vert];
        // fill the matrix up with maxes
        for (int[] row : matrix)
            java.util.Arrays.fill(row, 99);
        // populate x,y coords with the proper distances
        for (int i = 0; i < vert; i++) {
            for (int j = 0; j <= i; j++) {
                if (i == j) {
                    matrix[i][j] = 0;
                } else {
                    matrix[i][j] = vertices[i].getDistance(vertices[j]);
                    matrix[j][i] = vertices[j].getDistance(vertices[i]);
                }
            }
        }

        return matrix;
    }

    public static void PrintMatrixNicely(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.printf("%4d", matrix[i][j]);
            }
            System.out.println();
        }
    }

    private static void runAlgorithms() {
        TSP tsp = new TSP();

        int n = 10;
        int[][] matrix = new int[n][n];
        // for (int[] row : matrix)
        // java.util.Arrays.fill(row, 100);
        for (int i = 1; i < n; i++) {
            for (int j = 1; j <= i; j++) {
                if (i == j) {
                    matrix[i][j] = 0;
                } else {
                    int temp = rand.nextInt(50);
                    matrix[i][j] = temp;
                    matrix[j][i] = temp;
                }
            }
        }

        System.out.println("Normal tsp...");
        int[] bestTour = tsp.bruteForce(matrix);
        System.out.println(java.util.Arrays.toString(bestTour));

        double tourCost = tsp.computeTourCost(bestTour, matrix);
        System.out.println("Tour cost: " + tourCost);
        System.out.println();

        // GREEDY (nearest neighbor) ****************************************
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
        System.out.println();

        // GENETIC ********************************************************

        GeneticTSP geneticTSP = new GeneticTSP(n, matrix, 0, 0);
        Salesman genResult = geneticTSP.optimize();
        System.out.println("Genetic result...");
        System.out.println(genResult);
        System.out.println();
    }
}