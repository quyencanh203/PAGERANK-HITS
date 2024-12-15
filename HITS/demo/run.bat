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

hadoop jar target\demo-1.0-SNAPSHOT.jar com.example.HITS /input /output input/data.txt 10 0.2 true true

hdfs dfs -get /output/* output/