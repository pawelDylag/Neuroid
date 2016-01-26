package fais.com.neuroid.Neural;

import android.util.Log;

import java.util.Arrays;

import fais.com.neuroid.Neural.Data.ErrorMatrix;

/**
 * Created by paweldylag on 24/01/16.
 */
public class FirstNeuronLayer extends NeuronLayer {

    private static final int FIRST_LAYER_INPUT_SIZE = 1;

    public FirstNeuronLayer(int size, double sumOffset, double activationOffset, double learningOffset) {
        super(FIRST_LAYER_INPUT_SIZE, size, sumOffset, activationOffset, learningOffset);
    }

    @Override
    public double[] generateOutput(double[] inputs) {
        double[] outputs = new double[size];
        for(int i = 0; i < size; i++) {
            // podly hack - nie chce mi sie zmieniac klasy neurona,
            // wiec laduje w niego tablice o jednym polu ;D
            double[] input = new double [1];
            input[0] = inputs[i];
            outputs[i] = neurons.get(i).generateOutput(input);
        }
        return outputs;
    }

    @Override
    protected void init() {
        for (int i = 0; i < size; i++) {
            neurons.add(new Neuron(FIRST_LAYER_INPUT_SIZE, sumOffset, activationOffset));
        }
    }

    @Override
    public String toString() {
        return "FirstNeuronLayer{}";
    }

    @Override
    public ErrorMatrix train(ErrorMatrix previousLayerErrors) {
        if (previousLayerErrors.getRows() != this.size) throw new IllegalStateException("Previous layer error matrix size has different size.");
        // di = a * f(s) * (1-f(s)) * sum(wi*di)
        ErrorMatrix thisLayerErrors = new ErrorMatrix(size);
        // lecimy po wszystkich neuronach w tej warstwie
        for (int i = 0; i < this.size ; i++) {
            Neuron n = neurons.get(i);
            if (n.getState() != Neuron.STATE.LEARNING) throw new IllegalStateException("Neuron is not in LEARNING state.");
            // di = a * f(s) * (1-f(s)) * sum(wi*di)
            double neuronError = learningOffset * n.getLastOutput() * (1 - n.getLastOutput()) * previousLayerErrors.getSumOfRow(i);
            for (int j = 0; j < neuronInputSize; j++) {
                n.addErrorToWeight(j, learningOffset * neuronError * n.getLastInput()[0]);
            }
        }
        return thisLayerErrors;
    }
}
