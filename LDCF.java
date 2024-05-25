package bioinformatics;

import java.util.Random;

public class LDCF {
    private int[][][] buckets; // 3D array to represent multi-level buckets
    private int[] bucketSizes; // Array to store bucket sizes for each level
    private int maxKicks;
    private Random random;

    public LDCF(int initialNumBuckets, int initialBucketSize, int maxKicks) {
        this.buckets = new int[1][initialNumBuckets][initialBucketSize]; // 2D array to represent buckets, start with one level
        this.bucketSizes = new int[1];
        this.bucketSizes[0] = initialBucketSize;
        this.maxKicks = maxKicks;
        this.random = new Random();

        // Initialize all buckets with -1 to indicate empty slots
        for (int i = 0; i < initialNumBuckets; i++) {
            for (int j = 0; j < initialBucketSize; j++) {
                buckets[0][i][j] = -1;
            }
        }
    }

    // Determine the bucket indeces
    private int hash1(int item, int level) {
        return Math.abs((Integer.hashCode(item + level) ^ (Integer.hashCode(item) >>> 16)) % buckets[level].length);
    }

    private int hash2(int item, int level) {
        return Math.abs(((Integer.hashCode(item + level) / buckets[level].length) ^ (Integer.hashCode(item) >>> 16)) % buckets[level].length);
    }

    private void resize() {
        int newLevel = buckets.length;
        int newNumBuckets = buckets[0].length * 2;
        int newBucketSize = bucketSizes[0];

        int[][][] newBuckets = new int[newLevel + 1][newNumBuckets][newBucketSize];
        int[] newBucketSizes = new int[newLevel + 1];
        
        System.arraycopy(buckets, 0, newBuckets, 0, buckets.length);
        System.arraycopy(bucketSizes, 0, newBucketSizes, 0, bucketSizes.length);

        for (int i = 0; i < newNumBuckets; i++) {
            for (int j = 0; j < newBucketSize; j++) {
                newBuckets[newLevel][i][j] = -1;
            }
        }

        newBucketSizes[newLevel] = newBucketSize;
        buckets = newBuckets;
        bucketSizes = newBucketSizes;
    }

    private boolean insertAtLevel(int item, int level) {
        int i1 = hash1(item, level);
        int i2 = hash2(item, level);

        for (int j = 0; j < bucketSizes[level]; j++) {
            if (buckets[level][i1][j] == -1) {
                buckets[level][i1][j] = item;
                return true;
            }

            if (buckets[level][i2][j] == -1) {
                buckets[level][i2][j] = item;
                return true;
            }
        }

        // Eviction process if both buckets are full
        int currentBucket = random.nextBoolean() ? i1 : i2;
        for (int n = 0; n < maxKicks; n++) {
            int j = random.nextInt(bucketSizes[level]);
            int evictedItem = buckets[level][currentBucket][j];
            buckets[level][currentBucket][j] = item;
            item = evictedItem;

            currentBucket = (currentBucket == i1) ? i2 : i1;

            for (int k = 0; k < bucketSizes[level]; k++) {
                if (buckets[level][currentBucket][k] == -1) {
                    buckets[level][currentBucket][k] = item;
                    return true;
                }
            }
        }

        return false;
    }

    // Insert item in bucket
    public boolean insert(int item) {
        for (int level = 0; level < buckets.length; level++) {
            if (insertAtLevel(item, level)) {
                return true;
            }
        }
        resize();

        return insert(item);
    }

    private boolean lookupAtLevel(int item, int level) {
        int i1 = hash1(item, level);
        int i2 = hash2(item, level);

        for (int j = 0; j < bucketSizes[level]; j++) {
            if (buckets[level][i1][j] == item) {
                return true;
            }

            if (buckets[level][i2][j] == item) {
                return true;
            }
        }

        return false;
    }

    // Check if item is in one of the two buckets
    public boolean lookup(int item) {
        for (int level = 0; level < buckets.length; level++) {
            if (lookupAtLevel(item, level)) {
                return true;
            }
        }

        return false;
    }

    private boolean deleteAtLevel(int item, int level) {
        int i1 = hash1(item, level);
        int i2 = hash2(item, level);

        for (int j = 0; j < bucketSizes[level]; j++) {
            if (buckets[level][i1][j] == item) {
                buckets[level][i1][j] = -1;
                return true;
            }

            if (buckets[level][i2][j] == item) {
                buckets[level][i2][j] = -1;
                return true;
            }
        }

        return false;
    }

    // Remove item from bucket
    public boolean delete(int item) {
        for (int level = 0; level < buckets.length; level++) {
            if (deleteAtLevel(item, level)) {
                return true;
            }
        }

        return false;
    }

    public int numCollisions() {
        int count = 0;
        for (int level = 0; level < buckets.length; level++) {
            for (int i = 0; i < buckets[level].length; i++) {
                for (int j = 0; j < bucketSizes[level]; j++) {
                    if (buckets[level][i][j]!= -1) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    // public static String[] generateKMers(String gnome, int k) {
    //     String[] kmers = new String[gnome.length() - k + 1];

    //     for (int i = 0; i < gnome.length() - k + 1; i++) {
    //         kmers[i] = gnome.substring(i, i + k);
    //     }

    //     return kmers;
    // }

}