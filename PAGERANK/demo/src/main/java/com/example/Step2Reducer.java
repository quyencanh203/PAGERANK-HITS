package com.example;

import java.io.IOException;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Reducer;

public class Step2Reducer extends Reducer<Text, Text, Text, Text> 
{
    private float dampingFactor;
    private int nodesCount;

    @Override
    protected void setup(Context context)
    {
        dampingFactor = context.getConfiguration().getFloat("df", 0.85f);
        nodesCount = context.getConfiguration().getInt("nodesCount", 1); // Read N from Configuration
    }

    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException 
    {
        String outlinks = "";
        float totalRank = 0;

        for (Text text : values)
        {
            String val = text.toString();

            // If it's the list of outlinks
            if (val.startsWith("["))
            {
                outlinks = val.substring(1); // Remove the '['
                continue;
            }
            // If it's a rank contribution
            else
            {
                totalRank += Float.parseFloat(val);
            }
        }

        // Apply PageRank formula: totalRank = (1 - dampingFactor) * (1 / N) + dampingFactor * totalRank
        totalRank = (1 - dampingFactor) * (1.0f / nodesCount) + (dampingFactor * totalRank);
        
        context.write(key, new Text(Float.toString(totalRank) + "\t" + outlinks));
    }
}
