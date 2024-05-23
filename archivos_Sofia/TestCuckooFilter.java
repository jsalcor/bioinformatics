package bioinformatics.archivos_Sofia;

public class TestCuckooFilter {
    public static void main(String[] args) {
        int numBuckets = 1000;
        int bucketSize = 4;
        int maxKicks = 500;

        CuckooFilter filter = new CuckooFilter(numBuckets, bucketSize, maxKicks);

        // Test inserting items
        System.out.println("Testing insertion:");
        for (int i = 0; i < 100; i++) {
            // if (filter.insert(i) == false) {
            //     System.out.println("Failed to insert: " + i);
            // }
            assert filter.insert(i) : "Failed to insert: " + i;
        }

        // Test looking up for items already inserted
        System.out.println("Testing looking up for items:");
        for (int i = 0; i < 100; i++) {
            // if (filter.lookup(i) == false) {
            //     System.out.println("Failed to find: " + i);
            // }
            assert filter.lookup(i) : "Failed to lookup: " + i;
        }

        // Test looking up for items non-inserted
        System.out.println("Testing looking up for items:");
        for (int i = 100; i < 200; i++) {
            // if (filter.lookup(i) == false) {
            //     System.out.println("Failed to find: " + i);
            // }
            assert filter.lookup(i) : "Failed to lookup: " + i;
        }

        // Test deleting items
        System.out.println("Testing deletion:");
        for (int i = 0; i < 100; i++) {
            // if (filter.delete(i) == false) {
            //     System.out.println("Failed to delete: " + i);
            // }
            assert filter.delete(i) : "Failed to delete: " + i;
        }

        // Test looking up for items already deleted
        System.out.println("Testing looking up for items:");
        for (int i = 0; i < 100; i++) {
            // if (filter.lookup(i) == false) {
            //     System.out.println("Failed to find: " + i);
            // }
            assert filter.lookup(i) : "Failed to lookup: " + i;
        }
    }
}