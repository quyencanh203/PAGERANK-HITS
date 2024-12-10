package com.example;

import java.io.IOException;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;

public class Step2Mapper extends Mapper<LongWritable, Text, Text, Text> {
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] split = value.toString().split("\t");
        if (split.length < 5)
        	return;
        String node = split[0];
        float auth = Float.parseFloat(split[1]);
        float hub = Float.parseFloat(split[2]);
        String[] outlinks = split[3].split(",");
        String[] inlinks = split[4].split(",");

        if (outlinks.length > 0) {
            for (String outlink : outlinks) {
                float auth_ = hub;
                context.write(new Text(outlink), new Text("AUTH:" + auth_));
            }
        }
        if (inlinks.length > 0) {
            for (String inlink : inlinks) {
                float hub_ = auth;
                context.write(new Text(inlink), new Text("HUB:" + hub_));
            }
        }

        context.write(new Text(node), new Text("OUT:" + split[3]));
        context.write(new Text(node), new Text("IN:" + split[4]));
    }
}

