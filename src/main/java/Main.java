import org.jfugue.player.Player;

import java.util.*;

public class Main {

    private static Player player = new Player();
    private static Random rand = new Random();

    private static Integer initPopulationNum = 1000;
    private static ArrayList<ArrayList<String>> population = new ArrayList<>();

    private static ArrayList<String> notes = new ArrayList(Arrays.asList("C", "C#", "D","Db", "D#", "Eb", "E", "F", "F#","Gb", "G", "G#", "A","A#","Ab", "H", "B"));
    private static ArrayList<String> melody = new ArrayList<>();

    public static void main(String[] args) {

        // MELODY Volio bih da si tu
        final long startTime = System.currentTimeMillis();
        ArrayList<String> volioBihDaSiTu = new ArrayList<>(Arrays.asList("E","F#","G","H","H","C",
                "E","E","D","C","H","C","H",
                "E","F#","G","H","H","C",
                "E","D","C","H","A","H","C","H",
                "E","D","C","H","A","H","C","H",
                "G","F#","E","F#","G","F#",
                "F#","A","G","F#","E","G","F#","E","F#","G","F#"));

        melody = new ArrayList<>(volioBihDaSiTu);

        playMelody(melody);

        population = createPopulation(population);


        Integer fitCounter;
        Integer maxFitCounter = 0;
        Integer maxPosition = 0;
        Integer evolutionNum = 0;

        ArrayList<ArrayList<String>> newGeneration = new ArrayList<>(population);
        ArrayList<String> bestChromosom;
        ArrayList<Integer> counterList;

        while (maxFitCounter < melody.size()){
            System.out.println("\nPOPULATION SIZE: " + newGeneration.size());
            counterList = new ArrayList<>();

            for (int i = 0; i < newGeneration.size(); i++) {
                fitCounter = 0;

                for (int j = 0; j < melody.size(); j++) {

                    if (melody.get(j).equals(newGeneration.get(i).get(j))) {
                        fitCounter++;
                    } else {
                        newGeneration.get(i).set(j, notes.get(rand.nextInt(notes.size())));
                    }
                }
                counterList.add(fitCounter);

                if (fitCounter > maxFitCounter) {
                    maxFitCounter = fitCounter;
                    maxPosition = i;
                }
            }
            evolutionNum++;
            bestChromosom = newGeneration.get(maxPosition);

            System.out.println("GENERATION NUM: "+evolutionNum);
            System.out.println("BEST CHROMOSOM: " + bestChromosom);
            System.out.println("FIT: " + maxFitCounter);

            playMelody(bestChromosom);

            Integer minCounter = counterList
                    .stream()
                    .mapToInt(v -> v)
                    .min().orElseThrow();

            if (maxFitCounter < melody.size()) {
                // newGeneration = eliminate(newGeneration, counterList ,minCounter);
                newGeneration = eliminateAndRecombine(newGeneration, counterList, minCounter);
            }
        }

        final long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime));
    }

    private static ArrayList<ArrayList<String>> eliminateAndRecombine(ArrayList<ArrayList<String>> generation, ArrayList<Integer> counterList, Integer minCounter) {
        ArrayList<ArrayList<String>> newGeneration = new ArrayList<>();
        Integer counter = 0;
        for (int i = 0; i < generation.size(); i++) {
            if ((counterList.get(i) != minCounter) && (counterList.get(i) != minCounter + 1) ) {
                newGeneration.add(generation.get(i));
            } else {
                counter++;
            }
        }

        for (int i = 0; i < counter; i++) {
            ArrayList<String> chrom1 = newGeneration.get(rand.nextInt(newGeneration.size()));
            ArrayList<String> chrom2 = newGeneration.get(rand.nextInt(newGeneration.size()));
            ArrayList<String> newChrom = new ArrayList<>();
            for (int j = 0; j < chrom1.size(); j += 2) {
                newChrom.add(chrom1.get(j));
                if (j != chrom1.size()-1) {
                    newChrom.add(chrom2.get(j + 1));
                }
            }
            newGeneration.add(newChrom);
        }
        System.out.println("ELIMINATED AND RECOMBINED: " + counter);
        return newGeneration;
    }

    private static ArrayList<ArrayList<String>> eliminate(ArrayList<ArrayList<String>> generation, ArrayList<Integer> counterList, Integer minCounter) {
        ArrayList<ArrayList<String>> newGeneration = new ArrayList<>();
        for (int i = 0; i < generation.size(); i++) {
            if (counterList.get(i) != minCounter) {
                newGeneration.add(generation.get(i));
            }
        }
        return newGeneration;
    }

    private static ArrayList<ArrayList<String>> createPopulation(ArrayList<ArrayList<String>> population) {
        for (int i = 0; i < initPopulationNum; i++) {
            ArrayList<String> melodyInPopulation = new ArrayList<>();
            for (int j = 0; j < melody.size(); j++) {
                melodyInPopulation.add(notes.get(rand.nextInt(notes.size())));
            }
            population.add(melodyInPopulation);
        }
        return population;
    }

    public static void playMelody(ArrayList<String> melody){
        for (String s : melody) {
            player.play(s);
        }
    }
}
