package bioinformatics;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class mainclass {

    public static String[] generateKMers(String gnome, int k) {
        if (k <= 0) {
            throw new IllegalArgumentException("k must be a positive integer");
        }
        if (gnome == null || gnome.length() < k) {
            throw new IllegalArgumentException("gnome must be a string of length at least k");
        }

        String[] kmers = new String[gnome.length() - k + 1];

        for (int i = 0; i < gnome.length() - k + 1; i++) {
            kmers[i] = gnome.substring(i, i + k);
        }

        return kmers;
    }

    // public static void insertKmersWithProgress(LDCF ldcf, String[] kmers) {
    //     int totalKmers = kmers.length;

    //     for (int i = 0; i < totalKmers; i++) {
    //         String kmer = kmers[i];
    //         int hashCode = kmer.hashCode();
    //         ldcf.insert(hashCode);

    //         // Calculate and print progress
    //         double progress = ((double) (i + 1) / totalKmers) * 100;
    //         System.out.printf("Progress: %.2f%%\n", progress);
    //     }
    // }

    // Method to read the sequence from the file
    private static String readSequenceFromFile(String filePath) {
        StringBuilder sequence = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Read lines until the end of the file
            while ((line = br.readLine()) != null) {
                sequence.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sequence.toString();
    }

    public static void main(String[] args) {
        // String genome = "GTACTCAGTGTACGATCAGCTACCGACT";
        // String sequence = "TCAGTGTACG";

        // String genome = "GTTACGGACAGTCCCGTGGAGAATTTGCTACTACTGCGTGGCATGCGCTTATAGTCTACTTTTGAGACCGCAGGTCCGTACTTGACTGGCCTCTCCGAAGAGGCACAACGATCTTACTTGCTACCTTCTGTGAGGGTGAATAGGCGGATATAGGATGTAATATATCTAAGCATCGCTGCATCAAAAGCGCTGGTGTCACGTAACACCGGACGCCAACTTAACGTCAATCTGTGCGACTACGCCTAATTGAAGTTCTCGGTTCCGTTACTCCAATGAACTTCGTGCGTTATTGAGAAACTATACGTCCTATTGTTACAGTGCACGGCACGAGAGTTACGTTTGGCCTTACGTTCGAGTTGGAGGGACCGAAGCGGGGGGATTCGGACAAGGACGAACAGCGTCCCAATATGTTATACGAAAGCGAGATCTCATATCTGTCCGGTAGGGAAACTTCGGGGGTGGCCGAGAACCTGCAGTCATTCGGGTCACAGACAGACAAATGGTTCCATGGAGCGCCCAATAAGCGCAGACGGAGCAACGAGCTACCGGTTCGACGAAGTTCTTGTTATGCGCAGCGTGTGAAGATTCAGCAATCTCGACAGCAGTCCCAGGTATTGTGGAGATGTGTTCAGCAGTGACGTCTATTTATTCCGCCAGATCAGTCTTAAATCTGCGCGAGTTTTGTTACCTTGGGATCTCCTCCGCGTCGATACTATTAAT";
        String sequence = "CTTATTTCCA";
        
        int size_sequence = sequence.length();

        // String filePath = "bioinformatics/datasets/GCF_000008865.2_ASM886v2_genomic.fna";
        String filePath = "bioinformatics/synthetic_genome.fna";
        String genome = null;

        try {
            genome = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Rremove FASTA header if present
        genome = genome.replaceAll(">.*\n", "").replaceAll("\n", "");
        System.out.println(genome);

        LDCF ldcf = new LDCF(1024, 4, 500);

        int[] kValues = {size_sequence, 20};
        boolean exists = false;
        for (int k : kValues) {
            try {
                String[] kmers = generateKMers(genome, k);

                // Insert each k-mer's hash code into the Cuckoo Filter with progress
                // insertKmersWithProgress(ldcf, kmers);

                for (String kmer : kmers) {
                    // int hashCode = kmer.hashCode();
                    // ldcf.insert(hashCode);
                    ldcf.insert(kmer);
                    // System.out.println(kmer);
                    exists = ldcf.lookup(sequence);
                    System.out.println("Sequence exists: " + exists);  

                    if (exists) {
                        break;
                    }           
                }

                // System.out.println("k = " + k);
                // System.out.println("Number of collisions: " + ldcf.numCollisions());
                // System.out.println();

                // Example lookup
                // exists = ldcf.lookup(sequence.hashCode());
                // exists = ldcf.lookup(sequence);
                // System.out.println("Sequence exists: " + exists);

                if (exists) {
                    break;
                }

            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
