package bioinformatics;

public class mainclass {

    public static String[] generateKMers(String gnome, int k) {
        String[] kmers = new String[gnome.length() - k + 1];

        for (int i = 0; i < gnome.length() - k + 1; i++) {
            kmers[i] = gnome.substring(i, i + k);
        }

        return kmers;
    }

    public static void main(String[] args) {
        // Load E. coli genome
        String eColiGnome = ""
        + "GTACTCAGTGTACGATCAGCTACCGACT";

        LDCF ldcf = new LDCF(1024, 4, 500);

        int[] kValues = {10, 20, 50, 100, 200};
        for (int k : kValues) {
            String[] kmers = generateKMers(eColiGnome, k);

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

            // for (String kmer : kmers) {
            //     ldcf.delete(Integer.parseInt(kmer, 2));
            // }
            
            System.out.println("Number of collisions: " + ldcf.numCollisions());
            System.out.println();

            // Example lookup
            String sequence = "ACGTACGTAC";
            boolean exists = ldcf.lookup(sequence.hashCode());
            System.out.println("Sequence exists: " + exists);
        }
    }
}
