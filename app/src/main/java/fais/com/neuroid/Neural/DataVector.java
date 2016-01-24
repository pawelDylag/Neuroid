package fais.com.neuroid.Neural;

/**
 * Klasa trzymajaca wektor danych wejsciowych do sieci neuronowej.
 * Pod ta postacia moga byc rowniez dane trenujace.
 * Created by paweldylag on 24/01/16.
 */
public class DataVector {

    protected final double[] data;
    protected final int size;

    public DataVector(double[] data) {
        this.data = data;
        this.size = data.length;
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
}
