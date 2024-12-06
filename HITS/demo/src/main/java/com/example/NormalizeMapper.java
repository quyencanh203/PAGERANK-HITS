package com.example;

import java.io.IOException;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;

public class NormalizeMapper extends Mapper<LongWritable, Text, Text, Text> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // Mỗi dòng đầu vào sẽ có định dạng: Node \t authScore \t hubScore \t outlinks \t inlinks
        String[] parts = value.toString().split("\t");

        // Kiểm tra xem mảng có đủ phần tử
        if (parts.length == 5) {
            String nodeKey = parts[0];
            String authScore = parts[1];
            String hubScore = parts[2];
            String outlinks = parts[3];
            String inlinks = parts[4];

            context.write(new Text(nodeKey), new Text(authScore + "\t" + hubScore + "\t" + outlinks + "\t" + inlinks));
        } else {
            System.err.println("Error: Invalid input line: " + value.toString());
        }
    }
}
