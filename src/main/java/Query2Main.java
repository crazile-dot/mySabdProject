import Query2.Query2Preprocessing;
import Query2.Statistics;
import Query2.TrendCompute;
import Query2.util.Query2CsvWriter;
import Query2.util.State;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;
import scala.Tuple3;

import java.io.IOException;
import java.util.ArrayList;

public class Query2Main {

    private final static int weekLength = 7;

    public static void main (String[] args) {

        final String pathToFile = "s3://" + args[0] + "/time_series_covid19_confirmed_global.csv";
        final String output = "s3://" + args[0] + "/query2_output.csv";

        SparkConf conf = new SparkConf()
                .setAppName("myApplication");
        JavaSparkContext sc = new JavaSparkContext(conf);
        sc.setLogLevel("ERROR");

        JavaRDD<String> globalCovid19File = sc.textFile(pathToFile);
        JavaPairRDD<String, Long> rddWithIndex = globalCovid19File.zipWithIndex();
        JavaRDD<State> parsedRdd = Query2Preprocessing.preprocessing(rddWithIndex);
        JavaRDD<State> coefficients = TrendCompute.computeTrendlineCoefficient(parsedRdd);
        JavaRDD<State> first100 = TrendCompute.get100States(coefficients);
        JavaPairRDD<String, ArrayList<Tuple2<String, Integer>>> valuesByContinent = Statistics.getValuesWithDate(first100, rddWithIndex);

        JavaPairRDD<String, ArrayList<Tuple2<String, Double>>> meanRdd = Statistics.computeAverage(valuesByContinent, weekLength);
        JavaPairRDD<String, ArrayList<Tuple2<String, Double>>> standardDeviationRdd = Statistics.computeStandardDeviation(valuesByContinent, meanRdd, weekLength);
        JavaPairRDD<String, ArrayList<Tuple3<String, Integer, Integer>>> minMaxRdd = Statistics.computeMinMax(valuesByContinent, weekLength);
        try {
            Query2CsvWriter.makeCsv(meanRdd, standardDeviationRdd, minMaxRdd, output);
        } catch (IOException io) {
            io.printStackTrace();
            System.out.println("Errore del file (il file potrebbe già esistere)");
        }
        sc.close();
    }
}
