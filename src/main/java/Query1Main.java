import Query1.Average;
import Query1.Query1Preprocessing;
import Query1.util.DayIta;
import Query1.util.Query1CsvWriter;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.joda.time.DateTime;
import scala.Tuple2;

import java.io.IOException;

public class Query1Main {

    private final static int weekLength = 7;

    public static void main (String[] args) {

        final String pathToFile = "s3://" + args[0] + "/dpc-covid19-ita-andamento-nazionale.csv";
        final String output = "s3://" + args[0] + "/query1_output.csv";


        SparkConf conf = new SparkConf()
                .setAppName("myApplication");
        JavaSparkContext sc = new JavaSparkContext(conf);
        sc.setLogLevel("ERROR");

        JavaRDD<String> covid19File = sc.textFile(pathToFile);
        JavaPairRDD<Tuple2<DayIta, DayIta>, Long> rdd = Query1Preprocessing.preprocessing(covid19File, weekLength);
        JavaPairRDD<DateTime, Double> healedDischargedRdd = Average.computeHealedDischargedAverage(rdd, weekLength);
        JavaPairRDD<DateTime, Double> swabsRdd = Average.computeSwabsAverage(rdd, weekLength);

        try {
            Query1CsvWriter.makeCsv(healedDischargedRdd, swabsRdd, output);
        } catch (IOException io) {
            io.printStackTrace();
            System.out.println("Errore del file (il file potrebbe già esistere)");
        }
        sc.close();

    }
}
