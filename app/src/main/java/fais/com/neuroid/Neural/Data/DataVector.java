package fais.com.neuroid.Neural.Data;

import java.util.Arrays;

/**
 * Klasa trzymajaca wektor danych wejsciowych do sieci neuronowej.
 * Pod ta postacia moga byc rowniez dane trenujace.
 * Created by paweldylag on 24/01/16.
 */
public class DataVector {

    protected double[] data;
    protected final int size;
    protected final int patternId;

    public DataVector(double[] data) {
        this.data = data;
        this.size = data.length;
        this.patternId = -1;
    }

    public DataVector(double[] data, int patternId) {
        this.data = data;
        this.size = data.length;
        this.patternId = patternId;
    }

    /**
     * Tworzy wektor wyjsciowy z zapalonym bitem o podanym indexie;
     * @param index
     * @param size
     */
    public DataVector(int index, int size) {
        this.data = new double[size];
        this.size = size;
        this.patternId = index;
        for(int i = 0; i < size; i++) {
            if (i == index){
                this.data[i] = 1.0;
            } else this.data[i] = 0.0;
        }
    }


    public double[] getData() {
        return data;
    }

    public int getSize() {
        return size;
    }

    public double get(int index) {
        return this.data[index];
    }

    @Override
    public String toString() {
        return "DataVector{" +
                "data=" + Arrays.toString(data) +
                '}';
    }

    public String toUIString() {
        return Arrays.toString(data);
    }

    public int getPatternId() {
        return patternId;
    }
}
