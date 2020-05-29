package Query2.util;

import org.apache.commons.math3.util.Precision;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;
import scala.Tuple3;
import scala.Tuple4;
import java.io.IOException;
import java.util.ArrayList;

public class Query2CsvWriter {

    public static final char CSV_SEPARATOR = ',';

    /**Raggruppo i valori delle statistiche per settimana in un RDD del tipo
            JavaPairRDD<String, ArrayList<Tuple2<String, Tuple4<Double, Double, Integer, Integer>>>>
            dove il primo valore di tipo String è il continente, l'ArrayList contiene i valori di
            tutte le settimane relativi a quel continente. All'interno dell'ArrayList quindi
            troviamo un valore di tipo String che fa riferimento al primo giorno della settimana
            indicata, ed una tupla con i valori delle statistiche di quella settimana.
        */
    public static void makeCsv(JavaPairRDD<String, ArrayList<Tuple2<String, Double>>> rdd1, JavaPairRDD<String, ArrayList<Tuple2<String, Double>>> rdd2,
                               JavaPairRDD<String, ArrayList<Tuple3<String, Integer, Integer>>> rdd3, String output) throws IOException{
        JavaPairRDD<String, Tuple2<ArrayList<Tuple2<String, Double>>, ArrayList<Tuple2<String, Double>>>> temp = rdd1.join(rdd2);
        JavaPairRDD<String, Tuple2<Tuple2<ArrayList<Tuple2<String, Double>>, ArrayList<Tuple2<String, Double>>>, ArrayList<Tuple3<String, Integer, Integer>>>> join =
                temp.join(rdd3);

        JavaPairRDD<String, ArrayList<Tuple2<String, Tuple4<Double, Double, Integer, Integer>>>> valuesByDay =
                join.mapToPair(new PairFunction<Tuple2<String, Tuple2<Tuple2<ArrayList<Tuple2<String, Double>>, ArrayList<Tuple2<String, Double>>>, ArrayList<Tuple3<String, Integer, Integer>>>>, String, ArrayList<Tuple2<String, Tuple4<Double, Double, Integer, Integer>>>>() {
                    @Override
                    public Tuple2<String, ArrayList<Tuple2<String, Tuple4<Double, Double, Integer, Integer>>>> call
                            (Tuple2<String, Tuple2<Tuple2<ArrayList<Tuple2<String, Double>>, ArrayList<Tuple2<String, Double>>>, ArrayList<Tuple3<String, Integer, Integer>>>> t) throws Exception {
                        String date = "";
                        ArrayList<Tuple2<String, Tuple4<Double, Double, Integer, Integer>>> arrayList = new ArrayList<>();
                        if (t._2()._1()._1().size() == t._2()._1()._2().size() && t._2()._1()._2().size() == t._2()._2().size()) {
                            for (int i = 0; i < t._2()._1()._1().size(); i++) {
                                if (t._2()._1()._1().get(i)._1().equals(t._2()._1()._2().get(i)._1()) && t._2()._1()._2().get(i)._1().equals(t._2()._2().get(i)._1())) {
                                    date = t._2()._1()._1().get(i)._1();
                                    Tuple4<Double, Double, Integer, Integer> tuple4 = new Tuple4<>(Precision.round(t._2()._1()._1().get(i)._2(), 2),
                                            Precision.round(t._2()._1()._2().get(i)._2(), 2), t._2()._2().get(i)._2(), t._2()._2().get(i)._3());
                                    Tuple2<String, Tuple4<Double, Double, Integer, Integer>> tuple2 = new Tuple2<>(date, tuple4);
                                    arrayList.add(tuple2);
                                } else {
                                    System.out.println("La data non coincide\n");
                                }
                            }
                        } else {
                            System.out.println("Gli array non coincidono\n");
                        }
                        Tuple2<String, ArrayList<Tuple2<String, Tuple4<Double, Double, Integer, Integer>>>> tuple2Again =
                                new Tuple2<>(t._1(), arrayList);
                        return tuple2Again;
                    }
                });

        /*Aggiusto i valori per il salvataggio in formato csv*/
        valuesByDay.map(t -> {
            String line = "";
            for(int i = 0; i < t._2().size(); i++) {
                line += t._1() + CSV_SEPARATOR + t._2().get(i)._1() + CSV_SEPARATOR + t._2().get(i)._2()._1()
                        + CSV_SEPARATOR + t._2().get(i)._2()._2() + CSV_SEPARATOR + t._2().get(i)._2()._3()
                        + CSV_SEPARATOR + t._2().get(i)._2()._4();
                if(i != t._2().size()-1) {
                    line += System.lineSeparator();
                } else {
                    line += "";
                }
            }
            return line;
        }).saveAsTextFile(output);

    }

}
