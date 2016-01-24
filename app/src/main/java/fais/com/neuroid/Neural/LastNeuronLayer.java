package fais.com.neuroid.Neural;

import android.util.Log;

import java.util.Arrays;

import fais.com.neuroid.Neural.Data.DataVector;
import fais.com.neuroid.Neural.Data.ErrorMatrix;

/**
 * Created by paweldylag on 24/01/16.
 */
public class LastNeuronLayer extends NeuronLayer {

    public LastNeuronLayer(int size, double sumOffset, double activationOffset, double learningOffset) {
        super(size, sumOffset, activationOffset, learningOffset);
    }

    public ErrorMatrix train(DataVector trainData) {
        if (trainData.getSize() != this.size) throw new IllegalStateException("Data vector has different size than this layer.");
        // di = a * f(s) * (1-f(s)) * (A - I)
        ErrorMatrix thisLayerErrors = new ErrorMatrix(size);
        // lecimy po wszystkich neuronach w tej warstwie
        for (int i = 0; i < this.size ; i++) {
            Neuron n = neurons.get(i);
            if (n.getState() != Neuron.STATE.LEARNING) throw new IllegalStateException("Neuron is not in LEARNING state.");
            // di = a * f(s) * (1-f(s)) * (oczekiwana wartosc - ostatni wynik)
            double neuronError = learningOffset * n.getLastOutput() * (1 - n.getLastOutput()) * (trainData.get(i) - n.getLastOutput());
            // tutaj generujemy sum(wi*di), zeby kolejna warstwa miala latwiej :)
            // i przy okazji updateujemy wage tego neurona
            for (int j = 0; j < thisLayerErrors.getSize(); j++) {
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
