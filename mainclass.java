package bioinformatics;

import java.nio.file.Files;
import java.nio.file.Paths;
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

    public static void main(String[] args) {
        // SMALL EXAMPLE
        // String genome = "GTACTCAGTGTACGATCAGCTACCGACT";
        // String sequence = "GCTACCGACT";

        // MEDIUM EXAMPLE
        // String filePath = "bioinformatics/synthetic_genome.fna";
        
        // LARGE EXAMPLE (E.Coli example)
        String filePath = "bioinformatics/datasets/GCF_000008865.2_ASM886v2_genomic.fna";
        String sequence = "TCATTTGATCAGCAGTGATGGCGTAATTGTCATTAAGGCACAGGAATACCGCAGTCAGGAACTGAACCGCGAAGCGGCGCTGGCCCGGCTGGTGGCAGTGATTAAAGATTTAACAACAGAACAAAAAGCCCGACGACCCACGCGGCCCACCCGTGCATCGAAAGAGCGCAGGCTGGCATCGAAAGCACAAAAATCAAGCGTGAAGGCGATGCGCAGCGGTCGGGAATAAAAAGAAGGAATGG";
        
        int size_sequence = sequence.length();
        String genome = null;

        try {
            genome = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Rremove FASTA header if present
        genome = genome.replaceAll(">.*\n", "").replaceAll("\n", "");
        System.out.println("Looking for this sequence: " + sequence);

        LDCF ldcf = new LDCF(1024, 4, 500);

        // Small example
        // int[] kValues = {10, 20, 30, 40, 50};
        
        // Large example, more efficient
        int[] kValues = {size_sequence};

        long startTime = System.currentTimeMillis();
        boolean exists = false;
        for (int k : kValues) {
            try {
                String[] kmers = generateKMers(genome, k);

                for (String kmer : kmers) {
                    ldcf.insert(kmer);
                    exists = ldcf.lookup(sequence);

                    if (exists) {
                        System.out.println("Sequence exists: " + exists);
                        break;
                    }           
                }

                if (exists) {
                    break;
                }

            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        duration = duration / 1000;

        // Imprime la duración total
        System.out.println("The program lasted " + duration + " s.");
    }
}
