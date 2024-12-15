@echo off

hdfs dfs -mkdir -p /input
hdfs dfs -mkdir -p /user/%USERNAME%/input

hdfs dfs -put -f input\data.txt /input
hdfs dfs -put -f input\data.txt /user/%USERNAME%/input  

mvn clean
mvn package

hdfs dfs -test -d /output
IF %ERRORLEVEL% EQU 0 (
    hdfs dfs -rm -r -skipTrash /output
)

hadoop jar target/demo-1.0-SNAPSHOT.jar com.example.PageRank /input /output input/data.txt 0.85 5 0.01 true true

hdfs dfs -get /output/* output/