package genome;

import java.util.*;

public class Salesman implements Comparable {
    List<Integer> genome;
    int[][] travelPrices;
    int startingCity = 0;
    int numberOfCities = 0;
    int fitness;

    public Salesman(int numCities, int[][] costs, int starting) {
        numberOfCities = numCities;
        travelPrices = costs;
        startingCity = starting;
        genome = randomSalesman();
        fitness = this.calculateFitness();
    }

    public Salesman(List<Integer> permutation, int numCities, int[][] costs, int starting) {
        genome = permutation;
        numberOfCities = numCities;
        travelPrices = costs;
        startingCity = starting;
        fitness = this.calculateFitness();
    }

    public int calculateFitness() {
        int fitness = 0;
        int current = startingCity;

        for (int gene : genome) {
            fitness += travelPrices[current][gene];
            current = gene;
        }

        fitness += travelPrices[genome.get(numberOfCities - 2)][startingCity];

        return fitness;
    }

    private List<Integer> randomSalesman() {
        List<Integer> result = new ArrayList<Integer>();
        for (int i = 0; i < numberOfCities; i++) {
            if (i != startingCity) {
                result.add(i);
            }
        }

        Collections.shuffle(result);

        return result;
    }

    public List<Integer> getGenome() {
        return genome;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitVal) {
        fitness = fitVal;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Path: ");
        sb.append(startingCity);
        for (int gene : genome) {
            sb.append(" ");
            sb.append(gene);
        }
        sb.append(" ");
        sb.append(startingCity);
        sb.append("\nLength: ");
        sb.append(fitness);
        return sb.toString();
    }

    @Override
    public int compareTo(Object obj) {
        Salesman genome = (Salesman) obj;
        if (fitness > genome.getFitness())
            return 1;
        else if (fitness < genome.getFitness())
            return -1;
        else
            return 0;
    }
}