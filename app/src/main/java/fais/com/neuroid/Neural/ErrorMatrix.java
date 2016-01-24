package fais.com.neuroid.Neural;

import java.util.Arrays;

/**
 * Matrixy do trzymania informacji o bledach na poszczegolnych warstwach
 * X - INDEKS KOLUMNY - jeden neuron ma cala kolumne dla siebie
 * Y - INDEKS WIERSZA - wiersz to jedna z danych z jednego neuronu
 * Created by paweldylag on 24/01/16.
 */
public class ErrorMatrix {

    private double[][] errors;
    private final int size;

    public ErrorMatrix(int size) {
        this.size = size;
        errors = new double[size][size];
    }

    public double get(int x, int y) {
        if (x >= size || x < 0) throw new IndexOutOfBoundsException("Value x: " + x + " is out of matrix bounds.");
        if (y >= size|| y < 0) throw new IndexOutOfBoundsException("Value y: " + y + " is out of matrix bounds.");
        return errors[x][y];
    }

    public void set(int x, int y, double value) {
        if (x >= size || x < 0) throw new IndexOutOfBoundsException("Value x: " + x + " is out of matrix bounds.");
        if (y >= size || y < 0) throw new IndexOutOfBoundsException("Value y: " + y + " is out of matrix bounds.");
        this.errors[x][y] = value;
    }

    /**
     * Zwraca sume bledow w danej kolumnie. Przydatne do obliczania bledow dla warstwy
     * @param column - indeks kolumny.
     * @return - suma bledow
     */
    public double getSumOfColumn(int column) {
        if (column >= size || column < 0) throw new IndexOutOfBoundsException("Column value: " + column + " is out of matrix bounds.");
        double sum = 0;
        for (int i = 0; i < size; i++) {
            sum += errors[column][i];
        }
        return sum;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "ErrorMatrix{" +
                "errors=" + Arrays.toString(errors) +
                '}';
    }
}
