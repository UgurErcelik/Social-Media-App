import org.apache.spark.SparkConf;
import org.apache.spark.sql.*;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

import java.util.concurrent.TimeoutException;

public class StreamingConsumer {
    public static void main(String[] args) throws StreamingQueryException, TimeoutException {
        System.setProperty("hadoop.home.dir","C:\\Users\\ugure\\BigData\\hadoop-3.2.2");
        SparkSession sparkSession = SparkSession.builder().master("local").appName("Streaming-kafka").getOrCreate();

        StructType schema = new StructType().add("tweet","string").add("retweet_count","string");

        Dataset<Row> load = sparkSession.readStream().format("kafka")
                .option("kafka.bootstrap.servers","localhost:9092")
                .option("subscribe","twitter-search").load();

        Dataset<SearchTweetModel> data = load.selectExpr("CAST (value AS STRING) as message")
                .select(functions.from_json(functions.col("message"),schema).as("json"))
                .select("json.*")
                .as(Encoders.bean(SearchTweetModel.class));

        Dataset<Row> count = data.groupBy(data.col("retweet_count"),data.col("tweet")).count();

        StreamingQuery start =count.writeStream()
                .outputMode("complete")
                .format("console").start();

        start.awaitTermination();



    }
}
