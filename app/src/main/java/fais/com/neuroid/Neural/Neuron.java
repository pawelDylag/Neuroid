package fais.com.neuroid.Neural;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by paweldylag on 17/01/2016.
 */
public class Neuron {

    public enum STATE {NORMAL, LEARNING}

    /** wagi dla wjesc */
    private double[] weights;

    /** w wypadku uczenia sie, zapisujemy stare stany */
    private double lastOutput;
    private double[] lastInput;

    /** aktualny stan neurona */
    private STATE state;

    /** stale */
    private final double sumOffset;
    private final double activationOffset;

    public Neuron(int inputSize, double sumOffset, double activationOffset) {
        this.sumOffset = sumOffset;
        this.activationOffset = activationOffset;
        this.weights = new double[inputSize];
        this.state = STATE.NORMAL;
        Random r = new Random();
        for (int i = 0; i < inputSize; i++) {
            weights[i] = r.nextDouble();
        }
    }

    /**
     *  sumuje a potem podaje wynik do funkcji aktywacji.
     */
    public double generateOutput(double[] inputs) {
        // zapisujemy, aby pozniej bylo latwiej uczyc
        if (this.state == STATE.LEARNING) {
            lastInput = inputs;
            lastOutput = activationFunction(sumOffset + sumInputs(inputs));
            return lastOutput;
        } else {
            return activationFunction(sumOffset + sumInputs(inputs));
        }

    }

    /**
     *  sumuje iloczyny wag i wejsc
     */
    private double sumInputs(double[] inputs) {
        //Log.d("NEURON", "inputs: " + Arrays.toString(inputs) + " , weights: " + Arrays.toString(weights));
        double sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += inputs[i] * weights[i];
        }
        return sum;
    }

    /**
     * Funkcja aktywacji
     */
    private double activationFunction(double sum) {
        return (1 / (1 + Math.exp(-activationOffset * sum)));
    }


    /**
     * Zwraca aktualne wartosci wag
     */
    public double[] getWeights() {
        return weights;
    }

    /**
     * Zwraca aktualne wartosci wag
     */
    public void addErrorToWeight(int index, double value) {
        this.weights[index] += value;
    }

    /**
     * Zwraca ostatnia odpowiedz. Przydatne do trenowania.
     * @return
     */
    public double getLastOutput() {
        return lastOutput;
    }

    public void setState(STATE state) {
        this.state = state;
    }

    public STATE getState() {
        return state;
    }

    public double[] getLastInput() {
        return lastInput;
    }
}
