package bioinformatics;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class mainclasstest {

    public static String[] generateKMers(String genome, int k) {
        if (k <= 0) {
            throw new IllegalArgumentException("k must be a positive integer");
        }
        if (genome == null || genome.length() < k) {
            throw new IllegalArgumentException("genome must be a string of length at least k");
        }

        String[] kmers = new String[genome.length() - k + 1];

        for (int i = 0; i < genome.length() - k + 1; i++) {
            kmers[i] = genome.substring(i, i + k);
        }

        return kmers;
    }

    public static boolean lookupInFile(String item) {
        try (BufferedReader reader = new BufferedReader(new FileReader("bioinformatics/kmers.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(item)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        // SMALL EXAMPLE
        // String genome = "GTACGGTACTACTTACGCAGTGTACGATTACGCAGCTATACGCCGACT";
        // String sequence = "GTACGATTAC";
        // int[] kValues = {5, 10, 20, 30, 40, 50};

        // MEDIUM EXAMPLE
        // String filePath = "bioinformatics/datasets/synthetic_genome.fna";
        // String sequence = "TCGCGGGGTGTTAAACTGGGTTTAAATCTTGAAGTGTGATTCTAAGACTAACTAATCCCACTGGGCACAAGGACC";

        // MEDIUM EXAMPLE 1
        // String filePath = "bioinformatics/datasets/synthetic_genome_medium_example1.fna";
        // String sequence = "ACTTATGTCTGCAAGTTACGGGAGAACAGGCTCGACGGAG";

        // MEDIUM EXAMPLE 2
        // String filePath = "bioinformatics/datasets/synthetic_genome_medium_example2.fna";
        // String sequence = "GAGCTACTTACAACTCCGATAATGATATGGATTTACAATACCCCCCAGTCTGCCAGGCGCCTGCCGAGAT";

        // MEDIUM EXAMPLE 3
        String filePath = "bioinformatics/datasets/synthetic_genome_medium_example3.fna";
        String sequence = "TCAGCATATCTATACACGACACTTCTATTAAGAATGTCGTCCTGAGAGAAGGCAGTACCTGAGCGTGGACGAGTGAGTTAATTTCCAGGGTGGAAAAAGA";
        
        // LARGE EXAMPLE (E.Coli example)
        // String filePath = "bioinformatics/datasets/GCF_000008865.2_ASM886v2_genomic.fna";
        // String sequence = "TCATTTGATCAGCAGTGATGGCGTAATTGTCATTAAGGCACAGGAATACCGCAGTCAGGAACTGAACCGCGAAGCGGCGCTGGCCCGGCTGGTGGCAGTGATTAAAGATTTAACAACAGAACAAAAAGCCCGACGACCCACGCGGCCCACCCGTGCATCGAAAGAGCGCAGGCTGGCATCGAAAGCACAAAAATCAAGCGTGAAGGCGATGCGCAGCGGTCGGGAATAAAAAGAAGGAATGG";
        
        String genome = null;
        try {
            genome = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        genome = genome.replaceAll(">.*\n", "").replaceAll("\n", ""); // Rremove FASTA header if present
        
        int size_sequence = sequence.length();

        // deberia de funcionar con este vector para que haga todos del tiron, pero solo funciona bien al poner 1
        // int[] kValues = {50, size_sequence, 80, 90, 100, 200, 500, 1000, 1500, 2000};
        int[] kValues = {1500};

        System.out.println("Looking for this sequence: " + sequence);

        LDCF ldcf = new LDCF(1024, 4, 500);

        boolean exists;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("bioinformatics/kmers.txt"))) {
            for (int k : kValues) {
                try {
                    long startLoadingTime = System.currentTimeMillis();
                    long startLookingTime = 0;
                    exists = false; // set it to false at the beggining

                    String[] kmers = generateKMers(genome, k);
                    System.out.println("For K-value=" + k + ":");

                    for (String kmer : kmers) {
                        ldcf.insert(kmer);
                        writer.write(kmer);
                        writer.newLine(); // Write each kmer in a new line

                        // startLookingTime = System.currentTimeMillis();
                        // exists = ldcf.lookup(sequence);
                        
                        // if (exists) {
                        //     break;
                        // }
                    }

                    long endLoadingTime = System.currentTimeMillis();
                    float durationLoad = endLoadingTime - startLoadingTime;
                    durationLoad = durationLoad / 1000;
                    System.out.println("The program load lasted " + durationLoad + " s.");

                    writer.flush(); // Ensure all kmers are written to the file before looking up
                    startLookingTime = System.currentTimeMillis();
                    exists = lookupInFile(sequence);
                    long endLookupTime = System.currentTimeMillis();
                    
                    float durationLookup = endLookupTime - startLookingTime;
                    durationLookup = durationLookup / 1000;
                    System.out.println("The sequence lookup lasted " + durationLookup + " s.");

                    System.out.println("Sequence exists: " + exists);

                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
