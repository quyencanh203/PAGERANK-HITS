package com.example;

import java.io.IOException;
import java.util.HashSet;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Reducer;

public class Step1Reducer extends Reducer<IntWritable, IntWritable, IntWritable, Text> {
    public void reduce(IntWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        HashSet<Integer> outlinks = new HashSet<>();
        HashSet<Integer> inlinks = new HashSet<>();

        for (IntWritable value : values) {
            int node = value.get();
            if (node < 0) { // Out_link
                outlinks.add(-node); // Abs
            } else { // In_link
                inlinks.add(node);
            }
        }

        String outlinksStr = String.join(",", outlinks.stream().map(String::valueOf).toArray(String[]::new));
        String inlinksStr = String.join(",", inlinks.stream().map(String::valueOf).toArray(String[]::new));

        context.write(key, new Text("1.0\t1.0\t" + outlinksStr + "\t" + inlinksStr));
    }
}