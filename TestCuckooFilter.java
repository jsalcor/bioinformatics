package bioinformatics;

import bioinformatics.CuckooFilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TestCuckooFilter {
    public static void main(String[] args) {
        int numBuckets = 1000;
        int bucketSize = 4;
        int maxKicks = 500;

        CuckooFilter filter = new CuckooFilter(numBuckets, bucketSize, maxKicks);

        // Load sequences from FNA file
        String filename = "bioinformatics/datasets/GCF_000008865.2_ASM886v2_genomic.fna";
        // String filename = "datasets" + File.separator + "GCF_000008865.2_ASM886v2_genomic.fna";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            StringBuilder sequence = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(">")) {
                    if (sequence.length() > 0) {
                        // Insert the previous sequence into the filter
                        String seq = sequence.toString();
                        if (!filter.insert(seq)) {
                            System.out.println("Failed to insert sequence: " + seq);
                        }
                        sequence.setLength(0); // Clear the sequence for the next one
                    }
                } else {
                    sequence.append(line.trim());
                }
            }
            // Insert the last sequence
            if (sequence.length() > 0) {
                String seq = sequence.toString();
                if (!filter.insert(seq)) {
                    System.out.println("Failed to insert sequence: " + seq);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Test looking up for sequences
        System.out.println("Testing lookup for sequences:");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            StringBuilder sequence = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(">")) {
                    if (sequence.length() > 0) {
                        String seq = sequence.toString();
                        if (!filter.lookup(seq)) {
                            System.out.println("Failed to find sequence: " + seq);
                        }
                        sequence.setLength(0);
                    }
                } else {
                    sequence.append(line.trim());
                }
            }
            if (sequence.length() > 0) {
                String seq = sequence.toString();
                if (!filter.lookup(seq)) {
                    System.out.println("Failed to find sequence: " + seq);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Additional tests for deletion can be added similarly
    }
}
