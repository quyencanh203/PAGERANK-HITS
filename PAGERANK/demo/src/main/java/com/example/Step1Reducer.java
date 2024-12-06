package com.example;

import java.io.IOException;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Reducer;

public class Step1Reducer extends Reducer<IntWritable, IntWritable, IntWritable, Text> 
{
    private int nodesCount;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException 
    {
        // Read nodesCount from configuration
        nodesCount = context.getConfiguration().getInt("nodesCount", 1);
        System.out.println("nodesCount in Reducer: " + nodesCount); // Debug
    }

    public void reduce(IntWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException 
    {
        String outlinks = "";
        boolean first = true;

        for (IntWritable iw : values)
        {
            if (first) first = false;
            else outlinks += ",";
            outlinks += Integer.toString(iw.get());
        }

        // Set initial PageRank = 1.0 / nodesCount
        double initialPageRank = 1.0 / nodesCount;
        context.write(key, new Text(initialPageRank + "\t" + outlinks));
    }
}
