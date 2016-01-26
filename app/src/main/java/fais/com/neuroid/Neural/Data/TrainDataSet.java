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
    private final int outputVectorSize;
    private Random random;

    public TrainDataSet(int outputVectorSize) {
        this.dataset = new HashMap<>();
        this.random = new Random();
        this.outputVectorSize = outputVectorSize;
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


    public DataVector getPatternVector (int patternId) {
        return new DataVector(patternId, outputVectorSize);
    }
}
