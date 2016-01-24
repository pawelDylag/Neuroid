package fais.com.neuroid.Neural.Data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Ten set danych zwraca randomowe wektory uczace.
 * Created by paweldylag on 24/01/16.
 */
public class TrainDataSet {

    private Map<Integer, DataVector> dataset;
    private Random random;

    public TrainDataSet() {
        this.dataset = new HashMap<>();
        this.random = new Random();
    }

    public void add(DataVector d) {
        dataset.put(dataset.size(), d);
    }

    public int getSize () {
        return dataset.size();
    }

    public DataVector nextRandom() {
        int index = random.nextInt(dataset.size());
        return dataset.get(index);
    }
}
