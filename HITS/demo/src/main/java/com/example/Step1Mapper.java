package com.example;

import java.io.IOException;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;

public class Step1Mapper extends Mapper<LongWritable, Text, IntWritable, IntWritable> {
    private static int nodesCount;
    private static int currentNode;
    private static int edgesCount;
    private static int currentEdge;

    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        if (key.get() == 0) {
            String[] split = value.toString().split(" ");
            nodesCount = Integer.parseInt(split[0]);
            edgesCount = Integer.parseInt(split[1]);
        } else {
            if (currentNode < nodesCount) {
                currentNode++;
                return;
            } else if (currentEdge < edgesCount) {
                String[] split = value.toString().split(" ");
                int fromNode = Integer.parseInt(split[0]);
                int toNode = Integer.parseInt(split[1]);

                context.write(new IntWritable(fromNode), new IntWritable(-toNode)); // Out_link
                context.write(new IntWritable(toNode), new IntWritable(fromNode));  // In_link
                currentEdge++;
            }
        }
    }
}
