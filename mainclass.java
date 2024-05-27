package bioinformatics;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class mainclass {

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

    public static void main(String[] args) {
        // SMALL EXAMPLE
        // String genome = "GTACGGTACTACTTACGCAGTGTACGATTACGCAGCTATACGCCGACT";
        // String sequence = "GTACGATTAC";
        // int[] kValues = {5, 10, 20, 30, 40, 50};

        // MEDIUM EXAMPLE
        String filePath = "bioinformatics/datasets/synthetic_genome.fna";
        String sequence = "TCGCGGGGTGTTAAACTGGGTTTAAATCTTGAAGTGTGATTCTAAGACTAACTAATCCCACTGGGCACAAGGACC";
        
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
        int[] kValues = {1000};

        System.out.println("Looking for this sequence: " + sequence);

        LDCF ldcf = new LDCF(1024, 4, 500);

        boolean exists = false;
        for (int k : kValues) {
            try {
                long startTime = System.currentTimeMillis();
                String[] kmers = generateKMers(genome, k);
                exists = false;
                System.out.println("For K-value="+k+":");

                for (String kmer : kmers) {
                    ldcf.insert(kmer);
                    exists = ldcf.lookup(sequence);


                    if (exists) {
                        System.out.println("Sequence exists: " + exists);
                        break;
                    }     
                }

                if(exists == false){
                    System.out.println("Sequence exists: " + exists);
                        // break;
                }

                long endTime = System.currentTimeMillis();
                float duration = endTime - startTime;
                duration = duration / 1000;

                // Imprime la duración total
                System.out.println("The program lasted " + duration + " s.");

                // if (exists) {
                //     break;
                // }

            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        
    }
}
