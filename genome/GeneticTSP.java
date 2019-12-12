package genome;

import java.util.*;

public class GeneticTSP {
    int generationSize;
    int genomeSize;
    int numberOfCities;
    int reproductionSize;
    int maxIterations;
    float mutationRate;
    int tournamentSize;
    int[][] travelPrices;
    int startingCity = 0;
    int targetFitness;

    public GeneticTSP(int numCities, int[][] costs, int starting, int targetFit) {
        numberOfCities = numCities;
        genomeSize = numCities - 1;
        travelPrices = costs;
        startingCity = starting;
        targetFitness = targetFit;

        generationSize = 5000;
        reproductionSize = 200;
        maxIterations = 1000;
        mutationRate = 0.1f;
        tournamentSize = 40;
    }

    public List<Salesman> initialPopulation() {
        List<Salesman> population = new ArrayList<>();
        for (int i = 0; i < generationSize; i++) {
            population.add(new Salesman(numberOfCities, travelPrices, startingCity));
        }
        return population;
    }

    public List<Salesman> selection(List<Salesman> population) {
        List<Salesman> selected = new ArrayList<>();

        for (int i = 0; i < reproductionSize; i++) {
            selected.add(tournamentSelection(population));
        }

        return selected;
    }

    public static <E> List<E> pickNRandomElements(List<E> list, int n) {
        Random r = new Random();
        int length = list.size();

        if (length < n)
            return null;

        for (int i = length - 1; i >= length - n; --i) {
            Collections.swap(list, i, r.nextInt(i + 1));
        }
        return list.subList(length - n, length);
    }

    public Salesman tournamentSelection(List<Salesman> population) {
        List<Salesman> selected = pickNRandomElements(population, tournamentSize);
        return Collections.min(selected);
    }

    public Salesman mutate(Salesman salesman) {
        Random rand = new Random();
        float mutate = rand.nextFloat();

        if (mutate < mutationRate) {
            List<Integer> genome = salesman.getGenome();
            Collections.swap(genome, rand.nextInt(genomeSize), rand.nextInt(genomeSize));
            return new Salesman(genome, numberOfCities, travelPrices, startingCity);
        }
        return salesman;
    }

    public List<Salesman> createGeneration(List<Salesman> population) {
        List<Salesman> generation = new ArrayList<>();
        int currentGenerationSize = 0;
        while (currentGenerationSize < generationSize) {
            List<Salesman> parents = pickNRandomElements(population, 2);
            List<Salesman> children = crossover(parents);
            children.set(0, mutate(children.get(0)));
            children.set(1, mutate(children.get(1)));
            generation.addAll(children);
            currentGenerationSize += 2;
        }
        return generation;
    }

    public List<Salesman> crossover(List<Salesman> parents) {
        // set up
        Random random = new Random();
        int breakpoint = random.nextInt(genomeSize);
        List<Salesman> children = new ArrayList<>();

        // copy parental genomes - we copy so we wouldn't modify in case they were
        // chosen to participate in crossover multiple times
        List<Integer> parent1Genome = new ArrayList<>(parents.get(0).getGenome());
        List<Integer> parent2Genome = new ArrayList<>(parents.get(1).getGenome());

        // creating child 1
        for (int i = 0; i < breakpoint; i++) {
            int newVal;
            newVal = parent2Genome.get(i);
            Collections.swap(parent1Genome, parent1Genome.indexOf(newVal), i);
        }
        children.add(new Salesman(parent1Genome, numberOfCities, travelPrices, startingCity));
        parent1Genome = parents.get(0).getGenome(); // reseting the edited parent

        // creating child 2
        for (int i = breakpoint; i < genomeSize; i++) {
            int newVal = parent1Genome.get(i);
            Collections.swap(parent2Genome, parent2Genome.indexOf(newVal), i);
        }
        children.add(new Salesman(parent2Genome, numberOfCities, travelPrices, startingCity));

        return children;
    }

    public Salesman optimize() {
        List<Salesman> population = initialPopulation();
        Salesman globalBestGenome = population.get(0);
        for (int i = 0; i < maxIterations; i++) {
            List<Salesman> selected = selection(population);
            population = createGeneration(selected);
            globalBestGenome = Collections.min(population);
            if (globalBestGenome.getFitness() < targetFitness)
                break;
        }
        return globalBestGenome;
    }

    public void printGeneration(List<Salesman> generation) {
        for (Salesman genome : generation) {
            System.out.println(genome);
        }
    }

}