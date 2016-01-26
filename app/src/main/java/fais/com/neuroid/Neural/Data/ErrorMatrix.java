package fais.com.neuroid.Neural.Data;

import java.util.Arrays;

/**
 * Matrixy do trzymania informacji o bledach na poszczegolnych warstwach
 * X - INDEKS KOLUMNY - jeden neuron ma cala kolumne dla siebie
 * Y - INDEKS WIERSZA - wiersz to jedna z danych z jednego neuronu
 * Created by paweldylag on 24/01/16.
 */
public class ErrorMatrix {

    private double[][] errors;
    private final int columns;
    private final int rows;

    public ErrorMatrix(int size) {
        this.columns = size;
        this.rows = size;
        errors = new double[size][size];
    }

    public ErrorMatrix(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;
        errors = new double[columns][rows];
    }

    public double get(int x, int y) {
        if (x >= columns || x < 0) throw new IndexOutOfBoundsException("Value x: " + x + " is out of matrix bounds.");
        if (y >= rows|| y < 0) throw new IndexOutOfBoundsException("Value y: " + y + " is out of matrix bounds.");
        return errors[x][y];
    }

    public void set(int x, int y, double value) {
        if (x >= columns || x < 0) throw new IndexOutOfBoundsException("Value x: " + x + " is out of matrix bounds.");
        if (y >= rows || y < 0) throw new IndexOutOfBoundsException("Value y: " + y + " is out of matrix bounds.");
        this.errors[x][y] = value;
    }

    /**
     * Zwraca sume bledow w danej kolumnie. Przydatne do obliczania bledow dla warstwy
     * @param row - indeks kolumny.
     * @return - suma bledow
     */
    public double getSumOfRow(int row) {
        if (row >= this.rows || row < 0) throw new IndexOutOfBoundsException("row value: " + row + " is out of matrix bounds.");
        double sum = 0;
        for (int i = 0; i < columns; i++) {
            sum += errors[i][row];
        }
        return sum;
    }


    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    @Override
    public String toString() {
        return "ErrorMatrix{" +
                "errors=" + Arrays.toString(errors) +
                '}';
    }
}
