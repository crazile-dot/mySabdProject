package Query1.util;

import org.apache.spark.api.java.JavaPairRDD;
import org.joda.time.DateTime;
import scala.Tuple2;
import java.io.IOException;

public class Query1CsvWriter {

    public static final char CSV_SEPARATOR = ',';

    /**Aggiusto i valori per il salvataggio in formato csv*/
    public static void makeCsv(JavaPairRDD<DateTime, Double> rdd1, JavaPairRDD<DateTime, Double> rdd2, String output, String out) throws IOException{
        JavaPairRDD<DateTime, Tuple2<Double, Double>> joinedRdd = rdd1.join(rdd2).sortByKey().cache();
        joinedRdd.map(t -> t._1().toString() + CSV_SEPARATOR + t._2()._1() + CSV_SEPARATOR + t._2()._2()).saveAsTextFile(out);
    }

}
