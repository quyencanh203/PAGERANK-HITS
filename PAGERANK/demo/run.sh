#!/bin/bash
if hdfs dfs -test -d /input; then hdfs dfs -rm -r -skipTrash /input; fi
hdfs dfs -mkdir -p /input
hdfs dfs -mkdir -p /user/$USER/input
hdfs dfs -put -f input/data.txt /input
hdfs dfs -put -f input/data.txt /user/$USER/input  
mvn clean
mvn package
if hdfs dfs -test -d /output; then hdfs dfs -rm -r -skipTrash /output; fi  
hadoop jar target/demo-1.0-SNAPSHOT.jar com.example.PageRank /input /output input/data.txt 0.85 5 0.4 true true
if [ "$(ls -A output)" ]; then
    rm -r output/*
fi
hdfs dfs -get /output/* output/