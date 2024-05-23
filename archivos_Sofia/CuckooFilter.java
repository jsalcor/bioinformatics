package bioinformatics.archivos_Sofia;

import java.util.Random;

public class CuckooFilter {
    private int[][] buckets; // simplified representation
    private int bucketSize;
    private int maxKicks;
    private Random random;

    public CuckooFilter(int numBuckets, int bucketSize, int maxKicks) {
        this.buckets = new int[numBuckets][bucketSize]; // 2D array to represent buckets
        this.bucketSize = bucketSize;
        this.maxKicks = maxKicks;
        this.random = new Random();

        // Initialize all buckets with -1 to indicate empty slots
        for (int i = 0; i < numBuckets; i++) {
            for (int j = 0; j < bucketSize; j++) {
                buckets[i][j] = -1;
            }
        }
    }

    // Determine the bucket indeces
    private int hash1(int item) {
        return Integer.hashCode(item) % buckets.length;
    }

    private int hash2(int item) {
        return (Integer.hashCode(item) / buckets.length) % buckets.length;
    }

    // Insert item in bucket
    public boolean insert(int item) {
        int i1 = hash1(item);
        int i2 = hash2(item);

        for (int j = 0; j < bucketSize; j++) {
            if (buckets[i1][j] == -1) {
                buckets[i1][j] = item;
                return true;
            }

            if (buckets[i2][j] == -1) {
                buckets[i2][j] = item;
                return true;
            }
        }

        // Eviction process if both buckets are full
        int currentBucket = random.nextBoolean() ? i1 : i2;
        for (int n = 0; n < maxKicks; n++) {
            int j = random.nextInt(bucketSize);
            int evictedItem = buckets[currentBucket][j];
            buckets[currentBucket][j] = item;
            item = evictedItem;

            currentBucket = (currentBucket == i1) ? i2 : i1;

            for (int k = 0; k < bucketSize; k++) {
                if (buckets[currentBucket][k] == -1) {
                    buckets[currentBucket][k] = item;
                    return true;
                }
            }
        }

        return false;
    }

    // Check if item is in one of the two buckets
    public boolean lookup(int item) {
        int i1 = hash1(item);
        int i2 = hash2(item);

        for (int j = 0; j < bucketSize; j++) {
            if (buckets[i1][j] == item || buckets[i2][j] == item) {
                return true;
            }
        }

        return false;
    }

    // Remove item from bucket
    public boolean delete(int item) {
        int i1 = hash1(item);
        int i2 = hash2(item);

        for (int j = 0; j < bucketSize; j++) {
            if (buckets[i1][j] == item) {
                buckets[i1][j] = -1;
                return true;
            }

            if (buckets[i2][j] == item) {
                buckets[i2][j] = -1;
                return true;
            }
        }

        return false;
    }
}