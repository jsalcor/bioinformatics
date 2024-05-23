package bioinformatics;

import java.util.Random;

public class CuckooFilter {
    private String[][] buckets; // simplified representation
    private int bucketSize;
    private int maxKicks;
    private Random random;

    public CuckooFilter(int numBuckets, int bucketSize, int maxKicks) {
        this.buckets = new String[numBuckets][bucketSize]; // 2D array to represent buckets
        this.bucketSize = bucketSize;
        this.maxKicks = maxKicks;
        this.random = new Random();

        // Initialize all buckets with null to indicate empty slots
        for (int i = 0; i < numBuckets; i++) {
            for (int j = 0; j < bucketSize; j++) {
                buckets[i][j] = null;
            }
        }
    }

    // Determine the bucket indices
    private int hash1(String item) {
        return Math.abs(item.hashCode()) % buckets.length;
    }

    private int hash2(String item) {
        return (Math.abs(item.hashCode() / buckets.length)) % buckets.length;
    }

    // Insert item in bucket
    public boolean insert(String item) {
        int i1 = hash1(item);
        int i2 = hash2(item);

        for (int j = 0; j < bucketSize; j++) {
            if (buckets[i1][j] == null) {
                buckets[i1][j] = item;
                return true;
            }

            if (buckets[i2][j] == null) {
                buckets[i2][j] = item;
                return true;
            }
        }

        // Eviction process if both buckets are full
        int currentBucket = random.nextBoolean() ? i1 : i2;
        for (int n = 0; n < maxKicks; n++) {
            int j = random.nextInt(bucketSize);
            String evictedItem = buckets[currentBucket][j];
            buckets[currentBucket][j] = item;
            item = evictedItem;

            currentBucket = (currentBucket == i1) ? i2 : i1;

            for (int k = 0; k < bucketSize; k++) {
                if (buckets[currentBucket][k] == null) {
                    buckets[currentBucket][k] = item;
                    return true;
                }
            }
        }

        return false;
    }

    // Check if item is in one of the two buckets
    public boolean lookup(String item) {
        int i1 = hash1(item);
        int i2 = hash2(item);

        for (int j = 0; j < bucketSize; j++) {
            if (item.equals(buckets[i1][j]) || item.equals(buckets[i2][j])) {
                return true;
            }
        }

        return false;
    }

    // Remove item from bucket
    public boolean delete(String item) {
        int i1 = hash1(item);
        int i2 = hash2(item);

        for (int j = 0; j < bucketSize; j++) {
            if (item.equals(buckets[i1][j])) {
                buckets[i1][j] = null;
                return true;
            }

            if (item.equals(buckets[i2][j])) {
                buckets[i2][j] = null;
                return true;
            }
        }

        return false;
    }
}
