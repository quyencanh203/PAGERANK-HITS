package com.example;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;

public class HITS {
    public static void main(String[] args) throws Exception {
        if (args.length != 7) {
            System.out.println("Invalid arguments, expected 7 (inputpath, outputpath, datapath, maxruns, convergence, deleteoutput, showresults).");
            System.exit(1);
        }

        int maxRuns = Integer.parseInt(args[3]);
        float mindiff = Float.parseFloat(args[4]);
        FileSystem fs = FileSystem.get(new Configuration());

        if (Boolean.parseBoolean(args[5])) {
            Path outputPath = new Path(args[1]);
            if (fs.exists(outputPath)) {
                System.out.println("Deleting /output..");
                fs.delete(outputPath, true);
            }
        }

        boolean success = step1(args[0], args[1] + "/hitranks0");

        HashMap<Integer, Float> lastAuthorities = getAuthorities(fs, args[1] + "/hitranks0");

        for (int i = 0; i < maxRuns; i++) {
            String inputPath = (i == 0) ? args[1] + "/hitranks" + i : args[1] + "/normalized" + i;
            String outputPath = args[1] + "/hitranks" + (i + 1);

            success = success && step2(inputPath, outputPath);
            success = success && normalize(outputPath, args[1] + "/normalized" + (i + 1));

            HashMap<Integer, Float> newAuthorities = getAuthorities(fs, args[1] + "/normalized" + (i + 1));
            float authDiff = calculateDiff(lastAuthorities, newAuthorities);

            System.out.println("Run #" + (i + 1) + " finished (authority diff: " + authDiff + ").");

            if (authDiff < mindiff) {
                // Đảm bảo tạo thư mục normalized khi hội tụ
                success = success && normalize(outputPath, args[1] + "/normalized" + (i + 1));
                break;
            } else {
                lastAuthorities = newAuthorities;
            }
        }

        // success = success && step3(args[1] + "/normalized" + (lastAuthorities.size() - 1), args[1] + "/hitranking", args[2]);
        // Tìm thư mục normalized cuối cùng
        int lastNormalizedIndex = -1;
        for (int j = maxRuns; j >= 0; j--) {
            Path normalizedPath = new Path(args[1] + "/normalized" + j);
            if (fs.exists(normalizedPath)) {
                lastNormalizedIndex = j;
                break;
            }
        }
        
        if (lastNormalizedIndex == -1) {
            System.err.println("No normalized directory found!");
            System.exit(1);
        }
        
        // Sử dụng lastNormalizedIndex thay vì maxRuns
        success = success && step3(args[1] + "/normalized" + lastNormalizedIndex, args[1] + "/hitranking", args[2]);
        if (Boolean.parseBoolean(args[6])) {
            showResults(fs, args[1] + "/hitranking");
        }

        System.exit(success ? 0 : 1);
    }

    private static boolean step1(String input, String output) throws Exception {
        Configuration conf = new Configuration();
        conf.set("mapreduce.fileoutputcommitter.marksuccessfuljobs", "false");

        System.out.println("Step 1..");
        Job job = Job.getInstance(conf, "Step 1");
        job.setJarByClass(HITS.class);
        
        job.setMapperClass(Step1Mapper.class);
        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setReducerClass(Step1Reducer.class);
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(Text.class);
        
        FileInputFormat.addInputPath(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));
        
        return job.waitForCompletion(true);
    }
    
    private static boolean step2(String input, String output) throws Exception {
        Configuration conf = new Configuration();
        conf.set("mapreduce.fileoutputcommitter.marksuccessfuljobs", "false");
        FileSystem fs = FileSystem.get(conf); 
        Path outputPath = new Path(output); 
        if (fs.exists(outputPath)) { 
        	fs.delete(outputPath, true);
        }
        System.out.println("Step 2..");
        Job job = Job.getInstance(conf, "Step 2");
        job.setJarByClass(HITS.class);
        
        job.setMapperClass(Step2Mapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
    
        job.setReducerClass(Step2Reducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        
        FileInputFormat.addInputPath(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));
        
        return job.waitForCompletion(true);
    }
    
    private static boolean normalize(String input, String output) throws Exception {
        Configuration conf = new Configuration();
        conf.set("mapreduce.fileoutputcommitter.marksuccessfuljobs", "false");
        FileSystem fs = FileSystem.get(conf); 
        Path outputPath = new Path(output); 
        if (fs.exists(outputPath)) { 
        	fs.delete(outputPath, true);
        }
        System.out.println("Normalizing..");
        Job job = Job.getInstance(conf, "Normalize");
        job.setJarByClass(HITS.class);

        job.setMapperClass(NormalizeMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setReducerClass(NormalizeReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));

        return job.waitForCompletion(true);
    }

    private static boolean step3(String input, String output, String urlsPath) throws Exception {
        Configuration conf = new Configuration();
        conf.set("mapreduce.fileoutputcommitter.marksuccessfuljobs", "false");
        conf.set("urls_path", urlsPath);
        
        System.out.println("Step 3..");
        Job job = Job.getInstance(conf, "Step 3");
        job.setJarByClass(HITS.class);
        
        job.setMapperClass(Step3Mapper.class);
        job.setMapOutputKeyClass(FloatWritable.class);
        job.setMapOutputValueClass(Text.class);
        
        FileInputFormat.addInputPath(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));
        
        return job.waitForCompletion(true);
    }

    private static HashMap<Integer, Float> getAuthorities(FileSystem fs, String dir) throws Exception {
        Path path = new Path(dir + "/part-r-00000");
        if (!fs.exists(path))
            throw new Exception("The file part-r-00000 doesn't exist.");
        
        BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(path)));
        String line;
        HashMap<Integer, Float> authorities = new HashMap<Integer, Float>();
        
        while ((line = br.readLine()) != null) {
            String[] split = line.split("\t");
            authorities.put(Integer.parseInt(split[0]), Float.parseFloat(split[1])); // Assuming authorities score is at index 1
        }
        
        return authorities;
    }
    
    private static float calculateDiff(HashMap<Integer, Float> lastValues, HashMap<Integer, Float> newValues) {
        float diff = 0;
        
        for (int key : newValues.keySet()) {
            float lri = lastValues.containsKey(key) ? lastValues.get(key) : 0;
            diff += Math.abs(newValues.get(key) - lri);
        }
        
        return diff;
    }

    private static void showResults(FileSystem fs, String dir) throws Exception {
        Path path = new Path(dir + "/part-r-00000");
        if (!fs.exists(path)) {
            System.out.println("The file part-r-00000 doesn't exist.");
            return;
        }
        
        BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(path)));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }
}
