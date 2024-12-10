package com.example;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Reducer;

public class NormalizeReducer extends Reducer<Text, Text, Text, Text> {
    private float maxHub = 0;
    private float maxAuth = 0;
    private Map<Text, String> tempStorage = new HashMap<>();

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        float hubScore = 0;
        float authScore = 0;
        @SuppressWarnings("unused")
        String outlinks = "";
        @SuppressWarnings("unused")
        String inlinks = "";

        for (Text value : values) {
            String val = value.toString();
            String[] parts = val.split("\t");

            // Kiểm tra xem mảng có đủ phần tử
            if (parts.length == 4) {
                authScore = Float.parseFloat(parts[0]);
                hubScore = Float.parseFloat(parts[1]);
                outlinks = parts[2];
                inlinks = parts[3];

                if (hubScore > maxHub) {
                    maxHub = hubScore;
                }
                if (authScore > maxAuth) {
                    maxAuth = authScore;
                }

                tempStorage.put(new Text(key), val);
            } else {
                System.err.println("Error: Invalid input value: " + val);
            }
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        for (Map.Entry<Text, String> entry : tempStorage.entrySet()) {
            String val = entry.getValue();
            String[] parts = val.split("\t");

            float authScore = Float.parseFloat(parts[0]) / maxAuth;
            float hubScore = Float.parseFloat(parts[1]) / maxHub;
            String outlinks = parts[2];
            String inlinks = parts[3];

            // Sử dụng kết quả chuẩn hóa để cập nhật giá trị cho bước tiếp theo
            context.write(entry.getKey(), new Text(authScore + "\t" + hubScore + "\t" + outlinks + "\t" + inlinks));
        }
    }
}
