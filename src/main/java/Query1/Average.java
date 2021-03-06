package Query1;

import Query1.util.DayIta;
import org.apache.commons.math3.util.Precision;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.PairFunction;
import org.joda.time.DateTime;
import scala.Tuple2;

/**Per ogni coppia di domeniche calcolo la media che sarà proprio la media
 di una singola settimana. Come chiave uso la data della domenica della
 settimana di riferimento.
 */
public class Average {

    /**Calcolo la media dei dimessi guariti */
    public static JavaPairRDD<DateTime, Double>
        computeHealedDischargedAverage(JavaPairRDD<Tuple2<DayIta, DayIta>, Long> pairRDD, int weekLength) {

        JavaPairRDD<DateTime, Double> healedDischargedRdd = pairRDD.mapToPair(
                new PairFunction<Tuple2<Tuple2<DayIta, DayIta>, Long>, DateTime, Double>() {
                    @Override
                    public Tuple2<DateTime, Double> call(Tuple2<Tuple2<DayIta, DayIta>, Long> t) throws Exception{
                        DateTime date = t._1()._2().getDate();
                        double a, b;
                        double mean;
                        if (t._2() == 0) {
                            a = 0;
                            b = Double.valueOf(t._1()._2().getHealedDischarged());
                        } else {
                            a = Double.valueOf(t._1()._1().getHealedDischarged());
                            b = Double.valueOf(t._1()._2().getHealedDischarged());
                        }
                        mean = (b - a) / weekLength;
                        Tuple2<DateTime, Double> tuple = new Tuple2<>(date, Precision.round(mean, 2));
                        return tuple;
                    }
                }
        );
        return healedDischargedRdd;
    }

    /**Calcolo la media dei tamponi*/
    public static JavaPairRDD<DateTime, Double>
        computeSwabsAverage(JavaPairRDD<Tuple2<DayIta, DayIta>, Long> pairRDD, int weekLength) {

        JavaPairRDD<DateTime, Double> swabsRdd = pairRDD.mapToPair(
                new PairFunction<Tuple2<Tuple2<DayIta, DayIta>, Long>, DateTime, Double>() {
                    @Override
                    public Tuple2<DateTime, Double> call(Tuple2<Tuple2<DayIta, DayIta>, Long> t) throws Exception{
                        DateTime date = t._1()._2().getDate();
                        double a, b;
                        double mean;
                        if (t._2() == 0) {
                            a = 0;
                            b = Double.valueOf(t._1()._2().getSwabs());
                        } else {
                            a = Double.valueOf(t._1()._1().getSwabs());
                            b = Double.valueOf(t._1()._2().getSwabs());
                        }
                        mean = (b - a) / weekLength;
                        Tuple2<DateTime, Double> tuple = new Tuple2<>(date, Precision.round(mean, 2));
                        return tuple;
                    }
                }
        );
        return swabsRdd;
    }
}
