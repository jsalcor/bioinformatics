package bioinformatics;

import java.util.Random;

public class LDCF {
    private String[][][] buckets; // 3D array to represent multi-level buckets
    private int[] bucketSizes; // Array to store bucket sizes for each level
    private int maxKicks;
    private Random random;

    public LDCF(int initialNumBuckets, int initialBucketSize, int maxKicks) {
        this.buckets = new String[1][initialNumBuckets][initialBucketSize]; // 2D array to represent buckets, start with one level
        this.bucketSizes = new int[1];
        this.bucketSizes[0] = initialBucketSize;
        this.maxKicks = maxKicks;
        this.random = new Random();

        // Initialize all buckets with null to indicate empty slots
        for (int i = 0; i < initialNumBuckets; i++) {
            for (int j = 0; j < initialBucketSize; j++) {
                buckets[0][i][j] = null;
            }
        }
    }

    // Determine the bucket indices
    private int hash1(String item, int level) {
        return Math.abs((item.hashCode() + level) % buckets[level].length);
    }

    private int hash2(String item, int level) {
        return Math.abs(((item.hashCode() / buckets[level].length) + level) % buckets[level].length);
    }

    private void resize() {
        int newLevel = buckets.length;
        int newNumBuckets = buckets[0].length * 2;
        int newBucketSize = bucketSizes[0];

        String[][][] newBuckets = new String[newLevel + 1][newNumBuckets][newBucketSize];
        int[] newBucketSizes = new int[newLevel + 1];
        
        System.arraycopy(buckets, 0, newBuckets, 0, buckets.length);
        System.arraycopy(bucketSizes, 0, newBucketSizes, 0, bucketSizes.length);

        for (int i = 0; i < newNumBuckets; i++) {
            for (int j = 0; j < newBucketSize; j++) {
                newBuckets[newLevel][i][j] = null;
            }
        }

        newBucketSizes[newLevel] = newBucketSize;
        buckets = newBuckets;
        bucketSizes = newBucketSizes;
    }

    private boolean insertAtLevel(String item, int level) {
        int i1 = hash1(item, level);
        int i2 = hash2(item, level);

        for (int j = 0; j < bucketSizes[level]; j++) {
            if (buckets[level][i1][j] == null) {
                buckets[level][i1][j] = item;
                return true;
            }

            if (buckets[level][i2][j] == null) {
                buckets[level][i2][j] = item;
                return true;
            }
        }

        // Eviction process if both buckets are full
        int currentBucket = random.nextBoolean() ? i1 : i2;
        for (int n = 0; n < maxKicks; n++) {
            int j = random.nextInt(bucketSizes[level]);

            if (buckets[level][currentBucket][j] == null) {
                buckets[level][currentBucket][j] = item;
                return true;
            }

            String evictedItem = buckets[level][currentBucket][j];
            buckets[level][currentBucket][j] = item;
            item = evictedItem;
            currentBucket = (currentBucket == i1) ? i2 : i1;
        }

        return false;
    }

    // Insert item in bucket
    public boolean insert(String item) {
        for (int level = 0; level < buckets.length; level++) {
            if (insertAtLevel(item, level)) {
                return true;
            }
        }
        resize();

        int level = hash1(item, 0) % buckets.length;
        return insertAtLevel(item, level);
    }

    private boolean lookupAtLevel(String item, int level) {
        int i1 = hash1(item, level);
        int i2 = hash2(item, level);
    
        for (int j = 0; j < bucketSizes[level]; j++) {
            if (buckets[level][i1][j] != null && buckets[level][i1][j].contains(item)) {
                return true;
            }
            
            if (buckets[level][i2][j] != null && buckets[level][i2][j].contains(item)) {
                return true;
            }
        }
    
        return false;
    }
    

    // Check if item is in one of the two buckets
    public boolean lookup(String item) {
        for (int level = 0; level < buckets.length; level++) {
            if (lookupAtLevel(item, level)) {
                return true;
            }
        }

        return false;
    }

    private boolean deleteAtLevel(String item, int level) {
        int i1 = hash1(item, level);
        int i2 = hash2(item, level);

        for (int j = 0; j < bucketSizes[level]; j++) {
            if (item.equals(buckets[level][i1][j])) {
                buckets[level][i1][j] = null;
                return true;
            }

            if (item.equals(buckets[level][i2][j])) {
                buckets[level][i2][j] = null;
                return true;
            }
        }

        return false;
    }

    // Remove item from bucket
    public boolean delete(String item) {
        for (int level = 0; level < buckets.length; level++) {
            if (deleteAtLevel(item, level)) {
                return true;
            }
        }

        return false;
    }
}
