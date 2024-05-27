# Logarithmic Dynamic Cuckoo Filter (LDCF)

## Overview

This repository contains an implementation of a Logarithmic Dynamic Cuckoo Filter (LDCF) in Java. 
The LDCF is a probabilistic data structure that supports fast and space-efficient insertions, lookups, and deletions. 
It is particularly useful for applications in bioinformatics, such as sequence searching in large genomes.

## Requirements and Usage

- Java Development Kit (JDK) 8 or higher
  
To compile and run the the program, these commands must be executed from the origin folder:
- javac bioinformatics/LDCF.java bioinformatics/mainclass.java
- java bioinformatics.mainclass


## Features

- **Dynamic resizing**: Automatically adjusts the size of the filter to handle more items.
  
- **Efficient insertion**: Handles collisions through a limited number of relocations (kicks). This is performed through the next functions:
```java

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

```

```java
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

```

  
- **Fast lookup**: Quickly checks for the presence of an item. Mainly performed by:
```java

public boolean lookup(String item) {
    for (int level = 0; level < buckets.length; level++) {
        if (lookupAtLevel(item, level)) {
            return true;
        }
    }
    return false;
}

```

```java

private boolean lookupAtLevel(String item, int level) {
        int i1 = hash1(item, level);
        int i2 = hash2(item, level);

        for (int j = 0; j < bucketSizes[level]; j++) {
            if (item.equals(buckets[level][i1][j])) {
                return true;
            }

            if (item.equals(buckets[level][i2][j])) {
                return true;
            }
        }

        return false;
    }

```


- **Deletion support**: Allows items to be removed from the filter. It works calling the next functions:
```java

public boolean delete(String item) {
        for (int level = 0; level < buckets.length; level++) {
            if (deleteAtLevel(item, level)) {
                return true;
            }
        }

        return false;
    }

```

```java

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

```

## Code explanation
### LCDF Class
The LDCF class implements the core functionality of the Logarithmic Dynamic Cuckoo Filter. We can find the following **attributes**:
- **buckets**: A 3D array representing multiple levels of buckets.
- **bucketSizes**: An array storing the size of buckets for each level.
- **maxKicks**: The maximum number of relocations (kicks) allowed before resizing.
- **random**: A random number generator for stochastic decisions.

And the following **methods**:
- **hash1** and **hash2**: Calculate bucket indices for an item.
- **resize**: Expands the filter when necessary.
- **insertAtLevel**and **insert**: Inserts an item into the filter at a specified level.
- **lookupAtLevel** and **lookup**: Check for the presence of an item.
- **deleteAtLevel** and **delete**: Remove an item from the filter.

### Main class
The Main Class demonstrates a practical application of the LDCF, being these the main steps:
- Reads a genome file and a sequence to search.
- Generates kmers from the genome.
- Inserts kmers into the filter.
- Searches for the specified sequence within the filter.
- Measures and reports the execution time

In this case, we evaluate our program with three cases: a small, a medium and a large example, named like this due to the dataset and sequence length being used.
```java

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
        int[] kValues = {size_sequence};

        System.out.println("Looking for this sequence: " + sequence);

        LDCF ldcf = new LDCF(1024, 4, 500);

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
        float duration = endTime - startTime;
        duration = duration / 1000;

        System.out.println("The program lasted " + duration + " s.");
    }
}


```

### File Generator
A python script that creates a synthetical genome for evaluation purposes. Its lenght is randomly chosen between 10^3 and 10^7 characters.

### Datasets folder
The folder where the datasets of genome sequences are placed. We already provide two examples: the E.Coli example and a syntethical generated dataset using the previous file generator.

## Installation

Clone the repository to your local machine:

```sh
git clone https://github.com/your-username/ldcf.git
cd ldcf
```

## Contact

For any questions or suggestions, please open an issue or contact the repository owner.
Thank you for using the Logarithmic Dynamic Cuckoo Filter!


