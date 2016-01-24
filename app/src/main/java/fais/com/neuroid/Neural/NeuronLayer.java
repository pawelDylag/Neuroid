package fais.com.neuroid.Neural;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fais.com.neuroid.Neural.Data.ErrorMatrix;

/**
 * Created by paweldylag on 21/01/16.
 */
public class NeuronLayer {

    protected List<Neuron> neurons;
    protected int size;
    protected double sumOffset;
    protected double activationOffset;
    protected double learningOffset;


    public NeuronLayer(int size, double sumOffset, double activationOffset, double learningOffset) {
        this.neurons = new ArrayList<>();
        this.size = size;
        this.sumOffset = sumOffset;
        this.activationOffset = activationOffset;
        this.learningOffset = learningOffset;
        this.init();
    }

    protected void init() {
        for (int i = 0; i < size; i++) {
            neurons.add(new Neuron(size, sumOffset, activationOffset));
        }
    }

    public double[] generateOutput(double[] inputs) {
        double[] outputs = new double[size];
        for(int i = 0; i < size; i++) {
            outputs[i] = neurons.get(i).generateOutput(inputs);
        }
        return outputs;
    }

    /**
     * Ustawia stan warstwy i jej neuronow
     * @param state
     */
    public void setState(Neuron.STATE state) {
        for (Neuron n : neurons) {
            n.setState(state);
        }
    }

    /**
     * Oblicza bledy dla tej warstwy i trenuje jej neurony
     * @param previousLayerErrors - macierz bledow dla poprzedniej warstwy
     * @return - zwraca obliczona macierz zawierajaca bledy dla wszystkich neuronow w tej warstwie.
     * Kazdy wiersz dla neurona to jego di * wi, gdzie wi to waga kazdego wejscia
     */
    public ErrorMatrix train(ErrorMatrix previousLayerErrors) {
        if (previousLayerErrors.getSize() != this.size) throw new IllegalStateException("Previous layer error matrix size has different size.");
        // di = a * f(s) * (1-f(s)) * sum(wi*di)
        ErrorMatrix thisLayerErrors = new ErrorMatrix(size);
        // lecimy po wszystkich neuronach w tej warstwie
        for (int i = 0; i < this.size ; i++) {
            Neuron n = neurons.get(i);
            if (n.getState() != Neuron.STATE.LEARNING) throw new IllegalStateException("Neuron is not in LEARNING state.");
            // di = a * f(s) * (1-f(s)) * sum(wi*di)
            double neuronError = learningOffset * n.getLastOutput() * (1 - n.getLastOutput()) * previousLayerErrors.getSumOfColumn(i);
            // tutaj generujemy sum(wi*di), zeby kolejna warstwa miala latwiej :)
            // i przy okazji updateujemy wage tego neurona
            for (int j = 0; j < size; j++) {
                double sum = neuronError * n.getWeights()[j];
                // set(kolumna dla tego neurona, jego kolejna suma, wartosc tej sumy)
                thisLayerErrors.set(i, j, sum);
                // Ok, jak juz wygenerowalismy wartosc do macierzy bledow dla poprzedniej warstwy, to teraz poprawiamy wage w tej warstwie
                n.addErrorToWeight(j, learningOffset * neuronError * n.getLastInput()[j]);
            }
        }
        return thisLayerErrors;
    }

}
