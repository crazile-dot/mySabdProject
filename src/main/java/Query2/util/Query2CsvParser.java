package Query2.util;

import java.util.ArrayList;

public class Query2CsvParser {

    public static State parseCSV(String csvLine) {

        State state = null;
        String[] csvValues = csvLine.split(",");
        ArrayList<Integer> intValues = new ArrayList<>();

        String value1 = csvValues[0];
        String value2 = csvValues[1];
        double value3 = Double.parseDouble(csvValues[2]);
        double value4 = Double.parseDouble(csvValues[3]);
        for(int i = 4; i < csvValues.length; i++) {
            intValues.add(Integer.parseInt(csvValues[i]));
        }
        checkMonotonicity(intValues);
        state = new State("", value1, value2, value3, value4, intValues, 0.0);

        State anotherState = ContinentAllocator.allocateContinent(state);

        return anotherState;
    }

    public static void checkMonotonicity(ArrayList<Integer> arrayList) {
        for(int i = 1; i < arrayList.size(); i++) {
            if(arrayList.get(i-1) > arrayList.get(i)) {
                if(arrayList.indexOf(arrayList.get(i-1)) == 0) {
                    arrayList.set(0, arrayList.get(i));
                } else {
                    arrayList.set(arrayList.indexOf(arrayList.get(i-1)), (arrayList.get(i-2) + arrayList.get(i))/2);
                }
                checkMonotonicity(arrayList);
            }
        }
    }

    public static ArrayList<String> getDates(String csvLine) {
        ArrayList<String> res = new ArrayList<>();
        String[] csvValues = csvLine.split(",");

        for(int i = 4; i < csvValues.length; i++) {
            res.add(csvValues[i]);
        }
        return res;
    }

}
