package com.example;

import java.io.*;
import java.util.HashMap;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;

public class Step3Mapper extends Mapper<LongWritable, Text, FloatWritable, Text> {
    private HashMap<String, String> urls;

    @Override
    protected void setup(Context context) throws IOException {
        urls = new HashMap<String, String>();

        Path path = new Path(context.getConfiguration().get("urls_path"));
        FileSystem fs = FileSystem.get(new Configuration());
        BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(path)));

        boolean firstLine = true;
        int currentNode = 0;
        int nodesCount = 0;

        String line;
        while ((line = br.readLine()) != null) {
            String[] split = line.split(" ");

            if (firstLine) {
                nodesCount = Integer.parseInt(split[0]);
                firstLine = false;
            } else {
                if (currentNode < nodesCount) {
                    urls.put(split[0], split[1]);
                    currentNode++;
                } else {
                    break;
                }
            }
        }

        br.close();
    }

    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] split = value.toString().split("\t");
        if (split.length < 5)
        	return;
        String node = split[0]; // Lấy node
        float authority = Float.parseFloat(split[1]); // Lấy giá trị authority
        float hub = Float.parseFloat(split[2]); // Lấy giá trị hub

        String url = urls.get(node); // Lấy URL từ hashmap theo node

        // Xuất ra một dòng với định dạng: url - auth - hub
        context.write(new FloatWritable(authority), new Text(" - " + url + " - " + hub));
    }
}
