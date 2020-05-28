package Query1;

import Query1.util.DateParser;
import Query1.util.DayIta;
import Query1.util.Query1CsvParser;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.joda.time.DateTime;
import scala.Tuple2;

import java.util.List;

public class Query1Preprocessing {

    public static JavaPairRDD<Tuple2<DayIta, DayIta>, Long> preprocessing(JavaRDD<String> rdd, int weekLength) {

        JavaPairRDD<String, Long> rddWithIndex = rdd.zipWithIndex();
        JavaRDD<String> withoutFirstRow = rddWithIndex.filter((Tuple2<String, Long> t) ->
                t._2() > 0).map(t -> t._1());

        JavaRDD<DayIta> values = withoutFirstRow.map(line -> Query1CsvParser.parseCSV(line)).filter(x -> x != null)
                .map(new Function<DayIta, DayIta>() {
                    @Override
                    public DayIta call(DayIta dayIta) throws Exception {
                        if(dayIta.getDate().compareTo(DateParser.dateTimeParser("2020-03-10T18:00:00")) <= 0) {
                            dayIta.setDate(dayIta.getDate().plusHours(-1));
                        }
                        return dayIta;
                    }
                });
        JavaPairRDD<DayIta, Long> sortedRdd = values.sortBy(x -> x.getDate(), true, 1).zipWithIndex().cache();
        JavaRDD<DayIta> sunday = sortedRdd.filter(t -> t._2()%weekLength == weekLength-1).map(t -> t._1());
        JavaPairRDD<Tuple2<DayIta, DayIta>, Long> cartesian = sunday.cartesian(sunday).zipWithIndex();
        JavaPairRDD<Tuple2<DayIta, DayIta>, Long> needed = cartesian.filter(t -> t._2() == 0 || t._1()._1().getDate().plusDays(weekLength).compareTo(t._1()._2().getDate()) == 0
                || t._1()._1().getDate().plusDays(weekLength).plusHours(-1).compareTo(t._1()._2().getDate()) == 0).cache();


        return needed;
    }
}
