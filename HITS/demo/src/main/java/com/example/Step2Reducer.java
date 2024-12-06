package com.example;

import java.io.IOException;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Reducer;

public class Step2Reducer extends Reducer<Text, Text, Text, Text> {
    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        String outlinks = "";
        String inlinks = "";
        float totalHub = 0;
        float totalAuthority = 0;

        for (Text text : values) {
            String val = text.toString();
            
            if (val.startsWith("HUB:")) {
                totalHub += Float.parseFloat(val.substring(4));
            } else if (val.startsWith("AUTH:")) {
                totalAuthority += Float.parseFloat(val.substring(5));
            } else if (val.startsWith("OUT:")) {
                outlinks = val.substring(4);
            } else if (val.startsWith("IN:")) {
                inlinks = val.substring(3);
            }
        }

//        // Normalize authority and hub values
//        totalAuthority = (float) Math.sqrt(totalAuthority);
//        totalHub = (float) Math.sqrt(totalHub);

        context.write(key, new Text(Float.toString(totalAuthority) + "\t" + Float.toString(totalHub) + "\t" + outlinks + "\t" + inlinks));
    }
}



