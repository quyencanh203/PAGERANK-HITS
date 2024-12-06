#!/bin/bash
hdfs dfs -mkdir -p /input
hdfs dfs -mkdir -p /user/$USER/input
hdfs dfs -put -f input/data.txt /input
hdfs dfs -put -f input/data.txt /user/$USER/input  
mvn clean
mvn package
if hdfs dfs -test -d /output; then hdfs dfs -rm -r -skipTrash /output; fi  
hadoop jar target/demo-1.0-SNAPSHOT.jar com.example.HITS /input /output input/data.txt 10 0.2 true true
hdfs dfs -get /output/* output/