package bioinformatics;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

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
        // Load E. coli genome
        String genome = "GTACTCAGTGTACGATCAGCTACCGACT";

        // String filePath = "bioinformatics/datasets/GCF_000008865.2_ASM886v2_genomic.fna";
        // String genome = null;

        // try {
        //     genome = new String(Files.readAllBytes(Paths.get(filePath)));
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }

        // // Rremove FASTA header if present
        // genome = genome.replaceAll(">.*\n", "").replaceAll("\n", "");

        LDCF ldcf = new LDCF(1024, 4, 500);

        int[] kValues = {5, 10};
        for (int k : kValues) {
            try {
                String[] kmers = generateKMers(genome, k);

                for (String kmer : kmers) {
                    int hashCode = kmer.hashCode();
                    ldcf.insert(hashCode);
                }

                // for (String kmer : kmers) {
                //     ldcf.insert(Integer.parseInt(kmer, 2));
                // }

                System.out.println("k = " + k);
                System.out.println("Number of collisions: " + ldcf.numCollisions());
                System.out.println();
                
                // System.out.println("Number of collisions: " + ldcf.numCollisions());
                // System.out.println();

                // Example lookup
                // "TCCTTGGATGAGCGCG"
                String sequence = "GACT";
                boolean exists = ldcf.lookup(sequence.hashCode());
                System.out.println("Sequence exists: " + exists);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
